import java.awt.EventQueue
import javax.swing.*

class BigBrotherWindow(title: String) : JFrame() {

    var currentUsername: String? = null

    init {
        createUI(title)
    }

    private fun getInputAndStartWork(textField: JTextField) {
        currentUsername = textField.text.trim()
        MouseLogger(currentUsername!!)


    }

    private fun createUI(title: String) {

        setTitle(title)

        val input = JTextField("Фамилия_Имя_(Ваш факультет)х-хх")
        val goButton = JButton("Готово")

        goButton.addActionListener {
            getInputAndStartWork(input)
        }

        createLayout(input, goButton)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(400, 200)
        setLocationRelativeTo(null)
    }


    private fun createLayout(vararg arg: JComponent) {

        val gl = GroupLayout(contentPane)
        contentPane.layout = gl

        gl.autoCreateContainerGaps = true

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
        )

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
        )

        pack()
    }
}

private fun createAndShowGUI() {

    val frame = BigBrotherWindow("Большой брат следит за тобой")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}