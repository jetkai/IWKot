package iwkotc

import iwkotc.reflection.Factory
import iwkotc.tailer.ApacheTailer

/**
 * IIWKotClient
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
fun main() {
    //Uses reflection to load the classes within the command package
    Factory.init()

    //Temp path for log file, outputs command requests in here (live)
    val logPath = "C:\\Users\\Kai\\IntelliJProjects\\MW2-Server\\userraw\\logs\\server\\games_mp_local.log"

    //Using apache tailer to read lines that are added to the log file
    val tailer = ApacheTailer(logPath)
    tailer.run() //Running on single thread currently

}