package bbParser.parsers

import bbParser.models.KeyModel
import bbParser.prefixes.Prefixes

class KeysBbParser : BbParser(
    { file ->
        file.readLines().map { line ->
            val strValues = line.split(',')
            KeyModel(
                strValues[0].trim().removePrefix(Prefixes.KEY),
                strValues[1].trim().removePrefix(Prefixes.TIMESTAMP).toLong()
            )
        }
    }
)