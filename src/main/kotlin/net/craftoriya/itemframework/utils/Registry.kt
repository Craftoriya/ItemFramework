package me.nitkanikita.zomboid.utils

import net.kyori.adventure.key.Key
import org.slf4j.LoggerFactory

class Registry<T> private constructor(private val id: Key) {
    companion object {
        private val logger = LoggerFactory.getLogger("Registry")

        /*Registry registering logic*/
        private val registries = Registry<Registry<*>>(Key.key("registry", "registry"))
        fun <R> createRegistry(id: Key): Registry<R> {
            val registry = Registry<R>(id)
            registries.items[id] = registry

            return registry
        }
    }

    /*Logic*/
    private val items: MutableMap<Key, T> = mutableMapOf()

    fun getItems() = items.values
    fun getKeys() = items.keys

    fun get(id: Key) = items[id]

    fun register(id: Key, item: T): T {
        if (items.containsKey(id)) {
            logger.warn("Item with id \"$id\" is already registered. Item override")
        }
        if (items.containsValue(item)) {
            logger.warn("Item \"$id\" is already registered with other id. Item duplicated!")
        }
        items[id] = item
        logger.info("Item \"$id\" registered to ${this.id}")
        return item
    }
}

fun String.toKey() = Key.key(this.split(":")[0], this.split(":")[1]);