package dev.clepto.ironfurnaces;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.clepto.ironfurnaces.gui.GuiHandler;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class IronFurnaces {

    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @SidedProxy(
        clientSide = "dev.clepto.ironfurnaces.client.ClientProxy",
        serverSide = "dev.clepto.ironfurnaces.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("IronFurnaces")
    public static IronFurnaces instance;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(new File(event.getModConfigurationDirectory(), "ironfurnaces.cfg"));
        IronFurnacesBlocks.initBlocks();

    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        for (IronFurnaceType type : IronFurnaceType.values()) {
            GameRegistry.registerTileEntityWithAlternatives(type.clazz, "IronFurnace." + type.name(), type.name());
        }
        OreDictionary.registerOre("stone_furnace", Blocks.furnace);
        NetworkRegistry.INSTANCE.registerGuiHandler("ironfurnaces", new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("NotEnoughItems")) {
            IronFurnacesBlocks.hideActiveNEI();
        }
    }
}
