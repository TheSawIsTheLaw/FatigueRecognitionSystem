package analyze.models


data class FuzzyStatus(
    val isQuantized: Double = 0.0,
    val isBored: Double = 0.0,
    val isWorkable: Double = 0.0
) {

    fun isValid(): Boolean {
        return (isQuantized > 0.0 || isBored > 0.0 || isWorkable > 0.0) &&
                (isQuantized != isBored && isBored != isWorkable && isQuantized != isWorkable)
    }
}