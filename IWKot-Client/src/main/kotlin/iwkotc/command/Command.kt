package iwkotc.command

interface Command {

    fun execute()

    fun handleResponse(response : String)

}