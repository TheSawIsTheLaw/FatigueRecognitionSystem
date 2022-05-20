package examples.helloexample

import server.InfluxServiceServer

fun main() {
    val server = InfluxServiceServer(6666)

    server.run()
}