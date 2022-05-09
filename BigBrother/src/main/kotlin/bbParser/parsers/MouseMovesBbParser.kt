package bbParser.parsers

import bbParser.models.MousePositionModel
import bbParser.prefixes.Prefixes

class MouseMovesBbParser : BbParser(
    { file ->
        file.readLines().map { line ->
            val strValues = line.split(',')
            MousePositionModel(
                strValues[0].trim().removePrefix(Prefixes.X_COORDINATE).toInt(),
                strValues[1].trim().removePrefix(Prefixes.Y_COORDINATE).toInt(),
                strValues[2].trim().removePrefix(Prefixes.TIMESTAMP).toLong()
            )
        }
    }
)