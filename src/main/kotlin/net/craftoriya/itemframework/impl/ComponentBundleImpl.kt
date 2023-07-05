package net.craftoriya.itemframework.impl

import com.google.gson.Gson
import de.tr7zw.nbtapi.NBTItem
import net.craftoriya.itemframework.ItemFramework
import net.craftoriya.itemframework.api.Component
import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.api.ComponentBundleRawData
import net.craftoriya.itemframework.api.NBT_BUNDLE_KEY
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack

val NBT_COMPONENTS_DATA_KEY = "itemframework~~components_data"
val GSON = Gson()

class ComponentBundleImpl(private var id: Key? = null) : ComponentBundle {
    private val _components: MutableList<BaseComponent> = mutableListOf()
    private val _tempData: MutableMap<String, Any> = hashMapOf();

    init {
        if(id != null) {
            ComponentBundle.registry.register(id!!, this)
        }
    }

    override val components: List<Component>
        get() = _components;

    override fun addComponent(component: Component) {
        _components.add((component as BaseComponent).apply { id = Key.key(id.key(), generateComponentID(component)) })
        if (!ItemFramework.componentInstancesMap.containsValue(component)) {
            ItemFramework.registerComponent(component)
        }
    }
    override fun addComponent(component: Component, index: Int) {
        _components.add(index, (component as BaseComponent).apply { id = Key.key(id.key(), generateComponentID(component)) })
        if (!ItemFramework.componentInstancesMap.containsValue(component)) {
            ItemFramework.registerComponent(component)
        }
    }

    override fun createItemStack(base: ItemStack): ItemStack {
        val item = base.clone();
        applyToItemStack(item)
        return item;
    }

    override fun applyToItemStack(item: ItemStack) {
        _tempData.clear();
        markItem(item)
        _components.forEach { component ->
            component.applyComponent(this, item);
            component.mark(item)
        }
    }

    private fun markItem(itemStack: ItemStack) {
        NBTItem(itemStack).apply {
            if(id != null) {
                setString(NBT_BUNDLE_KEY, id!!.asString())
            }
            mergeNBT(itemStack)
        }
    }

    override fun <D : Any> getTempData(key: String) = _tempData[key] as D?
    override fun <D : Any> setTempData(key: String, data: D) {
        _tempData[key] = data
    }

    override fun getTempAllData(key: String): ComponentBundleRawData = _tempData
    override fun initTempDataIfNotExists(key: String, data: Any) {
        if (getTempData<Any>(key) == null) {
            setTempData(key, data)
        }
    }

    override fun clone(): Any {
        return super.clone()
    }
}