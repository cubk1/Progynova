package net.minecraft.client.gui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.我的手艺;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class 鬼SelectWorld extends 鬼Screen implements GuiYesNoCallback
{
    private static final Logger logger = LogManager.getLogger();
    private final DateFormat field_146633_h = new SimpleDateFormat();
    protected 鬼Screen parentScreen;
    protected String screenTitle = "Select world";
    private boolean field_146634_i;
    private int selectedIndex;
    private java.util.List<SaveFormatComparator> field_146639_s;
    private 鬼SelectWorld.List availableWorlds;
    private String field_146637_u;
    private String field_146636_v;
    private String[] field_146635_w = new String[4];
    private boolean confirmingDelete;
    private 鬼Button deleteButton;
    private 鬼Button selectButton;
    private 鬼Button renameButton;
    private 鬼Button recreateButton;

    public 鬼SelectWorld(鬼Screen parentScreenIn)
    {
        this.parentScreen = parentScreenIn;
    }

    public void initGui()
    {
        this.screenTitle = I18n.format("selectWorld.title", new Object[0]);

        try
        {
            this.loadLevelList();
        }
        catch (AnvilConverterException anvilconverterexception)
        {
            logger.error((String)"Couldn\'t load level list", (Throwable)anvilconverterexception);
            this.mc.displayGuiScreen(new 鬼ErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
            return;
        }

        this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
        this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
        this.availableWorlds = new 鬼SelectWorld.List(this.mc);
        this.availableWorlds.registerScrollButtons(4, 5);
        this.addWorldSelectionButtons();
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.availableWorlds.handleMouseInput();
    }

    private void loadLevelList() throws AnvilConverterException
    {
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        this.field_146639_s = isaveformat.getSaveList();
        Collections.sort(this.field_146639_s);
        this.selectedIndex = -1;
    }

    protected String func_146621_a(int p_146621_1_)
    {
        return ((SaveFormatComparator)this.field_146639_s.get(p_146621_1_)).getFileName();
    }

    protected String func_146614_d(int p_146614_1_)
    {
        String s = ((SaveFormatComparator)this.field_146639_s.get(p_146614_1_)).getDisplayName();

        if (StringUtils.isEmpty(s))
        {
            s = I18n.format("selectWorld.world", new Object[0]) + " " + (p_146614_1_ + 1);
        }

        return s;
    }

    public void addWorldSelectionButtons()
    {
        this.buttonList.add(this.selectButton = new 鬼Button(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
        this.buttonList.add(new 鬼Button(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(this.renameButton = new 鬼Button(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
        this.buttonList.add(this.deleteButton = new 鬼Button(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
        this.buttonList.add(this.recreateButton = new 鬼Button(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
        this.buttonList.add(new 鬼Button(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.recreateButton.enabled = false;
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
                String s = this.func_146614_d(this.selectedIndex);

                if (s != null)
                {
                    this.confirmingDelete = true;
                    鬼YesNo guiyesno = makeDeleteWorldYesNo(this, s, this.selectedIndex);
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1)
            {
                this.func_146615_e(this.selectedIndex);
            }
            else if (button.id == 3)
            {
                this.mc.displayGuiScreen(new 鬼CreateWorld(this));
            }
            else if (button.id == 6)
            {
                this.mc.displayGuiScreen(new 鬼RenameWorld(this, this.func_146621_a(this.selectedIndex)));
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 7)
            {
                鬼CreateWorld guicreateworld = new 鬼CreateWorld(this);
                ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.selectedIndex), false);
                WorldInfo worldinfo = isavehandler.loadWorldInfo();
                isavehandler.flush();
                guicreateworld.recreateFromExistingWorld(worldinfo);
                this.mc.displayGuiScreen(guicreateworld);
            }
            else
            {
                this.availableWorlds.actionPerformed(button);
            }
        }
    }

    public void func_146615_e(int p_146615_1_)
    {
        this.mc.displayGuiScreen((鬼Screen)null);

        if (!this.field_146634_i)
        {
            this.field_146634_i = true;
            String s = this.func_146621_a(p_146615_1_);

            if (s == null)
            {
                s = "World" + p_146615_1_;
            }

            String s1 = this.func_146614_d(p_146615_1_);

            if (s1 == null)
            {
                s1 = "World" + p_146615_1_;
            }

            if (this.mc.getSaveLoader().canLoadWorld(s))
            {
                this.mc.launchIntegratedServer(s, s1, (WorldSettings)null);
            }
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        if (this.confirmingDelete)
        {
            this.confirmingDelete = false;

            if (result)
            {
                ISaveFormat isaveformat = this.mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(this.func_146621_a(id));

                try
                {
                    this.loadLevelList();
                }
                catch (AnvilConverterException anvilconverterexception)
                {
                    logger.error((String)"Couldn\'t load level list", (Throwable)anvilconverterexception);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.availableWorlds.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static 鬼YesNo makeDeleteWorldYesNo(GuiYesNoCallback selectWorld, String name, int id)
    {
        String s = I18n.format("selectWorld.deleteQuestion", new Object[0]);
        String s1 = "\'" + name + "\' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
        String s2 = I18n.format("selectWorld.deleteButton", new Object[0]);
        String s3 = I18n.format("gui.cancel", new Object[0]);
        鬼YesNo guiyesno = new 鬼YesNo(selectWorld, s, s1, s2, s3, id);
        return guiyesno;
    }

    class List extends GuiSlot
    {
        public List(我的手艺 mcIn)
        {
            super(mcIn, 鬼SelectWorld.this.width, 鬼SelectWorld.this.height, 32, 鬼SelectWorld.this.height - 64, 36);
        }

        protected int getSize()
        {
            return 鬼SelectWorld.this.field_146639_s.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            鬼SelectWorld.this.selectedIndex = slotIndex;
            boolean flag = 鬼SelectWorld.this.selectedIndex >= 0 && 鬼SelectWorld.this.selectedIndex < this.getSize();
            鬼SelectWorld.this.selectButton.enabled = flag;
            鬼SelectWorld.this.deleteButton.enabled = flag;
            鬼SelectWorld.this.renameButton.enabled = flag;
            鬼SelectWorld.this.recreateButton.enabled = flag;

            if (isDoubleClick && flag)
            {
                鬼SelectWorld.this.func_146615_e(slotIndex);
            }
        }

        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == 鬼SelectWorld.this.selectedIndex;
        }

        protected int getContentHeight()
        {
            return 鬼SelectWorld.this.field_146639_s.size() * 36;
        }

        protected void drawBackground()
        {
            鬼SelectWorld.this.drawDefaultBackground();
        }

        protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
            SaveFormatComparator saveformatcomparator = (SaveFormatComparator) 鬼SelectWorld.this.field_146639_s.get(entryID);
            String s = saveformatcomparator.getDisplayName();

            if (StringUtils.isEmpty(s))
            {
                s = 鬼SelectWorld.this.field_146637_u + " " + (entryID + 1);
            }

            String s1 = saveformatcomparator.getFileName();
            s1 = s1 + " (" + 鬼SelectWorld.this.field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
            s1 = s1 + ")";
            String s2 = "";

            if (saveformatcomparator.requiresConversion())
            {
                s2 = 鬼SelectWorld.this.field_146636_v + " " + s2;
            }
            else
            {
                s2 = 鬼SelectWorld.this.field_146635_w[saveformatcomparator.getEnumGameType().getID()];

                if (saveformatcomparator.isHardcoreModeEnabled())
                {
                    s2 = 枚举聊天格式.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + 枚举聊天格式.RESET;
                }

                if (saveformatcomparator.getCheatsEnabled())
                {
                    s2 = s2 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
                }
            }

            鬼SelectWorld.this.drawString(鬼SelectWorld.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
            鬼SelectWorld.this.drawString(鬼SelectWorld.this.fontRendererObj, s1, p_180791_2_ + 2, p_180791_3_ + 12, 8421504);
            鬼SelectWorld.this.drawString(鬼SelectWorld.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 12 + 10, 8421504);
        }
    }
}
