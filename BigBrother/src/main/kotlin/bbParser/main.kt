package bbParser

import bbConverter.ClicksConverter
import bbConverter.KeysConverter
import bbParser.models.ReactionModel
import bbParser.parsers.KeysBbParser
import bbParser.parsers.MouseClicksBbParser
import bbParser.parsers.MouseMovesBbParser
import bbParser.parsers.ReactionsBbParser
import dateFormat.DateFormatter
import org.apache.commons.math3.ml.clustering.Cluster
import org.apache.commons.math3.ml.clustering.Clusterable
import org.apache.commons.math3.ml.clustering.Clusterer
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer
import java.lang.Math.abs

class ClusterPoint(val point: List<Double>) : Clusterable {

    override fun getPoint(): DoubleArray {
        return point.toDoubleArray()
    }
}

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
    val reactions =
        ReactionsBbParser().parseFile("${System.getProperty("user.dir")}/dataExample/test_Reactions.txt") as List<ReactionModel>
//    println(reactions)

    println("Got speeds for keys")
    val convertedKeys = KeysConverter().convert(keys)
    convertedKeys.entries.sortedBy { it.key.first }.forEach {
        println("${DateFormatter.dateFormat.format(it.key.first)}--${DateFormatter.dateFormat.format(it.key.second)} -- ${it.value} символов/минута")
    }
    reactions.forEach {
        println("${DateFormatter.dateFormat.format(it.mTimestamp)} -- ${it.mReactionMillis}")
    }

    reactions.forEach { reaction ->
        println("${DateFormatter.dateFormat.format(reaction.mTimestamp)} -- ${reaction.mReactionMillis} (reaction):")
        convertedKeys.entries.filter { filIt -> reaction.mTimestamp - filIt.key.first in (0..(10 * 1000 * 60)) }
            .sortedBy { it.key.first }.forEach { innerIt ->
                println(
                    "   ${DateFormatter.dateFormat.format(innerIt.key.first)}--${
                        DateFormatter.dateFormat.format(
                            innerIt.key.second
                        )
                    } -- ${(innerIt.value / (innerIt.key.second - innerIt.key.first).toDouble() * 1000 * 60).toInt()} символов в минуту"
                )
            }
    }

    println("Fuzzy clusters' centers:")

    val clusterer = FuzzyKMeansClusterer<ClusterPoint>(3, 5.0)
    val toMakeCluster = mutableListOf<ClusterPoint>()
    reactions.forEach { reaction ->
        convertedKeys.entries.filter { filIt -> reaction.mTimestamp - filIt.key.first in (0..(10 * 1000 * 60)) }
            .sortedBy { it.key.first }.forEach { innerIt ->
                toMakeCluster.add(
                    ClusterPoint(
                        listOf(
                            reaction.mReactionMillis.toDouble(),
                            (innerIt.value / (innerIt.key.second - innerIt.key.first).toDouble() * 1000 * 60)
                        )
                    )
                )
            }
    }

    clusterer.cluster(toMakeCluster)
    clusterer.clusters.forEach {
        println("Reaction: ${it.center.point[0]} for speed: ${it.center.point[1]}")
    }
//    val convertedClicks = ClicksConverter().convert(clicks)
//    convertedClicks.forEach {
//        println("${it.key.second - it.key.first} мс -- ${it.value}")
//    }
}