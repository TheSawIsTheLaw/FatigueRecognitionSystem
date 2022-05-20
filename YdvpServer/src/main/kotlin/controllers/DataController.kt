package controllers

import com.influxdb.exceptions.NotFoundException
import controllers.services.DataService
import domain.dtos.*
import domain.response.ResponseCreator
import org.koin.java.KoinJavaComponent.inject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

//@RequestMapping("/api/v1/data")
class DataController {
    private val dataService by inject<DataService>(DataService::class.java)

    //    @GetMapping
    fun getData(username: String, measurementsNames: List<String>): ResponseEntity<*> {
        val outList: List<MeasurementDTO>
        try {
            outList =
                dataService.getMeasurements(
                    username,
                    measurementsNames
                )
        }
        catch (exc: NotFoundException) {
            return ResponseCreator.userNotFoundResponse("USER NOT FOUND", "Add some data for user at first")
        }
        catch (exc: Exception) {
            return ResponseCreator.internalServerErrorResponse(
                "Data server is dead :(",
                "Let's dance on its grave!"
            )
        }

        return ResponseEntity(
            ResponseMeasurementsDTO(outList),
            HttpStatus.OK
        )
    }

    //    @PostMapping
    fun addData(username: String, measurementsList: AcceptMeasurementsListDTO): ResponseEntity<*> {
        try {
            dataService.sendMeasurements(username, measurementsList)
        } catch (exc: Exception) {
            println(exc.message)
            return ResponseCreator.internalServerErrorResponse(
                "Data server is dead :(",
                "Let's dance on its grave!"
            )
        }

        return ResponseCreator.okResponse(
            "Measurements were carefully sent",
            "We know all about you now >:c"
        )
    }
}