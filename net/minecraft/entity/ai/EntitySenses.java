package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体Living;

public class EntitySenses
{
    实体Living entityObj;
    List<实体> seenEntities = Lists.<实体>newArrayList();
    List<实体> unseenEntities = Lists.<实体>newArrayList();

    public EntitySenses(实体Living entityObjIn)
    {
        this.entityObj = entityObjIn;
    }

    public void clearSensingCache()
    {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    public boolean canSee(实体 实体In)
    {
        if (this.seenEntities.contains(实体In))
        {
            return true;
        }
        else if (this.unseenEntities.contains(实体In))
        {
            return false;
        }
        else
        {
            this.entityObj.worldObj.theProfiler.startSection("canSee");
            boolean flag = this.entityObj.canEntityBeSeen(实体In);
            this.entityObj.worldObj.theProfiler.endSection();

            if (flag)
            {
                this.seenEntities.add(实体In);
            }
            else
            {
                this.unseenEntities.add(实体In);
            }

            return flag;
        }
    }
}
