package net.minecraft.client.particle;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.实体;
import net.minecraft.src.Config;
import net.minecraft.world.World;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;

public class 实体PickupFX extends 实体FX
{
    private 实体 field_174840_a;
    private 实体 field_174843_ax;
    private int age;
    private int maxAge;
    private float field_174841_aA;
    private RenderManager field_174842_aB = 我的手艺.得到我的手艺().getRenderManager();

    public 实体PickupFX(World worldIn, 实体 p_i1233_2_, 实体 p_i1233_3_, float p_i1233_4_)
    {
        super(worldIn, p_i1233_2_.X坐标, p_i1233_2_.Y坐标, p_i1233_2_.Z坐标, p_i1233_2_.通便X, p_i1233_2_.motionY, p_i1233_2_.通便Z);
        this.field_174840_a = p_i1233_2_;
        this.field_174843_ax = p_i1233_3_;
        this.maxAge = 3;
        this.field_174841_aA = p_i1233_4_;
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        Program program = null;

        if (Config.isShaders())
        {
            program = Shaders.activeProgram;
            Shaders.nextEntity(this.field_174840_a);
        }

        float f = ((float)this.age + partialTicks) / (float)this.maxAge;
        f = f * f;
        double d0 = this.field_174840_a.X坐标;
        double d1 = this.field_174840_a.Y坐标;
        double d2 = this.field_174840_a.Z坐标;
        double d3 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.X坐标 - this.field_174843_ax.lastTickPosX) * (double)partialTicks;
        double d4 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.Y坐标 - this.field_174843_ax.lastTickPosY) * (double)partialTicks + (double)this.field_174841_aA;
        double d5 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.Z坐标 - this.field_174843_ax.lastTickPosZ) * (double)partialTicks;
        double d6 = d0 + (d3 - d0) * (double)f;
        double d7 = d1 + (d4 - d1) * (double)f;
        double d8 = d2 + (d5 - d2) * (double)f;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        d6 = d6 - interpPosX;
        d7 = d7 - interpPosY;
        d8 = d8 - interpPosZ;
        this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (double)((float)d6), (double)((float)d7), (double)((float)d8), this.field_174840_a.旋转侧滑, partialTicks);

        if (Config.isShaders())
        {
            Shaders.setEntityId((实体)null);
            Shaders.useProgram(program);
        }
    }

    public void onUpdate()
    {
        ++this.age;

        if (this.age == this.maxAge)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }
}
