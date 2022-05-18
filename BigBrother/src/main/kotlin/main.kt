import bigBrother.BigBrotherWindow
import java.awt.EventQueue

private fun createAndShowGUI() {
    val frame = BigBrotherWindow("BB (Неактивен)")
    frame.isVisible = true
}

fun main(args: Array<String>) {
    EventQueue.invokeLater(::createAndShowGUI)
}