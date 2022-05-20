package protocol

class YdvpHeader(val name: String, val value: String) {
    override fun toString(): String {
        return "$name: $value\n"
    }
}