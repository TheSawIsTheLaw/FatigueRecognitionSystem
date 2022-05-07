package loggers.reactionTest

import random.BBRandom
import window.Window
import java.awt.Font
import java.lang.Thread.sleep
import javax.swing.*
import kotlin.concurrent.thread

class ReactionTestWindow(title: String, username: String) : Window() {

    @Volatile
    private var testButtonPressed = false

    @Volatile
    private var reactionTimestamp = 0L

    private var reactionsTotal = 0L

    private var reactionLogger: ReactionLogger

    init {
        reactionLogger = ReactionLogger(username)

        reactionLogger.start()

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
        testButton.isVisible = true

        val spacer = JLabel(" ")
        spacer.font = Font("Arial", Font.PLAIN, 50)

        val startButton = JButton("Начать тест")
        startButton.addActionListener {
            startButton.isVisible = false
            thread {
                var startTime: Long
                for (i in 0 until NUMBER_OF_TESTS) {
                    sleep((3 + BBRandom.random.nextInt(8).toLong()) * 1000)
                    testButton.isVisible = true
                    startTime = System.currentTimeMillis()
                    while (!testButtonPressed) {
                        sleep(20)
                    }
                    reactionsTotal += reactionTimestamp - startTime
                            testButton.isVisible = false
                    testButtonPressed = false
                }

                this.isVisible = false

                reactionLogger.addRecord(reactionsTotal / NUMBER_OF_TESTS)
            }
        }


        createLayout(testButton, spacer, startButton)

        testButton.isVisible = false

        setLocationRelativeTo(null)
    }

    companion object {
        private const val NUMBER_OF_TESTS = 10
    }
}