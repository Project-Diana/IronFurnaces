package dev.clepto.ironfurnaces.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.clepto.ironfurnaces.IronFurnaceType;
import dev.clepto.ironfurnaces.blocks.BlockCustomFurnace;

public class TileEntityIronFurnace extends TileEntity implements ISidedInventory {

    private ItemStack[] slots = new ItemStack[3];

    private IronFurnaceType type;

    public int burnTime;
    public int currentItemBurnTime;
    public int cookTime;

    private int facing;

    public TileEntityIronFurnace() {
        this(IronFurnaceType.IRON);
    }

    public TileEntityIronFurnace(IronFurnaceType type) {
        super();
        this.type = type;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public String getInventoryName() {
        return type.friendlyName;
    }

    public IronFurnaceType getType() {
        return type;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return slots[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.slots[i] == null) {
            return null;
        }
        if (this.slots[i].stackSize <= j) {
            ItemStack itemStack = this.slots[i];
            this.slots[i] = null;
            return itemStack;
        }
        ItemStack itemStack = this.slots[i].splitStack(j);
        if (this.slots[i].stackSize == 0) {
            this.slots[i] = null;
        }
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.slots[i] != null) {
            ItemStack itemStack = this.slots[i];
            this.slots[i] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.slots[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();
    }

    public int getFacing() {
        return this.facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
            && entityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return (i != 2 && (i != 1 || isItemFuel(itemStack)));
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return (getItemBurnTime(itemStack) > 0);
    }

    private static int getItemBurnTime(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        Item item = itemStack.getItem();
        if (item instanceof net.minecraft.item.ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);
            if (block == Blocks.sapling) return 100;
            if (block == Blocks.coal_block) return 14400;
            if (block.getMaterial() == Material.wood) return 300;
        }
        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName()
            .equals("WOOD")) return 200;
        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName()
            .equals("WOOD")) return 200;
        if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName()
            .equals("WOOD")) return 200;
        if (item == Items.coal) return 1600;
        if (item == Items.stick) return 100;
        if (item == Items.lava_bucket) return 20000;
        if (item == Items.blaze_rod) return 2400;
        return GameRegistry.getFuelValue(itemStack);
    }

    public boolean isBurning() {
        return (this.burnTime > 0);
    }

    public void updateEntity() {
        boolean flag = (this.burnTime > 0);
        boolean flag2 = false;
        if (isBurning()) this.burnTime--;
        if (!this.worldObj.isRemote) {
            if (this.burnTime == 0 && canSmelt()) {
                int itemBurnTime = getItemBurnTime(this.slots[1]);
                this.burnTime = itemBurnTime;
                this.currentItemBurnTime = itemBurnTime;
                if (isBurning()) {
                    flag2 = true;
                    if (this.slots[1] != null) {
                        ItemStack itemStack = this.slots[1];
                        itemStack.stackSize--;
                        if (this.slots[1].stackSize == 0) this.slots[1] = this.slots[1].getItem()
                            .getContainerItem(this.slots[1]);
                    }
                }
            }
            if (isBurning() && canSmelt()) {
                this.cookTime++;
                if (this.cookTime == this.type.getCookSpeed()) {
                    this.cookTime = 0;
                    smeltItem();
                    flag2 = true;
                }
            } else {
                this.cookTime = 0;
            }
            if (flag != isBurning()) {
                flag2 = true;
                BlockCustomFurnace.updateCustomFurnaceBlockState(
                    (this.burnTime > 0),
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord);
            }
        }
        if (flag2) markDirty();
    }

    public boolean canSmelt() {
        if (this.slots[0] == null) return false;
        ItemStack itemStack = FurnaceRecipes.smelting()
            .getSmeltingResult(this.slots[0]);
        if (itemStack == null) return false;
        if (this.slots[2] == null) return true;
        if (!this.slots[2].isItemEqual(itemStack)) return false;
        int result = (this.slots[2]).stackSize + itemStack.stackSize;
        return (result <= getInventoryStackLimit() && result <= itemStack.getMaxStackSize());
    }

    public void smeltItem() {
        if (canSmelt()) {
            ItemStack itemStack = FurnaceRecipes.smelting()
                .getSmeltingResult(this.slots[0]);
            if (this.slots[2] == null) {
                this.slots[2] = itemStack.copy();
            } else if (this.slots[2].isItemEqual(itemStack)) {
                ItemStack itemStack1 = this.slots[2];
                itemStack1.stackSize += itemStack.stackSize;
            }
            ItemStack itemStack2 = this.slots[0];
            itemStack2.stackSize--;
            if (this.slots[0].stackSize <= 0) this.slots[0] = null;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return (i == 0) ? slots_bottom : ((i == 1) ? slots_top : slots_side);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return isItemValidForSlot(i, itemStack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return (j != 0 || i != 1 || itemStack.getItem() == Items.bucket);
    }

    public int getBurnTimeRemainingScaled(int i) {
        if (this.currentItemBurnTime == 0) this.currentItemBurnTime = this.type.getCookSpeed();
        return this.burnTime * i / this.currentItemBurnTime;
    }

    public int getCookProgressScaled(int i) {
        return this.cookTime * i / this.type.getCookSpeed();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Items", 10);
        this.slots = new ItemStack[getSizeInventory()];
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            byte b = compound.getByte("Slot");
            if (b >= 0 && b < this.slots.length) this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
        }
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.currentItemBurnTime = nbt.getShort("CurrentBurnTime");
        this.facing = nbt.getShort("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("BurnTime", (short) this.burnTime);
        nbt.setShort("CookTime", (short) this.cookTime);
        nbt.setShort("CurrentBurnTime", (short) this.currentItemBurnTime);
        nbt.setShort("facing", (short) this.facing);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] != null) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setByte("Slot", (byte) i);
                this.slots[i].writeToNBT(compound);
                list.appendTag((NBTBase) compound);
            }
        }
        nbt.setTag("Items", (NBTBase) list);
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    private static final int[] slots_top = new int[] { 0 };
    private static final int[] slots_bottom = new int[] { 2, 1 };
    private static final int[] slots_side = new int[] { 1 };

}
