package bbParser.models

import dateFormat.DateFormatter

class ReactionModel(
    val reactionTimeMillis: Int,
    timestamp: String
): Model(DateFormatter.dateFormat.parse(timestamp)) {
}