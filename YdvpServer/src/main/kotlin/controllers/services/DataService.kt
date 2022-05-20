package controllers.services

import data.CharRepositoryImpl
import domain.dtos.AcceptMeasurementsListDTO
import domain.dtos.MeasurementDTO
import domain.dtos.MeasurementData
import domain.dtos.MeasurementDataWithoutTime
import domain.logicentities.DSDataAccessInfo
import domain.logicentities.DSDataAddInfo
import domain.logicentities.DSMeasurement
import domain.logicentities.DSMeasurementList
import org.koin.java.KoinJavaComponent.inject
import java.time.Instant

class DataService {
    private val charRepository by inject<CharRepositoryImpl>(CharRepositoryImpl::class.java)

    private fun getMeasurement(
        bucketName: String,
        charName: String
    ): List<DSMeasurement> {
        val gotInformation = charRepository.get(
            DSDataAccessInfo(
                bucketName,
                Pair(0, 0),
                charName
            )
        )

        return gotInformation.map { DSMeasurement(charName, it.value, it.time) }
    }

    fun getMeasurements(
        bucketName: String,
        requiredNames: List<String>
    ): List<MeasurementDTO> {
        val outMeasurements: MutableList<MeasurementDTO> =
            mutableListOf()

        for (charName in requiredNames) {
            outMeasurements.add(
                MeasurementDTO(
                    charName, getMeasurement(bucketName, charName).map {
                        MeasurementData(
                            it.value, it.time
                        )
                    }
                )
            )
        }

        return outMeasurements
    }

    private fun sendMeasurement(
        bucketName: String,
        charName: String,
        chars: List<MeasurementDataWithoutTime>
    ) {
        charRepository.add(
            DSDataAddInfo(
                bucketName, DSMeasurementList
                    (
                    charName,
                    chars.map {
                        DSMeasurement(
                            charName,
                            it.value,
                            Instant.EPOCH
                        )
                    })
            )
        )
    }

    fun sendMeasurements(
        bucketName: String,
        chars: AcceptMeasurementsListDTO
    ) {
        for (measurement in chars.measurements) {
            sendMeasurement(
                bucketName,
                measurement.measurement, measurement.values
            )
        }
    }
}