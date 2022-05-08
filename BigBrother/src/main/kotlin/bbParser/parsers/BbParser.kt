package bbParser.parsers

import bbParser.models.Model

interface BbParser {
    fun parseFile(path: String): Model
}