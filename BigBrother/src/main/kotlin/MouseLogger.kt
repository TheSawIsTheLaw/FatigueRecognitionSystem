import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import kotlin.system.exitProcess

class MouseLogger(val username: String) : NativeMouseInputListener {
    init {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        GlobalScreen.addNativeMouseListener(this)
        GlobalScreen.addNativeMouseMotionListener(this)
    }

    fun stop() {
        GlobalScreen.removeNativeMouseListener(this)
        GlobalScreen.unregisterNativeHook()
    }

    override fun nativeMouseClicked(p0: NativeMouseEvent?) {

    }

    override fun nativeMousePressed(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseReleased(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseMoved(p0: NativeMouseEvent?) {
        println(p0!!.x)
        println(p0.y)
    }

    override fun nativeMouseDragged(p0: NativeMouseEvent?) {

    }

}