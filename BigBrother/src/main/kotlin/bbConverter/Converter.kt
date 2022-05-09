package bbConverter

import bbParser.models.Model
import java.util.Date
import kotlin.collections.HashMap

interface Converter {
    fun convert(models: List<Model>): HashMap<Pair<Long, Long>, Int>

    companion object Constants {
        const val TIME_DELAY_CHECK_MILLIS = 100
    }
}