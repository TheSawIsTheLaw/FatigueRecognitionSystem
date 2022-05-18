package analyze.analyzers

import analyze.clusterization.KeysClusterer
import analyze.models.FuzzyStatus
import bbConverter.KeysConverter
import bbParser.models.Model
import kotlin.math.abs

class KeysAnalyzer : Analyzer {

    private var fuzzyStatus = FuzzyStatus()
    private var keysConverter = KeysConverter()

    override fun prepareModel(keys: List<Model>, reactions: List<Model>): Boolean {
        val gotCenters = KeysClusterer().getClusters(keys, reactions)

        val centers = gotCenters.sortedBy { it.first() }.map { it[1] }

        fuzzyStatus = FuzzyStatus(centers[0], centers[1], centers[2])

        return true
    }

    override fun analyzeByModel(keys: List<Model>): List<Double> {
        if (fuzzyStatus.isQuantized == 0.0 ||
            fuzzyStatus.isBored == 0.0 ||
            fuzzyStatus.isWorkable == 0.0
        )
            return emptyList()

        var averageTypingSpeed = 0.0
        keysConverter.convert(keys).entries
            .sortedBy { it.key.first }
            .takeLast(NUMBER_OF_RECORDS_TO_BE_TAKEN)
            .forEach { averageTypingSpeed += it.value }

        averageTypingSpeed /= NUMBER_OF_RECORDS_TO_BE_TAKEN

        return listOf(
            abs(averageTypingSpeed - fuzzyStatus.isQuantized),
            abs(averageTypingSpeed - fuzzyStatus.isBored),
            abs(averageTypingSpeed - fuzzyStatus.isWorkable)
        )
    }

    companion object {
        const val NUMBER_OF_RECORDS_TO_BE_TAKEN = 5
    }
}