package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.Model图像位置;
import net.minecraft.entity.item.实体ItemFrame;
import net.minecraft.entity.实体;
import net.minecraft.entity.item.实体Item;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.minecraft.world.storage.MapData;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class RenderItemFrame extends Render<实体ItemFrame>
{
    private static final 图像位置 mapBackgroundTextures = new 图像位置("textures/map/map_background.png");
    private final 我的手艺 mc = 我的手艺.得到我的手艺();
    private final Model图像位置 itemFrameModel = new Model图像位置("item_frame", "normal");
    private final Model图像位置 mapModel = new Model图像位置("item_frame", "map");
    private RenderItem itemRenderer;
    private static double itemRenderDistanceSq = 4096.0D;

    public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    public void doRender(实体ItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        阻止位置 blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.X坐标 + x;
        double d1 = (double)blockpos.getY() - entity.Y坐标 + y;
        double d2 = (double)blockpos.getZ() - entity.Z坐标 + z;
        光照状态经理.理解(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        光照状态经理.辐射(180.0F - entity.旋转侧滑, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.绑定手感(TextureMap.locationBlocksTexture);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.filled_map)
        {
            ibakedmodel = modelmanager.getModel(this.mapModel);
        }
        else
        {
            ibakedmodel = modelmanager.getModel(this.itemFrameModel);
        }

        光照状态经理.推黑客帝国();
        光照状态经理.理解(-0.5F, -0.5F, -0.5F);
        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.流行音乐黑客帝国();
        光照状态经理.理解(0.0F, 0.0F, 0.4375F);
        this.renderItem(entity);
        光照状态经理.流行音乐黑客帝国();
        this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
    }

    protected 图像位置 getEntityTexture(实体ItemFrame entity)
    {
        return null;
    }

    private void renderItem(实体ItemFrame itemFrame)
    {
        ItemStack itemstack = itemFrame.getDisplayedItem();

        if (itemstack != null)
        {
            if (!this.isRenderItem(itemFrame))
            {
                return;
            }

            if (!Config.zoomMode)
            {
                实体 实体 = this.mc.宇轩游玩者;
                double d0 = itemFrame.getDistanceSq(实体.X坐标, 实体.Y坐标, 实体.Z坐标);

                if (d0 > 4096.0D)
                {
                    return;
                }
            }

            实体Item entityitem = new 实体Item(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
            Item item = entityitem.getEntityItem().getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            光照状态经理.推黑客帝国();
            光照状态经理.disableLighting();
            int i = itemFrame.getRotation();

            if (item instanceof ItemMap)
            {
                i = i % 4 * 2;
            }

            光照状态经理.辐射((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, new Object[] {itemFrame, this}))
            {
                if (item instanceof ItemMap)
                {
                    this.renderManager.renderEngine.绑定手感(mapBackgroundTextures);
                    光照状态经理.辐射(180.0F, 0.0F, 0.0F, 1.0F);
                    float f = 0.0078125F;
                    光照状态经理.障眼物(f, f, f);
                    光照状态经理.理解(-64.0F, -64.0F, 0.0F);
                    MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
                    光照状态经理.理解(0.0F, 0.0F, -1.0F);

                    if (mapdata != null)
                    {
                        this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                }
                else
                {
                    TextureAtlasSprite textureatlassprite = null;

                    if (item == Items.compass)
                    {
                        textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.locationSprite);
                        this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);

                        if (textureatlassprite instanceof TextureCompass)
                        {
                            TextureCompass texturecompass = (TextureCompass)textureatlassprite;
                            double d1 = texturecompass.currentAngle;
                            double d2 = texturecompass.angleDelta;
                            texturecompass.currentAngle = 0.0D;
                            texturecompass.angleDelta = 0.0D;
                            texturecompass.updateCompass(itemFrame.worldObj, itemFrame.X坐标, itemFrame.Z坐标, (double)MathHelper.wrapAngleTo180_float((float)(180 + itemFrame.facingDirection.getHorizontalIndex() * 90)), false, true);
                            texturecompass.currentAngle = d1;
                            texturecompass.angleDelta = d2;
                        }
                        else
                        {
                            textureatlassprite = null;
                        }
                    }

                    光照状态经理.障眼物(0.5F, 0.5F, 0.5F);

                    if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof ItemSkull)
                    {
                        光照状态经理.辐射(180.0F, 0.0F, 1.0F, 0.0F);
                    }

                    光照状态经理.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    光照状态经理.popAttrib();

                    if (textureatlassprite != null && textureatlassprite.getFrameCount() > 0)
                    {
                        textureatlassprite.updateAnimation();
                    }
                }
            }
            光照状态经理.enableLighting();
            光照状态经理.流行音乐黑客帝国();
        }
    }

    protected void renderName(实体ItemFrame entity, double x, double y, double z)
    {
        if (我的手艺.isGuiEnabled() && entity.getDisplayedItem() != null && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointed实体 == entity)
        {
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f2 = entity.正在下蹲() ? 32.0F : 64.0F;

            if (d0 < (double)(f2 * f2))
            {
                String s = entity.getDisplayedItem().getDisplayName();

                if (entity.正在下蹲())
                {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    光照状态经理.推黑客帝国();
                    光照状态经理.理解((float)x + 0.0F, (float)y + entity.height + 0.5F, (float)z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    光照状态经理.辐射(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    光照状态经理.辐射(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    光照状态经理.障眼物(-f1, -f1, f1);
                    光照状态经理.disableLighting();
                    光照状态经理.理解(0.0F, 0.25F / f1, 0.0F);
                    光照状态经理.depthMask(false);
                    光照状态经理.启用混合品();
                    光照状态经理.正常工作(770, 771);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    光照状态经理.禁用手感();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    光照状态经理.启用手感();
                    光照状态经理.depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    光照状态经理.enableLighting();
                    光照状态经理.禁用混合品();
                    光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                    光照状态经理.流行音乐黑客帝国();
                }
                else
                {
                    this.renderLivingLabel(entity, s, x, y, z, 64);
                }
            }
        }
    }

    private boolean isRenderItem(实体ItemFrame p_isRenderItem_1_)
    {
        if (Shaders.isShadowPass)
        {
            return false;
        }
        else
        {
            if (!Config.zoomMode)
            {
                实体 实体 = this.mc.getRenderViewEntity();
                double d0 = p_isRenderItem_1_.getDistanceSq(实体.X坐标, 实体.Y坐标, 实体.Z坐标);

                if (d0 > itemRenderDistanceSq)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static void updateItemRenderDistance()
    {
        我的手艺 宇轩的世界 = Config.getMinecraft();
        double d0 = (double)Config.limit(宇轩的世界.游戏一窝.fovSetting, 1.0F, 120.0F);
        double d1 = Math.max(6.0D * (double) 宇轩的世界.displayHeight / d0, 16.0D);
        itemRenderDistanceSq = d1 * d1;
    }
}
