package rip.liyuxuan.module.option.implement;

import net.minecraft.client.我的手艺;
import rip.liyuxuan.data.client.HSB数据;
import rip.liyuxuan.event.implement.宇轩の循环事件;
import rip.liyuxuan.event.李宇轩1337;
import rip.liyuxuan.module.option.宇轩の选项;
import rip.liyuxuan.宇轩科技;

import java.awt.*;
import java.util.function.Supplier;

public class 宇轩の颜色 extends 宇轩の选项<HSB数据> {

    private final boolean 可变化阿尔法, 可变化彩虹;

    private boolean 开启彩虹;

    public 宇轩の颜色(String 名字, String 介绍, HSB数据 设置, boolean 可变化彩虹, boolean 可变化阿尔法, Supplier<Boolean> 显示) {
        super(名字, 介绍, 设置, 显示);

        this.可变化阿尔法 = 可变化阿尔法;
        this.可变化彩虹 = 可变化彩虹;

        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().关注宇轩1337哟(this);
    }

    public 宇轩の颜色(String 名字, String 介绍, HSB数据 设置, boolean 可变化彩虹, boolean 可变化阿尔法) {
        super(名字, 介绍, 设置, () -> true);

        this.可变化阿尔法 = 可变化阿尔法;
        this.可变化彩虹 = 可变化彩虹;

        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().关注宇轩1337哟(this);
    }

    public 宇轩の颜色(String 名字, String 介绍, Color 设置, boolean 可变化彩虹, boolean 可变化阿尔法, Supplier<Boolean> 显示) {
        super(名字, 介绍, new HSB数据(Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[0],
                Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[1],
                Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[2], 设置.getAlpha() / 255F), 显示);

        this.可变化阿尔法 = 可变化阿尔法;
        this.可变化彩虹 = 可变化彩虹;

        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().关注宇轩1337哟(this);
    }

    public 宇轩の颜色(String 名字, String 介绍, Color 设置, boolean 可变化彩虹, boolean 可变化阿尔法) {
        super(名字, 介绍, new HSB数据(Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[0],
                Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[1],
                Color.RGBtoHSB(设置.getRed(), 设置.getGreen(), 设置.getBlue(), null)[2], 设置.getAlpha() / 255F), () -> true);

        this.可变化阿尔法 = 可变化阿尔法;
        this.可变化彩虹 = 可变化彩虹;

        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().关注宇轩1337哟(this);
    }

    public void 设置启动彩虹(boolean 开启彩虹) {
        if (!可变化彩虹) this.开启彩虹 = false;
        else this.开启彩虹 = 开启彩虹;
    }

    public Integer getRGB(){
        return 获取设置().得到颜色().getRGB();
    }

    public void setRGB(Integer rgb){
        进行一个宇轩设置(new HSB数据(new Color(rgb)));
    }

    private float 获取彩虹色度() {
        return (System.currentTimeMillis() % (long) ((float) 5 * 1000)) / ((float) 5 * 1000);
    }

    @李宇轩1337
    public void 宇轩循环(宇轩の循环事件 事件) {
        if (是可变化彩虹的吗() && 我的手艺.得到我的手艺().宇轩の世界 != null) {
            获取设置().设置色调(获取彩虹色度());
        }
    }

    public boolean 是可变化阿尔法的吗() {
        return 可变化阿尔法;
    }

    public boolean 是可变化彩虹的吗() {
        return 可变化彩虹;
    }

    public boolean 开启彩虹了吗() {
        return 开启彩虹;
    }
}
