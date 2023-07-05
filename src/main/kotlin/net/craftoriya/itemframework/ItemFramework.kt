package net.craftoriya.itemframework

import de.tr7zw.nbtapi.NBTItem
import net.craftoriya.ItemFrameworkPlugin
import net.craftoriya.itemframework.api.Component
import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.api.NBT_BUNDLE_KEY
import net.craftoriya.itemframework.api.NBT_COMPONENTS_KEY
import net.craftoriya.itemframework.impl.BaseComponent
import net.craftoriya.itemframework.impl.ComponentBundleImpl
import net.craftoriya.itemframework.impl.generateComponentID
import net.kyori.adventure.key.Key
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

object ItemFramework {
    internal val componentInstancesMap: MutableMap<String, BaseComponent> = mutableMapOf()


    fun createBundle(id: Key, vararg components: Component): ComponentBundle {
        val componentBundleImpl = ComponentBundleImpl(id)
        for (component in components) {
            componentBundleImpl.addComponent(component)
        }

        return componentBundleImpl;
    }

    fun registerComponent(component: Component) {
        componentInstancesMap[generateComponentID(component as BaseComponent)] =
            component as BaseComponent
        Bukkit.getPluginManager().registerEvents(component, ItemFrameworkPlugin.instance)
    }

    fun getBundle(itemStack: ItemStack): ComponentBundle {
        val nbtItem = NBTItem(itemStack)
        val newBundle = ComponentBundleImpl()

        nbtItem.getStringList(NBT_COMPONENTS_KEY).filter {
            val comp = componentInstancesMap[it]
            return@filter comp != null
        }.map {
            return@map componentInstancesMap[it] as Component
        }.forEach(newBundle::addComponent)

        return newBundle
    }

    fun hasBundle(itemStack: ItemStack): Boolean {
        val nbtItem = NBTItem(itemStack)
        return nbtItem.hasTag(NBT_BUNDLE_KEY)
    }

    fun hasComponent(component: Component, itemStack: ItemStack): Boolean {
        return getBundle(itemStack).components.contains(component)
    }

    inline fun <reified C : Component> hasAnyComponent(itemStack: ItemStack): Boolean {
        return getBundle(itemStack).components.first { it::class == C::class } != null
    }
}

fun ItemStack.createBundle(id: Key, vararg components: Component) = ItemFramework.createBundle(id, *components).let {
    it.createItemStack(this)
}

fun ItemStack.applyBundle(bundle: ComponentBundle) = bundle.applyToItemStack(this)
fun ItemStack.hasComponent(component: Component) = ItemFramework.hasComponent(component, this)
inline fun <reified C : Component> ItemStack.hasAnyComponent() = ItemFramework.hasAnyComponent<C>(this)
fun ItemStack.hasBundle() = ItemFramework.hasBundle(this)
fun ItemStack.getBundle() = ItemFramework.getBundle(this)