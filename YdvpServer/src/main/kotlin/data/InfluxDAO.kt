package data

import config.InfluxdbConfiguration
import domain.logicentities.DSMeasurement
import domain.logicentities.DSMeasurementList
import com.influxdb.client.domain.WritePrecision
import com.influxdb.query.FluxRecord
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant

class InfluxDAO(private val config: InfluxdbConfiguration) : CharDAOInterface {
    override fun get(
        connection: InfluxConnection,
        timeRange: Pair<Int, Int>,
        bucket: String,
        measurement: String
    ): List<DSMeasurement> {
        var rng = "start: ${timeRange.first}"
        if (timeRange.second != 0) {
            rng += ", stop: ${timeRange.second}}"
        }

        var query: String = "from(bucket: \"$bucket\")\n" +
                "|> range($rng)"
        if (measurement.isNotBlank()) {
            query += "\n|> filter(fn: (r) => (r[\"_measurement\"] == " +
                    "\"$measurement\"))"
        }

        val outList: MutableList<DSMeasurement> = mutableListOf()

        val result: Channel<FluxRecord>
        connection.getConnectionToDB().use {
            result = it.getQueryKotlinApi().query(query)
            runBlocking {
                for (i in result) {
                    val curVal = i.values
                    outList.add(
                        DSMeasurement(
                            curVal["_measurement"].toString(),
                            curVal["_value"].toString(),
                            curVal["_time"] as Instant
                        )
                    )
                }
            }
        }

        return outList
    }

    private fun getOrgIDByName(apiString: String, orgName: String): String {
        val urlWithParams = apiString.toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("org", orgName)
            .build()

        val request = Request.Builder()
            .url(urlWithParams)
            .addHeader(
                "Authorization",
                "Token ${config.configData.influxdbAdminToken}"
            )
            .get()
            .build()

        val httpClient = OkHttpClient()
        val retVal: String
        val response = httpClient.newCall(request).execute()
        response.use {
            if (response.code != 200) {
                throw Exception("Connection to database failed")
            }

            retVal = response.body!!.string()
        }

        val regex = "\"orgID\": \"[a-z0-9]+\"".toRegex()
        val res = regex.find(retVal) ?: throw Exception("Org ID is not defined")
        return retVal.substring(res.range.first + 10, res.range.last)
    }

    private fun createBucket(subjectName: String) {
        val httpClient = OkHttpClient()
        var apiString = config.configData.influxdbURL
        if (apiString.last() != '/') {
            apiString += '/'
        }
        apiString += "api/v2/buckets"

        val orgID =
            getOrgIDByName(apiString, config.configData.influxdbOrganization)
        val jsonContent = "{\n" +
                "  \"orgID\": \"$orgID\",\n" +
                "  \"name\": \"$subjectName\",\n" +
                "  \"retentionRules\": []\n" +
                "}"
        val body = jsonContent
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(apiString)
            .addHeader(
                "Authorization",
                "Token ${config.configData.influxdbAdminToken}"
            )
            .post(body)
            .build()

        httpClient.newCall(request).execute()
    }

    private fun bucketNotExists(bucketName: String): Boolean {
        val httpClient = OkHttpClient()

        var apiString = config.configData.influxdbURL
        if (apiString.last() != '/') {
            apiString += '/'
        }
        apiString += "api/v2/buckets"

        val urlWithParams = apiString.toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("name", bucketName)
            .build()

        val request = Request.Builder()
            .url(urlWithParams)
            .addHeader(
                "Authorization",
                "Token ${config.configData.influxdbAdminToken}"
            )
            .get()
            .build()

        val retVal: Boolean
        val response = httpClient.newCall(request).execute()
        response.use {
            if (response.code != 200) {
                throw Exception("Connection to database failed")
            }
            retVal = response.body!!.string().contains("\"buckets\": []")
        }

        return retVal
    }

    override fun add(connection: InfluxConnection, bucket: String, measurementList: DSMeasurementList) {
        connection.getConnectionWrite(bucket).use {
            if (bucketNotExists(bucket)) {
                createBucket(bucket)
            }

            val writeApi = it.getWriteKotlinApi()

            val name = measurementList.name
            runBlocking {
                for (i in measurementList.measurements) {
                    writeApi.writeRecord(
                        "$name $name=${i.value}",
                        WritePrecision.S
                    )
                }
            }
        }
    }

    fun getNewTokenForUser(username: String): Pair<String, String> {
        val httpClient = OkHttpClient()
        var apiString = config.configData.influxdbURL
        if (apiString.last() != '/') {
            apiString += '/'
        }
        apiString += "api/v2/authorizations"

        val orgID =
            getOrgIDByName(apiString, config.configData.influxdbOrganization)
        val jsonContent = "{\n" +
                "            \"description\": \"$username token\",\n" +
                "            \"status\": \"active\",\n" +
                "            \"orgID\": \"$orgID\",\n" +
                "            \"permissions\": [\n" +
                "            {\n" +
                "                \"action\": \"read\",\n" +
                "                \"resource\": {\n" +
                "                \"type\": \"buckets\"\n" +
                "            }\n" +
                "            },\n" +
                "            {\n" +
                "                \"action\": \"write\",\n" +
                "                \"resource\": {\n" +
                "                \"type\": \"buckets\"\n" +
                "            }\n" +
                "            }\n" +
                "            ]\n" +
                "        }"


        val body = okhttp3.RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            jsonContent
        )

        val request = Request.Builder()
            .url(apiString)
            .addHeader(
                "Authorization",
                "Token ${config.configData.influxdbAdminToken}"
            )
            .post(body)
            .build()

        var outBody: String

        val response = httpClient.newCall(request).execute()
        response.use { outBody = response.body!!.string() }

        var regex = "\"token\": \"[^\"]*\"".toRegex()
        var regRes = regex.find(outBody) ?: throw Exception("Token parse error")

        val newToken =
            outBody.substring(regRes.range.first + 10, regRes.range.last)

        regex = "\"id\": \"[^\"]*\"".toRegex()
        regRes = regex.find(outBody) ?: throw Exception("ID parse error")
        val tokenId =
            outBody.substring(regRes.range.first + 7, regRes.range.last)

        return Pair(newToken, tokenId)
    }

    fun deleteToken(tokenId: String): Int {
        val httpClient = OkHttpClient()
        var apiString = config.configData.influxdbURL
        if (apiString.last() != '/') {
            apiString += '/'
        }
        apiString += "api/v2/authorizations/${tokenId}"

        val request = Request.Builder()
            .url(apiString)
            .addHeader(
                "Authorization",
                "Token ${config.configData.influxdbAdminToken}"
            )
            .delete()
            .build()

        return httpClient.newCall(request).execute().code
    }
}