package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.实体Mooshroom;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class LayerMooshroomMushroom implements LayerRenderer<实体Mooshroom>
{
    private final RenderMooshroom mooshroomRenderer;
    private ModelRenderer modelRendererMushroom;
    private static final 图像位置 LOCATION_MUSHROOM_RED = new 图像位置("textures/entity/cow/mushroom_red.png");
    private static boolean hasTextureMushroom = false;

    public static void update()
    {
        hasTextureMushroom = Config.hasResource(LOCATION_MUSHROOM_RED);
    }

    public LayerMooshroomMushroom(RenderMooshroom mooshroomRendererIn)
    {
        this.mooshroomRenderer = mooshroomRendererIn;
        this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.mainModel);
        this.modelRendererMushroom.setTextureSize(16, 16);
        this.modelRendererMushroom.rotationPointX = -6.0F;
        this.modelRendererMushroom.rotationPointZ = -8.0F;
        this.modelRendererMushroom.rotateAngleY = MathHelper.PI / 4.0F;
        int[][] aint = new int[][] {null, null, {16, 16, 0, 0}, {16, 16, 0, 0}, null, null};
        this.modelRendererMushroom.addBox(aint, 0.0F, 0.0F, 10.0F, 20.0F, 16.0F, 0.0F, 0.0F);
        int[][] aint1 = new int[][] {null, null, null, null, {16, 16, 0, 0}, {16, 16, 0, 0}};
        this.modelRendererMushroom.addBox(aint1, 10.0F, 0.0F, 0.0F, 0.0F, 16.0F, 20.0F, 0.0F);
    }

    public void doRenderLayer(实体Mooshroom entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (!entitylivingbaseIn.isChild() && !entitylivingbaseIn.isInvisible())
        {
            BlockRendererDispatcher blockrendererdispatcher = 我的手艺.得到我的手艺().getBlockRendererDispatcher();

            if (hasTextureMushroom)
            {
                this.mooshroomRenderer.bindTexture(LOCATION_MUSHROOM_RED);
            }
            else
            {
                this.mooshroomRenderer.bindTexture(TextureMap.locationBlocksTexture);
            }

            光照状态经理.enableCull();
            光照状态经理.cullFace(1028);
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(1.0F, -1.0F, 1.0F);
            光照状态经理.理解(0.2F, 0.35F, 0.5F);
            光照状态经理.辐射(42.0F, 0.0F, 1.0F, 0.0F);
            光照状态经理.推黑客帝国();
            光照状态经理.理解(-0.5F, -0.5F, 0.5F);

            if (hasTextureMushroom)
            {
                this.modelRendererMushroom.render(0.0625F);
            }
            else
            {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();
            光照状态经理.理解(0.1F, 0.0F, -0.6F);
            光照状态经理.辐射(42.0F, 0.0F, 1.0F, 0.0F);
            光照状态经理.理解(-0.5F, -0.5F, 0.5F);

            if (hasTextureMushroom)
            {
                this.modelRendererMushroom.render(0.0625F);
            }
            else
            {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();
            ((ModelQuadruped)this.mooshroomRenderer.getMainModel()).head.postRender(0.0625F);
            光照状态经理.障眼物(1.0F, -1.0F, 1.0F);
            光照状态经理.理解(0.0F, 0.7F, -0.2F);
            光照状态经理.辐射(12.0F, 0.0F, 1.0F, 0.0F);
            光照状态经理.理解(-0.5F, -0.5F, 0.5F);

            if (hasTextureMushroom)
            {
                this.modelRendererMushroom.render(0.0625F);
            }
            else
            {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.cullFace(1029);
            光照状态经理.disableCull();
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
