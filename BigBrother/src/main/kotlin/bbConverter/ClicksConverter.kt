package bbConverter

import bbParser.models.Model
import bbParser.models.MouseClickModel
import kotlin.math.sqrt

@Suppress("UNCHECKED_CAST")
class ClicksConverter : Converter {

    /**
     *  Возвращает значения в виде:
     *  "(время первого клика - время последнего клика) - пройденное расстояние в пикселях"
     */
    override fun convert(clicks: List<Model>): HashMap<Pair<Long, Long>, Int> {
        val out = HashMap<Pair<Long, Long>, Int>()

        val sortedClicks = (clicks as List<MouseClickModel>).sortedBy { it.mTimestamp }

        var prevClick: MouseClickModel
        var curClick: MouseClickModel
        for (i in 1 until sortedClicks.size) {
            prevClick = sortedClicks[i - 1]
            curClick = sortedClicks[i]
            out[Pair(prevClick.mTimestamp, curClick.mTimestamp)] =
                sqrt(
                    ((curClick.mXCoordinate - prevClick.mXCoordinate) *
                            (curClick.mXCoordinate - prevClick.mXCoordinate) +
                            (curClick.mYCoordinate - prevClick.mYCoordinate) *
                            (curClick.mYCoordinate - prevClick.mYCoordinate)).toDouble()
                ).toInt()
        }

        return out
    }
}