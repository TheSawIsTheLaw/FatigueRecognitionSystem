package analyze.analyzers

import analyze.clusterization.BbClusterer
import analyze.models.FuzzyStatus
import bbConverter.Converter
import bbParser.models.Model
import kotlin.math.abs

class BbAnalyzer(private val converter: Converter) : Analyzer {

    private var fuzzyStatus = FuzzyStatus()

    override fun prepareModel(fDim: List<Model>, reactions: List<Model>): Boolean {
        val gotCenters = BbClusterer(converter).getClusters(fDim, reactions)

        val centers = gotCenters.sortedBy { it.first() }.map { it[1] }

        fuzzyStatus = FuzzyStatus(centers[0], centers[1], centers[2])

        return true
    }

    override fun analyzeByModel(values: List<Model>): List<Double> {
        if (!fuzzyStatus.isValid()) return emptyList()

        var average = 0.0
        converter.convert(values).entries.sortedBy { it.key.first }
            .takeLast(NUMBER_OF_RECORDS_TO_BE_TAKEN)
            .forEach { average += it.value }

        average /= NUMBER_OF_RECORDS_TO_BE_TAKEN

        return listOf(
            abs(average - fuzzyStatus.isQuantized),
            abs(average - fuzzyStatus.isBored),
            abs(average - fuzzyStatus.isWorkable)
        )
    }

    companion object {
        const val NUMBER_OF_RECORDS_TO_BE_TAKEN = 5
    }
}