package net.optifine.shaders;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.util.阻止位置;
import net.optifine.阻止位置M;

public class IteratorAxis implements Iterator<阻止位置>
{
    private double yDelta;
    private double zDelta;
    private int xStart;
    private int xEnd;
    private double yStart;
    private double yEnd;
    private double zStart;
    private double zEnd;
    private int xNext;
    private double yNext;
    private double zNext;
    private 阻止位置M pos = new 阻止位置M(0, 0, 0);
    private boolean hasNext = false;

    public IteratorAxis(阻止位置 posStart, 阻止位置 posEnd, double yDelta, double zDelta)
    {
        this.yDelta = yDelta;
        this.zDelta = zDelta;
        this.xStart = posStart.getX();
        this.xEnd = posEnd.getX();
        this.yStart = (double)posStart.getY();
        this.yEnd = (double)posEnd.getY() - 0.5D;
        this.zStart = (double)posStart.getZ();
        this.zEnd = (double)posEnd.getZ() - 0.5D;
        this.xNext = this.xStart;
        this.yNext = this.yStart;
        this.zNext = this.zStart;
        this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
    }

    public boolean hasNext()
    {
        return this.hasNext;
    }

    public 阻止位置 next()
    {
        if (!this.hasNext)
        {
            throw new NoSuchElementException();
        }
        else
        {
            this.pos.setXyz((double)this.xNext, this.yNext, this.zNext);
            this.nextPos();
            this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
            return this.pos;
        }
    }

    private void nextPos()
    {
        ++this.zNext;

        if (this.zNext >= this.zEnd)
        {
            this.zNext = this.zStart;
            ++this.yNext;

            if (this.yNext >= this.yEnd)
            {
                this.yNext = this.yStart;
                this.yStart += this.yDelta;
                this.yEnd += this.yDelta;
                this.yNext = this.yStart;
                this.zStart += this.zDelta;
                this.zEnd += this.zDelta;
                this.zNext = this.zStart;
                ++this.xNext;

                if (this.xNext >= this.xEnd)
                {
                    ;
                }
            }
        }
    }

    public void remove()
    {
        throw new RuntimeException("Not implemented");
    }

    public static void main(String[] args) throws Exception
    {
        阻止位置 blockpos = new 阻止位置(-2, 10, 20);
        阻止位置 blockpos1 = new 阻止位置(2, 12, 22);
        double d0 = -0.5D;
        double d1 = 0.5D;
        IteratorAxis iteratoraxis = new IteratorAxis(blockpos, blockpos1, d0, d1);
        System.out.println("Start: " + blockpos + ", end: " + blockpos1 + ", yDelta: " + d0 + ", zDelta: " + d1);

        while (iteratoraxis.hasNext())
        {
            阻止位置 blockpos2 = iteratoraxis.next();
            System.out.println("" + blockpos2);
        }
    }
}
