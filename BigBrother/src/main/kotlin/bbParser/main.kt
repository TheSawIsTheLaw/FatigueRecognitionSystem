package bbParser

import bbParser.parsers.KeysBbParser
import bbParser.parsers.MouseClicksBbParser
import bbParser.parsers.MouseMovesBbParser
import bbParser.parsers.ReactionsBbParser

fun main() {
    println("Keys")
    println(KeysBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/aaa_Keys.txt"))
    println("Mouse clicks")
    println(MouseClicksBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/aaa_Mouse_Clicks.txt"))
    println("Mouse moves")
    println(MouseMovesBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/aaa_Mouse_Moves.txt"))
    println("Reactions")
    println(ReactionsBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/aaa_Reactions.txt"))
}