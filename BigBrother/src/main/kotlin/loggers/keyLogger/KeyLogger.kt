package loggers.keyLogger

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import loggers.Logger
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.io.File
import java.util.LinkedList
import kotlin.system.exitProcess

class KeyLogger(username: String) : Logger, NativeKeyListener {
    private val mPathToFile = "${System.getProperty("user.dir")}/data/${username}_Keys.txt"

    private var mFile = File(mPathToFile)

    @Volatile
    private var queueToWrite = LinkedList<String>()

    private fun setFile() {
        if (!mFile.exists()) mFile.createNewFile()
    }

    override fun start() {
        try {
            if (!GlobalScreen.isNativeHookRegistered()) GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        setFile()

        GlobalScreen.addNativeKeyListener(this)
    }

    private fun clearQueue() {
        queueToWrite.forEach {
            mFile.appendText(it)
        }
        queueToWrite.clear()
    }

    override fun stop() {
        clearQueue()

        try {
            GlobalScreen.removeNativeKeyListener(this)
            GlobalScreen.unregisterNativeHook()
        } catch (e: Exception) {
        }
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        if (!e.isActionKey) {
            queueToWrite.add(
                "key=${NativeKeyEvent.getKeyText(e.keyCode)}," + " timestamp=${System.currentTimeMillis()}\n"
            )
            if (queueToWrite.size == 100) {
                clearQueue()
            }
        }
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }
}