package iwkotc.reflection

import iwkotc.reflection.hook.CommandHook
import org.reflections.Reflections
import java.lang.reflect.Modifier

/**
 * Factory
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
object Factory {

    val commandHookMap : HashMap<String, CommandHook> = HashMap()

    private val classes : HashMap<String, Reflection> = HashMap()

    fun init() {
        val reflectionClasses : Set<Class<out Reflection>> =
            Reflections("iwkotc.command").getSubTypesOf(Reflection::class.java)
        for (clazz in reflectionClasses) {
            if (Modifier.isAbstract(clazz.modifiers))
                continue
            try {
                val instance : Reflection = clazz.getDeclaredConstructor().newInstance()
                instance.init()
                classes[clazz.name] = instance
            } catch (t : Throwable) {
                t.printStackTrace()
                System.err.println("Failed to initialize class: " + clazz.simpleName + ".")
            }
        }
        println("Loaded " + classes.size + " classes.")
    }

    fun registerCommand(hook : CommandHook, name : String) {
        if (commandHookMap.containsKey(name))
            return System.err.println("Command $name is already registered.")
        commandHookMap[name] = hook
    }
}