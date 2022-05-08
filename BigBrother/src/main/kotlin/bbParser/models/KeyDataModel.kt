package bbParser.models

import dateFormat.DateFormatter

class KeyDataModel(
    val keyName: String,
    timestampString: String
): Model(DateFormatter.dateFormat.parse(timestampString)) {
}