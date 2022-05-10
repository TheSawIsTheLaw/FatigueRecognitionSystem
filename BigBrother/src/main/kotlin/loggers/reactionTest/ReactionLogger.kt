package loggers.reactionTest

import loggers.Logger
import java.io.File

class ReactionLogger(username: String) : Logger {

    private val mPathToFile = "${System.getProperty("user.dir")}/data/${username}_Reactions.txt"

    private val mFile = File(mPathToFile)

    private fun setFile() {
        if (!mFile.exists()) mFile.createNewFile()
    }

    override fun start() {
        setFile()
    }

    fun addRecord(resultInMillis: Long) {
        mFile.appendText(
            "reaction_time=${resultInMillis}, " +
                    "timestamp=${System.currentTimeMillis()}\n"
        )
    }

    override fun stop() {}
}