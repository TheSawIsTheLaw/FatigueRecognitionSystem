package bbConverter

import bbParser.models.Model
import java.util.Date
import kotlin.collections.HashMap

interface Converter {
    fun convert(models: List<Model>): HashMap<Pair<Long, Long>, Int>

    companion object Constants {
        const val MILLIS_IN_MINUTE = 1000 * 60
    }
}