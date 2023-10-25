package rip.liyuxuan.module.implement.visual;

import net.minecraft.client.gui.比例解析;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.图像位置;
import org.lwjgl.input.Keyboard;
import rip.liyuxuan.event.implement.宇轩の渲染2D事件;
import rip.liyuxuan.event.李宇轩1337;
import rip.liyuxuan.module.option.implement.宇轩の小连击;
import rip.liyuxuan.module.option.implement.宇轩の布尔;
import rip.liyuxuan.module.宇轩の模块;
import rip.liyuxuan.module.宇轩の模块种类;
import rip.liyuxuan.util.render.渲染的功能;
import rip.liyuxuan.宇轩科技;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class 宇轩的头部显示 extends 宇轩の模块 {

    private final 宇轩の布尔 DEOBF = new 宇轩の布尔("DEOBF?","DEOBF?",真);
    private final 宇轩の布尔 瞎几把转 = new 宇轩の布尔("瞎几把转","让宇轩美照瞎几把转", 假,DEOBF::获取设置);
    private final 宇轩の小连击 宇轩美照颜色模式 = new 宇轩の小连击("宇轩美照颜色模式","宇轩美照颜色显示模式","抽风","固定","彩虹");
    private final Color 李宇轩喜欢的颜色 = 宇轩の模块种类.values()[new Random().nextInt(宇轩の模块种类.values().length)].获取宇轩颜色();

    public 宇轩的头部显示 () {
        super("宇轩的头部显示", "显示宇轩", 宇轩の模块种类.视觉);
        设置按键(Keyboard.KEY_H);
        添加宇轩の选项(DEOBF);
    }


    @李宇轩1337
    public void 宇轩渲染事件(宇轩の渲染2D事件 事件) {
        int 洋 = 12;

        int 颜色 = -1;
        switch (宇轩美照颜色模式.获取设置()){
            case "抽风":{
                颜色 = 宇轩の模块种类.values()[new Random().nextInt(宇轩の模块种类.values().length)].获取宇轩颜色().getRGB();
                break;
            }
            case "固定":{
                颜色 = 李宇轩喜欢的颜色.getRGB();
                break;
            }
            case "彩虹":{
                Color 彩虹 = new Color(Color.HSBtoRGB((float)((double)this.我D世界.宇轩游玩者.已存在的刻度 / 50.0 + Math.sin((double)0 / 50.0 * 1.6)) % 1.0f, 1f, 1.0f));
                颜色 = 彩虹.getRGB();
            }
        }

        if(DEOBF.获取设置()){

            渲染的功能.渲染图片(new 图像位置("liyuxuan1337/DEOBF.png"),0,0,32,32,颜色);
            洋 += 32;
        }

        if(瞎几把转.获取设置()){
            int i = 0;
            while(i < 100){
                Random 宇轩的随机数 = new Random();
                比例解析 宇轩的位置 = new 比例解析(我D世界);
                渲染的功能.渲染图片(new 图像位置("liyuxuan1337/DEOBF.png"),宇轩的随机数.nextInt(宇轩的位置.getScaledWidth()),宇轩的随机数.nextInt(宇轩的位置.得到高度()),60,50, 颜色);
                i++;
            }
        }

        我D世界.字体渲染员.绘制纵梁带心理阴影("L" + 枚举聊天格式.白的 + "iYuXuan1337 - 想学写热注入的可爱的女孩子~",2, 洋, new Color(246,168,192).getRGB());
        洋 += 我D世界.字体渲染员.FONT_HEIGHT + 2;
        ArrayList<宇轩の模块> 宇轩の模块们但是排列过 = new ArrayList<>(宇轩科技.获取李宇轩1337().获取宇轩的模块管理员().获取宇轩的模块们());
        宇轩の模块们但是排列过.sort((o1, o2) -> 我D世界.字体渲染员.getStringWidth(o2.获取名字()) - 我D世界.字体渲染员.getStringWidth(o1.获取名字()));

        for(宇轩の模块 模块 : 宇轩の模块们但是排列过){
            if(!模块.是不是开启的())
                continue;
            我D世界.字体渲染员.绘制纵梁带心理阴影(模块.获取名字(),2,洋,模块.获取宇轩的模块种类().获取宇轩颜色().getRGB());
            洋 += 我D世界.字体渲染员.FONT_HEIGHT + 2;
        }
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 假;
    }
}
