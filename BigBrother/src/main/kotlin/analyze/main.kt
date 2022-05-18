package analyze

import analyze.analyzers.KeysAnalyzer
import bbParser.parsers.KeysBbParser
import bbParser.models.ReactionModel
import bbParser.parsers.MouseClicksBbParser
import bbParser.parsers.MouseMovesBbParser
import bbParser.parsers.ReactionsBbParser

fun main() {
    val keys = KeysBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Keys.txt")
//    println(keys)
    val clicks = MouseClicksBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Mouse_Clicks.txt")
//    println(clicks)
    val moves = MouseMovesBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Mouse_Moves.txt")
//    println(moves)
    val reactions =
        ReactionsBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Reactions.txt") as List<ReactionModel>


    val analyzer = KeysAnalyzer()
    analyzer.prepareModel(keys, reactions)
    println(analyzer.analyzeByModel(keys))
}