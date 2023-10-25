package net.optifine;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class 阻止位置M extends 阻止位置
{
    private int mx;
    private int my;
    private int mz;
    private int level;
    private 阻止位置M[] facings;
    private boolean needsUpdate;

    public 阻止位置M(int x, int y, int z)
    {
        this(x, y, z, 0);
    }

    public 阻止位置M(double xIn, double yIn, double zIn)
    {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    public 阻止位置M(int x, int y, int z, int level)
    {
        super(0, 0, 0);
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
    }

    public int getX()
    {
        return this.mx;
    }

    public int getY()
    {
        return this.my;
    }

    public int getZ()
    {
        return this.mz;
    }

    public void setXyz(int x, int y, int z)
    {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.needsUpdate = true;
    }

    public void setXyz(double xIn, double yIn, double zIn)
    {
        this.setXyz(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    public 阻止位置M set(Vec3i vec)
    {
        this.setXyz(vec.getX(), vec.getY(), vec.getZ());
        return this;
    }

    public 阻止位置M set(int xIn, int yIn, int zIn)
    {
        this.setXyz(xIn, yIn, zIn);
        return this;
    }

    public 阻止位置 offsetMutable(EnumFacing facing)
    {
        return this.offset(facing);
    }

    public 阻止位置 offset(EnumFacing facing)
    {
        if (this.level <= 0)
        {
            return super.offset(facing, 1);
        }
        else
        {
            if (this.facings == null)
            {
                this.facings = new 阻止位置M[EnumFacing.VALUES.length];
            }

            if (this.needsUpdate)
            {
                this.update();
            }

            int i = facing.getIndex();
            阻止位置M blockposm = this.facings[i];

            if (blockposm == null)
            {
                int j = this.mx + facing.getFrontOffsetX();
                int k = this.my + facing.getFrontOffsetY();
                int l = this.mz + facing.getFrontOffsetZ();
                blockposm = new 阻止位置M(j, k, l, this.level - 1);
                this.facings[i] = blockposm;
            }

            return blockposm;
        }
    }

    public 阻止位置 offset(EnumFacing facing, int n)
    {
        return n == 1 ? this.offset(facing) : super.offset(facing, n);
    }

    private void update()
    {
        for (int i = 0; i < 6; ++i)
        {
            阻止位置M blockposm = this.facings[i];

            if (blockposm != null)
            {
                EnumFacing enumfacing = EnumFacing.VALUES[i];
                int j = this.mx + enumfacing.getFrontOffsetX();
                int k = this.my + enumfacing.getFrontOffsetY();
                int l = this.mz + enumfacing.getFrontOffsetZ();
                blockposm.setXyz(j, k, l);
            }
        }

        this.needsUpdate = false;
    }

    public 阻止位置 toImmutable()
    {
        return new 阻止位置(this.mx, this.my, this.mz);
    }

    public static Iterable getAllInBoxMutable(阻止位置 from, 阻止位置 to)
    {
        final 阻止位置 blockpos = new 阻止位置(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final 阻止位置 blockpos1 = new 阻止位置(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable()
        {
            public Iterator iterator()
            {
                return new AbstractIterator()
                {
                    private 阻止位置M theBlockPosM = null;
                    protected 阻止位置M computeNext0()
                    {
                        if (this.theBlockPosM == null)
                        {
                            this.theBlockPosM = new 阻止位置M(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 3);
                            return this.theBlockPosM;
                        }
                        else if (this.theBlockPosM.equals(blockpos1))
                        {
                            return (阻止位置M)this.endOfData();
                        }
                        else
                        {
                            int i = this.theBlockPosM.getX();
                            int j = this.theBlockPosM.getY();
                            int k = this.theBlockPosM.getZ();

                            if (i < blockpos1.getX())
                            {
                                ++i;
                            }
                            else if (j < blockpos1.getY())
                            {
                                i = blockpos.getX();
                                ++j;
                            }
                            else if (k < blockpos1.getZ())
                            {
                                i = blockpos.getX();
                                j = blockpos.getY();
                                ++k;
                            }

                            this.theBlockPosM.setXyz(i, j, k);
                            return this.theBlockPosM;
                        }
                    }
                    protected Object computeNext()
                    {
                        return this.computeNext0();
                    }
                };
            }
        };
    }
}
