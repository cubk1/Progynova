package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.client.我的手艺;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.IChatComponent;

public class GuiUtilRenderComponents
{
    public static String func_178909_a(String p_178909_0_, boolean p_178909_1_)
    {
        return !p_178909_1_ && !我的手艺.得到我的手艺().游戏一窝.chatColours ? 枚举聊天格式.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }

    public static List<IChatComponent> splitText(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_)
    {
        int i = 0;
        IChatComponent ichatcomponent = new 交流组分文本("");
        List<IChatComponent> list = Lists.<IChatComponent>newArrayList();
        List<IChatComponent> list1 = Lists.newArrayList(p_178908_0_);

        for (int j = 0; j < ((List)list1).size(); ++j)
        {
            IChatComponent ichatcomponent1 = (IChatComponent)list1.get(j);
            String s = ichatcomponent1.getUnformattedTextForChat();
            boolean flag = false;

            if (s.contains("\n"))
            {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                交流组分文本 chatcomponenttext = new 交流组分文本(s1);
                chatcomponenttext.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                list1.add(j + 1, chatcomponenttext);
                flag = true;
            }

            String s4 = func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s, p_178908_4_);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = p_178908_2_.getStringWidth(s5);
            交流组分文本 chatcomponenttext1 = new 交流组分文本(s5);
            chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());

            if (i + i1 > p_178908_1_)
            {
                String s2 = p_178908_2_.trimStringToWidth(s4, p_178908_1_ - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && s3.length() > 0)
                {
                    int l = s2.lastIndexOf(" ");

                    if (l >= 0 && p_178908_2_.getStringWidth(s4.substring(0, l)) > 0)
                    {
                        s2 = s4.substring(0, l);

                        if (p_178908_3_)
                        {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    }
                    else if (i > 0 && !s4.contains(" "))
                    {
                        s2 = "";
                        s3 = s4;
                    }

                    交流组分文本 chatcomponenttext2 = new 交流组分文本(s3);
                    chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    list1.add(j + 1, chatcomponenttext2);
                }

                i1 = p_178908_2_.getStringWidth(s2);
                chatcomponenttext1 = new 交流组分文本(s2);
                chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= p_178908_1_)
            {
                i += i1;
                ichatcomponent.appendSibling(chatcomponenttext1);
            }
            else
            {
                flag = true;
            }

            if (flag)
            {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = new 交流组分文本("");
            }
        }

        list.add(ichatcomponent);
        return list;
    }
}
