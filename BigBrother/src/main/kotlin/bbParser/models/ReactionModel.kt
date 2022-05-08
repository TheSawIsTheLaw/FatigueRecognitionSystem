package bbParser.models

import dateFormat.DateFormatter

class ReactionModel(
    val mReactionMillis: Int,
    timestamp: String
): Model(DateFormatter.dateFormat.parse(timestamp)) {

    override fun toString(): String {
        return "[reaction_time=$mReactionMillis; " + super.toString()
    }
}