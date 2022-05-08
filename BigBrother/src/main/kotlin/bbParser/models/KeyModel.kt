package bbParser.models

import dateFormat.DateFormatter

class KeyModel(
    val mKeyName: String,
    timestampString: String
): Model(DateFormatter.dateFormat.parse(timestampString)) {

    override fun toString(): String {
        return "[key=$mKeyName; " + super.toString()
    }
}