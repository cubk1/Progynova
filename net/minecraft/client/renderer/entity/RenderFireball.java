package net.minecraft.client.renderer.entity;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends Render<EntityFireball>
{
    private float scale;

    public RenderFireball(RenderManager renderManagerIn, float scaleIn)
    {
        super(renderManagerIn);
        this.scale = scaleIn;
    }

    public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        this.bindEntityTexture(entity);
        光照状态经理.理解((float)x, (float)y, (float)z);
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(this.scale, this.scale, this.scale);
        TextureAtlasSprite textureatlassprite = 我的手艺.得到我的手艺().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMaxU();
        float f2 = textureatlassprite.getMinV();
        float f3 = textureatlassprite.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        光照状态经理.辐射(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5D, -0.25D, 0.0D).tex((double)f, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, -0.25D, 0.0D).tex((double)f1, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(0.5D, 0.75D, 0.0D).tex((double)f1, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(-0.5D, 0.75D, 0.0D).tex((double)f, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityFireball entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
