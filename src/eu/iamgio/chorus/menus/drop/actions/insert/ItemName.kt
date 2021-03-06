package eu.iamgio.chorus.menus.drop.actions.insert

import eu.iamgio.chorus.minecraft.item.Item
import eu.iamgio.chorus.util.config

/**
 * @author Gio
 */
class ItemName : EnumNameAction(Item::class.java) {

    override fun plus(): String {
        return if(config.getBoolean("4.Minecraft.5.Insert_item_data")) return ":0" else ""
    }
}