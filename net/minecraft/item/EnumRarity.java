package net.minecraft.item;

import net.minecraft.util.枚举聊天格式;

public enum EnumRarity
{
    COMMON(枚举聊天格式.白的, "Common"),
    UNCOMMON(枚举聊天格式.YELLOW, "Uncommon"),
    RARE(枚举聊天格式.AQUA, "Rare"),
    EPIC(枚举聊天格式.LIGHT_PURPLE, "Epic");

    public final 枚举聊天格式 rarityColor;
    public final String rarityName;

    private EnumRarity(枚举聊天格式 color, String name)
    {
        this.rarityColor = color;
        this.rarityName = name;
    }
}
