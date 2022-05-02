package loggers.reactionTest

import java.awt.Font
import java.lang.Thread.sleep
import java.util.*
import javax.swing.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class ReactionTestWindow(title: String) : JFrame() {

    private val NUMBER_OF_TESTS = 10

    private var numberOfPassed = 0

    private val random = Random()

    @Volatile
    private var testButtonPressed = false

    @Volatile
    private var reactionTimestamp = 0L

    private var reactionsTotal = 0L

    init {
        createUI(title)
    }

    private fun createUI(title: String) {

        setTitle(title)

        val testButton = JButton("Жать сюда!")
        testButton.font = Font("Arial", Font.PLAIN, 50)
        testButton.addActionListener {
            reactionTimestamp = System.currentTimeMillis()
            testButtonPressed = true
        }

        val spacer = JLabel(" ")
        spacer.font = Font("Arial", Font.PLAIN, 50)

        val startButton = JButton("Начать тест")
        startButton.addActionListener {
            testButton.isVisible = false
            thread {
                var startTime: Long
                for (i in 0 until NUMBER_OF_TESTS) {
                    sleep((3 + random.nextInt(8).toLong()) * 1000)
                    testButton.isVisible = true
                    startTime = System.currentTimeMillis()
                    while (!testButtonPressed) {
                        sleep(300)
                    }
                    reactionsTotal += startTime - reactionTimestamp
                    testButton.isVisible = false
                    testButtonPressed = false
                }
            }
        }

        createLayout(testButton, spacer, startButton)

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