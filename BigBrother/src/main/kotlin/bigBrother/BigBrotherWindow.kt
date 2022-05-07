package bigBrother

import loggers.keyLogger.KeyLogger
import loggers.mouseLogger.MouseLogger
import loggers.reactionTest.ReactionTestWindow
import window.Window
import java.io.File
import java.lang.Exception
import javax.swing.*

class BigBrotherWindow(title: String) : Window() {

    var currentUsername: String? = null

    var mouseLogger: MouseLogger? = null
    var keyLogger: KeyLogger? = null

    init {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (ex: Exception) {
        }

        File("${System.getProperty("user.dir")}\\data").mkdir()

        createUI(title)
    }

    private fun getInputAndStartLoggingMoves(textField: JTextField) {
        currentUsername = textField.text.trim()

        mouseLogger = MouseLogger(currentUsername!!)
        keyLogger = KeyLogger(currentUsername!!)

        mouseLogger?.start()
        keyLogger?.start()
    }

    private fun stopWork() {
        mouseLogger!!.stop()
        keyLogger!!.stop()
    }

    private fun createUI(title: String) {

        setTitle(title)

        val input = JTextField("Фамилия_Имя_(Ваш факультет)х-хх")
        input.horizontalAlignment = JTextField.CENTER

        val goButton = JButton("Начать слежку")
        goButton.addActionListener {
            getInputAndStartLoggingMoves(input)
            JOptionPane.showMessageDialog(
                this,
                "Наблюдаю за жизнедеятельностью",
                "Изменение статуса",
                JOptionPane.INFORMATION_MESSAGE
            )
            setTitle("BB (активен)")
        }

        val stopButton = JButton("Остановить слежку")
        stopButton.addActionListener {
            stopWork()
            JOptionPane.showMessageDialog(
                this,
                "Закончил наблюдение за жизнедеятельностью",
                "Изменение статуса",
                JOptionPane.INFORMATION_MESSAGE
            )
            setTitle("BB (неактивен)")
        }

        val checkReactionButton = JButton("Проверить реакцию")
        checkReactionButton.addActionListener {
            val frame = ReactionTestWindow("Тест реакции", currentUsername ?: input.text.trim())
            frame.isVisible = true
        }

        createLayout(input, goButton, stopButton, checkReactionButton)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(400, 200)
        setLocationRelativeTo(null)
    }
}