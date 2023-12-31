package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.实体PlayerSP;
import net.minecraft.client.我的手艺;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.minecraft.world.storage.MapData;
import net.optifine.DynamicLights;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class ItemRenderer
{
    private static final 图像位置 RES_MAP_BACKGROUND = new 图像位置("textures/map/map_background.png");
    private static final 图像位置 RES_UNDERWATER_OVERLAY = new 图像位置("textures/misc/underwater.png");
    private final 我的手艺 mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private int equippedItemSlot = -1;

    public ItemRenderer(我的手艺 mcIn)
    {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(实体LivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform)
    {
        if (heldStack != null)
        {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            光照状态经理.推黑客帝国();

            if (this.itemRenderer.shouldRenderItemIn3D(heldStack))
            {
                光照状态经理.障眼物(2.0F, 2.0F, 2.0F);

                if (this.isBlockTranslucent(block) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask))
                {
                    光照状态经理.depthMask(false);
                }
            }

            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);

            if (this.isBlockTranslucent(block))
            {
                光照状态经理.depthMask(true);
            }

            光照状态经理.流行音乐黑客帝国();
        }
    }

    private boolean isBlockTranslucent(Block blockIn)
    {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void rotateArroundXAndY(float angle, float angleY)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.辐射(angle, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        光照状态经理.流行音乐黑客帝国();
    }

    private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer)
    {
        int i = this.mc.宇轩の世界.getCombinedLight(new 阻止位置(clientPlayer.X坐标, clientPlayer.Y坐标 + (double)clientPlayer.getEyeHeight(), clientPlayer.Z坐标), 0);

        if (Config.isDynamicLights())
        {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }

        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateWithPlayerRotations(实体PlayerSP entityplayerspIn, float partialTicks)
    {
        float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        光照状态经理.辐射((entityplayerspIn.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射((entityplayerspIn.旋转侧滑 - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private float getMapAngleFromPitch(float pitch)
    {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void renderRightArm(RenderPlayer renderPlayerIn)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.辐射(54.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(64.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(-62.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.理解(0.25F, -0.85F, 0.75F);
        renderPlayerIn.renderRightArm(this.mc.宇轩游玩者);
        光照状态经理.流行音乐黑客帝国();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.辐射(92.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(45.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(41.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.理解(-0.3F, -1.1F, 0.45F);
        renderPlayerIn.renderLeftArm(this.mc.宇轩游玩者);
        光照状态经理.流行音乐黑客帝国();
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer)
    {
        this.mc.得到手感经理().绑定手感(clientPlayer.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.宇轩游玩者);
        RenderPlayer renderplayer = (RenderPlayer)render;

        if (!clientPlayer.isInvisible())
        {
            光照状态经理.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            光照状态经理.enableCull();
        }
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
        光照状态经理.理解(f, f1, f2);
        float f3 = this.getMapAngleFromPitch(pitch);
        光照状态经理.理解(0.0F, 0.04F, -0.72F);
        光照状态经理.理解(0.0F, equipmentProgress * -1.2F, 0.0F);
        光照状态经理.理解(0.0F, f3 * -0.5F, 0.0F);
        光照状态经理.辐射(90.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(0.0F, 1.0F, 0.0F, 0.0F);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        光照状态经理.辐射(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.障眼物(0.38F, 0.38F, 0.38F);
        光照状态经理.辐射(90.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(180.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(0.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.理解(-1.0F, -1.0F, 0.0F);
        光照状态经理.障眼物(0.015625F, 0.015625F, 0.015625F);
        this.mc.得到手感经理().绑定手感(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.宇轩の世界);

        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress)
    {
        float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F);
        float f2 = -0.4F * MathHelper.sin(swingProgress * (float)Math.PI);
        光照状态经理.理解(f, f1, f2);
        光照状态经理.理解(0.64000005F, -0.6F, -0.71999997F);
        光照状态经理.理解(0.0F, equipProgress * -0.6F, 0.0F);
        光照状态经理.辐射(45.0F, 0.0F, 1.0F, 0.0F);
        float f3 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        光照状态经理.辐射(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(clientPlayer.getLocationSkin());
        光照状态经理.理解(-1.0F, 3.6F, 3.5F);
        光照状态经理.辐射(120.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(200.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(-135.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.障眼物(1.0F, 1.0F, 1.0F);
        光照状态经理.理解(5.6F, 0.0F, 0.0F);
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.宇轩游玩者);
        光照状态经理.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.宇轩游玩者);
        光照状态经理.enableCull();
    }

    private void doItemUsedTransformations(float swingProgress)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
        光照状态经理.理解(f, f1, f2);
    }

    private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks)
    {
        float f = (float)clientPlayer.getItemInUseCount() - partialTicks + 1.0F;
        float f1 = f / (float)this.itemToRender.getMaxItemUseDuration();
        float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);

        if (f1 >= 0.8F)
        {
            f2 = 0.0F;
        }

        光照状态经理.理解(0.0F, f2, 0.0F);
        float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
        光照状态经理.理解(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
        光照状态经理.辐射(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress)
    {
        光照状态经理.理解(0.56F, -0.52F, -0.71999997F);
        光照状态经理.理解(0.0F, equipProgress * -0.6F, 0.0F);
        光照状态经理.辐射(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        光照状态经理.辐射(f * -20.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.障眼物(0.4F, 0.4F, 0.4F);
    }

    private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer)
    {
        光照状态经理.辐射(-18.0F, 0.0F, 0.0F, 1.0F);
        光照状态经理.辐射(-12.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-8.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.理解(-0.9F, 0.2F, 0.0F);
        float f = (float)this.itemToRender.getMaxItemUseDuration() - ((float)clientPlayer.getItemInUseCount() - partialTicks + 1.0F);
        float f1 = f / 20.0F;
        f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        if (f1 > 0.1F)
        {
            float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
            float f3 = f1 - 0.1F;
            float f4 = f2 * f3;
            光照状态经理.理解(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
        }

        光照状态经理.理解(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
        光照状态经理.障眼物(1.0F, 1.0F, 1.0F + f1 * 0.2F);
    }

    private void doBlockTransformations()
    {
        光照状态经理.理解(-0.5F, 0.2F, 0.0F);
        光照状态经理.辐射(30.0F, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-80.0F, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(60.0F, 0.0F, 1.0F, 0.0F);
    }

    public void renderItemInFirstPerson(float partialTicks)
    {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand())
        {
            float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            AbstractClientPlayer abstractclientplayer = this.mc.宇轩游玩者;
            float f1 = abstractclientplayer.getSwingProgress(partialTicks);
            float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.旋转侧滑 - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.rotateArroundXAndY(f2, f3);
            this.setLightMapFromPlayer(abstractclientplayer);
            this.rotateWithPlayerRotations((实体PlayerSP)abstractclientplayer, partialTicks);
            光照状态经理.enableRescaleNormal();
            光照状态经理.推黑客帝国();

            if (this.itemToRender != null)
            {
                if (this.itemToRender.getItem() instanceof ItemMap)
                {
                    this.renderItemMap(abstractclientplayer, f2, f, f1);
                }
                else if (abstractclientplayer.getItemInUseCount() > 0)
                {
                    EnumAction enumaction = this.itemToRender.getItemUseAction();

                    switch (enumaction)
                    {
                        case NONE:
                            this.transformFirstPersonItem(f, 0.0F);
                            break;

                        case EAT:
                        case DRINK:
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, 0.0F);
                            break;

                        case BLOCK:
                            this.transformFirstPersonItem(f, 0.0F);
                            this.doBlockTransformations();
                            break;

                        case BOW:
                            this.transformFirstPersonItem(f, 0.0F);
                            this.doBowTransformations(partialTicks, abstractclientplayer);
                    }
                }
                else
                {
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }

                this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            }
            else if (!abstractclientplayer.isInvisible())
            {
                this.renderPlayerArm(abstractclientplayer, f, f1);
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }
    }

    public void renderOverlays(float partialTicks)
    {
        光照状态经理.禁用希腊字母表的第1个字母();

        if (this.mc.宇轩游玩者.isEntityInsideOpaqueBlock())
        {
            IBlockState iblockstate = this.mc.宇轩の世界.getBlockState(new 阻止位置(this.mc.宇轩游玩者));
            阻止位置 blockpos = new 阻止位置(this.mc.宇轩游玩者);
            实体Player entityplayer = this.mc.宇轩游玩者;

            for (int i = 0; i < 8; ++i)
            {
                double d0 = entityplayer.X坐标 + (double)(((float)((i >> 0) % 2) - 0.5F) * entityplayer.width * 0.8F);
                double d1 = entityplayer.Y坐标 + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = entityplayer.Z坐标 + (double)(((float)((i >> 2) % 2) - 0.5F) * entityplayer.width * 0.8F);
                阻止位置 blockpos1 = new 阻止位置(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.宇轩の世界.getBlockState(blockpos1);

                if (iblockstate1.getBlock().isVisuallyOpaque())
                {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            if (iblockstate.getBlock().getRenderType() != -1)
            {
                Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);

                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, new Object[] {this.mc.宇轩游玩者, Float.valueOf(partialTicks), object, iblockstate, blockpos}))
                {
                    this.renderBlockInHand(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }

        if (!this.mc.宇轩游玩者.isSpectator())
        {
            if (this.mc.宇轩游玩者.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, new Object[] {this.mc.宇轩游玩者, Float.valueOf(partialTicks)}))
            {
                this.renderWaterOverlayTexture(partialTicks);
            }

            if (this.mc.宇轩游玩者.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, new Object[] {this.mc.宇轩游玩者, Float.valueOf(partialTicks)}))
            {
                this.renderFireInFirstPerson(partialTicks);
            }
        }

        光照状态经理.启用希腊字母表的第1个字母();
    }

    private void renderBlockInHand(float partialTicks, TextureAtlasSprite atlas)
    {
        this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = 0.1F;
        光照状态经理.色彩(0.1F, 0.1F, 0.1F, 0.5F);
        光照状态经理.推黑客帝国();
        float f1 = -1.0F;
        float f2 = 1.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = atlas.getMinU();
        float f7 = atlas.getMaxU();
        float f8 = atlas.getMinV();
        float f9 = atlas.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex((double)f7, (double)f9).endVertex();
        worldrenderer.pos(1.0D, -1.0D, -0.5D).tex((double)f6, (double)f9).endVertex();
        worldrenderer.pos(1.0D, 1.0D, -0.5D).tex((double)f6, (double)f8).endVertex();
        worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex((double)f7, (double)f8).endVertex();
        tessellator.draw();
        光照状态经理.流行音乐黑客帝国();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderWaterOverlayTexture(float partialTicks)
    {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay())
        {
            this.mc.得到手感经理().绑定手感(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = this.mc.宇轩游玩者.getBrightness(partialTicks);
            光照状态经理.色彩(f, f, f, 0.5F);
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            光照状态经理.推黑客帝国();
            float f1 = 4.0F;
            float f2 = -1.0F;
            float f3 = 1.0F;
            float f4 = -1.0F;
            float f5 = 1.0F;
            float f6 = -0.5F;
            float f7 = -this.mc.宇轩游玩者.旋转侧滑 / 64.0F;
            float f8 = this.mc.宇轩游玩者.rotationPitch / 64.0F;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex((double)(4.0F + f7), (double)(4.0F + f8)).endVertex();
            worldrenderer.pos(1.0D, -1.0D, -0.5D).tex((double)(0.0F + f7), (double)(4.0F + f8)).endVertex();
            worldrenderer.pos(1.0D, 1.0D, -0.5D).tex((double)(0.0F + f7), (double)(0.0F + f8)).endVertex();
            worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex((double)(4.0F + f7), (double)(0.0F + f8)).endVertex();
            tessellator.draw();
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.禁用混合品();
        }
    }

    private void renderFireInFirstPerson(float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 0.9F);
        光照状态经理.depthFunc(519);
        光照状态经理.depthMask(false);
        光照状态经理.启用混合品();
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        float f = 1.0F;

        for (int i = 0; i < 2; ++i)
        {
            光照状态经理.推黑客帝国();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = (0.0F - f) / 2.0F;
            float f6 = f5 + f;
            float f7 = 0.0F - f / 2.0F;
            float f8 = f7 + f;
            float f9 = -0.5F;
            光照状态经理.理解((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            光照状态经理.辐射((float)(i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.pos((double)f5, (double)f7, (double)f9).tex((double)f2, (double)f4).endVertex();
            worldrenderer.pos((double)f6, (double)f7, (double)f9).tex((double)f1, (double)f4).endVertex();
            worldrenderer.pos((double)f6, (double)f8, (double)f9).tex((double)f1, (double)f3).endVertex();
            worldrenderer.pos((double)f5, (double)f8, (double)f9).tex((double)f2, (double)f3).endVertex();
            tessellator.draw();
            光照状态经理.流行音乐黑客帝国();
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.禁用混合品();
        光照状态经理.depthMask(true);
        光照状态经理.depthFunc(515);
    }

    public void updateEquippedItem()
    {
        this.prevEquippedProgress = this.equippedProgress;
        实体Player entityplayer = this.mc.宇轩游玩者;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;

        if (this.itemToRender != null && itemstack != null)
        {
            if (!this.itemToRender.getIsItemStackEqual(itemstack))
            {
                if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists())
                {
                    boolean flag1 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, new Object[] {this.itemToRender, itemstack, Boolean.valueOf(this.equippedItemSlot != entityplayer.inventory.currentItem)});

                    if (!flag1)
                    {
                        this.itemToRender = itemstack;
                        this.equippedItemSlot = entityplayer.inventory.currentItem;
                        return;
                    }
                }

                flag = true;
            }
        }
        else if (this.itemToRender == null && itemstack == null)
        {
            flag = false;
        }
        else
        {
            flag = true;
        }

        float f2 = 0.4F;
        float f = flag ? 0.0F : 1.0F;
        float f1 = MathHelper.clamp_float(f - this.equippedProgress, -f2, f2);
        this.equippedProgress += f1;

        if (this.equippedProgress < 0.1F)
        {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;

            if (Config.isShaders())
            {
                Shaders.setItemToRenderMain(itemstack);
            }
        }
    }

    public void resetEquippedProgress()
    {
        this.equippedProgress = 0.0F;
    }

    public void resetEquippedProgress2()
    {
        this.equippedProgress = 0.0F;
    }
}
