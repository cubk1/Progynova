package net.minecraft.client.entity;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.我的手艺;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.鬼Enchantment;
import net.minecraft.client.gui.inventory.鬼EditSign;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.entity.passive.实体Horse;
import net.minecraft.entity.实体;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.实体Item;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.客户端数据包3玩家;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.图像位置;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import rip.liyuxuan.event.implement.宇轩の玩家更新事件;
import rip.liyuxuan.event.implement.宇轩の移动事件;
import rip.liyuxuan.宇轩科技;

public class 实体PlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter statWriter;
    private double lastReportedPosX;
    private double lastReportedPosY;
    private double lastReportedPosZ;
    private float lastReportedYaw;
    private float lastReportedPitch;
    private boolean serverSneakState;
    private boolean serverSprintState;
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String clientBrand;
    public MovementInput 移动输入;
    protected 我的手艺 mc;
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    public float timeInPortal;
    public float prevTimeInPortal;

    public 实体PlayerSP(我的手艺 mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile)
    {
        super(worldIn, netHandler.getGameProfile());
        this.sendQueue = netHandler;
        this.statWriter = statFile;
        this.mc = mcIn;
        this.dimension = 0;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    public void heal(float healAmount)
    {
    }

    public void mountEntity(实体 实体In)
    {
        super.mountEntity(实体In);

        if (实体In instanceof 实体Minecart)
        {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (实体Minecart) 实体In));
        }
    }

    public void onUpdate()
    {
        if (this.worldObj.isBlockLoaded(new 阻止位置(this.X坐标, 0.0D, this.Z坐标)))
        {
            super.onUpdate();

            if (this.isRiding())
            {
                this.sendQueue.addToSendQueue(new 客户端数据包3玩家.C05PacketPlayerLook(this.旋转侧滑, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.移动输入.跳跃, this.移动输入.蹲));
            }
            else
            {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    public void onUpdateWalkingPlayer()
    {
        boolean flag = this.isSprinting();

        if (flag != this.serverSprintState)
        {
            if (flag)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = this.正在下蹲();

        if (flag1 != this.serverSneakState)
        {
            if (flag1)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity())
        {
            final 宇轩の玩家更新事件 pre = new 宇轩の玩家更新事件(this.旋转侧滑, this.rotationPitch, this.X坐标, this.getEntityBoundingBox().minY, this.Z坐标, this.onGround);
            宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().别过少爷生活_起床啦(pre);

            if (pre.射了吗())
            {
                宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().别过少爷生活_起床啦(new 宇轩の玩家更新事件()); // POST
                return;
            }

            double x = pre.得到插();
            double y = pre.得到歪();
            double z = pre.得到贼();
            float yaw = pre.getYaw();
            float pitch = pre.得到投掷();
            boolean ground = pre.在地上吗();
            double d0 = x - this.lastReportedPosX;
            double d1 = y - this.lastReportedPosY;
            double d2 = z - this.lastReportedPosZ;
            double d3 = yaw - this.lastReportedYaw;
            double d4 = pitch - this.lastReportedPitch;
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.riding实体 == null)
            {
                if (flag2 && flag3) {
                    this.sendQueue.addToSendQueue(new 客户端数据包3玩家.C06PacketPlayerPosLook(x, y, z, yaw, pitch, ground));
                } else if (flag2) {
                    this.sendQueue.addToSendQueue(new 客户端数据包3玩家.C04PacketPlayerPosition(x, y, z, ground));
                } else if (flag3) {
                    this.sendQueue.addToSendQueue(new 客户端数据包3玩家.C05PacketPlayerLook(yaw, pitch, ground));
                } else {
                    this.sendQueue.addToSendQueue(new 客户端数据包3玩家(ground));
                }
            } else {
                this.sendQueue.addToSendQueue(new 客户端数据包3玩家.C06PacketPlayerPosLook(this.通便X, -999.0D, this.通便Z, yaw, pitch, ground));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2)
            {
                this.lastReportedPosX = x;
                this.lastReportedPosY = y;
                this.lastReportedPosZ = z;
                this.positionUpdateTicks = 0;
            }

            if (flag3)
            {
                this.lastReportedYaw = yaw;
                this.lastReportedPitch = pitch;
            }

            宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().别过少爷生活_起床啦(new 宇轩の玩家更新事件());
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        final 宇轩の移动事件 event = new 宇轩の移动事件(x, y, z);
        宇轩科技.获取李宇轩1337().获取宇轩の事件管理员().别过少爷生活_起床啦(event);
        super.moveEntity(event.得到插(), event.得到歪(), event.得到贼());
    }

    public 实体Item dropOneItem(boolean dropAll)
    {
        C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, 阻止位置.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    protected void joinEntityItemWithWorld(实体Item itemIn)
    {
    }

    public void sendChatMessage(String message)
    {
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }

    public void swingItem()
    {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    public void respawnPlayer()
    {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
        if (!this.isEntityInvulnerable(damageSrc))
        {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }

    public void closeScreen()
    {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    public void closeScreenAndDropStack()
    {
        this.inventory.setItemStack((ItemStack)null);
        super.closeScreen();
        this.mc.displayGuiScreen((鬼Screen)null);
    }

    public void setPlayerSPHealth(float health)
    {
        if (this.hasValidHealth)
        {
            float f = this.getHealth() - health;

            if (f <= 0.0F)
            {
                this.setHealth(health);

                if (f < 0.0F)
                {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else
            {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                this.hurtTime = this.maxHurtTime = 10;
            }
        }
        else
        {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }

    public void addStat(StatBase stat, int amount)
    {
        if (stat != null)
        {
            if (stat.isIndependent)
            {
                super.addStat(stat, amount);
            }
        }
    }

    public void sendPlayerAbilities()
    {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    public boolean isUser()
    {
        return true;
    }

    protected void sendHorseJump()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public void setClientBrand(String brand)
    {
        this.clientBrand = brand;
    }

    public String getClientBrand()
    {
        return this.clientBrand;
    }

    public StatFileWriter getStatFileWriter()
    {
        return this.statWriter;
    }

    public void addChatComponentMessage(IChatComponent chatComponent)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
        if (this.noClip)
        {
            return false;
        }
        else
        {
            阻止位置 blockpos = new 阻止位置(x, y, z);
            double d0 = x - (double)blockpos.getX();
            double d1 = z - (double)blockpos.getZ();

            if (!this.isOpenBlockSpace(blockpos))
            {
                int i = -1;
                double d2 = 9999.0D;

                if (this.isOpenBlockSpace(blockpos.west()) && d0 < d2)
                {
                    d2 = d0;
                    i = 0;
                }

                if (this.isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2)
                {
                    d2 = 1.0D - d0;
                    i = 1;
                }

                if (this.isOpenBlockSpace(blockpos.north()) && d1 < d2)
                {
                    d2 = d1;
                    i = 4;
                }

                if (this.isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2)
                {
                    d2 = 1.0D - d1;
                    i = 5;
                }

                float f = 0.1F;

                if (i == 0)
                {
                    this.通便X = (double)(-f);
                }

                if (i == 1)
                {
                    this.通便X = (double)f;
                }

                if (i == 4)
                {
                    this.通便Z = (double)(-f);
                }

                if (i == 5)
                {
                    this.通便Z = (double)f;
                }
            }

            return false;
        }
    }

    private boolean isOpenBlockSpace(阻止位置 pos)
    {
        return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
    }

    public void 设置宇轩的疾跑状态(boolean 疾跑状态)
    {
        super.设置宇轩的疾跑状态(疾跑状态);
        this.sprintingTicksLeft = 疾跑状态 ? 600 : 0;
    }

    public void setXPStats(float currentXP, int maxXP, int level)
    {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }

    public void 增添聊天讯息(IChatComponent component)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return permLevel <= 0;
    }

    public 阻止位置 getPosition()
    {
        return new 阻止位置(this.X坐标 + 0.5D, this.Y坐标 + 0.5D, this.Z坐标 + 0.5D);
    }

    public void playSound(String name, float volume, float pitch)
    {
        this.worldObj.playSound(this.X坐标, this.Y坐标, this.Z坐标, name, volume, pitch, false);
    }

    public boolean isServerWorld()
    {
        return true;
    }

    public boolean isRidingHorse()
    {
        return this.riding实体 != null && this.riding实体 instanceof 实体Horse && ((实体Horse)this.riding实体).isHorseSaddled();
    }

    public float getHorseJumpPower()
    {
        return this.horseJumpPower;
    }

    public void openEditSign(TileEntitySign signTile)
    {
        this.mc.displayGuiScreen(new 鬼EditSign(signTile));
    }

    public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic)
    {
        this.mc.displayGuiScreen(new 鬼CommandBlock(cmdBlockLogic));
    }

    public void displayGUIBook(ItemStack bookStack)
    {
        Item item = bookStack.getItem();

        if (item == Items.writable_book)
        {
            this.mc.displayGuiScreen(new 鬼ScreenBook(this, bookStack, true));
        }
    }

    public void displayGUIChest(IInventory chestInventory)
    {
        String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Chest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Hopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Furnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼BrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Beacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Chest(this.inventory, chestInventory));
        }
        else
        {
            this.mc.displayGuiScreen(new 鬼Dispenser(this.inventory, chestInventory));
        }
    }

    public void displayGUIHorse(实体Horse horse, IInventory horseInventory)
    {
        this.mc.displayGuiScreen(new 鬼ScreenHorseInventory(this.inventory, horseInventory, horse));
    }

    public void displayGui(IInteractionObject guiOwner)
    {
        String s = guiOwner.getGuiID();

        if ("minecraft:crafting_table".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Crafting(this.inventory, this.worldObj));
        }
        else if ("minecraft:enchanting_table".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Enchantment(this.inventory, this.worldObj, guiOwner));
        }
        else if ("minecraft:anvil".equals(s))
        {
            this.mc.displayGuiScreen(new 鬼Repair(this.inventory, this.worldObj));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager)
    {
        this.mc.displayGuiScreen(new 鬼Merchant(this.inventory, villager, this.worldObj));
    }

    public void onCriticalHit(实体 实体Hit)
    {
        this.mc.effectRenderer.emitParticleAtEntity(实体Hit, EnumParticleTypes.CRIT);
    }

    public void onEnchantmentCritical(实体 实体Hit)
    {
        this.mc.effectRenderer.emitParticleAtEntity(实体Hit, EnumParticleTypes.CRIT_MAGIC);
    }

    public boolean 正在下蹲()
    {
        boolean flag = this.移动输入 != null && this.移动输入.蹲;
        return flag && !this.sleeping;
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (this.isCurrentViewEntity())
        {
            this.moveStrafing = this.移动输入.侧向移动;
            this.moveForward = this.移动输入.向前移动;
            this.isJumping = this.移动输入.跳跃;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.旋转侧滑 - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity()
    {
        return this.mc.getRenderViewEntity() == this;
    }

    public void onLivingUpdate()
    {
        if (this.sprintingTicksLeft > 0)
        {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0)
            {
                this.设置宇轩的疾跑状态(false);
            }
        }

        if (this.sprintToggleTimer > 0)
        {
            --this.sprintToggleTimer;
        }

        this.prevTimeInPortal = this.timeInPortal;

        if (this.inPortal)
        {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
            {
                this.mc.displayGuiScreen((鬼Screen)null);
            }

            if (this.timeInPortal == 0.0F)
            {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new 图像位置("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F)
            {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F)
            {
                this.timeInPortal = 1.0F;
            }
        }
        else
        {
            if (this.timeInPortal > 0.0F)
            {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F)
            {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }

        boolean flag = this.移动输入.跳跃;
        boolean flag1 = this.移动输入.蹲;
        float f = 0.8F;
        boolean flag2 = this.移动输入.向前移动 >= f;
        this.移动输入.updatePlayerMoveState();

        if (this.isUsingItem() && !this.isRiding())
        {
            this.移动输入.侧向移动 *= 0.2F;
            this.移动输入.向前移动 *= 0.2F;
            this.sprintToggleTimer = 0;
        }

        this.pushOutOfBlocks(this.X坐标 - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.Z坐标 + (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.X坐标 - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.Z坐标 - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.X坐标 + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.Z坐标 - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.X坐标 + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.Z坐标 + (double)this.width * 0.35D);
        boolean flag3 = (float)this.获取饥饿值().获取饥饿等级() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && this.移动输入.向前移动 >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
        {
            if (this.sprintToggleTimer <= 0 && !this.mc.游戏一窝.keyBindSprint.键位绑定沿着())
            {
                this.sprintToggleTimer = 7;
            }
            else
            {
                this.设置宇轩的疾跑状态(true);
            }
        }

        if (!this.isSprinting() && this.移动输入.向前移动 >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.游戏一窝.keyBindSprint.键位绑定沿着())
        {
            this.设置宇轩的疾跑状态(true);
        }

        if (this.isSprinting() && (this.移动输入.向前移动 < f || this.isCollidedHorizontally || !flag3))
        {
            this.设置宇轩的疾跑状态(false);
        }

        if (this.capabilities.allowFlying)
        {
            if (this.mc.玩家控制者.isSpectatorMode())
            {
                if (!this.capabilities.isFlying)
                {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!flag && this.移动输入.跳跃)
            {
                if (this.flyToggleTimer == 0)
                {
                    this.flyToggleTimer = 7;
                }
                else
                {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.capabilities.isFlying && this.isCurrentViewEntity())
        {
            if (this.移动输入.蹲)
            {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
            }

            if (this.移动输入.跳跃)
            {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
            }
        }

        if (this.isRidingHorse())
        {
            if (this.horseJumpPowerCounter < 0)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0)
                {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.移动输入.跳跃)
            {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!flag && this.移动输入.跳跃)
            {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            }
            else if (flag)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10)
                {
                    this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
                }
                else
                {
                    this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        }
        else
        {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.capabilities.isFlying && !this.mc.玩家控制者.isSpectatorMode())
        {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
}
