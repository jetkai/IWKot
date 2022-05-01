package iwkotc.tailer.parser

/**
 * IW4x
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
class IW4x {

    fun parse(line : String): IW4xData? {

      //  var lineNumber : Long
        val lineText : String
        val payload = IWKotPayload("", "")

        if (!line.contains(":"))
            return null

        //Line Number
        val index2 = line.indexOf(":") + 3
       /* val index1 = line.indexOf("[") + 1
        val index2 = line.indexOf("]")
        lineNumber = try {
            val parsedLineNumber = line.substring(index1, index2).replace(" ", "").toLong()
            parsedLineNumber
        } catch (nfe : NumberFormatException) {
            println(nfe.message)
            -1L
        }*/

        //Line Text
        val lineTextPosition = index2 + 1
        val parsedLineText = line.substring(lineTextPosition)
        lineText = parsedLineText

        //IWKot Command
        val isIWKotCommand = line.substring(lineTextPosition, lineTextPosition + 2) == "++"
        if(isIWKotCommand) {
            val lineEndOfCommand = line.indexOf("::")
            val command = line.substring(lineTextPosition + 2, lineEndOfCommand)
            val data = line.substring(lineEndOfCommand + 2)
            payload.command = command
            payload.data = data
        }

        return IW4xData(lineText, payload)
    }

}

data class IW4xData(val lineText : String, val payload : IWKotPayload)

data class IWKotPayload(var command : String, var data : String)