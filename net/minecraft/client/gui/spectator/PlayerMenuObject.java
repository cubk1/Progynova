package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.我的手艺;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.鬼;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.图像位置;

public class PlayerMenuObject implements ISpectatorMenuObject
{
    private final GameProfile profile;
    private final 图像位置 图像位置;

    public PlayerMenuObject(GameProfile profileIn)
    {
        this.profile = profileIn;
        this.图像位置 = AbstractClientPlayer.getLocationSkin(profileIn.getName());
        AbstractClientPlayer.getDownloadImageSkin(this.图像位置, profileIn.getName());
    }

    public void func_178661_a(SpectatorMenu menu)
    {
        我的手艺.得到我的手艺().getNetHandler().addToSendQueue(new C18PacketSpectate(this.profile.getId()));
    }

    public IChatComponent getSpectatorName()
    {
        return new 交流组分文本(this.profile.getName());
    }

    public void func_178663_a(float p_178663_1_, int alpha)
    {
        我的手艺.得到我的手艺().得到手感经理().绑定手感(this.图像位置);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, (float)alpha / 255.0F);
        鬼.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        鬼.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
    }

    public boolean func_178662_A_()
    {
        return true;
    }
}
