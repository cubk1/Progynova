package net.minecraft.client.shader;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Framebuffer
{
    public int framebufferTextureWidth;
    public int framebufferTextureHeight;
    public int framebufferWidth;
    public int framebufferHeight;
    public boolean useDepth;
    public int framebufferObject;
    public int framebufferTexture;
    public int depthBuffer;
    public float[] framebufferColor;
    public int framebufferFilter;

    public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_)
    {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        this.framebufferColor = new float[4];
        this.framebufferColor[0] = 1.0F;
        this.framebufferColor[1] = 1.0F;
        this.framebufferColor[2] = 1.0F;
        this.framebufferColor[3] = 0.0F;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }

    public void createBindFramebuffer(int width, int height)
    {
        if (!OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
        }
        else
        {
            光照状态经理.启用纵深();

            if (this.framebufferObject >= 0)
            {
                this.deleteFramebuffer();
            }

            this.createFramebuffer(width, height);
            this.checkFramebufferComplete();
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void deleteFramebuffer()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();

            if (this.depthBuffer > -1)
            {
                OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
                this.depthBuffer = -1;
            }

            if (this.framebufferTexture > -1)
            {
                TextureUtil.deleteTexture(this.framebufferTexture);
                this.framebufferTexture = -1;
            }

            if (this.framebufferObject > -1)
            {
                OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
                OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }

    public void createFramebuffer(int width, int height)
    {
        this.framebufferWidth = width;
        this.framebufferHeight = height;
        this.framebufferTextureWidth = width;
        this.framebufferTextureHeight = height;

        if (!OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferClear();
        }
        else
        {
            this.framebufferObject = OpenGlHelper.glGenFramebuffers();
            this.framebufferTexture = TextureUtil.glGenTextures();

            if (this.useDepth)
            {
                this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }

            this.setFramebufferFilter(9728);
            光照状态经理.绑定手感(this.framebufferTexture);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);

            if (this.useDepth)
            {
                OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
                OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            }

            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int p_147607_1_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferFilter = p_147607_1_;
            光照状态经理.绑定手感(this.framebufferTexture);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
            光照状态经理.绑定手感(0);
        }
    }

    public void checkFramebufferComplete()
    {
        int i = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);

        if (i != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE)
        {
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            }
            else
            {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
            }
        }
    }

    public void bindFramebufferTexture()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            光照状态经理.绑定手感(this.framebufferTexture);
        }
    }

    public void unbindFramebufferTexture()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            光照状态经理.绑定手感(0);
        }
    }

    public void bindFramebuffer(boolean p_147610_1_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);

            if (p_147610_1_)
            {
                光照状态经理.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_)
    {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }

    public void framebufferRender(int p_147615_1_, int p_147615_2_)
    {
        this.framebufferRenderExt(p_147615_1_, p_147615_2_, true);
    }

    public void framebufferRenderExt(int p_178038_1_, int p_178038_2_, boolean p_178038_3_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            光照状态经理.colorMask(true, true, true, false);
            光照状态经理.禁用纵深();
            光照状态经理.depthMask(false);
            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            光照状态经理.ortho(0.0D, p_178038_1_, p_178038_2_, 0.0D, 1000.0D, 3000.0D);
            光照状态经理.matrixMode(5888);
            光照状态经理.loadIdentity();
            光照状态经理.理解(0.0F, 0.0F, -2000.0F);
            光照状态经理.viewport(0, 0, p_178038_1_, p_178038_2_);
            光照状态经理.启用手感();
            光照状态经理.disableLighting();
            光照状态经理.禁用希腊字母表的第1个字母();

            if (p_178038_3_)
            {
                光照状态经理.禁用混合品();
                光照状态经理.enableColorMaterial();
            }

            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindFramebufferTexture();
            float f = (float)p_178038_1_;
            float f1 = (float)p_178038_2_;
            float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
            float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            光照状态经理.depthMask(true);
            光照状态经理.colorMask(true, true, true, true);
        }
    }

    public void framebufferClear()
    {
        this.bindFramebuffer(true);
        光照状态经理.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int i = 16384;

        if (this.useDepth)
        {
            光照状态经理.clearDepth(1.0D);
            i |= 256;
        }

        光照状态经理.clear(i);
        this.unbindFramebuffer();
    }
}
