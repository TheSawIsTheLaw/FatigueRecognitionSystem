package loggers.keyLogger

import loggers.Logger
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.io.File
import kotlin.system.exitProcess

class KeyLogger(username: String) : Logger, NativeKeyListener {

    private var startTime = 0L

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

        startTime = System.currentTimeMillis()

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
                    " timestamp=${startTime + e.`when`}\n"
        )
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }
}