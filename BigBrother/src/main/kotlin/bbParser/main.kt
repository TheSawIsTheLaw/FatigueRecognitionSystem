package bbParser

import bbConverter.KeysConverter
import bbParser.parsers.KeysBbParser
import bbParser.parsers.MouseClicksBbParser
import bbParser.parsers.MouseMovesBbParser
import bbParser.parsers.ReactionsBbParser

fun main() {
    println("Keys")
    val keys = KeysBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Keys.txt")
//    println(keys)
    println("Mouse clicks")
    val clicks = MouseClicksBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Mouse_Clicks.txt")
//    println(clicks)
    println("Mouse moves")
    val moves = MouseMovesBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Mouse_Moves.txt")
//    println(moves)
    println("Reactions")
    val reactions = ReactionsBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Reactions.txt")
//    println(reactions)

    println("Got speeds")
    val converted = KeysConverter().convert(keys)
    converted.forEach {
        println("${it.key.second - it.key.first} мс -- ${it.value} символов/минута")
    }
    println(converted.size)
}