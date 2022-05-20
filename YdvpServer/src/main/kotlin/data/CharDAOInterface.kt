package data

import domain.logicentities.DSMeasurement
import domain.logicentities.DSMeasurementList

interface CharDAOInterface {
    fun get(
        connection: InfluxConnection,
        timeRange: Pair<Int, Int>,
        bucket: String,
        measurement: String
    ): List<DSMeasurement>

    fun add(connection: InfluxConnection, bucket: String, measurementList: DSMeasurementList)
}