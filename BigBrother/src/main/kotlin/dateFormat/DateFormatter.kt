package dateFormat

import java.text.SimpleDateFormat

object DateFormatter {
    val dateFormat by lazy { SimpleDateFormat("dd/M/yyyy HH:mm:ss") }
}