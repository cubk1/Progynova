package net.optifine.player;

import java.awt.image.BufferedImage;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.图像位置;

public class CapeImageBuffer extends ImageBufferDownload
{
    private AbstractClientPlayer player;
    private 图像位置 图像位置;
    private boolean elytraOfCape;

    public CapeImageBuffer(AbstractClientPlayer player, 图像位置 图像位置)
    {
        this.player = player;
        this.图像位置 = 图像位置;
    }

    public BufferedImage parseUserSkin(BufferedImage imageRaw)
    {
        BufferedImage bufferedimage = CapeUtils.parseCape(imageRaw);
        this.elytraOfCape = CapeUtils.isElytraCape(imageRaw, bufferedimage);
        return bufferedimage;
    }

    public void skinAvailable()
    {
        if (this.player != null)
        {
            this.player.setLocationOfCape(this.图像位置);
            this.player.setElytraOfCape(this.elytraOfCape);
        }

        this.cleanup();
    }

    public void cleanup()
    {
        this.player = null;
    }

    public boolean isElytraOfCape()
    {
        return this.elytraOfCape;
    }
}
