package loggers.mouseLogger

import dateFormat.DateFormatter
import loggers.Logger
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class MouseLogger(username: String) : Logger, NativeMouseInputListener {

    private val mPathToFileForMoves = "${System.getProperty("user.dir")}\\data\\${username}_Mouse_Moves.txt"
    private val mPathToFileForClicks = "${System.getProperty("user.dir")}\\data\\${username}_Mouse_Clicks.txt"

    private var mFileForMoves: File = File(mPathToFileForMoves)
    private var mFileForClicks: File = File(mPathToFileForClicks)

    private fun setFiles() {
        if (!mFileForMoves.exists()) mFileForMoves.createNewFile()
        if (!mFileForClicks.exists()) mFileForClicks.createNewFile()
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
        GlobalScreen.unregisterNativeHook()
    }

    override fun nativeMouseClicked(p0: NativeMouseEvent?) {
        mFileForClicks.appendText(
            "x=${p0!!.x}, y=${p0.y}, key=${p0.button}," +
                    " timestamp=${DateFormatter.dateFormat.format(Date())}\n"
        )
    }

    override fun nativeMousePressed(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseReleased(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseMoved(p0: NativeMouseEvent?) {
        mFileForMoves.appendText("x=${p0!!.x}, y=${p0.y}, timestamp=${DateFormatter.dateFormat.format(Date())}\n")
    }

    override fun nativeMouseDragged(p0: NativeMouseEvent?) {

    }

}