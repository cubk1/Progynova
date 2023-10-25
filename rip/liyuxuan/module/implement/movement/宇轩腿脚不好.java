package rip.liyuxuan.module.implement.movement;

import org.lwjgl.input.Keyboard;
import rip.liyuxuan.event.implement.宇轩の玩家更新事件;
import rip.liyuxuan.event.implement.宇轩の移动事件;
import rip.liyuxuan.event.李宇轩1337;
import rip.liyuxuan.module.宇轩の模块;
import rip.liyuxuan.module.宇轩の模块种类;
import rip.liyuxuan.util.玩家的功能;

@SuppressWarnings("unused")
public class 宇轩腿脚不好 extends 宇轩の模块 {

    public 宇轩腿脚不好 () {
        super("我是一个地理瘸腿的人", "母亲知道我想做女孩子之后，做了很多不合理的猜测，我其实很害怕这种猜测，为了不让她再去猜测，我告诉她我不想做女孩子了，但我，那时候心里很想那么做，那我应该怎么办，我一直在告诉自己我不想，但越是这样我越会想去做女孩子，我的压力也越大。" ,宇轩の模块种类.移动);
        设置按键(Keyboard.KEY_N);
    }

    @李宇轩1337
    public void 宇轩玩家更新(宇轩の移动事件 事件) {
        玩家的功能.设置李宇轩跑步速度(事件,0.1d);
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 真;
    }
}
