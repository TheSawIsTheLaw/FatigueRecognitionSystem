package analyze.clusterization

import org.apache.commons.math3.ml.clustering.Clusterable

class ClusterPoint(private val point: DoubleArray) : Clusterable {

    override fun getPoint(): DoubleArray {
        return point
    }
}