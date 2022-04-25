package iwkotc.util

import java.util.*
import java.util.concurrent.ThreadFactory

/**
 * @author Kai
 */
class IWKotThreadFactory (private val name : String) : ThreadFactory {

    private val threads : MutableList<Thread> = LinkedList()

    //Creates a new thread
    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(r)
        threads.add(thread)
        println("\rTotal Threads: "+threads.size)
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _: Thread?, _: Throwable?
            -> println("[ALERT] ONE OF THE PROXY THREADS HAVE CRASHED") }
        return thread
    }

    fun interruptThread(thread : Thread) {
        thread.interrupt()
    }

    fun interruptAllThreads() {
        threads.forEach { thread -> thread.interrupt() }
    }

}