package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.client.我的手艺;
import net.minecraft.util.IntHashMap;

public class GuiPageButtonList extends GuiListExtended
{
    private final List<GuiPageButtonList.GuiEntry> field_178074_u = Lists.<GuiPageButtonList.GuiEntry>newArrayList();
    private final IntHashMap<鬼> field_178073_v = new IntHashMap();
    private final List<鬼TextField> field_178072_w = Lists.<鬼TextField>newArrayList();
    private final GuiPageButtonList.GuiListEntry[][] field_178078_x;
    private int field_178077_y;
    private GuiPageButtonList.GuiResponder field_178076_z;
    private 鬼 field_178075_A;

    public GuiPageButtonList(我的手艺 mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, GuiPageButtonList.GuiResponder p_i45536_7_, GuiPageButtonList.GuiListEntry[]... p_i45536_8_)
    {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }

    private void func_178069_s()
    {
        for (GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry : this.field_178078_x)
        {
            for (int i = 0; i < aguipagebuttonlist$guilistentry.length; i += 2)
            {
                GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry = aguipagebuttonlist$guilistentry[i];
                GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry1 = i < aguipagebuttonlist$guilistentry.length - 1 ? aguipagebuttonlist$guilistentry[i + 1] : null;
                鬼 鬼 = this.func_178058_a(guipagebuttonlist$guilistentry, 0, guipagebuttonlist$guilistentry1 == null);
                鬼 鬼1 = this.func_178058_a(guipagebuttonlist$guilistentry1, 160, guipagebuttonlist$guilistentry == null);
                GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = new GuiPageButtonList.GuiEntry(鬼, 鬼1);
                this.field_178074_u.add(guipagebuttonlist$guientry);

                if (guipagebuttonlist$guilistentry != null && 鬼 != null)
                {
                    this.field_178073_v.addKey(guipagebuttonlist$guilistentry.func_178935_b(), 鬼);

                    if (鬼 instanceof 鬼TextField)
                    {
                        this.field_178072_w.add((鬼TextField) 鬼);
                    }
                }

                if (guipagebuttonlist$guilistentry1 != null && 鬼1 != null)
                {
                    this.field_178073_v.addKey(guipagebuttonlist$guilistentry1.func_178935_b(), 鬼1);

                    if (鬼1 instanceof 鬼TextField)
                    {
                        this.field_178072_w.add((鬼TextField) 鬼1);
                    }
                }
            }
        }
    }

    private void func_178055_t()
    {
        this.field_178074_u.clear();

        for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2)
        {
            GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry = this.field_178078_x[this.field_178077_y][i];
            GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry1 = i < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][i + 1] : null;
            鬼 鬼 = (鬼)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b());
            鬼 鬼1 = guipagebuttonlist$guilistentry1 != null ? (鬼)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()) : null;
            GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = new GuiPageButtonList.GuiEntry(鬼, 鬼1);
            this.field_178074_u.add(guipagebuttonlist$guientry);
        }
    }

    public void func_181156_c(int p_181156_1_)
    {
        if (p_181156_1_ != this.field_178077_y)
        {
            int i = this.field_178077_y;
            this.field_178077_y = p_181156_1_;
            this.func_178055_t();
            this.func_178060_e(i, p_181156_1_);
            this.amountScrolled = 0.0F;
        }
    }

    public int func_178059_e()
    {
        return this.field_178077_y;
    }

    public int func_178057_f()
    {
        return this.field_178078_x.length;
    }

    public 鬼 func_178056_g()
    {
        return this.field_178075_A;
    }

    public void func_178071_h()
    {
        if (this.field_178077_y > 0)
        {
            this.func_181156_c(this.field_178077_y - 1);
        }
    }

    public void func_178064_i()
    {
        if (this.field_178077_y < this.field_178078_x.length - 1)
        {
            this.func_181156_c(this.field_178077_y + 1);
        }
    }

    public 鬼 func_178061_c(int p_178061_1_)
    {
        return (鬼)this.field_178073_v.lookup(p_178061_1_);
    }

    private void func_178060_e(int p_178060_1_, int p_178060_2_)
    {
        for (GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry : this.field_178078_x[p_178060_1_])
        {
            if (guipagebuttonlist$guilistentry != null)
            {
                this.func_178066_a((鬼)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b()), false);
            }
        }

        for (GuiPageButtonList.GuiListEntry guipagebuttonlist$guilistentry1 : this.field_178078_x[p_178060_2_])
        {
            if (guipagebuttonlist$guilistentry1 != null)
            {
                this.func_178066_a((鬼)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()), true);
            }
        }
    }

    private void func_178066_a(鬼 p_178066_1_, boolean p_178066_2_)
    {
        if (p_178066_1_ instanceof 鬼Button)
        {
            ((鬼Button)p_178066_1_).visible = p_178066_2_;
        }
        else if (p_178066_1_ instanceof 鬼TextField)
        {
            ((鬼TextField)p_178066_1_).setVisible(p_178066_2_);
        }
        else if (p_178066_1_ instanceof 鬼Label)
        {
            ((鬼Label)p_178066_1_).visible = p_178066_2_;
        }
    }

    private 鬼 func_178058_a(GuiPageButtonList.GuiListEntry p_178058_1_, int p_178058_2_, boolean p_178058_3_)
    {
        return (鬼)(p_178058_1_ instanceof GuiPageButtonList.GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiSlideEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiButtonEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.EditBoxEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiLabelEntry)p_178058_1_, p_178058_3_) : null))));
    }

    public void func_181155_a(boolean p_181155_1_)
    {
        for (GuiPageButtonList.GuiEntry guipagebuttonlist$guientry : this.field_178074_u)
        {
            if (guipagebuttonlist$guientry.field_178029_b instanceof 鬼Button)
            {
                ((鬼Button)guipagebuttonlist$guientry.field_178029_b).enabled = p_181155_1_;
            }

            if (guipagebuttonlist$guientry.field_178030_c instanceof 鬼Button)
            {
                ((鬼Button)guipagebuttonlist$guientry.field_178030_c).enabled = p_181155_1_;
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent)
    {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
        int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);

        if (i >= 0)
        {
            GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = this.getListEntry(i);

            if (this.field_178075_A != guipagebuttonlist$guientry.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof 鬼TextField)
            {
                ((鬼TextField)this.field_178075_A).setFocused(false);
            }

            this.field_178075_A = guipagebuttonlist$guientry.field_178028_d;
        }

        return flag;
    }

    private 鬼Slider func_178067_a(int p_178067_1_, int p_178067_2_, GuiPageButtonList.GuiSlideEntry p_178067_3_)
    {
        鬼Slider guislider = new 鬼Slider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
        guislider.visible = p_178067_3_.func_178934_d();
        return guislider;
    }

    private 鬼ListButton func_178065_a(int p_178065_1_, int p_178065_2_, GuiPageButtonList.GuiButtonEntry p_178065_3_)
    {
        鬼ListButton guilistbutton = new 鬼ListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
        guilistbutton.visible = p_178065_3_.func_178934_d();
        return guilistbutton;
    }

    private 鬼TextField func_178068_a(int p_178068_1_, int p_178068_2_, GuiPageButtonList.EditBoxEntry p_178068_3_)
    {
        鬼TextField guitextfield = new 鬼TextField(p_178068_3_.func_178935_b(), this.mc.字体渲染员, p_178068_1_, p_178068_2_, 150, 20);
        guitextfield.setText(p_178068_3_.func_178936_c());
        guitextfield.func_175207_a(this.field_178076_z);
        guitextfield.setVisible(p_178068_3_.func_178934_d());
        guitextfield.setValidator(p_178068_3_.func_178950_a());
        return guitextfield;
    }

    private 鬼Label func_178063_a(int p_178063_1_, int p_178063_2_, GuiPageButtonList.GuiLabelEntry p_178063_3_, boolean p_178063_4_)
    {
        鬼Label guilabel;

        if (p_178063_4_)
        {
            guilabel = new 鬼Label(this.mc.字体渲染员, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
        }
        else
        {
            guilabel = new 鬼Label(this.mc.字体渲染员, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
        }

        guilabel.visible = p_178063_3_.func_178934_d();
        guilabel.func_175202_a(p_178063_3_.func_178936_c());
        guilabel.setCentered();
        return guilabel;
    }

    public void func_178062_a(char p_178062_1_, int p_178062_2_)
    {
        if (this.field_178075_A instanceof 鬼TextField)
        {
            鬼TextField guitextfield = (鬼TextField)this.field_178075_A;

            if (!鬼Screen.isKeyComboCtrlV(p_178062_2_))
            {
                if (p_178062_2_ == 15)
                {
                    guitextfield.setFocused(false);
                    int k = this.field_178072_w.indexOf(this.field_178075_A);

                    if (鬼Screen.isShiftKeyDown())
                    {
                        if (k == 0)
                        {
                            k = this.field_178072_w.size() - 1;
                        }
                        else
                        {
                            --k;
                        }
                    }
                    else if (k == this.field_178072_w.size() - 1)
                    {
                        k = 0;
                    }
                    else
                    {
                        ++k;
                    }

                    this.field_178075_A = (鬼)this.field_178072_w.get(k);
                    guitextfield = (鬼TextField)this.field_178075_A;
                    guitextfield.setFocused(true);
                    int l = guitextfield.yPosition + this.slotHeight;
                    int i1 = guitextfield.yPosition;

                    if (l > this.bottom)
                    {
                        this.amountScrolled += (float)(l - this.bottom);
                    }
                    else if (i1 < this.top)
                    {
                        this.amountScrolled = (float)i1;
                    }
                }
                else
                {
                    guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
                }
            }
            else
            {
                String s = 鬼Screen.getClipboardString();
                String[] astring = s.split(";");
                int i = this.field_178072_w.indexOf(this.field_178075_A);
                int j = i;

                for (String s1 : astring)
                {
                    ((鬼TextField)this.field_178072_w.get(j)).setText(s1);

                    if (j == this.field_178072_w.size() - 1)
                    {
                        j = 0;
                    }
                    else
                    {
                        ++j;
                    }

                    if (j == i)
                    {
                        break;
                    }
                }
            }
        }
    }

    public GuiPageButtonList.GuiEntry getListEntry(int index)
    {
        return (GuiPageButtonList.GuiEntry)this.field_178074_u.get(index);
    }

    public int getSize()
    {
        return this.field_178074_u.size();
    }

    public int getListWidth()
    {
        return 400;
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 32;
    }

    public static class EditBoxEntry extends GuiPageButtonList.GuiListEntry
    {
        private final Predicate<String> field_178951_a;

        public EditBoxEntry(int p_i45534_1_, String p_i45534_2_, boolean p_i45534_3_, Predicate<String> p_i45534_4_)
        {
            super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
            this.field_178951_a = (Predicate)Objects.firstNonNull(p_i45534_4_, Predicates.alwaysTrue());
        }

        public Predicate<String> func_178950_a()
        {
            return this.field_178951_a;
        }
    }

    public static class GuiButtonEntry extends GuiPageButtonList.GuiListEntry
    {
        private final boolean field_178941_a;

        public GuiButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_)
        {
            super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
            this.field_178941_a = p_i45535_4_;
        }

        public boolean func_178940_a()
        {
            return this.field_178941_a;
        }
    }

    public static class GuiEntry implements GuiListExtended.IGuiListEntry
    {
        private final 我的手艺 field_178031_a = 我的手艺.得到我的手艺();
        private final 鬼 field_178029_b;
        private final 鬼 field_178030_c;
        private 鬼 field_178028_d;

        public GuiEntry(鬼 p_i45533_1_, 鬼 p_i45533_2_)
        {
            this.field_178029_b = p_i45533_1_;
            this.field_178030_c = p_i45533_2_;
        }

        public 鬼 func_178022_a()
        {
            return this.field_178029_b;
        }

        public 鬼 func_178021_b()
        {
            return this.field_178030_c;
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
        {
            this.func_178017_a(this.field_178029_b, y, mouseX, mouseY, false);
            this.func_178017_a(this.field_178030_c, y, mouseX, mouseY, false);
        }

        private void func_178017_a(鬼 p_178017_1_, int p_178017_2_, int p_178017_3_, int p_178017_4_, boolean p_178017_5_)
        {
            if (p_178017_1_ != null)
            {
                if (p_178017_1_ instanceof 鬼Button)
                {
                    this.func_178024_a((鬼Button)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                }
                else if (p_178017_1_ instanceof 鬼TextField)
                {
                    this.func_178027_a((鬼TextField)p_178017_1_, p_178017_2_, p_178017_5_);
                }
                else if (p_178017_1_ instanceof 鬼Label)
                {
                    this.func_178025_a((鬼Label)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                }
            }
        }

        private void func_178024_a(鬼Button p_178024_1_, int p_178024_2_, int p_178024_3_, int p_178024_4_, boolean p_178024_5_)
        {
            p_178024_1_.yPosition = p_178024_2_;

            if (!p_178024_5_)
            {
                p_178024_1_.drawButton(this.field_178031_a, p_178024_3_, p_178024_4_);
            }
        }

        private void func_178027_a(鬼TextField p_178027_1_, int p_178027_2_, boolean p_178027_3_)
        {
            p_178027_1_.yPosition = p_178027_2_;

            if (!p_178027_3_)
            {
                p_178027_1_.drawTextBox();
            }
        }

        private void func_178025_a(鬼Label p_178025_1_, int p_178025_2_, int p_178025_3_, int p_178025_4_, boolean p_178025_5_)
        {
            p_178025_1_.field_146174_h = p_178025_2_;

            if (!p_178025_5_)
            {
                p_178025_1_.drawLabel(this.field_178031_a, p_178025_3_, p_178025_4_);
            }
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
        {
            this.func_178017_a(this.field_178029_b, p_178011_3_, 0, 0, true);
            this.func_178017_a(this.field_178030_c, p_178011_3_, 0, 0, true);
        }

        public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
        {
            boolean flag = this.func_178026_a(this.field_178029_b, p_148278_2_, p_148278_3_, p_148278_4_);
            boolean flag1 = this.func_178026_a(this.field_178030_c, p_148278_2_, p_148278_3_, p_148278_4_);
            return flag || flag1;
        }

        private boolean func_178026_a(鬼 p_178026_1_, int p_178026_2_, int p_178026_3_, int p_178026_4_)
        {
            if (p_178026_1_ == null)
            {
                return false;
            }
            else if (p_178026_1_ instanceof 鬼Button)
            {
                return this.func_178023_a((鬼Button)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            else
            {
                if (p_178026_1_ instanceof 鬼TextField)
                {
                    this.func_178018_a((鬼TextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
                }

                return false;
            }
        }

        private boolean func_178023_a(鬼Button p_178023_1_, int p_178023_2_, int p_178023_3_, int p_178023_4_)
        {
            boolean flag = p_178023_1_.mousePressed(this.field_178031_a, p_178023_2_, p_178023_3_);

            if (flag)
            {
                this.field_178028_d = p_178023_1_;
            }

            return flag;
        }

        private void func_178018_a(鬼TextField p_178018_1_, int p_178018_2_, int p_178018_3_, int p_178018_4_)
        {
            p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);

            if (p_178018_1_.isFocused())
            {
                this.field_178028_d = p_178018_1_;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.func_178016_b(this.field_178029_b, x, y, mouseEvent);
            this.func_178016_b(this.field_178030_c, x, y, mouseEvent);
        }

        private void func_178016_b(鬼 p_178016_1_, int p_178016_2_, int p_178016_3_, int p_178016_4_)
        {
            if (p_178016_1_ != null)
            {
                if (p_178016_1_ instanceof 鬼Button)
                {
                    this.func_178019_b((鬼Button)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
                }
            }
        }

        private void func_178019_b(鬼Button p_178019_1_, int p_178019_2_, int p_178019_3_, int p_178019_4_)
        {
            p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
        }
    }

    public static class GuiLabelEntry extends GuiPageButtonList.GuiListEntry
    {
        public GuiLabelEntry(int p_i45532_1_, String p_i45532_2_, boolean p_i45532_3_)
        {
            super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
        }
    }

    public static class GuiListEntry
    {
        private final int field_178939_a;
        private final String field_178937_b;
        private final boolean field_178938_c;

        public GuiListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_)
        {
            this.field_178939_a = p_i45531_1_;
            this.field_178937_b = p_i45531_2_;
            this.field_178938_c = p_i45531_3_;
        }

        public int func_178935_b()
        {
            return this.field_178939_a;
        }

        public String func_178936_c()
        {
            return this.field_178937_b;
        }

        public boolean func_178934_d()
        {
            return this.field_178938_c;
        }
    }

    public interface GuiResponder
    {
        void func_175321_a(int p_175321_1_, boolean p_175321_2_);

        void onTick(int id, float value);

        void func_175319_a(int p_175319_1_, String p_175319_2_);
    }

    public static class GuiSlideEntry extends GuiPageButtonList.GuiListEntry
    {
        private final 鬼Slider.FormatHelper field_178949_a;
        private final float field_178947_b;
        private final float field_178948_c;
        private final float field_178946_d;

        public GuiSlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, 鬼Slider.FormatHelper p_i45530_4_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_)
        {
            super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
            this.field_178949_a = p_i45530_4_;
            this.field_178947_b = p_i45530_5_;
            this.field_178948_c = p_i45530_6_;
            this.field_178946_d = p_i45530_7_;
        }

        public 鬼Slider.FormatHelper func_178945_a()
        {
            return this.field_178949_a;
        }

        public float func_178943_e()
        {
            return this.field_178947_b;
        }

        public float func_178944_f()
        {
            return this.field_178948_c;
        }

        public float func_178942_g()
        {
            return this.field_178946_d;
        }
    }
}
