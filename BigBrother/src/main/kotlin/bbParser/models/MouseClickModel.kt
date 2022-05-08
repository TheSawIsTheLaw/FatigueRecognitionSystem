package bbParser.models

import dateFormat.DateFormatter

class MouseClickModel(
    val mXCoordinate: Int,
    val mYCoordinate: Int,
    val mKey: Int,
    timestamp: String
): Model(DateFormatter.dateFormat.parse(timestamp)) {

    override fun toString(): String {
        return "[x=$mXCoordinate; y=$mYCoordinate; key=$mKey; " + super.toString()
    }
}