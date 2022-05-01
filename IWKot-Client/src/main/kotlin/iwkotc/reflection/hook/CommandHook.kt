package iwkotc.reflection.hook

import iwkotc.tailer.parser.IWKotPayload

/**
 * CommandHook
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
interface CommandHook {

    fun execute(payload : IWKotPayload): Boolean

}