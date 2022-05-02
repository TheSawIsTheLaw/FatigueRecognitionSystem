package loggers.reactionTest

import java.io.File

class ReactionLogger(username: String) {

    private val pathToFile = "${System.getProperty("user.dir")}\\data\\${username}_Keys.txt"

    private val mFile = File(pathToFile)

    private fun setFile() {
        if (!mFile.exists()) mFile.createNewFile()
    }

    init {

    }

    fun stop() {

    }
}