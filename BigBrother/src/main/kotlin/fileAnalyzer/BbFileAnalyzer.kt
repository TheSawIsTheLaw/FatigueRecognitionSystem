package fileAnalyzer

import analyze.analyzers.Analyzer
import bbParser.parsers.BbParser

class BbFileAnalyzer(
    modelPathFileFDimension: String,
    modelPathFileSDimension: String,
    private val mMainParser: BbParser,
    slaveParser: BbParser,
    private val mAnalyzer: Analyzer
) {

    init {
        val fDim = mMainParser.parseFile(modelPathFileFDimension)
        val sDim = slaveParser.parseFile(modelPathFileSDimension)

        mAnalyzer.prepareModel(fDim, sDim)
    }

    fun analyzeByFile(filepath: String): String {
        val dim = mMainParser.parseFile(filepath)

        val verdictList = mAnalyzer.analyzeByModel(dim)

        return when (verdictList.indexOf(verdictList.minOrNull())) {
            0 -> "Quantized"
            1 -> "Bored"
            else -> "Workable"
        }
    }
}