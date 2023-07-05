package net.craftoriya.itemframework.basic

import net.craftoriya.itemframework.api.ComponentBundle
import net.craftoriya.itemframework.getBundle
import net.craftoriya.itemframework.hasBundle
import net.craftoriya.itemframework.impl.BaseComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

class UpdateBundleWhenMovingComponent: BaseComponent() {
    override fun applyComponent(bundle: ComponentBundle, itemStack: ItemStack) {
        println();
    }
    @EventHandler
    fun onMoving(e: PlayerMoveEvent) {
        e.player.inventory.filterNotNull().forEach {
            if(!it.hasBundle())return;
            it.getBundle().applyToItemStack(it)
        }
    }
}