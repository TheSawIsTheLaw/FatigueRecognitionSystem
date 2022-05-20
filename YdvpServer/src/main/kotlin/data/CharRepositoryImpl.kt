package data

import config.InfluxdbConfiguration
import domain.charrepository.CharRepositoryInterface
import domain.logicentities.*
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import org.koin.java.KoinJavaComponent.inject
import org.springframework.stereotype.Component

class InfluxConnection(
    private val connectionString: String,
    private val token: String,
    private val org: String
) {

    fun getConnectionToDB(): InfluxDBClientKotlin {
        return InfluxDBClientKotlinFactory.create(
            connectionString, token
                .toCharArray(), org
        )
    }

    fun getConnectionWrite(bucketName: String): InfluxDBClientKotlin {
        return InfluxDBClientKotlinFactory
            .create(
                connectionString,
                token.toCharArray(),
                org,
                bucketName
            )
    }
}

class CharRepositoryImpl : CharRepositoryInterface {
    private val config by inject<InfluxdbConfiguration>(InfluxdbConfiguration::class.java)

    private val influxDAO = InfluxDAO(config)

    private fun get(
        dataAccessInfo: DSDataAccessInfo,
        connection: InfluxConnection
    ): List<DSMeasurement> {
        return influxDAO.get(
            connection,
            dataAccessInfo.timeRange,
            dataAccessInfo.bucketName,
            dataAccessInfo.measurementName
        )
    }

    override fun get(dataAccessInfo: DSDataAccessInfo): List<DSMeasurement> {
        return get(
            dataAccessInfo, InfluxConnection(
                config.configData.influxdbURL,
                config.configData.influxdbAdminToken,
                config.configData.influxdbOrganization
            )
        )
    }

    private fun add(dataAddInfo: DSDataAddInfo, connection: InfluxConnection) {
        influxDAO.add(connection, dataAddInfo.bucket, dataAddInfo.measurementList)
    }

    override fun add(dataAddInfo: DSDataAddInfo) {
        add(
            dataAddInfo, InfluxConnection(
                config.configData.influxdbURL,
                config.configData.influxdbAdminToken,
                config.configData.influxdbOrganization
            )
        )
    }

    fun getNewTokenForUser(user: USUserCredentials): TokenInformation {
        val newTokenInformation = influxDAO.getNewTokenForUser(user.username)

        return TokenInformation(newTokenInformation.first, newTokenInformation.second)
    }

    fun deleteToken(tokenToDelete: TokenInformation): Boolean {
        return (influxDAO.deleteToken(tokenToDelete.tokenID) != 204)
    }
}