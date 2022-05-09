package bbParser.models

import java.util.*

abstract class Model(val mTimestamp: Long) {

    override fun toString(): String {
        return "timestamp=$mTimestamp]"
    }
}