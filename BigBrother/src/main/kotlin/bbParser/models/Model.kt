package bbParser.models

import java.util.*

abstract class Model(val mTimestamp: Date) {
    override fun toString(): String {
        return "timestamp=$mTimestamp]"
    }
}