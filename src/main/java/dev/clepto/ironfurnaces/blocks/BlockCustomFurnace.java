package dev.clepto.ironfurnaces.blocks;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.clepto.ironfurnaces.IronFurnaceType;
import dev.clepto.ironfurnaces.tileentities.TileEntityIronFurnace;

public abstract class BlockCustomFurnace extends BlockContainer {

    private IronFurnaceType type;

    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;

    private static boolean keepInventory = false;

    private final boolean isActive;

    private Random random;

    public BlockCustomFurnace(IronFurnaceType type, boolean isActive) {
        super(Material.iron);
        this.type = type;
        setHardness(3.0F);
        setHarvestLevel("pickaxe", 1);
        this.isActive = isActive;
        this.random = new Random();
    }

    @Override
    public TileEntity createNewTileEntity(World w, int i) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return IronFurnaceType.makeEntity(type.ordinal());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        return type.getIcon(i, j, this.isActive);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        type.makeIcons(par1IconRegister);
        this.blockIcon = type.iconSide;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) FMLNetworkHandler.openGui(player, "ironfurnaces", 0, world, x, y, z);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        int facing = MathHelper.floor_double((double) ((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        if (facing == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        if (facing == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        if (facing == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) {
        if (!keepInventory) {
            TileEntityIronFurnace teif = (TileEntityIronFurnace) world.getTileEntity(x, y, z);
            if (teif != null) {
                for (int i = 0; i < teif.getSizeInventory(); i++) {
                    ItemStack itemStack = teif.getStackInSlot(i);
                    if (itemStack != null) {
                        float f = this.random.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.random.nextFloat() * 0.8F + 0.1F;
                        float f3 = this.random.nextFloat() * 0.8F + 0.1F;
                        while (itemStack.stackSize > 0) {
                            int j = this.random.nextInt(21) + 10;
                            if (j > itemStack.stackSize) j = itemStack.stackSize;
                            ItemStack itemStack1 = itemStack;
                            itemStack1.stackSize -= j;
                            EntityItem item = new EntityItem(
                                world,
                                (x + f),
                                (y + f2),
                                (z + f3),
                                new ItemStack(itemStack.getItem(), j, itemStack.getItemDamage()));
                            if (itemStack.hasTagCompound()) item.getEntityItem()
                                .setTagCompound(
                                    (NBTTagCompound) itemStack.getTagCompound()
                                        .copy());
                            world.spawnEntityInWorld((Entity) item);
                        }
                    }
                }
                // updateNeighborsAboutBlockChange
                world.func_147453_f(x, y, z, oldBlock);
            }
        }
        super.breakBlock(world, x, y, z, oldBlock, oldMetadata);
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(IronFurnaceType.getBlockForType(this.type, false));
    }

    @Override
    public Item getItemDropped(int i, Random random, int j) {
        return Item.getItemFromBlock(IronFurnaceType.getBlockForType(this.type, false));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (!this.isActive) {
            return;
        }
        int direction = world.getBlockMetadata(x, y, z);
        float x2 = x + 0.5F;
        float y2 = y + random.nextFloat() * 6.0F / 16.0F;
        float z2 = z + 0.5F;
        float f = 0.52F;
        float f2 = random.nextFloat() * 0.6F - 0.3F;
        if (direction == 4) {
            world.spawnParticle("smoke", (x2 - 0.52F), y2, (z2 + f2), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", (x2 - 0.52F), y2, (z2 + f2), 0.0D, 0.0D, 0.0D);
        } else if (direction == 5) {
            world.spawnParticle("smoke", (x2 + 0.52F), y2, (z2 + f2), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", (x2 + 0.52F), y2, (z2 + f2), 0.0D, 0.0D, 0.0D);
        } else if (direction == 2) {
            world.spawnParticle("smoke", (x2 - f2), y2, (z2 - 0.52F), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", (x2 - f2), y2, (z2 - 0.52F), 0.0D, 0.0D, 0.0D);
        } else if (direction == 3) {
            world.spawnParticle("smoke", (x2 + f2), y2, (z2 + 0.52F), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", (x2 + f2), y2, (z2 + 0.52F), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        if (type.isExplosionResistant()) {
            return 10000f;
        }
        return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    private static final ForgeDirection[] validRotationAxes = new ForgeDirection[] { UP, DOWN };

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) {
        return validRotationAxes;
    }

    public static void updateCustomFurnaceBlockState(boolean active, World worldObj, int xCoord, int yCoord,
        int zCoord) {
        int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord, zCoord);
        if (te == null) {
            return;
        }
        TileEntityIronFurnace teif = (TileEntityIronFurnace) te;
        keepInventory = true;
        worldObj.setBlock(xCoord, yCoord, zCoord, IronFurnaceType.getBlockForType(teif.getType(), active));
        keepInventory = false;
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
        te.validate();
        worldObj.setTileEntity(xCoord, yCoord, zCoord, te);
    }
}
