package dev.clepto.ironfurnaces;

import dev.clepto.ironfurnaces.blocks.BlockCustomFurnace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.clepto.ironfurnaces.tileentities.TileEntityCrystalFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityDiamondFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityGoldFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityIronFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityObsidianFurnace;

public enum IronFurnaceType {

    IRON(Config.IronFurnaceCookTime, "Iron Furnace", TileEntityIronFurnace.class),
    GOLD(Config.GoldFurnaceCookTime, "Gold Furnace", TileEntityGoldFurnace.class),
    DIAMOND(Config.DiamondFurnaceCookTime, "Diamond Furnace", TileEntityDiamondFurnace.class),
    CRYSTAL(Config.CrystalFurnaceCookTime, "Crystal Furnace", TileEntityCrystalFurnace.class),
    OBSIDIAN(Config.ObsidianFurnaceCookTime, "Obsidian Furnace", TileEntityObsidianFurnace.class);

    private final int cookSpeed;
    public final String friendlyName;
    public final Class<? extends TileEntityIronFurnace> clazz;

    IronFurnaceType(int cookSpeed, String friendlyName, Class<? extends TileEntityIronFurnace> clazz) {
        this.cookSpeed = cookSpeed;
        this.friendlyName = friendlyName;
        this.clazz = clazz;
    }

    public int getCookSpeed() {
        return cookSpeed;
    }

    public boolean isExplosionResistant() {
        return this == OBSIDIAN;
    }

    public static TileEntityIronFurnace makeEntity(int metadata) {
        int furnaceType = validateMeta(metadata);
        if (furnaceType == metadata) {
            try {
                return values()[furnaceType].clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int validateMeta(int i) {
        if (i < values().length) {
            return i;
        } else {
            return 0;
        }
    }

    public static BlockCustomFurnace getBlockForType(IronFurnaceType type, boolean isActive) {
        switch (type) {
            case IRON:
                return (BlockCustomFurnace) (isActive ? IronFurnacesBlocks.IronFurnaceActive : IronFurnacesBlocks.IronFurnaceIdle);
            case GOLD:
                return (BlockCustomFurnace) (isActive ? IronFurnacesBlocks.GoldFurnaceActive : IronFurnacesBlocks.GoldFurnaceIdle);
            case DIAMOND:
                return (BlockCustomFurnace) (isActive ? IronFurnacesBlocks.DiamondFurnaceActive : IronFurnacesBlocks.DiamondFurnaceIdle);
            case CRYSTAL:
                return (BlockCustomFurnace) (isActive ? IronFurnacesBlocks.CrystalFurnaceActive : IronFurnacesBlocks.CrystalFurnaceIdle);
            case OBSIDIAN:
                return (BlockCustomFurnace) (isActive ? IronFurnacesBlocks.ObsidianFurnaceActive : IronFurnacesBlocks.ObsidianFurnaceIdle);
            default:
                return null;
        }
    }

    public boolean isValidForCreativeMode() {
        return validateMeta(ordinal()) == ordinal();
    }

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconFrontActive;

    @SideOnly(Side.CLIENT)
    public IIcon iconSide;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    public void makeIcons(IIconRegister par1IconRegister) {
        if (isValidForCreativeMode()) {
            iconFrontActive = par1IconRegister
                .registerIcon(String.format("ironfurnaces:%s_front_on", name().toLowerCase()));
            iconFront = par1IconRegister.registerIcon(String.format("ironfurnaces:%s_front", name().toLowerCase()));
            iconSide = par1IconRegister.registerIcon(String.format("ironfurnaces:%s_side", name().toLowerCase()));
            iconTop = par1IconRegister.registerIcon(String.format("ironfurnaces:%s_top", name().toLowerCase()));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata, boolean isActive) {
        IIcon iconFront = (isActive ? this.iconFrontActive : this.iconFront);
        return (metadata == 0 && side == 3) ? iconFront
            : ((side == 1) ? this.iconTop
                : ((side == 0) ? this.iconTop : ((side == metadata) ? iconFront : this.iconSide)));
    }
}
