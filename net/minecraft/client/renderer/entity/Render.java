package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public abstract class Render<T extends 实体> implements IEntityRenderer
{
    private static final 图像位置 shadowTextures = new 图像位置("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    public float shadowSize;
    protected float shadowOpaque = 1.0F;
    private Class entityClass = null;
    private 图像位置 locationTextureCustom = null;

    protected Render(RenderManager renderManager)
    {
        this.renderManager = renderManager;
    }

    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = livingEntity.getEntityBoundingBox();

        if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0D)
        {
            axisalignedbb = new AxisAlignedBB(livingEntity.X坐标 - 2.0D, livingEntity.Y坐标 - 2.0D, livingEntity.Z坐标 - 2.0D, livingEntity.X坐标 + 2.0D, livingEntity.Y坐标 + 2.0D, livingEntity.Z坐标 + 2.0D);
        }

        return livingEntity.isInRangeToRender3d(camX, camY, camZ) && (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.renderName(entity, x, y, z);
    }

    protected void renderName(T entity, double x, double y, double z)
    {
        if (this.canRenderName(entity))
        {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
        }
    }

    protected boolean canRenderName(T entity)
    {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }

    protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_)
    {
        this.renderLivingLabel(entityIn, str, x, y, z, 64);
    }

    protected abstract 图像位置 getEntityTexture(T entity);

    protected boolean bindEntityTexture(T entity)
    {
        图像位置 resourcelocation = this.getEntityTexture(entity);

        if (this.locationTextureCustom != null)
        {
            resourcelocation = this.locationTextureCustom;
        }

        if (resourcelocation == null)
        {
            return false;
        }
        else
        {
            this.bindTexture(resourcelocation);
            return true;
        }
    }

    public void bindTexture(图像位置 location)
    {
        this.renderManager.renderEngine.绑定手感(location);
    }

    private void renderEntityOnFire(实体 实体, double x, double y, double z, float partialTicks)
    {
        光照状态经理.disableLighting();
        TextureMap texturemap = 我的手艺.得到我的手艺().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        float f = 实体.width * 1.4F;
        光照状态经理.障眼物(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 实体.height / f;
        float f4 = (float)(实体.Y坐标 - 实体.getEntityBoundingBox().minY);
        光照状态经理.辐射(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        光照状态经理.理解(0.0F, 0.0F, -0.3F + (float)((int)f3) * 0.02F);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        float f5 = 0.0F;
        int i = 0;
        boolean flag = Config.isMultiTexture();

        if (flag)
        {
            worldrenderer.setBlockLayer(EnumWorldBlockLayer.SOLID);
        }

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        while (f3 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            worldrenderer.setSprite(textureatlassprite2);
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            worldrenderer.pos((double)(f1 - f2), (double)(0.0F - f4), (double)f5).tex((double)f8, (double)f9).endVertex();
            worldrenderer.pos((double)(-f1 - f2), (double)(0.0F - f4), (double)f5).tex((double)f6, (double)f9).endVertex();
            worldrenderer.pos((double)(-f1 - f2), (double)(1.4F - f4), (double)f5).tex((double)f6, (double)f7).endVertex();
            worldrenderer.pos((double)(f1 - f2), (double)(1.4F - f4), (double)f5).tex((double)f8, (double)f7).endVertex();
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
            ++i;
        }

        tessellator.draw();

        if (flag)
        {
            worldrenderer.setBlockLayer((EnumWorldBlockLayer)null);
            光照状态经理.bindCurrentTexture();
        }

        光照状态经理.流行音乐黑客帝国();
        光照状态经理.enableLighting();
    }

    private void renderShadow(实体 实体In, double x, double y, double z, float shadowAlpha, float partialTicks)
    {
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow)
        {
            光照状态经理.启用混合品();
            光照状态经理.正常工作(770, 771);
            this.renderManager.renderEngine.绑定手感(shadowTextures);
            World world = this.getWorldFromRenderManager();
            光照状态经理.depthMask(false);
            float f = this.shadowSize;

            if (实体In instanceof 实体Living)
            {
                实体Living entityliving = (实体Living) 实体In;
                f *= entityliving.getRenderSizeModifier();

                if (entityliving.isChild())
                {
                    f *= 0.5F;
                }
            }

            double d5 = 实体In.lastTickPosX + (实体In.X坐标 - 实体In.lastTickPosX) * (double)partialTicks;
            double d0 = 实体In.lastTickPosY + (实体In.Y坐标 - 实体In.lastTickPosY) * (double)partialTicks;
            double d1 = 实体In.lastTickPosZ + (实体In.Z坐标 - 实体In.lastTickPosZ) * (double)partialTicks;
            int i = MathHelper.floor_double(d5 - (double)f);
            int j = MathHelper.floor_double(d5 + (double)f);
            int k = MathHelper.floor_double(d0 - (double)f);
            int l = MathHelper.floor_double(d0);
            int i1 = MathHelper.floor_double(d1 - (double)f);
            int j1 = MathHelper.floor_double(d1 + (double)f);
            double d2 = x - d5;
            double d3 = y - d0;
            double d4 = z - d1;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

            for (阻止位置 blockpos : 阻止位置.getAllInBoxMutable(new 阻止位置(i, k, i1), new 阻止位置(j, l, j1)))
            {
                Block block = world.getBlockState(blockpos.down()).getBlock();

                if (block.getRenderType() != -1 && world.getLightFromNeighbors(blockpos) > 3)
                {
                    this.renderShadowBlock(block, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
                }
            }

            tessellator.draw();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.禁用混合品();
            光照状态经理.depthMask(true);
        }
    }

    private World getWorldFromRenderManager()
    {
        return this.renderManager.worldObj;
    }

    private void renderShadowBlock(Block blockIn, double p_180549_2_, double p_180549_4_, double p_180549_6_, 阻止位置 pos, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_)
    {
        if (blockIn.isFullCube())
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            double d0 = ((double)p_180549_9_ - (p_180549_4_ - ((double)pos.getY() + p_180549_13_)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getLightBrightness(pos);

            if (d0 >= 0.0D)
            {
                if (d0 > 1.0D)
                {
                    d0 = 1.0D;
                }

                double d1 = (double)pos.getX() + blockIn.getBlockBoundsMinX() + p_180549_11_;
                double d2 = (double)pos.getX() + blockIn.getBlockBoundsMaxX() + p_180549_11_;
                double d3 = (double)pos.getY() + blockIn.getBlockBoundsMinY() + p_180549_13_ + 0.015625D;
                double d4 = (double)pos.getZ() + blockIn.getBlockBoundsMinZ() + p_180549_15_;
                double d5 = (double)pos.getZ() + blockIn.getBlockBoundsMaxZ() + p_180549_15_;
                float f = (float)((p_180549_2_ - d1) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f1 = (float)((p_180549_2_ - d2) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f2 = (float)((p_180549_6_ - d4) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f3 = (float)((p_180549_6_ - d5) / 2.0D / (double)p_180549_10_ + 0.5D);
                worldrenderer.pos(d1, d3, d4).tex((double)f, (double)f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                worldrenderer.pos(d1, d3, d5).tex((double)f, (double)f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                worldrenderer.pos(d2, d3, d5).tex((double)f1, (double)f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                worldrenderer.pos(d2, d3, d4).tex((double)f1, (double)f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
            }
        }
    }

    public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z)
    {
        光照状态经理.禁用手感();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        worldrenderer.setTranslation(x, y, z);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        光照状态经理.启用手感();
    }

    public void doRenderShadowAndFire(实体 实体In, double x, double y, double z, float yaw, float partialTicks)
    {
        if (this.renderManager.options != null)
        {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !实体In.isInvisible() && this.renderManager.isRenderShadow())
            {
                double d0 = this.renderManager.getDistanceToCamera(实体In.X坐标, 实体In.Y坐标, 实体In.Z坐标);
                float f = (float)((1.0D - d0 / 256.0D) * (double)this.shadowOpaque);

                if (f > 0.0F)
                {
                    this.renderShadow(实体In, x, y, z, f, partialTicks);
                }
            }

            if (实体In.canRenderOnFire() && (!(实体In instanceof 实体Player) || !((实体Player) 实体In).isSpectator()))
            {
                this.renderEntityOnFire(实体In, x, y, z, partialTicks);
            }
        }
    }

    public FontRenderer getFontRendererFromRenderManager()
    {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance)
    {
        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d0 <= (double)(maxDistance * maxDistance))
        {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            光照状态经理.推黑客帝国();
            光照状态经理.理解((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            光照状态经理.障眼物(-f1, -f1, f1);
            光照状态经理.disableLighting();
            光照状态经理.depthMask(false);
            光照状态经理.禁用纵深();
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (str.equals("deadmau5"))
            {
                i = -10;
            }

            int j = fontrenderer.getStringWidth(str) / 2;
            光照状态经理.禁用手感();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            光照状态经理.启用手感();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            光照状态经理.启用纵深();
            光照状态经理.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            光照状态经理.enableLighting();
            光照状态经理.禁用混合品();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.流行音乐黑客帝国();
        }
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    public boolean isMultipass()
    {
        return false;
    }

    public void renderMultipass(T p_renderMultipass_1_, double p_renderMultipass_2_, double p_renderMultipass_4_, double p_renderMultipass_6_, float p_renderMultipass_8_, float p_renderMultipass_9_)
    {
    }

    public Class getEntityClass()
    {
        return this.entityClass;
    }

    public void setEntityClass(Class p_setEntityClass_1_)
    {
        this.entityClass = p_setEntityClass_1_;
    }

    public 图像位置 getLocationTextureCustom()
    {
        return this.locationTextureCustom;
    }

    public void setLocationTextureCustom(图像位置 p_setLocationTextureCustom_1_)
    {
        this.locationTextureCustom = p_setLocationTextureCustom_1_;
    }

    public static void setModelBipedMain(RenderBiped p_setModelBipedMain_0_, ModelBiped p_setModelBipedMain_1_)
    {
        p_setModelBipedMain_0_.modelBipedMain = p_setModelBipedMain_1_;
    }
}
