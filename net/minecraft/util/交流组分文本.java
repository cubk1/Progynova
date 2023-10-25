package net.minecraft.util;

public class 交流组分文本 extends ChatComponentStyle
{
    private final String text;

    public 交流组分文本(String msg)
    {
        this.text = msg;
    }

    public String getChatComponentText_TextValue()
    {
        return this.text;
    }

    public String getUnformattedTextForChat()
    {
        return this.text;
    }

    public 交流组分文本 createCopy()
    {
        交流组分文本 chatcomponenttext = new 交流组分文本(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());

        for (IChatComponent ichatcomponent : this.getSiblings())
        {
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponenttext;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof 交流组分文本))
        {
            return false;
        }
        else
        {
            交流组分文本 chatcomponenttext = (交流组分文本)p_equals_1_;
            return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
        }
    }

    public String toString()
    {
        return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
