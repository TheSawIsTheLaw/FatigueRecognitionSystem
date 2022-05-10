package bbConverter

import bbParser.models.Model
import bbParser.models.MouseClickModel
import kotlin.math.abs

@Suppress("UNCHECKED_CAST")
class ClicksConverter : Converter {

    override fun convert(clicks: List<Model>): HashMap<Pair<Long, Long>, Pair<Int, Int>> {
        val out = HashMap<Pair<Long, Long>, Pair<Int, Int>>()

        val sortedClicks = (clicks as List<MouseClickModel>).sortedBy { it.mTimestamp }

        var prevClick: MouseClickModel
        var curClick: MouseClickModel
        for (i in 1 until sortedClicks.size) {
            prevClick = sortedClicks[i - 1]
            curClick = sortedClicks[i]
            out[Pair(prevClick.mTimestamp, curClick.mTimestamp)] = Pair(
                abs(curClick.mXCoordinate - prevClick.mXCoordinate),
                abs(curClick.mYCoordinate - prevClick.mYCoordinate)
            )
        }

        return out
    }
}