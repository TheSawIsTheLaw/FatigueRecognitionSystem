package analyze.clusterization

import bbConverter.Converter
import bbParser.models.Model
import bbParser.models.ReactionModel

class BbClusterer(converter: Converter, k: Int = 3, fuzziness: Double = 5.0) : Clusterer(converter, k, fuzziness) {

    override fun getClusters(fDimension: List<Model>, reactions: List<Model>): List<List<Double>> {
        val converted = converter.convert(fDimension)
        reactions as List<ReactionModel>

        val clusterable = mutableListOf<ClusterPoint>().apply {
            reactions.forEach { reaction ->
                converted.entries.filter { filIt ->
                    reaction.mTimestamp - filIt.key.first in (0..(10 * MILLIS_IN_MINUTE))
                }.sortedBy { it.key.first }.forEach { innerIt ->
                    add(
                        ClusterPoint(
                            doubleArrayOf(
                                reaction.mReactionMillis.toDouble(),
                                (innerIt.value / (innerIt.key.second - innerIt.key.first).toDouble() * 1000 * 60)
                            )
                        )
                    )
                }
            }
        }

        clusterer.cluster(clusterable)
        return clusterer.clusters.map { listOf(it.center.point[0], it.center.point[1]) }
    }
}