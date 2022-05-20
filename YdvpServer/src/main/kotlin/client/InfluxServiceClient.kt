package client

import protocol.YDVP
import protocol.parser.YdvpParser
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class InfluxServiceClient(private val ip: String, private val port: Int): Closeable {
    private var clientSocket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    fun connect() {
        clientSocket = Socket(ip, port)
        output = PrintWriter(clientSocket!!.getOutputStream(), true)
        input = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))

        println("Client connected from ${clientSocket!!.localSocketAddress} to ${clientSocket!!.remoteSocketAddress}")
    }

    fun sendRequestAndGetResponse(request: YDVP): YDVP {
        if (output == null || input == null)
            throw Exception("Cannot connect to the server")

        output!!.println(request.createStringRequest())
        print("Formed request in client: \n${request.createStringRequest()}")

        var response = input!!.readLine() + "\n"
        while(input!!.ready())
            response += input!!.readLine() + "\n"

        println("Response in client:\n$response")

        return YdvpParser().parseResponse(response)
    }

    override fun close() {
        output?.close()
        input?.close()

        if (clientSocket == null)
            return

        if (!clientSocket!!.isClosed || clientSocket!!.isBound || clientSocket!!.isConnected)
            clientSocket!!.close()
    }
}