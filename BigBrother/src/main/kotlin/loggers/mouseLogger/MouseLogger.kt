package loggers.mouseLogger

import loggers.Logger
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
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

    private var queueOfMoves = LinkedList<String>()
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
        GlobalScreen.removeNativeMouseListener(this)
        // Causes System.exit(1) on Arch and Mint :)
        GlobalScreen.unregisterNativeHook()
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