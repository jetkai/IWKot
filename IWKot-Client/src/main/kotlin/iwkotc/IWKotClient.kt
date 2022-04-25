package iwkotc

import iwkotc.tailer.ApacheTailer

fun main() {
    //val client = Mw2HttpClient(URI.create("http://localhost:28600"), "1234")
    //println(client.requestBody())
    val tailer = ApacheTailer("C:\\Users\\Kai\\IntelliJProjects\\MW2-Server\\userraw\\logs\\server\\games_mp_local.log")
    tailer.run()

}