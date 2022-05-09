package bbParser.models

import dateFormat.DateFormatter

class KeyModel(
    val mKeyName: String,
    timestamp: Long
): Model(timestamp) {

    override fun toString(): String {
        return "[key=$mKeyName; " + super.toString()
    }
}