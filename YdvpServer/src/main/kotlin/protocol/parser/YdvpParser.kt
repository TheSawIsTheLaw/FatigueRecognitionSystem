package protocol.parser

import protocol.YDVP
import protocol.YdvpHeader
import protocol.YdvpStartingLineRequest
import protocol.YdvpStartingLineResponse

val ALLOWED_METHODS = listOf("GET", "POST")
val ALLOWED_VERSIONS = listOf("0.1")

class YdvpParser {
    private fun parseRequestStartingLine(line: String): YdvpStartingLineRequest {
        val tempLine = line.split(" ")

        val method = tempLine[0]
        if (method !in ALLOWED_METHODS)
            throw Exception("YDVP method type not allowed")

        val uri = tempLine[1]
        if (uri.first() != '/')
            throw Exception("URI format error")

        val version = tempLine[2].split("/")[1]
        if (version !in ALLOWED_VERSIONS)
            throw Exception("YDVP version not supported")

        return YdvpStartingLineRequest(method, uri, version)
    }

    private fun parseResponseStartingLine(line: String): YdvpStartingLineResponse {
        val tempLine = line.split(" ")

        val version = tempLine[0].split("/")[1]
        if (version !in ALLOWED_VERSIONS)
            throw Exception("YDVP version not supported")

        val responseCode = tempLine[1]
        if (responseCode.toIntOrNull() == null)
            throw Exception("YDVP invalid response code")

        val explanation = tempLine[2]

        return YdvpStartingLineResponse(version, responseCode, explanation)
    }

    private fun parseHeaders(headers: List<String>): List<YdvpHeader> {
        val outHeaders = mutableListOf<YdvpHeader>()

        var currentPairInHeader: List<String>
        for (currentHeader in headers) {
            currentPairInHeader = currentHeader.split(":").map { it.trim() }
            outHeaders.add(YdvpHeader(currentPairInHeader[0], currentPairInHeader[1]))
        }

        return outHeaders
    }

    private fun parseHeadersAndBody(lines: List<String>): Pair<List<YdvpHeader>, String> {
        val workLines = lines.toMutableList()
        workLines.removeAt(0)

        while (workLines.last().isEmpty())
            workLines.removeAt(workLines.size - 1)

        val partWithHeaders = workLines.takeWhile { it.isNotEmpty() }
        val headers = parseHeaders(partWithHeaders)

        var body = ""
        if (partWithHeaders.size != workLines.size) {
            val bodyWorkLines = workLines.takeLastWhile { it.isNotEmpty() }
            if (bodyWorkLines.isNotEmpty())
            {
                body = bodyWorkLines.reduce { acc, s -> acc + "\n" + s }
            }
        }

        return Pair(headers, body)
    }

    fun parseRequest(request: String): YDVP {
        val requestLines = request.split("\n").toMutableList()

        val startingLine = parseRequestStartingLine(requestLines.first())

        val headersAndBody = parseHeadersAndBody(requestLines)

        return YDVP(startingLine, headersAndBody.first, headersAndBody.second)
    }

    fun parseResponse(response: String): YDVP {
        val requestLines = response.split("\n").toMutableList()

        val startingLine = parseResponseStartingLine(requestLines.first())

        val headersAndBody = parseHeadersAndBody(requestLines)

        return YDVP(startingLine, headersAndBody.first, headersAndBody.second)
    }
}

// A small test
//fun main()
//{
//    val stringToParse = "GET /user/pulse YDVP/0.1\n" +
//            "Host: 127.0.0.1\n" +
//            "\n" +
//            "{\n" +
//            "   \"someval\": 30\n" +
//            "}"
//
//    val parsed = YdvpParser().parseRequest(stringToParse)
//    println(parsed.startingLine)
//    println("Headers:")
//    parsed.headers.forEach { println(it) }
//    println(parsed.body)
//}