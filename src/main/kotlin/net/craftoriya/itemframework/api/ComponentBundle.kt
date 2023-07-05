package net.craftoriya.itemframework.api

import me.nitkanikita.zomboid.utils.Registry
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack

const val NBT_COMPONENTS_KEY = "itemframework~~component_bundle"
const val NBT_BUNDLE_KEY = "itemframework~~list_component_bundles"
interface ComponentBundle : Cloneable {
    companion object {
        val registry: Registry<ComponentBundle> = Registry.createRegistry(Key.key("itemframework:component_bundles"))
    }

    val components: List<Component>
    fun addComponent(component: Component)
    fun addComponent(component: Component, index: Int)

    fun createItemStack(base: ItemStack): ItemStack
    fun applyToItemStack(stack: ItemStack)

    fun <D: Any> getTempData(key: String): D?
    fun <D : Any> setTempData(key: String, data: D)
    fun getTempAllData(key: String): ComponentBundleRawData
    fun initTempDataIfNotExists(key: String, data: Any)
}

typealias ComponentBundleRawData = MutableMap<String, Any>;

inline fun <reified T : Component> ComponentBundle.getComponent(): T? {
    return components.find { c -> c::class == T::class } as T?
}