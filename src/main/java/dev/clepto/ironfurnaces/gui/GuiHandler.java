package dev.clepto.ironfurnaces.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import dev.clepto.ironfurnaces.ContainerCustomFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityIronFurnace;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if (te instanceof TileEntityIronFurnace)
                return new ContainerCustomFurnace(player.inventory, (TileEntityIronFurnace) te);
            return null;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if (te instanceof TileEntityIronFurnace)
                return new GuiCustomFurnace(player.inventory, (TileEntityIronFurnace) te);
            return null;
        }
        return null;
    }
}
