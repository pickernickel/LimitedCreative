package de.jaschastarke.minecraft.limitedcreative.inventories;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.bukkit.lib.configuration.ConfigurationContainer;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.configuration.annotations.IsConfigurationNode;
import de.jaschastarke.maven.ArchiveDocComments;
import de.jaschastarke.maven.PluginConfigurations;
import de.jaschastarke.minecraft.limitedcreative.Config;
import de.jaschastarke.minecraft.limitedcreative.ModInventories;
import de.jaschastarke.modularize.IModule;
import de.jaschastarke.modularize.ModuleEntry;

/**
 * Inventory-Feature
 * 
 * http://dev.bukkit.org/server-mods/limited-creative/pages/features/inventory/
 */
@ArchiveDocComments
@PluginConfigurations(parent = Config.class)
public class InventoryConfig extends Configuration implements IConfigurationSubGroup {
    protected ModInventories mod;
    protected ModuleEntry<IModule> entry;
    
    public InventoryConfig(ConfigurationContainer container) {
        super(container);
    }
    public InventoryConfig(ModInventories modInventories, ModuleEntry<IModule> modEntry) {
        super(modInventories.getPlugin().getDocCommentStorage());
        mod = modInventories;
        entry = modEntry;
    }
    
    @Override
    public void setValue(IConfigurationNode node, Object pValue) throws InvalidValueException {
        super.setValue(node, pValue);
        if (node.getName().equals("enabled")) {
            entry.setEnabled(getEnabled());
        }
    }

    @Override
    public void setValues(ConfigurationSection sect) {
        if (sect == null || sect.getValues(false).size() == 0) {
            ConfigurationSection parent_sect = mod.getPlugin().getPluginConfig().getValues();
            if (parent_sect.contains("store")) {
                sect = parent_sect.createSection(this.getName(), parent_sect.getConfigurationSection("store").getValues(true));
            }
        }
        
        super.setValues(sect);
        entry.setDefaultEnabled(getEnabled());
        
        // Config Upgrade
        if (!sect.contains("storeCreative") && sect.contains("creative"))
            sect.set("storeCreative", sect.getBoolean("creative"));
        if (!sect.contains("separateAdventure") && sect.contains("adventure"))
            sect.set("separateAdventure", sect.getBoolean("adventure"));
    }
    @Override
    public String getName() {
        return "inventory";
    }
    @Override
    public int getOrder() {
        return 100;
    }
    
    /**
     * InventoryEnabled
     * 
     * Use this option to disable the separated inventories feature, for example if you use another Plugin handling 
     * the inventories, like Multiverse-Inventories.
     * 
     * default: true
     */
    @IsConfigurationNode(order = 100)
    public boolean getEnabled() {
        return config.getBoolean("enabled", true);
    }
    
    /**
     * InventoryStoreCreative
     * 
     * Should the creative-inventory also be stored on disk, when switching to survival?
     * If disabled, the inventory gets cleared every time on switching to creative.
     * 
     * default: true
     */
    @IsConfigurationNode(order = 200)
    public boolean getStoreCreative() {
        return config.getBoolean("storeCreative", true);
    }
    
    /**
     * InventorySeparateAdventure
     * 
     * When true, your players get a separate inventory when switching to adventure gamemode (2). Otherwise 
     * they have the default survival inventory while in adventure gamemode.
     * 
     * default: false
     */
    @IsConfigurationNode(order = 300)
    public boolean getSeparateAdventure() {
        return config.getBoolean("separateAdventure", true);
    }
    
    /**
     * InventoryFolder
     * 
     * The folder inside the datadir-folder (plugin/LimitedCreative) where the inventories are saved to.
     * By default the inventories are saved to "plugin/LimitedCreative/inventories".
     * 
     * default: "inventories"
     */
    @IsConfigurationNode(order = 400, readonly = true)
    public String getFolder() {
        return config.getString("folder", "inventories");
    }

}
