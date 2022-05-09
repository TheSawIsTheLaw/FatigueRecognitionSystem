package bbParser.parsers

import bbParser.models.MouseClickModel
import bbParser.prefixes.Prefixes

class MouseClicksBbParser : BbParser(
    { file ->
        file.readLines().map { line ->
            val strValues = line.split(',')
            MouseClickModel(
                strValues[0].trim().removePrefix(Prefixes.X_COORDINATE).toInt(),
                strValues[1].trim().removePrefix(Prefixes.Y_COORDINATE).toInt(),
                strValues[2].trim().removePrefix(Prefixes.KEY).toInt(),
                strValues[3].trim().removePrefix(Prefixes.TIMESTAMP).toLong()
            )
        }
    }
)