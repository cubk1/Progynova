package rip.liyuxuan.event.implement;

import rip.liyuxuan.data.位置;
import rip.liyuxuan.event.misc.可以被射的宇轩事件;

public class 宇轩の玩家更新事件 extends 可以被射的宇轩事件 {

    private float 偏航, 大曲线球;
    private double 插, 歪, 贼;
    private boolean 在地上;
    private boolean 旋转了;
    private final boolean 前;

    public 宇轩の玩家更新事件(float 偏航, float 大曲线球, double 插, double 歪, double 贼, boolean 在地上) {
        this.偏航 = 偏航;
        this.大曲线球 = 大曲线球;
        this.插 = 插;
        this.歪 = 歪;
        this.贼 = 贼;
        this.在地上 = 在地上;
        this.旋转了 = false;
        this.前 = true;
    }

    public 宇轩の玩家更新事件() {
        this.前 = false;
    }

    public boolean 是前面吗() {
        return 前;
    }

    public boolean 是后面吗() {
        return !前;
    }

    public double 得到插() {
        return 插;
    }

    public void 设置插(double 插) {
        this.插 = 插;
    }

    public double 得到歪() {
        return 歪;
    }

    public void 设置歪(double 歪) {
        this.歪 = 歪;
    }

    public double 得到贼() {
        return 贼;
    }

    public void 设置贼(double 贼) {
        this.贼 = 贼;
    }

    public void 设置位置(位置 位置) {
        插 = 位置.获取英语字母表的第24个字母();
        歪 = 位置.获取英语字母表的第25个字母();
        贼 = 位置.获取英语字母表的第26个字母();
    }

    public boolean 在地上吗() {
        return 在地上;
    }

    public void 设置地上(boolean 在地上) {
        this.在地上 = 在地上;
    }

    public float getYaw() {
        return 偏航;
    }

    public void 设置偏航(float 偏航) {
        this.偏航 = 偏航;
        this.旋转了 = true;
    }

    public float 得到投掷() {
        return 大曲线球;
    }

    public void 设置投掷(float 大曲线球) {
        this.大曲线球 = 大曲线球;
    }

    public boolean 旋转了吗() {
        return 旋转了;
    }
}
