package analyze

import analyze.analyzers.BbAnalyzer
import bbConverter.KeysConverter
import bbParser.parsers.KeysBbParser
import bbParser.parsers.ReactionsBbParser
import fileAnalyzer.BbFileAnalyzer

fun main() {

    val fileAnalyzer = BbFileAnalyzer(
        "${System.getProperty("user.dir")}/dataExample/test_Keys.txt",
        "${System.getProperty("user.dir")}/dataExample/test_Reactions.txt",
        KeysBbParser(), ReactionsBbParser(), BbAnalyzer(KeysConverter())
    )

    println(fileAnalyzer.analyzeByFile("${System.getProperty("user.dir")}/dataExample/test_Keys.txt"))
}