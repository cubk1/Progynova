package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.entity.player.实体Player;
import net.minecraft.util.枚举聊天格式;

public class GoalColor implements IScoreObjectiveCriteria
{
    private final String goalName;

    public GoalColor(String p_i45549_1_, 枚举聊天格式 p_i45549_2_)
    {
        this.goalName = p_i45549_1_ + p_i45549_2_.getFriendlyName();
        IScoreObjectiveCriteria.INSTANCES.put(this.goalName, this);
    }

    public String getName()
    {
        return this.goalName;
    }

    public int setScore(List<实体Player> p_96635_1_)
    {
        return 0;
    }

    public boolean isReadOnly()
    {
        return false;
    }

    public IScoreObjectiveCriteria.EnumRenderType getRenderType()
    {
        return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
    }
}
