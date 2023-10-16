package dev.clepto.ironfurnaces.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import dev.clepto.ironfurnaces.ContainerCustomFurnace;
import dev.clepto.ironfurnaces.tileentities.TileEntityIronFurnace;

public class GuiCustomFurnace extends GuiContainer {

    public TileEntityIronFurnace teif;

    public static final ResourceLocation bground = new ResourceLocation("textures/gui/container/furnace.png");

    public GuiCustomFurnace(InventoryPlayer inventoryPlayer, TileEntityIronFurnace teif) {
        super((Container) new ContainerCustomFurnace(inventoryPlayer, teif));
        this.teif = teif;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.teif.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj
            .drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager()
            .bindTexture(bground);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        if (this.teif.isBurning()) {
            int i1 = this.teif.getBurnTimeRemainingScaled(13);
            drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
            i1 = this.teif.getCookProgressScaled(24);
            drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
        }
    }
}
