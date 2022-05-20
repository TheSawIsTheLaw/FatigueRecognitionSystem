package domain.dtos

import java.time.Instant

data class MeasurementData(
    val value: String,
    val time: Instant
)

data class MeasurementDTO(
    val measurement: String,
    val values: List<MeasurementData>
)

data class ResponseMeasurementsDTO(
    val measurementsList: List<MeasurementDTO>
)

data class MeasurementDataWithoutTime(
    val value: String
)

data class AcceptMeasurementsDTO(
    val measurement: String,
    val values: List<MeasurementDataWithoutTime>
)

data class AcceptMeasurementsListDTO(
    val measurements: List<AcceptMeasurementsDTO>
)

data class MeasurementsNamesDTO(
    val measurementsNames: List<String>
)