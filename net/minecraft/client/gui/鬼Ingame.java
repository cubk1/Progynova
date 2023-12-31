package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.图像位置;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import net.optifine.CustomColors;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.宇轩科技;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.visual.宇轩的头部显示;

public class 鬼Ingame extends 鬼
{
    private static final 图像位置 vignetteTexPath = new 图像位置("textures/misc/vignette.png");
    private static final 图像位置 widgetsTexPath = new 图像位置("textures/gui/widgets.png");
    private static final 图像位置 pumpkinBlurTexPath = new 图像位置("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final 我的手艺 mc;
    private final RenderItem itemRenderer;
    private final 鬼NewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying = "";
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness = 1.0F;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final 鬼OverlayDebug overlayDebug;
    private final 鬼Spectator spectatorGui;
    private final 鬼PlayerTabOverlay overlayPlayerList;
    private int titlesTimer;
    private String displayedTitle = "";
    private String displayedSubTitle = "";
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long lastSystemTime = 0L;
    private long healthUpdateCounter = 0L;

    public 鬼Ingame(我的手艺 mcIn)
    {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new 鬼OverlayDebug(mcIn);
        this.spectatorGui = new 鬼Spectator(mcIn);
        this.persistantChatGUI = new 鬼NewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new 鬼PlayerTabOverlay(mcIn, this);
        this.setDefaultTitlesTimes();
    }

    public void setDefaultTitlesTimes()
    {
        this.titleFadeIn = 10;
        this.titleDisplayTime = 70;
        this.titleFadeOut = 20;
    }

    public void renderGameOverlay(float partialTicks)
    {
        比例解析 scaledresolution = new 比例解析(this.mc);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.得到高度();
        this.mc.entityRenderer.setupOverlayRendering();
        光照状态经理.启用混合品();

        if (Config.isVignetteEnabled())
        {
            this.renderVignette(this.mc.宇轩游玩者.getBrightness(partialTicks), scaledresolution);
        }
        else
        {
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        ItemStack itemstack = this.mc.宇轩游玩者.inventory.armorItemInSlot(3);

        if (this.mc.游戏一窝.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
        {
            this.renderPumpkinOverlay(scaledresolution);
        }

        if (!this.mc.宇轩游玩者.isPotionActive(Potion.confusion))
        {
            float f = this.mc.宇轩游玩者.prevTimeInPortal + (this.mc.宇轩游玩者.timeInPortal - this.mc.宇轩游玩者.prevTimeInPortal) * partialTicks;

            if (f > 0.0F)
            {
                this.renderPortal(f, scaledresolution);
            }
        }

        if (this.mc.玩家控制者.isSpectator())
        {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else
        {
            this.renderTooltip(scaledresolution, partialTicks);
        }

        宇轩的头部显示.宇轩渲染事件();

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(icons);
        光照状态经理.启用混合品();

        if (this.showCrosshair())
        {
            光照状态经理.tryBlendFuncSeparate(775, 769, 1, 0);
            光照状态经理.启用希腊字母表的第1个字母();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }

        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();

        if (this.mc.玩家控制者.shouldDrawHUD())
        {
            this.renderPlayerStats(scaledresolution);
        }

        光照状态经理.禁用混合品();

        if (this.mc.宇轩游玩者.getSleepTimer() > 0)
        {
            this.mc.mcProfiler.startSection("sleep");
            光照状态经理.禁用纵深();
            光照状态经理.禁用希腊字母表的第1个字母();
            int j1 = this.mc.宇轩游玩者.getSleepTimer();
            float f1 = (float)j1 / 100.0F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F - (float)(j1 - 100) / 10.0F;
            }

            int k = (int)(220.0F * f1) << 24 | 1052704;
            drawRect(0, 0, i, j, k);
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.启用纵深();
            this.mc.mcProfiler.endSection();
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        int k1 = i / 2 - 91;

        if (this.mc.宇轩游玩者.isRidingHorse())
        {
            this.renderHorseJumpBar(scaledresolution, k1);
        }
        else if (this.mc.玩家控制者.gameIsSurvivalOrAdventure())
        {
            this.renderExpBar(scaledresolution, k1);
        }

        if (this.mc.游戏一窝.heldItemTooltips && !this.mc.玩家控制者.isSpectator())
        {
            this.renderSelectedItem(scaledresolution);
        }
        else if (this.mc.宇轩游玩者.isSpectator())
        {
            this.spectatorGui.renderSelectedItem(scaledresolution);
        }

        if (this.mc.isDemo())
        {
            this.renderDemo(scaledresolution);
        }

        if (this.mc.游戏一窝.showDebugInfo)
        {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }

        if (this.recordPlayingUpFor > 0)
        {
            this.mc.mcProfiler.startSection("overlayMessage");
            float f2 = (float)this.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f2 * 255.0F / 20.0F);

            if (l1 > 255)
            {
                l1 = 255;
            }

            if (l1 > 8)
            {
                光照状态经理.推黑客帝国();
                光照状态经理.理解((float)(i / 2), (float)(j - 68), 0.0F);
                光照状态经理.启用混合品();
                光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
                int l = 16777215;

                if (this.recordIsPlaying)
                {
                    l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
                光照状态经理.禁用混合品();
                光照状态经理.流行音乐黑客帝国();
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.titlesTimer > 0)
        {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float)this.titlesTimer - partialTicks;
            int i2 = 255;

            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime)
            {
                float f4 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
                i2 = (int)(f4 * 255.0F / (float)this.titleFadeIn);
            }

            if (this.titlesTimer <= this.titleFadeOut)
            {
                i2 = (int)(f3 * 255.0F / (float)this.titleFadeOut);
            }

            i2 = MathHelper.clamp_int(i2, 0, 255);

            if (i2 > 8)
            {
                光照状态经理.推黑客帝国();
                光照状态经理.理解((float)(i / 2), (float)(j / 2), 0.0F);
                光照状态经理.启用混合品();
                光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
                光照状态经理.推黑客帝国();
                光照状态经理.障眼物(4.0F, 4.0F, 4.0F);
                int j2 = i2 << 24 & -16777216;
                this.getFontRenderer().drawString(this.displayedTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0F, 16777215 | j2, true);
                光照状态经理.流行音乐黑客帝国();
                光照状态经理.推黑客帝国();
                光照状态经理.障眼物(2.0F, 2.0F, 2.0F);
                this.getFontRenderer().drawString(this.displayedSubTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0F, 16777215 | j2, true);
                光照状态经理.流行音乐黑客帝国();
                光照状态经理.禁用混合品();
                光照状态经理.流行音乐黑客帝国();
            }

            this.mc.mcProfiler.endSection();
        }

        Scoreboard scoreboard = this.mc.宇轩の世界.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.宇轩游玩者.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null)
        {
            this.renderScoreboard(scoreobjective1, scaledresolution);
        }

        光照状态经理.启用混合品();
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        光照状态经理.禁用希腊字母表的第1个字母();
        光照状态经理.推黑客帝国();
        光照状态经理.理解(0.0F, (float)(j - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        光照状态经理.流行音乐黑客帝国();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (this.mc.游戏一窝.keyBindPlayerList.键位绑定沿着() && (!this.mc.isIntegratedServerRunning() || this.mc.宇轩游玩者.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null))
        {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        }
        else
        {
            this.overlayPlayerList.updatePlayerList(false);
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.disableLighting();
        光照状态经理.启用希腊字母表的第1个字母();
    }

    protected void renderTooltip(比例解析 sr, float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof 实体Player)
        {
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.得到手感经理().绑定手感(widgetsTexPath);
            实体Player entityplayer = (实体Player)this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            this.zLevel = -90.0F;
            this.drawTexturedModalRect(i - 91, sr.得到高度() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.得到高度() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f;
            光照状态经理.enableRescaleNormal();
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j)
            {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.得到高度() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            光照状态经理.disableRescaleNormal();
            光照状态经理.禁用混合品();
        }
    }

    public void renderHorseJumpBar(比例解析 scaledRes, int x)
    {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.得到手感经理().绑定手感(鬼.icons);
        float f = this.mc.宇轩游玩者.getHorseJumpPower();
        int i = 182;
        int j = (int)(f * (float)(i + 1));
        int k = scaledRes.得到高度() - 32 + 3;
        this.drawTexturedModalRect(x, k, 0, 84, i, 5);

        if (j > 0)
        {
            this.drawTexturedModalRect(x, k, 0, 89, j, 5);
        }

        this.mc.mcProfiler.endSection();
    }

    public void renderExpBar(比例解析 scaledRes, int x)
    {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.得到手感经理().绑定手感(鬼.icons);
        int i = this.mc.宇轩游玩者.xpBarCap();

        if (i > 0)
        {
            int j = 182;
            int k = (int)(this.mc.宇轩游玩者.experience * (float)(j + 1));
            int l = scaledRes.得到高度() - 32 + 3;
            this.drawTexturedModalRect(x, l, 0, 64, j, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(x, l, 0, 69, k, 5);
            }
        }

        this.mc.mcProfiler.endSection();

        if (this.mc.宇轩游玩者.experienceLevel > 0)
        {
            this.mc.mcProfiler.startSection("expLevel");
            int k1 = 8453920;

            if (Config.isCustomColors())
            {
                k1 = CustomColors.getExpBarTextColor(k1);
            }

            String s = "" + this.mc.宇轩游玩者.experienceLevel;
            int l1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int i1 = scaledRes.得到高度() - 31 - 4;
            int j1 = 0;
            this.getFontRenderer().drawString(s, l1 + 1, i1, 0);
            this.getFontRenderer().drawString(s, l1 - 1, i1, 0);
            this.getFontRenderer().drawString(s, l1, i1 + 1, 0);
            this.getFontRenderer().drawString(s, l1, i1 - 1, 0);
            this.getFontRenderer().drawString(s, l1, i1, k1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderSelectedItem(比例解析 scaledRes)
    {
        this.mc.mcProfiler.startSection("selectedItemName");

        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null)
        {
            String s = this.highlightingItemStack.getDisplayName();

            if (this.highlightingItemStack.hasDisplayName())
            {
                s = 枚举聊天格式.ITALIC + s;
            }

            int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = scaledRes.得到高度() - 59;

            if (!this.mc.玩家控制者.shouldDrawHUD())
            {
                j += 14;
            }

            int k = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);

            if (k > 255)
            {
                k = 255;
            }

            if (k > 0)
            {
                光照状态经理.推黑客帝国();
                光照状态经理.启用混合品();
                光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().绘制纵梁带心理阴影(s, (float)i, (float)j, 16777215 + (k << 24));
                光照状态经理.禁用混合品();
                光照状态经理.流行音乐黑客帝国();
            }
        }

        this.mc.mcProfiler.endSection();
    }

    public void renderDemo(比例解析 scaledRes)
    {
        this.mc.mcProfiler.startSection("demo");
        String s = "";

        if (this.mc.宇轩の世界.getTotalWorldTime() >= 120500L)
        {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else
        {
            s = I18n.format("demo.remainingTime", new Object[] {StringUtils.ticksToElapsedTime((int)(120500L - this.mc.宇轩の世界.getTotalWorldTime()))});
        }

        int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().绘制纵梁带心理阴影(s, (float)(scaledRes.getScaledWidth() - i - 10), 5.0F, 16777215);
        this.mc.mcProfiler.endSection();
    }

    protected boolean showCrosshair()
    {
        if (this.mc.游戏一窝.showDebugInfo && !this.mc.宇轩游玩者.hasReducedDebug() && !this.mc.游戏一窝.reducedDebugInfo)
        {
            return false;
        }
        else if (this.mc.玩家控制者.isSpectator())
        {
            if (this.mc.pointed实体 != null)
            {
                return true;
            }
            else
            {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    阻止位置 blockpos = this.mc.objectMouseOver.getBlockPos();

                    if (this.mc.宇轩の世界.getTileEntity(blockpos) instanceof IInventory)
                    {
                        return true;
                    }
                }

                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void renderStreamIndicator(比例解析 scaledRes)
    {
        this.streamIndicator.render(scaledRes.getScaledWidth() - 10, 10);
    }

    private void renderScoreboard(ScoreObjective objective, 比例解析 scaledRes)
    {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>()
        {
            public boolean apply(Score p_apply_1_)
            {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));

        if (list.size() > 15)
        {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        }
        else
        {
            collection = list;
        }

        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

        for (Score score : collection)
        {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + 枚举聊天格式.RED + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }

        int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        int j1 = scaledRes.得到高度() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;

        for (Score score1 : collection)
        {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = 枚举聊天格式.RED + "" + score1.getScorePoints();
            int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
            int l = scaledRes.getScaledWidth() - k1 + 2;
            drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
            this.getFontRenderer().drawString(s1, l1, k, 553648127);
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 553648127);

            if (j == collection.size())
            {
                String s3 = objective.getDisplayName();
                drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                drawRect(l1 - 2, k - 1, l, k, 1342177280);
                this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 553648127);
            }
        }
    }

    private void renderPlayerStats(比例解析 scaledRes)
    {
        if (this.mc.getRenderViewEntity() instanceof 实体Player)
        {
            实体Player entityplayer = (实体Player)this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;

            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = 我的手艺.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 20);
            }
            else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = 我的手艺.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 10);
            }

            if (我的手艺.getSystemTime() - this.lastSystemTime > 1000L)
            {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = 我的手艺.getSystemTime();
            }

            this.playerHealth = i;
            int j = this.lastPlayerHealth;
            this.rand.setSeed((long)(this.updateCounter * 312871));
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.获取饥饿值();
            int k = foodstats.获取饥饿等级();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = scaledRes.getScaledWidth() / 2 - 91;
            int j1 = scaledRes.getScaledWidth() / 2 + 91;
            int k1 = scaledRes.得到高度() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration))
            {
                l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            this.mc.mcProfiler.startSection("armor");

            for (int i3 = 0; i3 < 10; ++i3)
            {
                if (k2 > 0)
                {
                    int j3 = i1 + i3 * 8;

                    if (i3 * 2 + 1 < k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 == k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 > k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endStartSection("health");

            for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; --i6)
            {
                int j6 = 16;

                if (entityplayer.isPotionActive(Potion.poison))
                {
                    j6 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither))
                {
                    j6 += 72;
                }

                int k3 = 0;

                if (flag)
                {
                    k3 = 1;
                }

                int l3 = MathHelper.ceiling_float_int((float)(i6 + 1) / 10.0F) - 1;
                int i4 = i1 + i6 % 10 * 8;
                int j4 = k1 - l3 * i2;

                if (i <= 4)
                {
                    j4 += this.rand.nextInt(2);
                }

                if (i6 == l2)
                {
                    j4 -= 2;
                }

                int k4 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled())
                {
                    k4 = 5;
                }

                this.drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);

                if (flag)
                {
                    if (i6 * 2 + 1 < j)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == j)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
                    }
                }

                if (f2 <= 0.0F)
                {
                    if (i6 * 2 + 1 < i)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == i)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
                    }
                }
                else
                {
                    if (f2 == f1 && f1 % 2.0F == 1.0F)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
                    }

                    f2 -= 2.0F;
                }
            }

            实体 实体 = entityplayer.riding实体;

            if (实体 == null)
            {
                this.mc.mcProfiler.endStartSection("food");

                for (int k6 = 0; k6 < 10; ++k6)
                {
                    int j7 = k1;
                    int l7 = 16;
                    int k8 = 0;

                    if (entityplayer.isPotionActive(Potion.hunger))
                    {
                        l7 += 36;
                        k8 = 13;
                    }

                    if (entityplayer.获取饥饿值().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0)
                    {
                        j7 = k1 + (this.rand.nextInt(3) - 1);
                    }

                    if (flag1)
                    {
                        k8 = 1;
                    }

                    int j9 = j1 - k6 * 8 - 9;
                    this.drawTexturedModalRect(j9, j7, 16 + k8 * 9, 27, 9, 9);

                    if (flag1)
                    {
                        if (k6 * 2 + 1 < l)
                        {
                            this.drawTexturedModalRect(j9, j7, l7 + 54, 27, 9, 9);
                        }

                        if (k6 * 2 + 1 == l)
                        {
                            this.drawTexturedModalRect(j9, j7, l7 + 63, 27, 9, 9);
                        }
                    }

                    if (k6 * 2 + 1 < k)
                    {
                        this.drawTexturedModalRect(j9, j7, l7 + 36, 27, 9, 9);
                    }

                    if (k6 * 2 + 1 == k)
                    {
                        this.drawTexturedModalRect(j9, j7, l7 + 45, 27, 9, 9);
                    }
                }
            }
            else if (实体 instanceof 实体LivingBase)
            {
                this.mc.mcProfiler.endStartSection("mountHealth");
                实体LivingBase entitylivingbase = (实体LivingBase) 实体;
                int i7 = (int)Math.ceil((double)entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int j8 = (int)(f3 + 0.5F) / 2;

                if (j8 > 30)
                {
                    j8 = 30;
                }

                int i9 = k1;

                for (int k9 = 0; j8 > 0; k9 += 20)
                {
                    int l4 = Math.min(j8, 10);
                    j8 -= l4;

                    for (int i5 = 0; i5 < l4; ++i5)
                    {
                        int j5 = 52;
                        int k5 = 0;

                        if (flag1)
                        {
                            k5 = 1;
                        }

                        int l5 = j1 - i5 * 8 - 9;
                        this.drawTexturedModalRect(l5, i9, j5 + k5 * 9, 9, 9, 9);

                        if (i5 * 2 + 1 + k9 < i7)
                        {
                            this.drawTexturedModalRect(l5, i9, j5 + 36, 9, 9, 9);
                        }

                        if (i5 * 2 + 1 + k9 == i7)
                        {
                            this.drawTexturedModalRect(l5, i9, j5 + 45, 9, 9, 9);
                        }
                    }

                    i9 -= 10;
                }
            }

            this.mc.mcProfiler.endStartSection("air");

            if (entityplayer.isInsideOfMaterial(Material.water))
            {
                int l6 = this.mc.宇轩游玩者.getAir();
                int k7 = MathHelper.ceiling_double_int((double)(l6 - 2) * 10.0D / 300.0D);
                int i8 = MathHelper.ceiling_double_int((double)l6 * 10.0D / 300.0D) - k7;

                for (int l8 = 0; l8 < k7 + i8; ++l8)
                {
                    if (l8 < k7)
                    {
                        this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }

    private void renderBossHealth()
    {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
        {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.字体渲染员;
            比例解析 scaledresolution = new 比例解析(this.mc);
            int i = scaledresolution.getScaledWidth();
            int j = 182;
            int k = i / 2 - j / 2;
            int l = (int)(BossStatus.healthScale * (float)(j + 1));
            int i1 = 12;
            this.drawTexturedModalRect(k, i1, 0, 74, j, 5);
            this.drawTexturedModalRect(k, i1, 0, 74, j, 5);

            if (l > 0)
            {
                this.drawTexturedModalRect(k, i1, 0, 79, l, 5);
            }

            String s = BossStatus.bossName;
            this.getFontRenderer().绘制纵梁带心理阴影(s, (float)(i / 2 - this.getFontRenderer().getStringWidth(s) / 2), (float)(i1 - 10), 16777215);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.得到手感经理().绑定手感(icons);
        }
    }

    private void renderPumpkinOverlay(比例解析 scaledRes)
    {
        光照状态经理.禁用纵深();
        光照状态经理.depthMask(false);
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.禁用希腊字母表的第1个字母();
        this.mc.得到手感经理().绑定手感(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double)scaledRes.得到高度(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.得到高度(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        光照状态经理.depthMask(true);
        光照状态经理.启用纵深();
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderVignette(float lightLevel, 比例解析 scaledRes)
    {
        if (!Config.isVignetteEnabled())
        {
            光照状态经理.启用纵深();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        else
        {
            lightLevel = 1.0F - lightLevel;
            lightLevel = MathHelper.clamp_float(lightLevel, 0.0F, 1.0F);
            WorldBorder worldborder = this.mc.宇轩の世界.getWorldBorder();
            float f = (float)worldborder.getClosestDistance(this.mc.宇轩游玩者);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max((double)worldborder.getWarningDistance(), d0);

            if ((double)f < d1)
            {
                f = 1.0F - (float)((double)f / d1);
            }
            else
            {
                f = 0.0F;
            }

            this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(lightLevel - this.prevVignetteBrightness) * 0.01D);
            光照状态经理.禁用纵深();
            光照状态经理.depthMask(false);
            光照状态经理.tryBlendFuncSeparate(0, 769, 1, 0);

            if (f > 0.0F)
            {
                光照状态经理.色彩(0.0F, f, f, 1.0F);
            }
            else
            {
                光照状态经理.色彩(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
            }

            this.mc.得到手感经理().绑定手感(vignetteTexPath);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0D, (double)scaledRes.得到高度(), -90.0D).tex(0.0D, 1.0D).endVertex();
            worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.得到高度(), -90.0D).tex(1.0D, 1.0D).endVertex();
            worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            光照状态经理.depthMask(true);
            光照状态经理.启用纵深();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }

    private void renderPortal(float timeInPortal, 比例解析 scaledRes)
    {
        if (timeInPortal < 1.0F)
        {
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }

        光照状态经理.禁用希腊字母表的第1个字母();
        光照状态经理.禁用纵深();
        光照状态经理.depthMask(false);
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, timeInPortal);
        this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double)scaledRes.得到高度(), -90.0D).tex((double)f, (double)f3).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.得到高度(), -90.0D).tex((double)f2, (double)f3).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex((double)f2, (double)f1).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex((double)f, (double)f1).endVertex();
        tessellator.draw();
        光照状态经理.depthMask(true);
        光照状态经理.启用纵深();
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, 实体Player player)
    {
        ItemStack itemstack = player.inventory.mainInventory[index];

        if (itemstack != null)
        {
            float f = (float)itemstack.animationsToGo - partialTicks;

            if (f > 0.0F)
            {
                光照状态经理.推黑客帝国();
                float f1 = 1.0F + f / 5.0F;
                光照状态经理.理解((float)(xPos + 8), (float)(yPos + 12), 0.0F);
                光照状态经理.障眼物(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                光照状态经理.理解((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0F);
            }

            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F)
            {
                光照状态经理.流行音乐黑客帝国();
            }

            this.itemRenderer.renderItemOverlays(this.mc.字体渲染员, itemstack, xPos, yPos);
        }
    }

    public void updateTick()
    {
        if (this.recordPlayingUpFor > 0)
        {
            --this.recordPlayingUpFor;
        }

        if (this.titlesTimer > 0)
        {
            --this.titlesTimer;

            if (this.titlesTimer <= 0)
            {
                this.displayedTitle = "";
                this.displayedSubTitle = "";
            }
        }

        ++this.updateCounter;
        this.streamIndicator.updateStreamAlpha();

        if (this.mc.宇轩游玩者 != null)
        {
            ItemStack itemstack = this.mc.宇轩游玩者.inventory.getCurrentItem();

            if (itemstack == null)
            {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata()))
            {
                if (this.remainingHighlightTicks > 0)
                {
                    --this.remainingHighlightTicks;
                }
            }
            else
            {
                this.remainingHighlightTicks = 40;
            }

            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String recordName)
    {
        this.setRecordPlaying(I18n.format("record.nowPlaying", new Object[] {recordName}), true);
    }

    public void setRecordPlaying(String message, boolean isPlaying)
    {
        this.recordPlaying = message;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = isPlaying;
    }

    public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut)
    {
        if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0)
        {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
            this.titlesTimer = 0;
        }
        else if (title != null)
        {
            this.displayedTitle = title;
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
        }
        else if (subTitle != null)
        {
            this.displayedSubTitle = subTitle;
        }
        else
        {
            if (timeFadeIn >= 0)
            {
                this.titleFadeIn = timeFadeIn;
            }

            if (displayTime >= 0)
            {
                this.titleDisplayTime = displayTime;
            }

            if (timeFadeOut >= 0)
            {
                this.titleFadeOut = timeFadeOut;
            }

            if (this.titlesTimer > 0)
            {
                this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
            }
        }
    }

    public void setRecordPlaying(IChatComponent component, boolean isPlaying)
    {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying);
    }

    public 鬼NewChat getChatGUI()
    {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter()
    {
        return this.updateCounter;
    }

    public FontRenderer getFontRenderer()
    {
        return this.mc.字体渲染员;
    }

    public 鬼Spectator getSpectatorGui()
    {
        return this.spectatorGui;
    }

    public 鬼PlayerTabOverlay getTabList()
    {
        return this.overlayPlayerList;
    }

    public void resetPlayersOverlayFooterHeader()
    {
        this.overlayPlayerList.resetFooterHeader();
    }
}
