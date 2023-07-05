package net.craftoriya.itemframework.impl

import de.tr7zw.nbtapi.NBTItem
import net.craftoriya.itemframework.ItemFramework
import net.craftoriya.itemframework.api.Component
import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.api.NBT_COMPONENTS_KEY
import net.kyori.adventure.key.Key
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

/***
 * Базовий клас для компонентів.
 * Компонент представляє собою об'єкт який описує поведінку предмета. Цілий предмет складається з
 * колекції компонентів, що представляють його унікальну поведінку.
 *
 * Конструктор компонента - це пропси, які є вхідними для ініціалізації компненту
 * Данні компонента - це данні які зберігаються на предметі (тобто в NBT), і які можна отримати з нього, чи напроти записати (взаємодія з данними)
 */
abstract class BaseComponent : Component, Listener {
    internal var id: Key = Key.key("");

    open fun applyComponent(bundle: ComponentBundle, itemStack: ItemStack) {} //maybe changed by child

    fun mark(itemStack: ItemStack) {
        val nbtItem = NBTItem(itemStack)
        nbtItem.getStringList(NBT_COMPONENTS_KEY).apply {
            if (!contains(generateComponentID(this@BaseComponent))) {
                add(generateComponentID(this@BaseComponent))
            }
        }
        nbtItem.mergeNBT(itemStack)
    }

    inline fun <reified S : Any> loadData(itemStack: ItemStack): S {
        val nbtItem = NBTItem(itemStack)
        val compound = nbtItem.getCompound(NBT_COMPONENTS_DATA_KEY)
        val json = compound.getString(generateComponentID(this))
        val data = GSON.fromJson(json, S::class.java)
        return data
    }


    inline fun <reified S : Any> writeData(itemStack: ItemStack, data: S) {
        val nbtItem = NBTItem(itemStack)
        val compound = nbtItem.getCompound(NBT_COMPONENTS_DATA_KEY)
        val json = GSON.toJson(data, S::class.java)
        compound.setString(generateComponentID(this), json)

        nbtItem.mergeNBT(itemStack)
    }

    open fun <S : Any?> loadNbtData(itemStack: ItemStack): S? {
        return null as S
    }

    open fun <S : Any> writeNbtData(itemStack: ItemStack, data: S) {
        return
    }
}

fun <T : BaseComponent> generateComponentID(objectt: T): String {
    return objectt::class.simpleName!!.lowercase();
}