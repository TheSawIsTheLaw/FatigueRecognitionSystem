package dateFormat

import java.time.format.DateTimeFormatter

object DateFormatter {

    val dateFormat by lazy { DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm:ss.ms") }
}