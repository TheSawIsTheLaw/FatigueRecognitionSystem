package protocol

class YDVP(
    val startingLine: YdvpStartingLine,
    val headers: List<YdvpHeader>,
    val body: String = ""
) {
    init {
        if (headers.isEmpty())
            throw Exception("Headers cannot be empty")
    }

    fun createStringRequest(): String {
        var requestString = "${startingLine as YdvpStartingLineRequest}"
        headers.forEach { requestString += it.toString() }

        if (body.isNotEmpty())
            requestString += "\n$body\n"

        return requestString
    }

    fun createStringResponse(): String {
        var responseString = "${startingLine as YdvpStartingLineResponse}"
        headers.forEach { responseString += it.toString() }

        if (body.isNotEmpty())
            responseString += "\n$body\n"

        return responseString
    }
}