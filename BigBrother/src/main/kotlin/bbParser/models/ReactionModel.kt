package bbParser.models

import dateFormat.DateFormatter

class ReactionModel(
    val mReactionMillis: Int,
    timestamp: String
): Model(DateFormatter.dateFormat.parse(timestamp)) {
}