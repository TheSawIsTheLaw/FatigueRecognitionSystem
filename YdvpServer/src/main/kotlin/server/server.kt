package server

import config.InfluxdbConfiguration
import controllers.DataController
import controllers.services.DataService
import data.CharRepositoryImpl
import domain.dtos.AcceptMeasurementsListDTO
import gson.GsonObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import protocol.YDVP
import protocol.YdvpHeader
import protocol.YdvpStartingLineRequest
import protocol.YdvpStartingLineResponse
import protocol.parser.YdvpParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.NullPointerException
import java.lang.Runtime.getRuntime
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

class InfluxServiceClientHandler(private val clientSocket: Socket) {
    private val ydvpVersion = "0.1"
    private val defaultHeader = YdvpHeader("Server", "127.0.0.1")

    private val controller by inject<DataController>(DataController::class.java)

    private fun anyResponse(code: String, explanation: String, body: Any): YDVP {
        return YDVP(
            YdvpStartingLineResponse(
                ydvpVersion,
                code,
                explanation
            ),
            listOf(defaultHeader),
            GsonObject.gson.toJson(body)
        )
    }

    private val badRequestResponse by lazy {
        YDVP(
            YdvpStartingLineResponse(ydvpVersion, "400", "BAD REQUEST"),
            listOf(defaultHeader)
        )
    }
    private val internalServerErrorResponse by lazy {
        YDVP(
            YdvpStartingLineResponse(ydvpVersion, "500", "INTERNAL SERVER ERROR"),
            listOf(defaultHeader)
        )
    }

    private val methodNotAllowed by lazy {
        YDVP(
            YdvpStartingLineResponse(ydvpVersion, "405", "METHOD NOT ALLOWED"),
            listOf(defaultHeader)
        )
    }

    private val notFoundResponse by lazy {
        YDVP(
            YdvpStartingLineResponse(ydvpVersion, "404", "NOT FOUND"),
            listOf(defaultHeader)
        )
    }

    private fun prepareUri(uri: String): List<String> {
        val parsedUri = uri.split("/").toMutableList()

        if (parsedUri[0] != "")
            throw UnsupportedUriException("URI format error")

        parsedUri.removeAt(0)
        return parsedUri
    }

    private fun controllerPostMethod(uri: String, body: String): YDVP {
        val parsedUri = prepareUri(uri)

        return when (parsedUri.first()) {
            "data" -> {
                if (parsedUri.size < 2)
                    throw UnsupportedUriException("Not enough inline arguments")
                val response = controller.addData(
                    parsedUri[1],
                    GsonObject.gson.fromJson(body, AcceptMeasurementsListDTO::class.java)
                )

                anyResponse(response.statusCodeValue.toString(), response.statusCode.name, response.body)
            }
            else -> throw UnsupportedUriException("Unsupported URI")
        }
    }

    private fun controllerGetMethod(uri: String, body: String): YDVP {
        val parsedUri = prepareUri(uri)

        return when (parsedUri.first()) {
            "data" -> {
                if (parsedUri.size < 2)
                    throw UnsupportedUriException("Not enough inline arguments")
                val response =
                    controller.getData(parsedUri[1], GsonObject.gson.fromJson(body, Array<String>::class.java).toList())

                anyResponse(response.statusCodeValue.toString(), response.statusCode.name, response.body)
            }
            else -> throw UnsupportedUriException("Way not found")
        }
    }

    private fun controllerWayByMethod(ydvpRequest: YDVP): YDVP {
        ydvpRequest.startingLine as YdvpStartingLineRequest
        val method = ydvpRequest.startingLine.method
        val uri = ydvpRequest.startingLine.uri

        val body = ydvpRequest.body

        return try {
            when (method) {
                "GET" -> controllerGetMethod(uri, body)
                "POST" -> controllerPostMethod(uri, body)
                else -> methodNotAllowed
            }
        } catch (exc: NullPointerException) {
            badRequestResponse
        } catch (exc: UnsupportedUriException) {
            notFoundResponse
        } catch (exc: Exception) {
            println("EXCEPTION")
            println(exc.javaClass)
            println(exc.localizedMessage)
            internalServerErrorResponse
        }
    }

    fun run() {
        println("Accepted client on ${clientSocket.localSocketAddress} from ${clientSocket.remoteSocketAddress}")

        val bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

        var gotRequest = bufferedReader.readLine() + "\n"
        while (bufferedReader.ready())
            gotRequest += bufferedReader.readLine() + "\n"

        val clientOut = PrintWriter(clientSocket.getOutputStream(), true)

        val ydvpRequest = try {
            YdvpParser().parseRequest(gotRequest)
        } catch (exc: Exception) {
            clientOut.println(badRequestResponse)

            return
        }

        /* And here comes protocol */
        /* FUS ROH DAH */
        val response = controllerWayByMethod(ydvpRequest).createStringResponse()
        println("Returned to client:\n$response")
        clientOut.println(response)
    }
}

class InfluxServiceServer(socketPort: Int) {
    init {
        startKoin {
            modules(module {
                single { InfluxdbConfiguration() }

                single { CharRepositoryImpl() }

                single { DataService() }

                single { DataController() }
            })
        }
    }

    private val serverSocket = ServerSocket(socketPort)

    fun run() {
        getRuntime().addShutdownHook(Thread {
            println("Server on port ${serverSocket.localPort} stopped")
        })

        if (!serverSocket.isBound || serverSocket.isClosed) {
            throw SocketException("Server socket is already in use")
        }

        println("Server started on port ${serverSocket.localPort}")

        while (true) {
            val clientSocket = serverSocket.accept()
            thread {
                InfluxServiceClientHandler(clientSocket).run()
            }
        }
    }
}