package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

import net.minecraft.client.我的手艺;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.鬼;
import net.minecraft.client.gui.鬼Spectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List<ISpectatorMenuObject> field_178672_a = Lists.<ISpectatorMenuObject>newArrayList();

    public TeleportToTeam()
    {
        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();

        for (ScorePlayerTeam scoreplayerteam : 宇轩的世界.宇轩の世界.getScoreboard().getTeams())
        {
            this.field_178672_a.add(new TeleportToTeam.TeamSelectionObject(scoreplayerteam));
        }
    }

    public List<ISpectatorMenuObject> func_178669_a()
    {
        return this.field_178672_a;
    }

    public IChatComponent func_178670_b()
    {
        return new 交流组分文本("Select a team to teleport to");
    }

    public void func_178661_a(SpectatorMenu menu)
    {
        menu.func_178647_a(this);
    }

    public IChatComponent getSpectatorName()
    {
        return new 交流组分文本("Teleport to team member");
    }

    public void func_178663_a(float p_178663_1_, int alpha)
    {
        我的手艺.得到我的手艺().得到手感经理().绑定手感(鬼Spectator.field_175269_a);
        鬼.绘制模态矩形以自定义大小纹理(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
    }

    public boolean func_178662_A_()
    {
        for (ISpectatorMenuObject ispectatormenuobject : this.field_178672_a)
        {
            if (ispectatormenuobject.func_178662_A_())
            {
                return true;
            }
        }

        return false;
    }

    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam field_178676_b;
        private final ResourceLocation field_178677_c;
        private final List<NetworkPlayerInfo> field_178675_d;

        public TeamSelectionObject(ScorePlayerTeam p_i45492_2_)
        {
            this.field_178676_b = p_i45492_2_;
            this.field_178675_d = Lists.<NetworkPlayerInfo>newArrayList();

            for (String s : p_i45492_2_.getMembershipCollection())
            {
                NetworkPlayerInfo networkplayerinfo = 我的手艺.得到我的手艺().getNetHandler().getPlayerInfo(s);

                if (networkplayerinfo != null)
                {
                    this.field_178675_d.add(networkplayerinfo);
                }
            }

            if (!this.field_178675_d.isEmpty())
            {
                String s1 = ((NetworkPlayerInfo)this.field_178675_d.get((new Random()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
                this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);
            }
            else
            {
                this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }

        public void func_178661_a(SpectatorMenu menu)
        {
            menu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }

        public IChatComponent getSpectatorName()
        {
            return new 交流组分文本(this.field_178676_b.getTeamName());
        }

        public void func_178663_a(float p_178663_1_, int alpha)
        {
            int i = -1;
            String s = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());

            if (s.length() >= 2)
            {
                i = 我的手艺.得到我的手艺().fontRendererObj.getColorCode(s.charAt(1));
            }

            if (i >= 0)
            {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                鬼.drawRect(1, 1, 15, 15, MathHelper.func_180183_b(f * p_178663_1_, f1 * p_178663_1_, f2 * p_178663_1_) | alpha << 24);
            }

            我的手艺.得到我的手艺().得到手感经理().绑定手感(this.field_178677_c);
            光照状态经理.色彩(p_178663_1_, p_178663_1_, p_178663_1_, (float)alpha / 255.0F);
            鬼.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
            鬼.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        }

        public boolean func_178662_A_()
        {
            return !this.field_178675_d.isEmpty();
        }
    }
}
