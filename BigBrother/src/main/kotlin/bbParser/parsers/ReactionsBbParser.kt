package bbParser.parsers

import bbParser.models.ReactionModel
import bbParser.prefixes.Prefixes

class ReactionsBbParser : BbParser(
    { file ->
        file.readLines().map { line ->
            val strValues = line.split(',')
            ReactionModel(
                strValues[0].trim().removePrefix(Prefixes.REACTION).toInt(),
                strValues[1].trim().removePrefix(Prefixes.TIMESTAMP).toLong()
            )
        }
    }
)