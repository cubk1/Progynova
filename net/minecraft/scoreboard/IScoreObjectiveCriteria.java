package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.枚举聊天格式;

public interface IScoreObjectiveCriteria
{
    Map<String, IScoreObjectiveCriteria> INSTANCES = Maps.<String, IScoreObjectiveCriteria>newHashMap();
    IScoreObjectiveCriteria DUMMY = new ScoreDummyCriteria("dummy");
    IScoreObjectiveCriteria TRIGGER = new ScoreDummyCriteria("trigger");
    IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");
    IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria("playerKillCount");
    IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria("totalKillCount");
    IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
    IScoreObjectiveCriteria[] field_178792_h = new IScoreObjectiveCriteria[] {new GoalColor("teamkill.", 枚举聊天格式.BLACK), new GoalColor("teamkill.", 枚举聊天格式.DARK_BLUE), new GoalColor("teamkill.", 枚举聊天格式.DARK_GREEN), new GoalColor("teamkill.", 枚举聊天格式.DARK_AQUA), new GoalColor("teamkill.", 枚举聊天格式.DARK_RED), new GoalColor("teamkill.", 枚举聊天格式.DARK_PURPLE), new GoalColor("teamkill.", 枚举聊天格式.GOLD), new GoalColor("teamkill.", 枚举聊天格式.GRAY), new GoalColor("teamkill.", 枚举聊天格式.DARK_GRAY), new GoalColor("teamkill.", 枚举聊天格式.BLUE), new GoalColor("teamkill.", 枚举聊天格式.GREEN), new GoalColor("teamkill.", 枚举聊天格式.AQUA), new GoalColor("teamkill.", 枚举聊天格式.RED), new GoalColor("teamkill.", 枚举聊天格式.LIGHT_PURPLE), new GoalColor("teamkill.", 枚举聊天格式.YELLOW), new GoalColor("teamkill.", 枚举聊天格式.白的)};
    IScoreObjectiveCriteria[] field_178793_i = new IScoreObjectiveCriteria[] {new GoalColor("killedByTeam.", 枚举聊天格式.BLACK), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_BLUE), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_GREEN), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_AQUA), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_RED), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_PURPLE), new GoalColor("killedByTeam.", 枚举聊天格式.GOLD), new GoalColor("killedByTeam.", 枚举聊天格式.GRAY), new GoalColor("killedByTeam.", 枚举聊天格式.DARK_GRAY), new GoalColor("killedByTeam.", 枚举聊天格式.BLUE), new GoalColor("killedByTeam.", 枚举聊天格式.GREEN), new GoalColor("killedByTeam.", 枚举聊天格式.AQUA), new GoalColor("killedByTeam.", 枚举聊天格式.RED), new GoalColor("killedByTeam.", 枚举聊天格式.LIGHT_PURPLE), new GoalColor("killedByTeam.", 枚举聊天格式.YELLOW), new GoalColor("killedByTeam.", 枚举聊天格式.白的)};

    String getName();

    int setScore(List<EntityPlayer> p_96635_1_);

    boolean isReadOnly();

    IScoreObjectiveCriteria.EnumRenderType getRenderType();

    public static enum EnumRenderType
    {
        INTEGER("integer"),
        HEARTS("hearts");

        private static final Map<String, IScoreObjectiveCriteria.EnumRenderType> field_178801_c = Maps.<String, IScoreObjectiveCriteria.EnumRenderType>newHashMap();
        private final String field_178798_d;

        private EnumRenderType(String p_i45548_3_)
        {
            this.field_178798_d = p_i45548_3_;
        }

        public String func_178796_a()
        {
            return this.field_178798_d;
        }

        public static IScoreObjectiveCriteria.EnumRenderType func_178795_a(String p_178795_0_)
        {
            IScoreObjectiveCriteria.EnumRenderType iscoreobjectivecriteria$enumrendertype = (IScoreObjectiveCriteria.EnumRenderType)field_178801_c.get(p_178795_0_);
            return iscoreobjectivecriteria$enumrendertype == null ? INTEGER : iscoreobjectivecriteria$enumrendertype;
        }

        static {
            for (IScoreObjectiveCriteria.EnumRenderType iscoreobjectivecriteria$enumrendertype : values())
            {
                field_178801_c.put(iscoreobjectivecriteria$enumrendertype.func_178796_a(), iscoreobjectivecriteria$enumrendertype);
            }
        }
    }
}
