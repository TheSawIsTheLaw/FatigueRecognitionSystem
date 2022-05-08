package bbParser.models

import dateFormat.DateFormatter

class MouseClickModel(
    val xCoord: Int,
    val yCoord: Int,
    val key: Int,
    timestamp: String
): Model(DateFormatter.dateFormat.parse(timestamp)) {
}