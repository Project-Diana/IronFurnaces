package dev.clepto.ironfurnaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.clepto.ironfurnaces.tileentities.TileEntityIronFurnace;

public class ContainerCustomFurnace extends Container {

    private TileEntityIronFurnace teif;

    public int lastBurnTime;
    public int lastCurrentItemBurnTime;
    public int lastCookTime;

    public ContainerCustomFurnace(InventoryPlayer inventory, TileEntityIronFurnace teif) {
        this.teif = teif;
        addSlotToContainer(new Slot((IInventory) teif, 0, 56, 17));
        addSlotToContainer(new Slot((IInventory) teif, 1, 56, 53));
        addSlotToContainer((Slot) new SlotFurnace(inventory.player, (IInventory) teif, 2, 116, 35));
        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot((IInventory) inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            addSlotToContainer(new Slot((IInventory) inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.teif.cookTime);
        crafting.sendProgressBarUpdate(this, 1, this.teif.burnTime);
        crafting.sendProgressBarUpdate(this, 2, this.teif.currentItemBurnTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting crafting = (ICrafting) this.crafters.get(i);
            if (this.lastCookTime != this.teif.cookTime) crafting.sendProgressBarUpdate(this, 0, this.teif.cookTime);
            if (this.lastBurnTime != this.teif.burnTime) crafting.sendProgressBarUpdate(this, 1, this.teif.burnTime);
            if (this.lastCurrentItemBurnTime != this.teif.currentItemBurnTime)
                crafting.sendProgressBarUpdate(this, 2, this.teif.currentItemBurnTime);
        }
        this.lastCookTime = this.teif.cookTime;
        this.lastBurnTime = this.teif.burnTime;
        this.lastCurrentItemBurnTime = this.teif.currentItemBurnTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) this.teif.cookTime = par2;
        if (par1 == 1) this.teif.burnTime = par2;
        if (par1 == 2) this.teif.currentItemBurnTime = par2;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (i == 2) {
                if (!mergeItemStack(itemStack1, 3, 39, true)) return null;
                slot.onSlotChange(itemStack1, itemStack);
            } else if (i != 1 && i != 0) {
                if (FurnaceRecipes.smelting()
                    .getSmeltingResult(itemStack1) != null) {
                    if (!mergeItemStack(itemStack1, 0, 1, false)) return null;
                } else if (TileEntityIronFurnace.isItemFuel(itemStack1)) {
                    if (!mergeItemStack(itemStack1, 1, 2, false)) return null;
                } else if (i >= 3 && i < 30) {
                    if (!mergeItemStack(itemStack1, 30, 39, false)) return null;
                } else if (i >= 30 && i < 39 && !mergeItemStack(itemStack1, 3, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemStack1, 3, 39, false)) {
                return null;
            }
            if (itemStack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemStack1.stackSize == itemStack.stackSize) return null;
            slot.onPickupFromSlot(entityPlayer, itemStack1);
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
}
