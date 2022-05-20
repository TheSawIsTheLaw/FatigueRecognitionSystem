package config

import com.google.gson.Gson
import java.io.File

data class InfluxConfigData(
        val influxdbURL: String = "",
        val influxdbAdminToken: String = "",
        val influxdbOrganization: String = ""
)

class InfluxdbConfiguration(
        configFile: File = File("FDInfluxConf.json")
) {
    val configData: InfluxConfigData

    init {
        val stream = try {
            configFile.inputStream()
        }
        catch (exc: Exception) {
            println(exc.message)
            throw exc
        }

        // https://github.com/google/gson/issues/1657
        // https://github.com/google/gson/issues/1550
        val jsonToConvert = stream.bufferedReader().use { it.readText() }
        configData = Gson().fromJson(jsonToConvert, InfluxConfigData::class.java)
    }
}