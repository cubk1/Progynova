package net.minecraft.client.gui.achievement;

import java.io.IOException;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.gui.*;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import org.lwjgl.input.Mouse;

public class 鬼Achievements extends 鬼Screen implements IProgressMeter
{
    private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
    private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
    private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
    private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
    private static final 图像位置 ACHIEVEMENT_BACKGROUND = new 图像位置("textures/gui/achievement/achievement_background.png");
    protected 鬼Screen parentScreen;
    protected int field_146555_f = 256;
    protected int field_146557_g = 202;
    protected int field_146563_h;
    protected int field_146564_i;
    protected float field_146570_r = 1.0F;
    protected double field_146569_s;
    protected double field_146568_t;
    protected double field_146567_u;
    protected double field_146566_v;
    protected double field_146565_w;
    protected double field_146573_x;
    private int field_146554_D;
    private StatFileWriter statFileWriter;
    private boolean loadingAchievements = true;

    public 鬼Achievements(鬼Screen parentScreenIn, StatFileWriter statFileWriterIn)
    {
        this.parentScreen = parentScreenIn;
        this.statFileWriter = statFileWriterIn;
        int i = 141;
        int j = 141;
        this.field_146569_s = this.field_146567_u = this.field_146565_w = (double)(AchievementList.openInventory.displayColumn * 24 - i / 2 - 12);
        this.field_146568_t = this.field_146566_v = this.field_146573_x = (double)(AchievementList.openInventory.displayRow * 24 - j / 2);
    }

    public void initGui()
    {
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        this.buttonList.clear();
        this.buttonList.add(new 鬼OptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (!this.loadingAchievements)
        {
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == this.mc.游戏一窝.keyBindInventory.getKeyCode())
        {
            this.mc.displayGuiScreen((鬼Screen)null);
            this.mc.setIngameFocus();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.loadingAchievements)
        {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(我的手艺.getSystemTime() / 150L % (long)lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else
        {
            if (Mouse.isButtonDown(0))
            {
                int i = (this.width - this.field_146555_f) / 2;
                int j = (this.height - this.field_146557_g) / 2;
                int k = i + 8;
                int l = j + 17;

                if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155)
                {
                    if (this.field_146554_D == 0)
                    {
                        this.field_146554_D = 1;
                    }
                    else
                    {
                        this.field_146567_u -= (double)((float)(mouseX - this.field_146563_h) * this.field_146570_r);
                        this.field_146566_v -= (double)((float)(mouseY - this.field_146564_i) * this.field_146570_r);
                        this.field_146565_w = this.field_146569_s = this.field_146567_u;
                        this.field_146573_x = this.field_146568_t = this.field_146566_v;
                    }

                    this.field_146563_h = mouseX;
                    this.field_146564_i = mouseY;
                }
            }
            else
            {
                this.field_146554_D = 0;
            }

            int i1 = Mouse.getDWheel();
            float f3 = this.field_146570_r;

            if (i1 < 0)
            {
                this.field_146570_r += 0.25F;
            }
            else if (i1 > 0)
            {
                this.field_146570_r -= 0.25F;
            }

            this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);

            if (this.field_146570_r != f3)
            {
                float f5 = f3 - this.field_146570_r;
                float f4 = f3 * (float)this.field_146555_f;
                float f = f3 * (float)this.field_146557_g;
                float f1 = this.field_146570_r * (float)this.field_146555_f;
                float f2 = this.field_146570_r * (float)this.field_146557_g;
                this.field_146567_u -= (double)((f1 - f4) * 0.5F);
                this.field_146566_v -= (double)((f2 - f) * 0.5F);
                this.field_146565_w = this.field_146569_s = this.field_146567_u;
                this.field_146573_x = this.field_146568_t = this.field_146566_v;
            }

            if (this.field_146565_w < (double)field_146572_y)
            {
                this.field_146565_w = (double)field_146572_y;
            }

            if (this.field_146573_x < (double)field_146571_z)
            {
                this.field_146573_x = (double)field_146571_z;
            }

            if (this.field_146565_w >= (double)field_146559_A)
            {
                this.field_146565_w = (double)(field_146559_A - 1);
            }

            if (this.field_146573_x >= (double)field_146560_B)
            {
                this.field_146573_x = (double)(field_146560_B - 1);
            }

            this.drawDefaultBackground();
            this.drawAchievementScreen(mouseX, mouseY, partialTicks);
            光照状态经理.disableLighting();
            光照状态经理.禁用纵深();
            this.drawTitle();
            光照状态经理.enableLighting();
            光照状态经理.启用纵深();
        }
    }

    public void doneLoading()
    {
        if (this.loadingAchievements)
        {
            this.loadingAchievements = false;
        }
    }

    public void updateScreen()
    {
        if (!this.loadingAchievements)
        {
            this.field_146569_s = this.field_146567_u;
            this.field_146568_t = this.field_146566_v;
            double d0 = this.field_146565_w - this.field_146567_u;
            double d1 = this.field_146573_x - this.field_146566_v;

            if (d0 * d0 + d1 * d1 < 4.0D)
            {
                this.field_146567_u += d0;
                this.field_146566_v += d1;
            }
            else
            {
                this.field_146567_u += d0 * 0.85D;
                this.field_146566_v += d1 * 0.85D;
            }
        }
    }

    protected void drawTitle()
    {
        int i = (this.width - this.field_146555_f) / 2;
        int j = (this.height - this.field_146557_g) / 2;
        this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), i + 15, j + 5, 4210752);
    }

    protected void drawAchievementScreen(int p_146552_1_, int p_146552_2_, float p_146552_3_)
    {
        int i = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * (double)p_146552_3_);
        int j = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * (double)p_146552_3_);

        if (i < field_146572_y)
        {
            i = field_146572_y;
        }

        if (j < field_146571_z)
        {
            j = field_146571_z;
        }

        if (i >= field_146559_A)
        {
            i = field_146559_A - 1;
        }

        if (j >= field_146560_B)
        {
            j = field_146560_B - 1;
        }

        int k = (this.width - this.field_146555_f) / 2;
        int l = (this.height - this.field_146557_g) / 2;
        int i1 = k + 16;
        int j1 = l + 17;
        this.zLevel = 0.0F;
        光照状态经理.depthFunc(518);
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)i1, (float)j1, -200.0F);
        光照状态经理.障眼物(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
        光照状态经理.启用手感();
        光照状态经理.disableLighting();
        光照状态经理.enableRescaleNormal();
        光照状态经理.enableColorMaterial();
        int k1 = i + 288 >> 4;
        int l1 = j + 288 >> 4;
        int i2 = (i + 288) % 16;
        int j2 = (j + 288) % 16;
        int k2 = 4;
        int l2 = 8;
        int i3 = 10;
        int j3 = 22;
        int k3 = 37;
        Random random = new Random();
        float f = 16.0F / this.field_146570_r;
        float f1 = 16.0F / this.field_146570_r;

        for (int l3 = 0; (float)l3 * f - (float)j2 < 155.0F; ++l3)
        {
            float f2 = 0.6F - (float)(l1 + l3) / 25.0F * 0.3F;
            光照状态经理.色彩(f2, f2, f2, 1.0F);

            for (int i4 = 0; (float)i4 * f1 - (float)i2 < 224.0F; ++i4)
            {
                random.setSeed((long)(this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16));
                int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
                TextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);

                if (j4 <= 37 && l1 + l3 != 35)
                {
                    if (j4 == 22)
                    {
                        if (random.nextInt(2) == 0)
                        {
                            textureatlassprite = this.func_175371_a(Blocks.diamond_ore);
                        }
                        else
                        {
                            textureatlassprite = this.func_175371_a(Blocks.redstone_ore);
                        }
                    }
                    else if (j4 == 10)
                    {
                        textureatlassprite = this.func_175371_a(Blocks.iron_ore);
                    }
                    else if (j4 == 8)
                    {
                        textureatlassprite = this.func_175371_a(Blocks.coal_ore);
                    }
                    else if (j4 > 4)
                    {
                        textureatlassprite = this.func_175371_a(Blocks.stone);
                    }
                    else if (j4 > 0)
                    {
                        textureatlassprite = this.func_175371_a(Blocks.dirt);
                    }
                }
                else
                {
                    Block block = Blocks.bedrock;
                    textureatlassprite = this.func_175371_a(block);
                }

                this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
                this.drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
            }
        }

        光照状态经理.启用纵深();
        光照状态经理.depthFunc(515);
        this.mc.得到手感经理().绑定手感(ACHIEVEMENT_BACKGROUND);

        for (int j5 = 0; j5 < AchievementList.achievementList.size(); ++j5)
        {
            Achievement achievement1 = (Achievement)AchievementList.achievementList.get(j5);

            if (achievement1.parentAchievement != null)
            {
                int k5 = achievement1.displayColumn * 24 - i + 11;
                int l5 = achievement1.displayRow * 24 - j + 11;
                int j6 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
                int k6 = achievement1.parentAchievement.displayRow * 24 - j + 11;
                boolean flag = this.statFileWriter.hasAchievementUnlocked(achievement1);
                boolean flag1 = this.statFileWriter.canUnlockAchievement(achievement1);
                int k4 = this.statFileWriter.func_150874_c(achievement1);

                if (k4 <= 4)
                {
                    int l4 = -16777216;

                    if (flag)
                    {
                        l4 = -6250336;
                    }
                    else if (flag1)
                    {
                        l4 = -16711936;
                    }

                    this.drawHorizontalLine(k5, j6, l5, l4);
                    this.drawVerticalLine(j6, l5, k6, l4);

                    if (k5 > j6)
                    {
                        this.drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
                    }
                    else if (k5 < j6)
                    {
                        this.drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
                    }
                    else if (l5 > k6)
                    {
                        this.drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
                    }
                    else if (l5 < k6)
                    {
                        this.drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
                    }
                }
            }
        }

        Achievement achievement = null;
        float f3 = (float)(p_146552_1_ - i1) * this.field_146570_r;
        float f4 = (float)(p_146552_2_ - j1) * this.field_146570_r;
        RenderHelper.enableGUIStandardItemLighting();
        光照状态经理.disableLighting();
        光照状态经理.enableRescaleNormal();
        光照状态经理.enableColorMaterial();

        for (int i6 = 0; i6 < AchievementList.achievementList.size(); ++i6)
        {
            Achievement achievement2 = (Achievement)AchievementList.achievementList.get(i6);
            int l6 = achievement2.displayColumn * 24 - i;
            int j7 = achievement2.displayRow * 24 - j;

            if (l6 >= -24 && j7 >= -24 && (float)l6 <= 224.0F * this.field_146570_r && (float)j7 <= 155.0F * this.field_146570_r)
            {
                int l7 = this.statFileWriter.func_150874_c(achievement2);

                if (this.statFileWriter.hasAchievementUnlocked(achievement2))
                {
                    float f5 = 0.75F;
                    光照状态经理.色彩(f5, f5, f5, 1.0F);
                }
                else if (this.statFileWriter.canUnlockAchievement(achievement2))
                {
                    float f6 = 1.0F;
                    光照状态经理.色彩(f6, f6, f6, 1.0F);
                }
                else if (l7 < 3)
                {
                    float f7 = 0.3F;
                    光照状态经理.色彩(f7, f7, f7, 1.0F);
                }
                else if (l7 == 3)
                {
                    float f8 = 0.2F;
                    光照状态经理.色彩(f8, f8, f8, 1.0F);
                }
                else
                {
                    if (l7 != 4)
                    {
                        continue;
                    }

                    float f9 = 0.1F;
                    光照状态经理.色彩(f9, f9, f9, 1.0F);
                }

                this.mc.得到手感经理().绑定手感(ACHIEVEMENT_BACKGROUND);

                if (achievement2.getSpecial())
                {
                    this.drawTexturedModalRect(l6 - 2, j7 - 2, 26, 202, 26, 26);
                }
                else
                {
                    this.drawTexturedModalRect(l6 - 2, j7 - 2, 0, 202, 26, 26);
                }

                if (!this.statFileWriter.canUnlockAchievement(achievement2))
                {
                    float f10 = 0.1F;
                    光照状态经理.色彩(f10, f10, f10, 1.0F);
                    this.itemRender.isNotRenderingEffectsInGUI(false);
                }

                光照状态经理.enableLighting();
                光照状态经理.enableCull();
                this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, l6 + 3, j7 + 3);
                光照状态经理.正常工作(770, 771);
                光照状态经理.disableLighting();

                if (!this.statFileWriter.canUnlockAchievement(achievement2))
                {
                    this.itemRender.isNotRenderingEffectsInGUI(true);
                }

                光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);

                if (f3 >= (float)l6 && f3 <= (float)(l6 + 22) && f4 >= (float)j7 && f4 <= (float)(j7 + 22))
                {
                    achievement = achievement2;
                }
            }
        }

        光照状态经理.禁用纵深();
        光照状态经理.启用混合品();
        光照状态经理.流行音乐黑客帝国();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(ACHIEVEMENT_BACKGROUND);
        this.drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
        this.zLevel = 0.0F;
        光照状态经理.depthFunc(515);
        光照状态经理.禁用纵深();
        光照状态经理.启用手感();
        super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);

        if (achievement != null)
        {
            String s = achievement.getStatName().getUnformattedText();
            String s1 = achievement.getDescription();
            int i7 = p_146552_1_ + 12;
            int k7 = p_146552_2_ - 4;
            int i8 = this.statFileWriter.func_150874_c(achievement);

            if (this.statFileWriter.canUnlockAchievement(achievement))
            {
                int j8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                int i9 = this.fontRendererObj.splitStringWidth(s1, j8);

                if (this.statFileWriter.hasAchievementUnlocked(achievement))
                {
                    i9 += 12;
                }

                this.drawGradientRect(i7 - 3, k7 - 3, i7 + j8 + 3, k7 + i9 + 3 + 12, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s1, i7, k7 + 12, j8, -6250336);

                if (this.statFileWriter.hasAchievementUnlocked(achievement))
                {
                    this.fontRendererObj.绘制纵梁带心理阴影(I18n.format("achievement.taken", new Object[0]), (float)i7, (float)(k7 + i9 + 4), -7302913);
                }
            }
            else if (i8 == 3)
            {
                s = I18n.format("achievement.unknown", new Object[0]);
                int k8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                String s2 = (new ChatComponentTranslation("achievement.requires", new Object[] {achievement.parentAchievement.getStatName()})).getUnformattedText();
                int i5 = this.fontRendererObj.splitStringWidth(s2, k8);
                this.drawGradientRect(i7 - 3, k7 - 3, i7 + k8 + 3, k7 + i5 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s2, i7, k7 + 12, k8, -9416624);
            }
            else if (i8 < 3)
            {
                int l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                String s3 = (new ChatComponentTranslation("achievement.requires", new Object[] {achievement.parentAchievement.getStatName()})).getUnformattedText();
                int j9 = this.fontRendererObj.splitStringWidth(s3, l8);
                this.drawGradientRect(i7 - 3, k7 - 3, i7 + l8 + 3, k7 + j9 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s3, i7, k7 + 12, l8, -9416624);
            }
            else
            {
                s = null;
            }

            if (s != null)
            {
                this.fontRendererObj.绘制纵梁带心理阴影(s, (float)i7, (float)k7, this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1) : (achievement.getSpecial() ? -8355776 : -8355712));
            }
        }

        光照状态经理.启用纵深();
        光照状态经理.enableLighting();
        RenderHelper.disableStandardItemLighting();
    }

    private TextureAtlasSprite func_175371_a(Block p_175371_1_)
    {
        return 我的手艺.得到我的手艺().getBlockRendererDispatcher().getBlockModelShapes().getTexture(p_175371_1_.getDefaultState());
    }

    public boolean doesGuiPauseGame()
    {
        return !this.loadingAchievements;
    }
}
