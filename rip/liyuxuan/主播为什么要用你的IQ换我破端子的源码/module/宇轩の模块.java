package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module;

import net.minecraft.client.我的手艺;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.宇轩科技;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.宇轩の选项;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public abstract class 宇轩の模块 {

    public static final 我的手艺 我D世界 = 我的手艺.得到我的手艺();

    private final String 名字, 介绍;

    private final 宇轩の模块种类 宇轩的模块种类;

    private int 按键;

    private boolean 开启;

    private String 后缀;

    private final List<宇轩の选项<?>> 宇轩の选项们 = new ArrayList<>();

    // sth
    public static final boolean 真 = true;
    public static final boolean 假 = false;

    public 宇轩の模块(String 名字, String 介绍, 宇轩の模块种类 宇轩的模块种类) {
        this.名字 = 名字;
        this.介绍 = 介绍;
        this.宇轩的模块种类 = 宇轩的模块种类;

        this.按键 = Keyboard.KEY_NONE;
        this.后缀 = "";
        this.开启 = false;
    }

    public void 添加宇轩の选项(宇轩の选项<?>... 选项们) {
        for (宇轩の选项<?> 选项 : 选项们) if (!宇轩の选项们.contains(选项)) 宇轩の选项们.add(选项);
    }

    public List<宇轩の选项<?>> 获取宇轩の选项们(boolean 检测是否可视) {
        if (!检测是否可视) return 宇轩の选项们;
        else {
            final List<宇轩の选项<?>> checked = new ArrayList<>(宇轩の选项们);
            checked.removeIf(value -> !value.显示宇轩的鸡巴吗());
            return checked;
        }
    }

    public 宇轩の选项<?> 获取宇轩の选项(String 选项名字) {

        宇轩の选项<?> result = null;

        for (宇轩の选项<?> 选项 : 获取宇轩の选项们(false))
            if (选项.获取简单の名字().equalsIgnoreCase(选项名字)) {
                result = 选项;
                break;
            }

        return result;
    }

    public abstract boolean 是否为高雅的李宇轩功能();

    public String 获取名字() {
        return 名字;
    }

    public String 获取介绍() {
        return 介绍;
    }

    public 宇轩の模块种类 获取宇轩的模块种类() {
        return 宇轩的模块种类;
    }

    public int 获取按键() {
        return 按键;
    }

    public void 设置按键(int 按键) {
        this.按键 = 按键;

        // TODO 保存文件
    }

    public String 获取后缀() {
        return 后缀;
    }

    public void 设置后缀(String 后缀) {
        this.后缀 = 后缀;
    }

    public boolean 是不是开启的() {
        return 开启;
    }

    public void 设置是否开启(boolean 开启) {
        if (this.开启 != 开启) {
            this.开启 = 开启;
            if (开启) 开启();
            else 关闭();
        }

        // TODO 保存文件
    }

    public void 开关() {
        设置是否开启(!是不是开启的());
    }

    public void 开启时() {
    }

    public void 关闭时() {
    }

    private void 开启() {
        try {
            开启时();
        } catch (Exception exception) {
            关闭();
            开启 = false;
            if (我D世界.宇轩游玩者 != null || 我D世界.宇轩の世界 != null) {
                exception.printStackTrace();
            }
        }
    }

    private void 关闭() {
        try {
            关闭时();
        } catch (Exception exception) {
            if (我D世界.宇轩游玩者 != null || 我D世界.宇轩の世界 != null) {
                exception.printStackTrace();
            }
        }
    }
}
