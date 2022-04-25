package iwkotc.tailer

import iwkotc.command.SendConnection
import iwkotc.tailer.parser.IW4x
import org.apache.commons.io.input.Tailer
import org.apache.commons.io.input.TailerListenerAdapter
import java.io.File

class ApacheTailer(filePath : String) {

    private val parser = IW4x()

    private var listener : TailerListener = TailerListener(parser)
    private var tailer : Tailer = Tailer.create(File(filePath), listener, 500)

    fun run() {
        try {
            tailer.run()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

} class TailerListener(private val parser : IW4x) : TailerListenerAdapter() {

    override fun handle(line : String) {
        try {
            val parsedLine = parser.parse(line)
            if (parsedLine != null) {
                println(parsedLine) //Debugging

                //TODO - Temp, use reflection to clean this up
                if(parsedLine.payload.command == "join") {
                    SendConnection(parsedLine.payload).execute()
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}