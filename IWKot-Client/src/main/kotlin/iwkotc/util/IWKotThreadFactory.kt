package iwkotc.util

import java.util.*
import java.util.concurrent.ThreadFactory

/**
 * IWKotThreadFactory
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
class IWKotThreadFactory : ThreadFactory {

    private val threads : MutableList<Thread> = LinkedList()

    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(r)
        threads.add(thread)
        thread.uncaughtExceptionHandler =
            Thread.UncaughtExceptionHandler {
                    t : Thread, _: Throwable -> println("Thread-${t.id} has died.")
            }
        return thread
    }

}