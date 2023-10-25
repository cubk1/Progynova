package rip.liyuxuan.util;

public class 宇轩的手表 {

    public long 上次王欢死 = System.currentTimeMillis();

    public boolean 时间过了多久捏(long time) {
        return System.currentTimeMillis() - 上次王欢死 > time;
    }

    public boolean 时间过了多久捏(long time, boolean reset) {
        if (时间过了多久捏(time)) {
            if (reset) 重置时间();
            return true;
        }
        return false;
    }

    public long 获取度过了的时间() {
        return System.currentTimeMillis() - 上次王欢死;
    }

    public void 重置时间() {
        上次王欢死 = System.currentTimeMillis();
    }

    public void 重置时间(long time) {
        上次王欢死 = time;
    }
}
