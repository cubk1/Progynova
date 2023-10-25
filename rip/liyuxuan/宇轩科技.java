package rip.liyuxuan;

import org.lwjgl.opengl.Display;
import rip.liyuxuan.data.client.李宇轩信息;
import rip.liyuxuan.data.client.李宇轩制造者;
import rip.liyuxuan.event.宇轩の事件管理员;
import rip.liyuxuan.module.宇轩の模块管理员;

public class 宇轩科技 {

    private static final 宇轩科技 李宇轩1337 = new 宇轩科技();

    public static 宇轩科技 获取李宇轩1337() {
        return 李宇轩1337;
    }

    private final 李宇轩信息 李宇轩の信息 = new 李宇轩信息("李宇轩客户端", "是可爱的女孩子哟~",
            new 李宇轩制造者("李一多", "shabi.wiki/wiki/CanYingisme"),
            new 李宇轩制造者("王欢", "shabi.wiki/wiki/CanYingisme"));

    public 李宇轩信息 获取李宇轩の信息() {
        return 李宇轩の信息;
    }

    private 宇轩の事件管理员 宇轩的事件管理员;

    public 宇轩の事件管理员 获取宇轩の事件管理员() {
        return 宇轩的事件管理员;
    }

    private 宇轩の模块管理员 宇轩的模块管理员;

    public 宇轩の模块管理员 获取宇轩的模块管理员() {
        return 宇轩的模块管理员;
    }

    public void 最开始の宇轩() {
        Display.setTitle(李宇轩の信息.获取名字() + " " + 李宇轩の信息.获取版本() + " | 你说得对，但是李宇轩是一个想学写热注入的可爱女孩子");

        this.宇轩的事件管理员 = new 宇轩の事件管理员();
        this.宇轩的模块管理员 = new 宇轩の模块管理员();

        Runtime.getRuntime().addShutdownHook(new Thread(this::结束の宇轩));
    }

    public void 结束の宇轩() {

    }
}
