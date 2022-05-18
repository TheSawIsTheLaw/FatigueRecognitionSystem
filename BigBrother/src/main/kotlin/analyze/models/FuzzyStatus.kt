package analyze.models


data class FuzzyStatus(
    val isQuantized: Double = 0.0,
    val isBored: Double = 0.0,
    val isWorkable: Double = 0.0
)