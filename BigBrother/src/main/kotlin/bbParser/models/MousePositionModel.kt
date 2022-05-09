package bbParser.models

class MousePositionModel(
    val mXCoordinate: Int,
    val mYCoordinate: Int,
    timestamp: Long
) : Model(timestamp) {

    override fun toString(): String {
        return "[x=$mXCoordinate; y=$mYCoordinate; " + super.toString()
    }
}