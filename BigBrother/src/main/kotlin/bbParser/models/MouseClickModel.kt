package bbParser.models

class MouseClickModel(
    val mXCoordinate: Int,
    val mYCoordinate: Int,
    val mKey: Int,
    timestamp: Long
): Model(timestamp) {

    override fun toString(): String {
        return "[x=$mXCoordinate; y=$mYCoordinate; key=$mKey; " + super.toString()
    }
}