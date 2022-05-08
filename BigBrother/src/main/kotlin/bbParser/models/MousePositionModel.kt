package bbParser.models

import dateFormat.DateFormatter

class MousePositionModel(
    val mXCoordinate: Int,
    val mYCoordinate: Int,
    timestamp: String
) : Model(DateFormatter.dateFormat.parse(timestamp)) {
}