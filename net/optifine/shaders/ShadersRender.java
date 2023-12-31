package net.optifine.shaders;

import java.nio.IntBuffer;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.实体;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.图像位置;
import net.optifine.reflect.Reflector;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShadersRender
{
    private static final 图像位置 END_PORTAL_TEXTURE = new 图像位置("textures/entity/end_portal.png");

    public static void setFrustrumPosition(ICamera frustum, double x, double y, double z)
    {
        frustum.setPosition(x, y, z);
    }

    public static void setupTerrain(RenderGlobal renderGlobal, 实体 view实体, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator)
    {
        renderGlobal.setupTerrain(view实体, partialTicks, camera, frameCount, playerSpectator);
    }

    public static void beginTerrainSolid()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.fogEnabled = true;
            Shaders.useProgram(Shaders.ProgramTerrain);
        }
    }

    public static void beginTerrainCutoutMipped()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.useProgram(Shaders.ProgramTerrain);
        }
    }

    public static void beginTerrainCutout()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.useProgram(Shaders.ProgramTerrain);
        }
    }

    public static void endTerrain()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.useProgram(Shaders.ProgramTexturedLit);
        }
    }

    public static void beginTranslucent()
    {
        if (Shaders.isRenderingWorld)
        {
            if (Shaders.usedDepthBuffers >= 2)
            {
                光照状态经理.setActiveTexture(33995);
                Shaders.checkGLError("pre copy depth");
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
                Shaders.checkGLError("copy depth");
                光照状态经理.setActiveTexture(33984);
            }

            Shaders.useProgram(Shaders.ProgramWater);
        }
    }

    public static void endTranslucent()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.useProgram(Shaders.ProgramTexturedLit);
        }
    }

    public static void renderHand0(EntityRenderer er, float par1, int par2)
    {
        if (!Shaders.isShadowPass)
        {
            boolean flag = Shaders.isItemToRenderMainTranslucent();
            boolean flag1 = Shaders.isItemToRenderOffTranslucent();

            if (!flag || !flag1)
            {
                Shaders.readCenterDepth();
                Shaders.beginHand(false);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Shaders.setSkipRenderHands(flag, flag1);
                er.renderHand(par1, par2, true, false, false);
                Shaders.endHand();
                Shaders.setHandsRendered(!flag, !flag1);
                Shaders.setSkipRenderHands(false, false);
            }
        }
    }

    public static void renderHand1(EntityRenderer er, float par1, int par2)
    {
        if (!Shaders.isShadowPass && !Shaders.isBothHandsRendered())
        {
            Shaders.readCenterDepth();
            光照状态经理.启用混合品();
            Shaders.beginHand(true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Shaders.setSkipRenderHands(Shaders.isHandRenderedMain(), Shaders.isHandRenderedOff());
            er.renderHand(par1, par2, true, false, true);
            Shaders.endHand();
            Shaders.setHandsRendered(true, true);
            Shaders.setSkipRenderHands(false, false);
        }
    }

    public static void renderItemFP(ItemRenderer itemRenderer, float par1, boolean renderTranslucent)
    {
        Shaders.setRenderingFirstPersonHand(true);
        光照状态经理.depthMask(true);

        if (renderTranslucent)
        {
            光照状态经理.depthFunc(519);
            GL11.glPushMatrix();
            IntBuffer intbuffer = Shaders.activeDrawBuffers;
            Shaders.setDrawBuffers(Shaders.drawBuffersNone);
            Shaders.renderItemKeepDepthMask = true;
            itemRenderer.renderItemInFirstPerson(par1);
            Shaders.renderItemKeepDepthMask = false;
            Shaders.setDrawBuffers(intbuffer);
            GL11.glPopMatrix();
        }

        光照状态经理.depthFunc(515);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        itemRenderer.renderItemInFirstPerson(par1);
        Shaders.setRenderingFirstPersonHand(false);
    }

    public static void renderFPOverlay(EntityRenderer er, float par1, int par2)
    {
        if (!Shaders.isShadowPass)
        {
            Shaders.beginFPOverlay();
            er.renderHand(par1, par2, false, true, false);
            Shaders.endFPOverlay();
        }
    }

    public static void beginBlockDamage()
    {
        if (Shaders.isRenderingWorld)
        {
            Shaders.useProgram(Shaders.ProgramDamagedBlock);

            if (Shaders.ProgramDamagedBlock.getId() == Shaders.ProgramTerrain.getId())
            {
                Shaders.setDrawBuffers(Shaders.drawBuffersColorAtt0);
                光照状态经理.depthMask(false);
            }
        }
    }

    public static void endBlockDamage()
    {
        if (Shaders.isRenderingWorld)
        {
            光照状态经理.depthMask(true);
            Shaders.useProgram(Shaders.ProgramTexturedLit);
        }
    }

    public static void renderShadowMap(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano)
    {
        if (Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0)
        {
            我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
            宇轩的世界.mcProfiler.endStartSection("shadow pass");
            RenderGlobal renderglobal = 宇轩的世界.renderGlobal;
            Shaders.isShadowPass = true;
            Shaders.shadowPassCounter = Shaders.shadowPassInterval;
            Shaders.preShadowPassThirdPersonView = 宇轩的世界.游戏一窝.thirdPersonView;
            宇轩的世界.游戏一窝.thirdPersonView = 1;
            Shaders.checkGLError("pre shadow");
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            宇轩的世界.mcProfiler.endStartSection("shadow clear");
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.sfb);
            Shaders.checkGLError("shadow bind sfb");
            宇轩的世界.mcProfiler.endStartSection("shadow camera");
            entityRenderer.setupCameraTransform(partialTicks, 2);
            Shaders.setCameraShadow(partialTicks);
            Shaders.checkGLError("shadow camera");
            Shaders.useProgram(Shaders.ProgramShadow);
            GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers");
            GL11.glReadBuffer(0);
            Shaders.checkGLError("shadow readbuffer");
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.sfbDepthTextures.get(0), 0);

            if (Shaders.usedShadowColorBuffers != 0)
            {
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, Shaders.sfbColorTextures.get(0), 0);
            }

            Shaders.checkFramebufferStatus("shadow fb");
            GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glClear(Shaders.usedShadowColorBuffers != 0 ? GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT : GL11.GL_DEPTH_BUFFER_BIT);
            Shaders.checkGLError("shadow clear");
            宇轩的世界.mcProfiler.endStartSection("shadow frustum");
            ClippingHelper clippinghelper = ClippingHelperShadow.getInstance();
            宇轩的世界.mcProfiler.endStartSection("shadow culling");
            Frustum frustum = new Frustum(clippinghelper);
            实体 实体 = 宇轩的世界.getRenderViewEntity();
            double d0 = 实体.lastTickPosX + (实体.X坐标 - 实体.lastTickPosX) * (double)partialTicks;
            double d1 = 实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks;
            double d2 = 实体.lastTickPosZ + (实体.Z坐标 - 实体.lastTickPosZ) * (double)partialTicks;
            frustum.setPosition(d0, d1, d2);
            光照状态经理.shadeModel(7425);
            光照状态经理.启用纵深();
            光照状态经理.depthFunc(515);
            光照状态经理.depthMask(true);
            光照状态经理.colorMask(true, true, true, true);
            光照状态经理.disableCull();
            宇轩的世界.mcProfiler.endStartSection("shadow prepareterrain");
            宇轩的世界.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
            宇轩的世界.mcProfiler.endStartSection("shadow setupterrain");
            int i = 0;
            i = entityRenderer.frameCount;
            entityRenderer.frameCount = i + 1;
            renderglobal.setupTerrain(实体, (double)partialTicks, frustum, i, 宇轩的世界.宇轩游玩者.isSpectator());
            宇轩的世界.mcProfiler.endStartSection("shadow updatechunks");
            宇轩的世界.mcProfiler.endStartSection("shadow terrain");
            光照状态经理.matrixMode(5888);
            光照状态经理.推黑客帝国();
            光照状态经理.禁用希腊字母表的第1个字母();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)partialTicks, 2, 实体);
            Shaders.checkGLError("shadow terrain solid");
            光照状态经理.启用希腊字母表的第1个字母();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, 2, 实体);
            Shaders.checkGLError("shadow terrain cutoutmipped");
            宇轩的世界.得到手感经理().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, 2, 实体);
            Shaders.checkGLError("shadow terrain cutout");
            宇轩的世界.得到手感经理().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            光照状态经理.shadeModel(7424);
            光照状态经理.alphaFunc(516, 0.1F);
            光照状态经理.matrixMode(5888);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();
            宇轩的世界.mcProfiler.endStartSection("shadow entities");

            if (Reflector.ForgeHooksClient_setRenderPass.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(0)});
            }

            renderglobal.renderEntities(实体, frustum, partialTicks);
            Shaders.checkGLError("shadow entities");
            光照状态经理.matrixMode(5888);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.depthMask(true);
            光照状态经理.禁用混合品();
            光照状态经理.enableCull();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            光照状态经理.alphaFunc(516, 0.1F);

            if (Shaders.usedShadowDepthBuffers >= 2)
            {
                光照状态经理.setActiveTexture(33989);
                Shaders.checkGLError("pre copy shadow depth");
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
                Shaders.checkGLError("copy shadow depth");
                光照状态经理.setActiveTexture(33984);
            }

            光照状态经理.禁用混合品();
            光照状态经理.depthMask(true);
            宇轩的世界.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
            光照状态经理.shadeModel(7425);
            Shaders.checkGLError("shadow pre-translucent");
            GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers pre-translucent");
            Shaders.checkFramebufferStatus("shadow pre-translucent");

            if (Shaders.isRenderShadowTranslucent())
            {
                宇轩的世界.mcProfiler.endStartSection("shadow translucent");
                renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, 2, 实体);
                Shaders.checkGLError("shadow translucent");
            }

            if (Reflector.ForgeHooksClient_setRenderPass.exists())
            {
                RenderHelper.enableStandardItemLighting();
                Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(1)});
                renderglobal.renderEntities(实体, frustum, partialTicks);
                Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(-1)});
                RenderHelper.disableStandardItemLighting();
                Shaders.checkGLError("shadow entities 1");
            }

            光照状态经理.shadeModel(7424);
            光照状态经理.depthMask(true);
            光照状态经理.enableCull();
            光照状态经理.禁用混合品();
            GL11.glFlush();
            Shaders.checkGLError("shadow flush");
            Shaders.isShadowPass = false;
            宇轩的世界.游戏一窝.thirdPersonView = Shaders.preShadowPassThirdPersonView;
            宇轩的世界.mcProfiler.endStartSection("shadow postprocess");

            if (Shaders.hasGlGenMipmap)
            {
                if (Shaders.usedShadowDepthBuffers >= 1)
                {
                    if (Shaders.shadowMipmapEnabled[0])
                    {
                        光照状态经理.setActiveTexture(33988);
                        光照状态经理.绑定手感(Shaders.sfbDepthTextures.get(0));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, Shaders.shadowFilterNearest[0] ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_LINEAR_MIPMAP_LINEAR);
                    }

                    if (Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1])
                    {
                        光照状态经理.setActiveTexture(33989);
                        光照状态经理.绑定手感(Shaders.sfbDepthTextures.get(1));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, Shaders.shadowFilterNearest[1] ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_LINEAR_MIPMAP_LINEAR);
                    }

                    光照状态经理.setActiveTexture(33984);
                }

                if (Shaders.usedShadowColorBuffers >= 1)
                {
                    if (Shaders.shadowColorMipmapEnabled[0])
                    {
                        光照状态经理.setActiveTexture(33997);
                        光照状态经理.绑定手感(Shaders.sfbColorTextures.get(0));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, Shaders.shadowColorFilterNearest[0] ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_LINEAR_MIPMAP_LINEAR);
                    }

                    if (Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1])
                    {
                        光照状态经理.setActiveTexture(33998);
                        光照状态经理.绑定手感(Shaders.sfbColorTextures.get(1));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, Shaders.shadowColorFilterNearest[1] ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_LINEAR_MIPMAP_LINEAR);
                    }

                    光照状态经理.setActiveTexture(33984);
                }
            }

            Shaders.checkGLError("shadow postprocess");
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
            GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.activeDrawBuffers = null;
            宇轩的世界.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
            Shaders.useProgram(Shaders.ProgramTerrain);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            Shaders.checkGLError("shadow end");
        }
    }

    public static void preRenderChunkLayer(EnumWorldBlockLayer blockLayerIn)
    {
        if (Shaders.isRenderBackFace(blockLayerIn))
        {
            光照状态经理.disableCull();
        }

        if (OpenGlHelper.useVbo())
        {
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
        }
    }

    public static void postRenderChunkLayer(EnumWorldBlockLayer blockLayerIn)
    {
        if (OpenGlHelper.useVbo())
        {
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
        }

        if (Shaders.isRenderBackFace(blockLayerIn))
        {
            光照状态经理.enableCull();
        }
    }

    public static void setupArrayPointersVbo()
    {
        int i = 14;
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 56, 0L);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 56, 12L);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 56, 16L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexCoordPointer(2, GL11.GL_SHORT, 56, 24L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glNormalPointer(GL11.GL_BYTE, 56, 28L);
        GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, GL11.GL_FLOAT, false, 56, 32L);
        GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, GL11.GL_SHORT, false, 56, 40L);
        GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, GL11.GL_SHORT, false, 56, 48L);
    }

    public static void beaconBeamBegin()
    {
        Shaders.useProgram(Shaders.ProgramBeaconBeam);
    }

    public static void beaconBeamStartQuad1()
    {
    }

    public static void beaconBeamStartQuad2()
    {
    }

    public static void beaconBeamDraw1()
    {
    }

    public static void beaconBeamDraw2()
    {
        光照状态经理.禁用混合品();
    }

    public static void renderEnchantedGlintBegin()
    {
        Shaders.useProgram(Shaders.ProgramArmorGlint);
    }

    public static void renderEnchantedGlintEnd()
    {
        if (Shaders.isRenderingWorld)
        {
            if (Shaders.isRenderingFirstPersonHand() && Shaders.isRenderBothHands())
            {
                Shaders.useProgram(Shaders.ProgramHand);
            }
            else
            {
                Shaders.useProgram(Shaders.ProgramEntities);
            }
        }
        else
        {
            Shaders.useProgram(Shaders.ProgramNone);
        }
    }

    public static boolean renderEndPortal(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float offset)
    {
        if (!Shaders.isShadowPass && Shaders.activeProgram.getId() == 0)
        {
            return false;
        }
        else
        {
            光照状态经理.disableLighting();
            Config.getTextureManager().绑定手感(END_PORTAL_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
            float f = 0.5F;
            float f1 = f * 0.15F;
            float f2 = f * 0.3F;
            float f3 = f * 0.4F;
            float f4 = 0.0F;
            float f5 = 0.2F;
            float f6 = (float)(System.currentTimeMillis() % 100000L) / 100000.0F;
            int i = 240;
            worldrenderer.pos(x, y + (double)offset, z + 1.0D).color(f1, f2, f3, 1.0F).tex((double)(f4 + f6), (double)(f4 + f6)).lightmap(i, i).endVertex();
            worldrenderer.pos(x + 1.0D, y + (double)offset, z + 1.0D).color(f1, f2, f3, 1.0F).tex((double)(f4 + f6), (double)(f5 + f6)).lightmap(i, i).endVertex();
            worldrenderer.pos(x + 1.0D, y + (double)offset, z).color(f1, f2, f3, 1.0F).tex((double)(f5 + f6), (double)(f5 + f6)).lightmap(i, i).endVertex();
            worldrenderer.pos(x, y + (double)offset, z).color(f1, f2, f3, 1.0F).tex((double)(f5 + f6), (double)(f4 + f6)).lightmap(i, i).endVertex();
            tessellator.draw();
            光照状态经理.enableLighting();
            return true;
        }
    }
}
