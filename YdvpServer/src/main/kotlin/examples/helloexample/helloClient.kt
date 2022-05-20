package examples.helloexample

import client.InfluxServiceClient
import domain.dtos.AcceptMeasurementsDTO
import domain.dtos.AcceptMeasurementsListDTO
import domain.dtos.MeasurementDataWithoutTime
import domain.dtos.ResponseMeasurementsDTO
import gson.GsonObject
import protocol.YDVP
import protocol.YdvpHeader
import protocol.YdvpStartingLineRequest
import protocol.YdvpStartingLineResponse
import java.net.ConnectException

val measurementsToSend = AcceptMeasurementsListDTO(
    listOf(
        AcceptMeasurementsDTO(
            "pulse", listOf(
                MeasurementDataWithoutTime("30"),
                MeasurementDataWithoutTime("40")
            )
        ),
        AcceptMeasurementsDTO(
            "botArterialPressure", listOf(
                MeasurementDataWithoutTime("40"),
                MeasurementDataWithoutTime("50")
            )
        ),
        AcceptMeasurementsDTO(
            "topArterialPressure", listOf(
                MeasurementDataWithoutTime("80"),
                MeasurementDataWithoutTime("90")
            )
        )
    )
)

fun sendTest() {
    val client = InfluxServiceClient("localhost", 6666)

    client.use {
        try {
            client.connect()
        } catch (exc: ConnectException) {
            println("Server is dead")
            return
        }

        client.sendRequestAndGetResponse(
            YDVP(
                YdvpStartingLineRequest("POST", "/data/TestUser", "0.1"),
                listOf(YdvpHeader("Host", "127.0.0.1")),
                GsonObject.gson.toJson(measurementsToSend)
            )
        )
    }
}

fun getTest() {
    val client = InfluxServiceClient("localhost", 6666)

    client.use {
        try {
            client.connect()
        } catch (exc: ConnectException) {
            println("Server is dead")
            return
        }

        client.sendRequestAndGetResponse(
            YDVP(
                YdvpStartingLineRequest("GET", "/data/TestUser", "0.1"),
                listOf(YdvpHeader("Host", "127.0.0.1")),
                GsonObject.gson.toJson(listOf("pulse", "botArterialPressure"))
            )
        )
    }
}

fun main() {
    sendTest()
//    getTest()
}