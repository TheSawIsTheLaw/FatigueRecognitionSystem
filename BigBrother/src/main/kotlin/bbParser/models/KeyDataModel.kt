package bbParser.models

import dateFormat.DateFormatter

class KeyDataModel(
    val mKeyName: String,
    timestampString: String
): Model(DateFormatter.dateFormat.parse(timestampString)) {
}