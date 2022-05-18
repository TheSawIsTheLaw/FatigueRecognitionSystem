package bbConverter

import bbConverter.Converter.Constants.MILLIS_IN_MINUTE
import bbParser.models.KeyModel
import bbParser.models.Model
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class KeysConverter : Converter {

    /**
     *  Возвращает значения в виде:
     *  "(время первого клика - время последнего клика) - количество введённых символов"
     */
    override fun convert(keys: List<Model>): HashMap<Pair<Long, Long>, Int> {
        val out = hashMapOf<Pair<Long, Long>, Int>()

        val sortedKeys = (keys as List<KeyModel>).sortedBy { it.mTimestamp }

        var currentTimestamp = sortedKeys.first().mTimestamp
        var passedKeys = 1
        for (i in 1 until sortedKeys.size) {
            passedKeys++
            if (sortedKeys[i].mTimestamp - currentTimestamp > MILLIS_IN_MINUTE) {
                out[Pair(currentTimestamp, sortedKeys[i - 1].mTimestamp)] = passedKeys
                passedKeys = 1
                currentTimestamp = sortedKeys[i].mTimestamp
            }
        }

        out[Pair(currentTimestamp, sortedKeys.last().mTimestamp)] = passedKeys

        return out
    }
}