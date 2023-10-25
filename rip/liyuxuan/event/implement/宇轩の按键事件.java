package rip.liyuxuan.event.implement;

import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の按键事件 extends 宇轩の事件 {

    private final int 按键;

    public 宇轩の按键事件(int 按键) {
        this.按键 = 按键;
    }

    public int 获取按键() {
        return 按键;
    }
}
