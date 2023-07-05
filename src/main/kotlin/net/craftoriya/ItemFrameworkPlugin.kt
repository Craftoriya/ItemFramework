package net.craftoriya

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class ItemFrameworkPlugin : JavaPlugin(), Listener {
    companion object {
        internal lateinit var instance: ItemFrameworkPlugin
            private set;
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        instance = this;
    }


}