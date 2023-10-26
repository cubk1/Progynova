package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.data.client;

import java.util.Arrays;
import java.util.List;

public class 李宇轩信息 {

    private final String 名字;
    private final String 版本;
    private final List<李宇轩制造者> 李宇轩制造者们;

    public 李宇轩信息(String 名字, String 版本, 李宇轩制造者... 李宇轩制造者们) {
        this.名字 = 名字;
        this.版本 = 版本;
        this.李宇轩制造者们 = Arrays.asList(李宇轩制造者们);
    }

    public String 获取名字() {
        return 名字;
    }

    public String 获取版本() {
        return 版本;
    }

    public List<李宇轩制造者> 获取李宇轩制造者们() {
        return 李宇轩制造者们;
    }
}
