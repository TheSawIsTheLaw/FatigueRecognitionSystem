package bbConverter

import bbParser.models.KeyModel
import bbParser.models.Model
import kotlin.collections.HashMap

class KeysConverter : Converter {

    private fun checkIfListOfKeys(listToCheck: List<Model>): Boolean {
        listToCheck.forEach {
            if (it !is KeyModel) return false
        }

        return true
    }

    override fun convert(keys: List<Model>): HashMap<Pair<Long, Long>, Int> {
        val map = hashMapOf<Pair<Long, Long>, Int>()
        if (checkIfListOfKeys(keys)) {
            val workKeys = (keys as List<KeyModel>).sortedBy { it.mTimestamp }
//          way with checks in moments
//            var beginTimestamp = keys.first().mTimestamp
//            var passedKeys = 1
//            for (i in 1 until keys.size - 1) {
//                passedKeys++
//                if (keys[i].mTimestamp - keys[i + 1].mTimestamp > TIME_DELAY_CHECK_MILLIS) {
//                    map[Pair(beginTimestamp, keys[i].mTimestamp)] =
//                        passedKeys.toFloat() / (keys[i].mTimestamp - beginTimestamp).toFloat() * 1000 * 60
//                    passedKeys = 0
//                    beginTimestamp = keys[i + 1].mTimestamp
//                }
//            }

            var currentTimestamp = keys.first().mTimestamp
            var passedKeys = 1
            for (i in 1 until keys.size - 1) {
                passedKeys++
//                println(workKeys[i].mKeyName)
//                println("${workKeys[i + 1].mTimestamp} - $currentTimestamp = ${workKeys[i + 1].mTimestamp - currentTimestamp}")
                if (workKeys[i + 1].mTimestamp - currentTimestamp > 1000 * 60) {
                    map[Pair(currentTimestamp, workKeys[i].mTimestamp)] = passedKeys
                    passedKeys = 0
                    currentTimestamp = keys[i + 1].mTimestamp
                }
            }
        }

        return map
    }
}