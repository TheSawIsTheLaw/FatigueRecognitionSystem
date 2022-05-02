package loggers.mouseLogger

import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class MouseLogger(username: String) : NativeMouseInputListener {

    private val mDateFormat = SimpleDateFormat("dd/M/yyyy HH:mm:ss.SSS")

    private val pathToFileForMoves = "${System.getProperty("user.dir")}\\data\\${username}_Mouse_Moves.txt"
    private val pathToFileForClicks = "${System.getProperty("user.dir")}\\data\\${username}_Mouse_Clicks.txt"

    private var fileForMoves: File = File(pathToFileForMoves)
    private var fileForClicks: File = File(pathToFileForClicks)

    private fun setFiles() {
        if (!fileForMoves.exists()) fileForMoves.createNewFile()
        if (!fileForClicks.exists()) fileForClicks.createNewFile()
    }

    init {
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

    fun stop() {
        GlobalScreen.removeNativeMouseListener(this)
        GlobalScreen.unregisterNativeHook()
    }

    override fun nativeMouseClicked(p0: NativeMouseEvent?) {
        fileForClicks.appendText("x=${p0!!.x}, y=${p0.y}, key=${p0.button}, timestamp=${mDateFormat.format(Date())}\n")
    }

    override fun nativeMousePressed(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseReleased(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseMoved(p0: NativeMouseEvent?) {
        fileForMoves.appendText("x=${p0!!.x}, y=${p0.y}, timestamp=${mDateFormat.format(Date())}\n")
    }

    override fun nativeMouseDragged(p0: NativeMouseEvent?) {

    }

}