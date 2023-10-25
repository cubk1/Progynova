package rip.liyuxuan.module;

import rip.liyuxuan.event.implement.宇轩の按键事件;
import rip.liyuxuan.event.李宇轩1337;
import rip.liyuxuan.module.implement.combat.释放剑气;
import rip.liyuxuan.module.implement.movement.宇轩腿脚不好;
import rip.liyuxuan.module.implement.movement.宇轩自动跑动;
import rip.liyuxuan.module.implement.visual.宇轩的头部显示;
import rip.liyuxuan.宇轩科技;

import java.util.ArrayList;
import java.util.List;

public class 宇轩の模块管理员 {

    private final List<宇轩の模块> 宇轩の模块们 = new ArrayList<>();

    public 宇轩の模块管理员() {
        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().关注宇轩1337哟(this);

        宇轩の模块们.add(new 宇轩自动跑动());
        宇轩の模块们.add(new 宇轩的头部显示());
        宇轩の模块们.add(new 宇轩腿脚不好());
        宇轩の模块们.add(new 释放剑气());
    }

    public List<宇轩の模块> 获取宇轩的模块们 () {
        return 宇轩の模块们;
    }

    @李宇轩1337
    public void 按键事件(宇轩の按键事件 事件) {
        for (宇轩の模块 m : 宇轩の模块们) {
            if (m.获取按键() == 事件.获取按键()) m.开关();
        }
    }
}
