import bigBrother.BigBrotherWindow
import java.awt.EventQueue

class BigBrother {

    companion object {
        private fun createAndShowGUI() {
            val frame = BigBrotherWindow("BB (Неактивен)")
            frame.isVisible = true
        }

        @JvmStatic
        fun main(args: Array<String>) {
            EventQueue.invokeLater(::createAndShowGUI)
        }
    }
}