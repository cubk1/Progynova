package rip.liyuxuan.event.misc;

public class 可以被射的宇轩事件 extends 宇轩の事件 {

    private boolean 射了;

    public void 射不射(boolean 射) {
        this.射了 = 射;
    }

    public boolean 射了吗() {
        return 射了;
    }
}
