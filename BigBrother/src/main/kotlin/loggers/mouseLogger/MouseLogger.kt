package loggers.mouseLogger

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener
import loggers.Logger
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class MouseLogger(username: String) : Logger, NativeMouseInputListener {

    private val mPathToFileForMoves = "${System.getProperty("user.dir")}/data/${username}_Mouse_Moves.txt"
    private val mPathToFileForClicks = "${System.getProperty("user.dir")}/data/${username}_Mouse_Clicks.txt"

    @Volatile
    private var mFileForMoves = File(mPathToFileForMoves)

    @Volatile
    private var mFileForClicks = File(mPathToFileForClicks)

    @Volatile
    private var queueOfMoves = LinkedList<String>()

    @Volatile
    private var queueOfClicks = LinkedList<String>()

    private fun setFiles() {
        if (!mFileForMoves.exists()) mFileForMoves.createNewFile()
        if (!mFileForClicks.exists()) mFileForClicks.createNewFile()
    }

    private fun clearToFile(queue: LinkedList<String>, file: File) {
        queue.forEach {
            file.appendText(it)
        }
        queue.clear()
    }

    override fun start() {
        try {
            if (!GlobalScreen.isNativeHookRegistered())
                GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            exitProcess(1)
        }

        setFiles()

        GlobalScreen.addNativeMouseListener(this)
        GlobalScreen.addNativeMouseMotionListener(this)
    }

    override fun stop() {
        clearToFile(queueOfClicks, mFileForClicks)
        clearToFile(queueOfMoves, mFileForMoves)

        try {
            GlobalScreen.removeNativeMouseListener(this)
            GlobalScreen.removeNativeMouseMotionListener(this)
            GlobalScreen.unregisterNativeHook()
        } catch (e: Exception) {
        }
    }

    override fun nativeMouseClicked(e: NativeMouseEvent?) {
        queueOfClicks.add(
            "x=${e!!.x}, y=${e.y}, key=${e.button}," +
                    " timestamp=${System.currentTimeMillis()}\n"
        )

        if (queueOfClicks.size == 100) {
            clearToFile(queueOfClicks, mFileForClicks)
        }
    }

    override fun nativeMousePressed(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseReleased(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseMoved(e: NativeMouseEvent?) {
        queueOfMoves.add(
            "x=${e!!.x}, y=${e.y}," +
                    " timestamp=${System.currentTimeMillis()}\n"
        )

        if (queueOfMoves.size == 100) {
            clearToFile(queueOfMoves, mFileForMoves)
        }
    }

    override fun nativeMouseDragged(p0: NativeMouseEvent?) {

    }

}