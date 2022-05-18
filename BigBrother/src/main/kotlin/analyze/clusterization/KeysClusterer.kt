package analyze.clusterization

import bbConverter.KeysConverter
import bbParser.models.Model
import bbParser.models.ReactionModel

class KeysClusterer(k: Int = 3, fuzziness: Double = 5.0) : Clusterer(k, fuzziness) {

    private val keysConverter = KeysConverter()

    override fun getClusters(keys: List<Model>, reactions: List<Model>): List<List<Double>> {
        val convertedKeys = keysConverter.convert(keys)
        reactions as List<ReactionModel>

        val clusterable = mutableListOf<ClusterPoint>().apply {
            reactions.forEach { reaction ->
                convertedKeys.entries
                    .filter { filIt ->
                        reaction.mTimestamp - filIt.key.first in (0..(10 * MILLIS_IN_MINUTE))
                    }
                    .sortedBy { it.key.first }
                    .forEach { innerIt ->
                        add(
                            ClusterPoint(
                                doubleArrayOf(
                                    reaction.mReactionMillis.toDouble(),
                                    (innerIt.value / (innerIt.key.second - innerIt.key.first).toDouble() * MILLIS_IN_MINUTE)
                                )
                            )
                        )
                    }
            }
        }

        clusterer.cluster(clusterable)
        return clusterer.clusters.map { listOf(it.center.point[0], it.center.point[1]) }
    }

    companion object {
        const val MILLIS_IN_MINUTE = 1000 * 60
    }
}