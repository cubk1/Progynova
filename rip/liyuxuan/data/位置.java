package rip.liyuxuan.data;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class 位置 {

    private double x, y, z;

    public 位置(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public 位置(C03PacketPlayer packet) {
        this.x = packet.getPositionX();
        this.y = packet.getPositionY();
        this.z = packet.getPositionZ();
    }

    public 位置(Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public void 加(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void 加(位置 位置) {
        this.x += 位置.getX();
        this.y += 位置.getY();
        this.z += 位置.getZ();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public BlockPos getAsBlockPos() {
        return new BlockPos(x, y, z);
    }
}
