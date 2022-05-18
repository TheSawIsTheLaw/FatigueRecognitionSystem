package analyze.analyzers

import bbParser.models.Model

interface Analyzer {

    fun prepareModel(fDim: List<Model>, sDim: List<Model>): Boolean

    fun analyzeByModel(values: List<Model>): List<Double>
}