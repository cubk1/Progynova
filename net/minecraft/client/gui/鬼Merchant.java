package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.inventory.鬼Container;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.图像位置;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class 鬼Merchant extends 鬼Container
{
    private static final Logger logger = LogManager.getLogger();
    private static final 图像位置 MERCHANT_GUI_TEXTURE = new 图像位置("textures/gui/container/villager.png");
    private IMerchant merchant;
    private 鬼Merchant.MerchantButton nextButton;
    private 鬼Merchant.MerchantButton previousButton;
    private int selectedMerchantRecipe;
    private IChatComponent chatComponent;

    public 鬼Merchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn)
    {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        this.merchant = p_i45500_2_;
        this.chatComponent = p_i45500_2_.getDisplayName();
    }

    public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.buttonList.add(this.nextButton = new 鬼Merchant.MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
        this.buttonList.add(this.previousButton = new 鬼Merchant.MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.chatComponent.getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    public void updateScreen()
    {
        super.updateScreen();
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.宇轩游玩者);

        if (merchantrecipelist != null)
        {
            this.nextButton.enabled = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
            this.previousButton.enabled = this.selectedMerchantRecipe > 0;
        }
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        boolean flag = false;

        if (button == this.nextButton)
        {
            ++this.selectedMerchantRecipe;
            MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.宇轩游玩者);

            if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size())
            {
                this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
            }

            flag = true;
        }
        else if (button == this.previousButton)
        {
            --this.selectedMerchantRecipe;

            if (this.selectedMerchantRecipe < 0)
            {
                this.selectedMerchantRecipe = 0;
            }

            flag = true;
        }

        if (flag)
        {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.selectedMerchantRecipe);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(MERCHANT_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.宇轩游玩者);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int k = this.selectedMerchantRecipe;

            if (k < 0 || k >= merchantrecipelist.size())
            {
                return;
            }

            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);

            if (merchantrecipe.isRecipeDisabled())
            {
                this.mc.得到手感经理().绑定手感(MERCHANT_GUI_TEXTURE);
                光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                光照状态经理.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.宇轩游玩者);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            int k = this.selectedMerchantRecipe;
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
            ItemStack itemstack = merchantrecipe.getItemToBuy();
            ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
            ItemStack itemstack2 = merchantrecipe.getItemToSell();
            光照状态经理.推黑客帝国();
            RenderHelper.enableGUIStandardItemLighting();
            光照状态经理.disableLighting();
            光照状态经理.enableRescaleNormal();
            光照状态经理.enableColorMaterial();
            光照状态经理.enableLighting();
            this.itemRender.zLevel = 100.0F;
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);

            if (itemstack1 != null)
            {
                this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i + 62, j + 24);
            }

            this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 120, j + 24);
            this.itemRender.zLevel = 0.0F;
            光照状态经理.disableLighting();

            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null)
            {
                this.renderToolTip(itemstack, mouseX, mouseY);
            }
            else if (itemstack1 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack1 != null)
            {
                this.renderToolTip(itemstack1, mouseX, mouseY);
            }
            else if (itemstack2 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack2 != null)
            {
                this.renderToolTip(itemstack2, mouseX, mouseY);
            }
            else if (merchantrecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY)))
            {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.enableLighting();
            光照状态经理.启用纵深();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public IMerchant getMerchant()
    {
        return this.merchant;
    }

    static class MerchantButton extends 鬼Button
    {
        private final boolean field_146157_o;

        public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_)
        {
            super(buttonID, x, y, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }

        public void drawButton(我的手艺 mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                mc.得到手感经理().绑定手感(鬼Merchant.MERCHANT_GUI_TEXTURE);
                光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = 0;
                int j = 176;

                if (!this.enabled)
                {
                    j += this.width * 2;
                }
                else if (flag)
                {
                    j += this.width;
                }

                if (!this.field_146157_o)
                {
                    i += this.height;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
            }
        }
    }
}
