package net.craftoriya.itemframework.basic

import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.impl.BaseComponent
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

class DisplayComponent: BaseComponent() {

    private val DESCRIPTION_LINES_KEY_DATA = "displaycomponent~~description";
    private val TITLE_PARTS_KEY_DATA = "displaycomponent~~title_parts";

    fun addDescriptionLines(bundle: ComponentBundle, vararg components: Component): DisplayComponent {
        bundle.initTempDataIfNotExists(DESCRIPTION_LINES_KEY_DATA, mutableListOf<Component>())

        bundle
            .getTempData<MutableList<Component>>(DESCRIPTION_LINES_KEY_DATA)
            ?.addAll(components)
        return this
    }
    fun addTitleParts(bundle: ComponentBundle, vararg components: Component): DisplayComponent {
        bundle.initTempDataIfNotExists(TITLE_PARTS_KEY_DATA, mutableListOf<Component>())

        bundle
            .getTempData<MutableList<Component>>(TITLE_PARTS_KEY_DATA)
            ?.addAll(components)
        return this
    }

    override fun applyComponent(bundle: ComponentBundle, itemStack: ItemStack) {
        itemStack.editMeta {
            it.displayName(Component.space())
            it.lore(listOf<Component>())
        }

        bundle.getTempData<MutableList<Component>>(TITLE_PARTS_KEY_DATA)?.forEachIndexed { index, component: Component ->
            if(index != 0){
                itemStack.editMeta {
                    it.displayName(it.displayName()?.append(Component.space()))
                }
            }
            itemStack.editMeta {
                it.displayName(it.displayName()?.append(component))
            }
        }
        bundle.getTempData<MutableList<Component>>(DESCRIPTION_LINES_KEY_DATA)?.apply {
            itemStack.editMeta {
                it.lore(this)
            }
        }
    }
}