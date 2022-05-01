package iwkotc.tailer

import iwkotc.reflection.Factory
import iwkotc.tailer.parser.IW4x
import org.apache.commons.io.input.Tailer
import org.apache.commons.io.input.TailerListenerAdapter
import java.io.File

/**
 * ApacheTailer
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
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
            if (parsedLine?.payload?.command?.isNotEmpty() == true) {
                println(parsedLine) //Debugging
                Factory.commandHookMap[parsedLine.payload.command]?.execute(parsedLine.payload)
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}