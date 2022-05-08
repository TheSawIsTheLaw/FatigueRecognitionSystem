package bbParser.models

import dateFormat.DateFormatter

class MousePositionModel(
    val xCoord: Int,
    val yCoord: Int,
    timestamp: String
) : Model(DateFormatter.dateFormat.parse(timestamp)) {
}