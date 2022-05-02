package bigBrother

import loggers.keyLogger.KeyLogger
import loggers.mouseLogger.MouseLogger
import loggers.reactionTest.ReactionLogger
import loggers.reactionTest.ReactionTestWindow
import java.awt.EventQueue
import java.io.File
import javax.swing.*

class BigBrotherWindow(title: String) : JFrame() {

    var currentUsername: String? = null

    var mouseLogger: MouseLogger? = null
    var keyLogger: KeyLogger? = null
    var reactionLogger: ReactionLogger? = null

    init {
        createUI(title)
    }

    private fun getInputAndStartWork(textField: JTextField) {
        currentUsername = textField.text.trim()

        File("${System.getProperty("user.dir")}\\data").mkdir()

        mouseLogger = MouseLogger(currentUsername!!)
        keyLogger = KeyLogger(currentUsername!!)
    }

    private fun stopWork() {
        mouseLogger?.stop()
        keyLogger?.stop()
    }

    private fun createUI(title: String) {

        setTitle(title)

        val input = JTextField("Фамилия_Имя_(Ваш факультет)х-хх")

        val goButton = JButton("Начать слежку")
        goButton.addActionListener {
            getInputAndStartWork(input)
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
            val frame = ReactionTestWindow("Тест реакции")
            frame.isVisible = true
        }

        createLayout(input, goButton, stopButton, checkReactionButton)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(400, 200)
        setLocationRelativeTo(null)
    }


    private fun createLayout(vararg components: JComponent) {

        val gl = GroupLayout(contentPane)
        contentPane.layout = gl

        gl.autoCreateContainerGaps = true

        val horizontalGroup = gl.createParallelGroup()
        components.forEach { horizontalGroup.addComponent(it) }

        gl.setHorizontalGroup(horizontalGroup)


        val verticalGroup = gl.createSequentialGroup()
        components.forEach { verticalGroup.addComponent(it) }

        gl.setVerticalGroup(verticalGroup)

        pack()
    }
}

private fun createAndShowGUI() {

    val frame = BigBrotherWindow("BB (Неактивен)")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}