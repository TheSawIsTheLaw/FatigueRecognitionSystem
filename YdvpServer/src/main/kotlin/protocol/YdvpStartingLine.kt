package protocol

abstract class YdvpStartingLine

class YdvpStartingLineRequest(val method: String, val uri: String, val version: String) : YdvpStartingLine() {
    override fun toString(): String {
        return "$method $uri YDVP/$version\n"
    }
}

class YdvpStartingLineResponse(val version: String, val responseCode: String, val explanation: String) :
    YdvpStartingLine() {
    override fun toString(): String {
        return "YDVP/$version $responseCode $explanation\n"
    }
}