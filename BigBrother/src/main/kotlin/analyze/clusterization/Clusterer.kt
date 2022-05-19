package analyze.clusterization

import bbConverter.Converter
import bbParser.models.Model
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer

abstract class Clusterer(val converter: Converter, k: Int = 3, fuzziness: Double = 5.0) {

    val clusterer = FuzzyKMeansClusterer<ClusterPoint>(k, fuzziness)

    abstract fun getClusters(fDimension: List<Model>, sDimension: List<Model>): List<List<Double>>

    companion object {
        const val MILLIS_IN_MINUTE = 1000 * 60
    }
}