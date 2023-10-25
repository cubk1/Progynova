package rip.liyuxuan.event;

import com.google.common.base.Objects;
import rip.liyuxuan.event.misc.宇轩の事件;
import rip.liyuxuan.event.misc.宇轩の方法数据;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class 宇轩の事件管理员 {

    private final List<宇轩の方法数据> 宇轩の方法数据库 = new CopyOnWriteArrayList<>();

    public void 关注宇轩1337哟(Object 宇轩の实例) {
        for (Method declaredMethod : 宇轩の实例.getClass().getDeclaredMethods()) {
            if  (!declaredMethod.isAnnotationPresent(李宇轩1337.class)) continue;

            if (declaredMethod.getParameterTypes().length != 1) continue;

            final 宇轩の方法数据 库 = new 宇轩の方法数据(宇轩の实例, declaredMethod, declaredMethod.getParameterTypes()[0], declaredMethod.getAnnotation(李宇轩1337.class).value());

            if (!库.获取宇轩の方法().isAccessible()) 库.获取宇轩の方法().setAccessible(true);

            宇轩の方法数据库.add(库);
            宇轩の方法数据库.sort((o1, o2) -> o2.获取射宇轩的优先级() - o1.获取射宇轩的优先级());
        }
    }

    public void 傻逼李宇轩取关了(Object 宇轩の实例) {
        宇轩の方法数据库.removeIf(宇轩の方法数据 -> 宇轩の方法数据.获取宇轩实例().equals(宇轩の实例));
    }

    public void 别过少爷生活_起床啦(宇轩の事件 宇轩の事件) {
        for (宇轩の方法数据 宇轩の方法数据 : 宇轩の方法数据库) {
            if (Objects.equal(宇轩の方法数据.获取宇轩的类(), 宇轩の事件.getClass())) {
                try {
                    宇轩の方法数据.获取宇轩の方法().invoke(宇轩の方法数据.获取宇轩实例(), 宇轩の事件);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
