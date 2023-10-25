package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import org.lwjgl.util.vector.Matrix4f;

public class Shader
{
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers = Lists.newArrayList();
    private final List<String> listAuxNames = Lists.newArrayList();
    private final List<Integer> listAuxWidths = Lists.newArrayList();
    private final List<Integer> listAuxHeights = Lists.newArrayList();
    private Matrix4f projectionMatrix;

    public Shader(IResourceManager p_i45089_1_, String p_i45089_2_, Framebuffer p_i45089_3_, Framebuffer p_i45089_4_) throws IOException
    {
        this.manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
        this.framebufferIn = p_i45089_3_;
        this.framebufferOut = p_i45089_4_;
    }

    public void deleteShader()
    {
        this.manager.deleteShader();
    }

    public void addAuxFramebuffer(String p_148041_1_, Object p_148041_2_, int p_148041_3_, int p_148041_4_)
    {
        this.listAuxNames.add(this.listAuxNames.size(), p_148041_1_);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), p_148041_2_);
        this.listAuxWidths.add(this.listAuxWidths.size(), p_148041_3_);
        this.listAuxHeights.add(this.listAuxHeights.size(), p_148041_4_);
    }

    private void preLoadShader()
    {
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.禁用混合品();
        光照状态经理.禁用纵深();
        光照状态经理.禁用希腊字母表的第1个字母();
        光照状态经理.disableFog();
        光照状态经理.disableLighting();
        光照状态经理.disableColorMaterial();
        光照状态经理.启用手感();
        光照状态经理.绑定手感(0);
    }

    public void setProjectionMatrix(Matrix4f p_148045_1_)
    {
        this.projectionMatrix = p_148045_1_;
    }

    public void loadShader(float p_148042_1_)
    {
        this.preLoadShader();
        this.framebufferIn.unbindFramebuffer();
        float f = (float)this.framebufferOut.framebufferTextureWidth;
        float f1 = (float)this.framebufferOut.framebufferTextureHeight;
        光照状态经理.viewport(0, 0, (int)f, (int)f1);
        this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);

        for (int i = 0; i < this.listAuxFramebuffers.size(); ++i)
        {
            this.manager.addSamplerTexture(this.listAuxNames.get(i), this.listAuxFramebuffers.get(i));
            this.manager.getShaderUniformOrDefault("AuxSize" + i).set((float) this.listAuxWidths.get(i), (float) this.listAuxHeights.get(i));
        }

        this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
        this.manager.getShaderUniformOrDefault("InSize").set((float)this.framebufferIn.framebufferTextureWidth, (float)this.framebufferIn.framebufferTextureHeight);
        this.manager.getShaderUniformOrDefault("OutSize").set(f, f1);
        this.manager.getShaderUniformOrDefault("Time").set(p_148042_1_);
        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
        this.manager.getShaderUniformOrDefault("ScreenSize").set((float) 宇轩的世界.displayWidth, (float) 宇轩的世界.displayHeight);
        this.manager.useShader();
        this.framebufferOut.framebufferClear();
        this.framebufferOut.bindFramebuffer(false);
        光照状态经理.depthMask(false);
        光照状态经理.colorMask(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(0.0D, f1, 500.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, f1, 500.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        光照状态经理.depthMask(true);
        光照状态经理.colorMask(true, true, true, true);
        this.manager.endShader();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();

        for (Object object : this.listAuxFramebuffers)
        {
            if (object instanceof Framebuffer)
            {
                ((Framebuffer)object).unbindFramebufferTexture();
            }
        }
    }

    public ShaderManager getShaderManager()
    {
        return this.manager;
    }
}
