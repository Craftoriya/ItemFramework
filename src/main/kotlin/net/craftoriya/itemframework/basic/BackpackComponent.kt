package net.craftoriya.itemframework.basic

import de.tr7zw.nbtapi.NBTItem
import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.api.getComponent
import net.craftoriya.itemframework.impl.BaseComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack

private const val NBT_BACKPACK_ITEMS = "backpack:items"

class BackpackComponent(private val sizeHeight: Int) : BaseComponent() {

    override fun applyComponent(bundle: ComponentBundle, itemStack: ItemStack) {
        val items = loadNbtData<List<ItemStack>>(itemStack);


        val display = bundle.getComponent<DisplayComponent>()
        display
            ?.addTitleParts(
                bundle,
                MiniMessage
                    .miniMessage()
                    .deserialize("<green>Рюкзак")
            )
        if (items != null) {
            display
                ?.addTitleParts(
                    bundle,
                    MiniMessage
                        .miniMessage()
                        .deserialize("<yellow>${items.size}/${sizeHeight * 9}")
                )
        }

    }

    

    override fun <S : Any?> loadNbtData(itemStack: ItemStack): S? {
        val nbt = NBTItem(itemStack)
        if (!nbt.hasTag(NBT_BACKPACK_ITEMS)) {
            return null;
        }
        return NBTItem.convertNBTtoItemArray(nbt.getCompound(NBT_BACKPACK_ITEMS)).toList() as S?
    }
}