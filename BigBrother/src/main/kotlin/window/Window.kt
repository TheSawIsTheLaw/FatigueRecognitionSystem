package window

import javax.swing.GroupLayout
import javax.swing.JComponent
import javax.swing.JFrame

open class Window: JFrame() {
    fun createLayout(vararg components: JComponent) {
        val gl = GroupLayout(contentPane)
        contentPane.layout = gl

        gl.autoCreateContainerGaps = true

        val horizontalGroup = gl.createParallelGroup(GroupLayout.Alignment.CENTER)
        components.forEach { horizontalGroup.addComponent(it) }

        gl.setHorizontalGroup(horizontalGroup)


        val verticalGroup = gl.createSequentialGroup()
        components.forEach { verticalGroup.addComponent(it) }

        gl.setVerticalGroup(verticalGroup)

        pack()
    }
}