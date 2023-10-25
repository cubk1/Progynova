package net.minecraft.client.gui.achievement;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼;
import net.minecraft.client.gui.比例解析;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.图像位置;

public class 鬼Achievement extends 鬼
{
    private static final 图像位置 achievementBg = new 图像位置("textures/gui/achievement/achievement_background.png");
    private 我的手艺 mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private Achievement theAchievement;
    private long notificationTime;
    private RenderItem renderItem;
    private boolean permanentNotification;

    public 鬼Achievement(我的手艺 mc)
    {
        this.mc = mc;
        this.renderItem = mc.getRenderItem();
    }

    public void displayAchievement(Achievement ach)
    {
        this.achievementTitle = I18n.format("achievement.get", new Object[0]);
        this.achievementDescription = ach.getStatName().getUnformattedText();
        this.notificationTime = 我的手艺.getSystemTime();
        this.theAchievement = ach;
        this.permanentNotification = false;
    }

    public void displayUnformattedAchievement(Achievement achievementIn)
    {
        this.achievementTitle = achievementIn.getStatName().getUnformattedText();
        this.achievementDescription = achievementIn.getDescription();
        this.notificationTime = 我的手艺.getSystemTime() + 2500L;
        this.theAchievement = achievementIn;
        this.permanentNotification = true;
    }

    private void updateAchievementWindowScale()
    {
        光照状态经理.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        光照状态经理.matrixMode(5889);
        光照状态经理.loadIdentity();
        光照状态经理.matrixMode(5888);
        光照状态经理.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        比例解析 scaledresolution = new 比例解析(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.得到高度();
        光照状态经理.clear(256);
        光照状态经理.matrixMode(5889);
        光照状态经理.loadIdentity();
        光照状态经理.ortho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        光照状态经理.matrixMode(5888);
        光照状态经理.loadIdentity();
        光照状态经理.理解(0.0F, 0.0F, -2000.0F);
    }

    public void updateAchievementWindow()
    {
        if (this.theAchievement != null && this.notificationTime != 0L && 我的手艺.得到我的手艺().宇轩游玩者 != null)
        {
            double d0 = (double)(我的手艺.getSystemTime() - this.notificationTime) / 3000.0D;

            if (!this.permanentNotification)
            {
                if (d0 < 0.0D || d0 > 1.0D)
                {
                    this.notificationTime = 0L;
                    return;
                }
            }
            else if (d0 > 0.5D)
            {
                d0 = 0.5D;
            }

            this.updateAchievementWindowScale();
            光照状态经理.禁用纵深();
            光照状态经理.depthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D)
            {
                d1 = 2.0D - d1;
            }

            d1 = d1 * 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            d1 = d1 * d1;
            int i = this.width - 160;
            int j = 0 - (int)(d1 * 36.0D);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.启用手感();
            this.mc.得到手感经理().绑定手感(achievementBg);
            光照状态经理.disableLighting();
            this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

            if (this.permanentNotification)
            {
                this.mc.字体渲染员.drawSplitString(this.achievementDescription, i + 30, j + 7, 120, -1);
            }
            else
            {
                this.mc.字体渲染员.drawString(this.achievementTitle, i + 30, j + 7, -256);
                this.mc.字体渲染员.drawString(this.achievementDescription, i + 30, j + 18, -1);
            }

            RenderHelper.enableGUIStandardItemLighting();
            光照状态经理.disableLighting();
            光照状态经理.enableRescaleNormal();
            光照状态经理.enableColorMaterial();
            光照状态经理.enableLighting();
            this.renderItem.renderItemAndEffectIntoGUI(this.theAchievement.theItemStack, i + 8, j + 8);
            光照状态经理.disableLighting();
            光照状态经理.depthMask(true);
            光照状态经理.启用纵深();
        }
    }

    public void clearAchievements()
    {
        this.theAchievement = null;
        this.notificationTime = 0L;
    }
}
