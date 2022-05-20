package domain.logicentities

import java.time.Instant

data class DSMeasurementList(
    val name: String,
    val measurements: List<DSMeasurement>
)

data class DSMeasurement(
    val name: String,
    val value: String,
    val time: Instant
)

data class DSDataAccessInfo(
    val bucketName: String,
    val timeRange: Pair<Int, Int>,
    val measurementName: String
)

data class DSDataAddInfo(
    val bucket: String,
    val measurementList: DSMeasurementList
)

data class DSUserCredentials(
    val username: String,
    val password: String,
    val dbToken: String
)