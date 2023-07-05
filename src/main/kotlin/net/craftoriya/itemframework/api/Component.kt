package net.craftoriya.itemframework.api

import net.craftoriya.itemframework.ItemFramework
import org.bukkit.inventory.ItemStack

interface Component {
}

fun Component.hasHave(item: ItemStack) = ItemFramework.hasComponent(this, item)