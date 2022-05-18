package analyze.clusterization

import bbParser.models.Model
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer

abstract class Clusterer(k: Int = 3, fuzziness: Double = 5.0) {

    val clusterer = FuzzyKMeansClusterer<ClusterPoint>(k, fuzziness)

    abstract fun getClusters(fDimension: List<Model>, sDimension: List<Model>): List<List<Double>>
}