package net.minecraft.client.gui.inventory;

import java.io.IOException;

import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.achievement.鬼Achievements;
import net.minecraft.client.gui.achievement.鬼Stats;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class 鬼Inventory extends InventoryEffectRenderer
{
    private float oldMouseX;
    private float oldMouseY;

    public 鬼Inventory(EntityPlayer p_i1094_1_)
    {
        super(p_i1094_1_.inventoryContainer);
        this.allowUserInput = true;
    }

    public void updateScreen()
    {
        if (this.mc.玩家控制者.是创造模式吗())
        {
            this.mc.displayGuiScreen(new 鬼ContainerCreative(this.mc.宇轩游玩者));
        }

        this.updateActivePotionEffects();
    }

    public void initGui()
    {
        this.buttonList.clear();

        if (this.mc.玩家控制者.是创造模式吗())
        {
            this.mc.displayGuiScreen(new 鬼ContainerCreative(this.mc.宇轩游玩者));
        }
        else
        {
            super.initGui();
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 86, 16, 4210752);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(inventoryBackground);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mc.宇轩游玩者);
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        光照状态经理.enableColorMaterial();
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)posX, (float)posY, 50.0F);
        光照状态经理.障眼物((float)(-scale), (float)scale, (float)scale);
        光照状态经理.辐射(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.旋转侧滑;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        光照状态经理.辐射(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        光照状态经理.辐射(-135.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.旋转侧滑 = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.旋转侧滑;
        ent.prevRotationYawHead = ent.旋转侧滑;
        光照状态经理.理解(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.旋转侧滑 = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        光照状态经理.流行音乐黑客帝国();
        RenderHelper.disableStandardItemLighting();
        光照状态经理.disableRescaleNormal();
        光照状态经理.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        光照状态经理.禁用手感();
        光照状态经理.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new 鬼Achievements(this, this.mc.宇轩游玩者.getStatFileWriter()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new 鬼Stats(this, this.mc.宇轩游玩者.getStatFileWriter()));
        }
    }
}
