package bbParser.models

class ReactionModel(
    val mReactionMillis: Int,
    timestamp: Long
): Model(timestamp) {

    override fun toString(): String {
        return "[reaction_time=$mReactionMillis; " + super.toString()
    }
}