package dev.clepto.ironfurnaces;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.clepto.ironfurnaces.blocks.BlockCrystalFurnace;
import dev.clepto.ironfurnaces.blocks.BlockDiamondFurnace;
import dev.clepto.ironfurnaces.blocks.BlockGoldFurnace;
import dev.clepto.ironfurnaces.blocks.BlockIronFurnace;
import dev.clepto.ironfurnaces.blocks.BlockObsidianFurnace;

public class IronFurnacesBlocks {

    public static Block IronFurnaceActive;
    public static Block IronFurnaceIdle;
    public static Block GoldFurnaceActive;
    public static Block GoldFurnaceIdle;
    public static Block DiamondFurnaceActive;
    public static Block DiamondFurnaceIdle;
    public static Block CrystalFurnaceActive;
    public static Block CrystalFurnaceIdle;
    public static Block ObsidianFurnaceActive;
    public static Block ObsidianFurnaceIdle;

    public static void initBlocks() {
        IronFurnaceActive = new BlockIronFurnace(true).setBlockName("IronFurnaceActive")
            .setLightLevel(1.0f);
        IronFurnaceIdle = new BlockIronFurnace(false).setBlockName("IronFurnaceIdle");
        GoldFurnaceActive = new BlockGoldFurnace(true).setBlockName("GoldFurnaceActive")
            .setLightLevel(1.0f);
        GoldFurnaceIdle = new BlockGoldFurnace(false).setBlockName("GoldFurnaceIdle");
        DiamondFurnaceActive = new BlockDiamondFurnace(true).setBlockName("DiamondFurnaceActive")
            .setLightLevel(1.0f);
        DiamondFurnaceIdle = new BlockDiamondFurnace(false).setBlockName("DiamondFurnaceIdle");
        CrystalFurnaceActive = new BlockCrystalFurnace(true).setBlockName("CrystalFurnaceActive")
            .setLightLevel(1.0f);
        CrystalFurnaceIdle = new BlockCrystalFurnace(false).setBlockName("CrystalFurnaceIdle");
        ObsidianFurnaceActive = new BlockObsidianFurnace(true).setBlockName("ObsidianFurnaceActive")
            .setLightLevel(1.0f);
        ObsidianFurnaceIdle = new BlockObsidianFurnace(false).setBlockName("ObsidianFurnaceIdle");
        GameRegistry.registerBlock(IronFurnaceActive, "iron_furnace_active");
        GameRegistry.registerBlock(IronFurnaceIdle, "iron_furnace");
        GameRegistry.registerBlock(GoldFurnaceActive, "gold_furnace_active");
        GameRegistry.registerBlock(GoldFurnaceIdle, "gold_furnace");
        GameRegistry.registerBlock(DiamondFurnaceActive, "diamond_furnace_active");
        GameRegistry.registerBlock(DiamondFurnaceIdle, "diamond_furnace");
        GameRegistry.registerBlock(CrystalFurnaceActive, "crystal_furnace_active");
        GameRegistry.registerBlock(CrystalFurnaceIdle, "crystal_furnace");
        GameRegistry.registerBlock(ObsidianFurnaceActive, "obsidian_furnace_active");
        GameRegistry.registerBlock(ObsidianFurnaceIdle, "obsidian_furnace");
        GameRegistry.addShapedRecipe(
            new ItemStack(IronFurnaceIdle, 1),
            new Object[] { "III", "IFI", "III", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('F'),
                Blocks.furnace });
        GameRegistry.addShapedRecipe(
            new ItemStack(GoldFurnaceIdle, 1),
            new Object[] { "GGG", "GfG", "GGG", Character.valueOf('G'), Items.gold_ingot, Character.valueOf('f'),
                IronFurnaceIdle });
        GameRegistry.addShapedRecipe(
            new ItemStack(DiamondFurnaceIdle, 1),
            new Object[] { "GDG", "DgD", "GDG", Character.valueOf('G'), Blocks.glass, Character.valueOf('g'),
                GoldFurnaceIdle, Character.valueOf('D'), Items.diamond });
        GameRegistry.addShapedRecipe(
            new ItemStack(ObsidianFurnaceIdle, 1),
            new Object[] { "OOO", "OdO", "OOO", Character.valueOf('d'), DiamondFurnaceIdle, Character.valueOf('O'),
                Blocks.obsidian });
        GameRegistry.addRecipe(
            (IRecipe) new ShapedOreRecipe(
                CrystalFurnaceIdle,
                new Object[] { "GGG", "GoG", "GGG", Character.valueOf('G'), "blockGlass", Character.valueOf('o'),
                    ObsidianFurnaceIdle }));
    }

    public static void hideActiveNEI() {
        API.hideItem(new ItemStack(Item.getItemFromBlock(IronFurnaceActive), 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Item.getItemFromBlock(GoldFurnaceActive), 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Item.getItemFromBlock(DiamondFurnaceActive), 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Item.getItemFromBlock(CrystalFurnaceActive), 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Item.getItemFromBlock(ObsidianFurnaceActive), 1, OreDictionary.WILDCARD_VALUE));
    }

    public static final CreativeTabs tabIronFurnace = new CreativeTabs("IronFurnaces") {

        public Item getTabIconItem() {
            return Item.getItemFromBlock(IronFurnacesBlocks.IronFurnaceIdle);
        }
    };

}
