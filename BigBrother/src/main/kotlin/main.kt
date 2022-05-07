import bigBrother.BigBrotherWindow
import java.awt.EventQueue
import javax.swing.UIManager

private fun createAndShowGUI() {
    val frame = BigBrotherWindow("BB (Неактивен)")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}