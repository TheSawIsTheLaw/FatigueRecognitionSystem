package bbParser.parsers

import bbParser.models.Model
import java.io.File

abstract class BbParser(private val parseFun: (File) -> List<Model>) {

    fun parseFile(path: String): List<Model> {
        val file = File(path)
        return if (file.exists() && file.canRead()) parseFun(file) else listOf()
    }
}