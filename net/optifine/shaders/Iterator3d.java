package net.optifine.shaders;

import java.util.Iterator;
import net.minecraft.util.阻止位置;
import net.minecraft.util.Vec3;
import net.optifine.阻止位置M;

public class Iterator3d implements Iterator<阻止位置>
{
    private IteratorAxis iteratorAxis;
    private 阻止位置M blockPos = new 阻止位置M(0, 0, 0);
    private int axis = 0;
    private int kX;
    private int kY;
    private int kZ;
    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 1;
    private static final int AXIS_Z = 2;

    public Iterator3d(阻止位置 posStart, 阻止位置 posEnd, int width, int height)
    {
        boolean flag = posStart.getX() > posEnd.getX();
        boolean flag1 = posStart.getY() > posEnd.getY();
        boolean flag2 = posStart.getZ() > posEnd.getZ();
        posStart = this.reverseCoord(posStart, flag, flag1, flag2);
        posEnd = this.reverseCoord(posEnd, flag, flag1, flag2);
        this.kX = flag ? -1 : 1;
        this.kY = flag1 ? -1 : 1;
        this.kZ = flag2 ? -1 : 1;
        Vec3 vec3 = new Vec3((double)(posEnd.getX() - posStart.getX()), (double)(posEnd.getY() - posStart.getY()), (double)(posEnd.getZ() - posStart.getZ()));
        Vec3 vec31 = vec3.normalize();
        Vec3 vec32 = new Vec3(1.0D, 0.0D, 0.0D);
        double d0 = vec31.dotProduct(vec32);
        double d1 = Math.abs(d0);
        Vec3 vec33 = new Vec3(0.0D, 1.0D, 0.0D);
        double d2 = vec31.dotProduct(vec33);
        double d3 = Math.abs(d2);
        Vec3 vec34 = new Vec3(0.0D, 0.0D, 1.0D);
        double d4 = vec31.dotProduct(vec34);
        double d5 = Math.abs(d4);

        if (d5 >= d3 && d5 >= d1)
        {
            this.axis = 2;
            阻止位置 blockpos3 = new 阻止位置(posStart.getZ(), posStart.getY() - width, posStart.getX() - height);
            阻止位置 blockpos5 = new 阻止位置(posEnd.getZ(), posStart.getY() + width + 1, posStart.getX() + height + 1);
            int k = posEnd.getZ() - posStart.getZ();
            double d9 = (double)(posEnd.getY() - posStart.getY()) / (1.0D * (double)k);
            double d11 = (double)(posEnd.getX() - posStart.getX()) / (1.0D * (double)k);
            this.iteratorAxis = new IteratorAxis(blockpos3, blockpos5, d9, d11);
        }
        else if (d3 >= d1 && d3 >= d5)
        {
            this.axis = 1;
            阻止位置 blockpos2 = new 阻止位置(posStart.getY(), posStart.getX() - width, posStart.getZ() - height);
            阻止位置 blockpos4 = new 阻止位置(posEnd.getY(), posStart.getX() + width + 1, posStart.getZ() + height + 1);
            int j = posEnd.getY() - posStart.getY();
            double d8 = (double)(posEnd.getX() - posStart.getX()) / (1.0D * (double)j);
            double d10 = (double)(posEnd.getZ() - posStart.getZ()) / (1.0D * (double)j);
            this.iteratorAxis = new IteratorAxis(blockpos2, blockpos4, d8, d10);
        }
        else
        {
            this.axis = 0;
            阻止位置 blockpos = new 阻止位置(posStart.getX(), posStart.getY() - width, posStart.getZ() - height);
            阻止位置 blockpos1 = new 阻止位置(posEnd.getX(), posStart.getY() + width + 1, posStart.getZ() + height + 1);
            int i = posEnd.getX() - posStart.getX();
            double d6 = (double)(posEnd.getY() - posStart.getY()) / (1.0D * (double)i);
            double d7 = (double)(posEnd.getZ() - posStart.getZ()) / (1.0D * (double)i);
            this.iteratorAxis = new IteratorAxis(blockpos, blockpos1, d6, d7);
        }
    }

    private 阻止位置 reverseCoord(阻止位置 pos, boolean revX, boolean revY, boolean revZ)
    {
        if (revX)
        {
            pos = new 阻止位置(-pos.getX(), pos.getY(), pos.getZ());
        }

        if (revY)
        {
            pos = new 阻止位置(pos.getX(), -pos.getY(), pos.getZ());
        }

        if (revZ)
        {
            pos = new 阻止位置(pos.getX(), pos.getY(), -pos.getZ());
        }

        return pos;
    }

    public boolean hasNext()
    {
        return this.iteratorAxis.hasNext();
    }

    public 阻止位置 next()
    {
        阻止位置 blockpos = this.iteratorAxis.next();

        switch (this.axis)
        {
            case 0:
                this.blockPos.setXyz(blockpos.getX() * this.kX, blockpos.getY() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;

            case 1:
                this.blockPos.setXyz(blockpos.getY() * this.kX, blockpos.getX() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;

            case 2:
                this.blockPos.setXyz(blockpos.getZ() * this.kX, blockpos.getY() * this.kY, blockpos.getX() * this.kZ);
                return this.blockPos;

            default:
                this.blockPos.setXyz(blockpos.getX() * this.kX, blockpos.getY() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;
        }
    }

    public void remove()
    {
        throw new RuntimeException("Not supported");
    }

    public static void main(String[] args)
    {
        阻止位置 blockpos = new 阻止位置(10, 20, 30);
        阻止位置 blockpos1 = new 阻止位置(30, 40, 20);
        Iterator3d iterator3d = new Iterator3d(blockpos, blockpos1, 1, 1);

        while (iterator3d.hasNext())
        {
            阻止位置 blockpos2 = iterator3d.next();
            System.out.println("" + blockpos2);
        }
    }
}
