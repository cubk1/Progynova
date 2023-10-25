package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.实体Chicken;
import net.minecraft.entity.passive.实体Villager;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class 实体Zombie extends 实体Mob
{
    protected static final IAttribute reinforcementChance = (new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
    private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
    private int conversionTime;
    private boolean isBreakDoorsTaskSet = false;
    private float zombieWidth = -1.0F;
    private float zombieHeight;

    public 实体Zombie(World worldIn)
    {
        super(worldIn);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 实体Player.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, 实体Player.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
        this.setSize(0.6F, 1.95F);
    }

    protected void applyEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 实体Villager.class, 1.0D, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 实体IronGolem.class, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {实体PigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, 实体Player.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, 实体Villager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, 实体IronGolem.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
        this.getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(13, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(14, Byte.valueOf((byte)0));
    }

    public int getTotalArmorValue()
    {
        int i = super.getTotalArmorValue() + 2;

        if (i > 20)
        {
            i = 20;
        }

        return i;
    }

    public boolean isBreakDoorsTaskSet()
    {
        return this.isBreakDoorsTaskSet;
    }

    public void setBreakDoorsAItask(boolean par1)
    {
        if (this.isBreakDoorsTaskSet != par1)
        {
            this.isBreakDoorsTaskSet = par1;

            if (par1)
            {
                this.tasks.addTask(1, this.breakDoor);
            }
            else
            {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }

    public boolean isChild()
    {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    protected int getExperiencePoints(实体Player player)
    {
        if (this.isChild())
        {
            this.experienceValue = (int)((float)this.experienceValue * 2.5F);
        }

        return super.getExperiencePoints(player);
    }

    public void setChild(boolean childZombie)
    {
        this.getDataWatcher().updateObject(12, Byte.valueOf((byte)(childZombie ? 1 : 0)));

        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(babySpeedBoostModifier);

            if (childZombie)
            {
                iattributeinstance.applyModifier(babySpeedBoostModifier);
            }
        }

        this.setChildSize(childZombie);
    }

    public boolean isVillager()
    {
        return this.getDataWatcher().getWatchableObjectByte(13) == 1;
    }

    public void setVillager(boolean villager)
    {
        this.getDataWatcher().updateObject(13, Byte.valueOf((byte)(villager ? 1 : 0)));
    }

    public void onLivingUpdate()
    {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
        {
            float f = this.getBrightness(1.0F);
            阻止位置 blockpos = new 阻止位置(this.X坐标, (double)Math.round(this.Y坐标), this.Z坐标);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos))
            {
                boolean flag = true;
                ItemStack itemstack = this.getEquipmentInSlot(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }

        if (this.isRiding() && this.getAttackTarget() != null && this.riding实体 instanceof 实体Chicken)
        {
            ((实体Living)this.riding实体).getNavigator().setPath(this.getNavigator().getPath(), 1.5D);
        }

        super.onLivingUpdate();
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (super.attackEntityFrom(source, amount))
        {
            实体LivingBase entitylivingbase = this.getAttackTarget();

            if (entitylivingbase == null && source.getEntity() instanceof 实体LivingBase)
            {
                entitylivingbase = (实体LivingBase)source.getEntity();
            }

            if (entitylivingbase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && (double)this.rand.nextFloat() < this.getEntityAttribute(reinforcementChance).getAttributeValue())
            {
                int i = MathHelper.floor_double(this.X坐标);
                int j = MathHelper.floor_double(this.Y坐标);
                int k = MathHelper.floor_double(this.Z坐标);
                实体Zombie entityzombie = new 实体Zombie(this.worldObj);

                for (int l = 0; l < 50; ++l)
                {
                    int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);

                    if (World.doesBlockHaveSolidTopSurface(this.worldObj, new 阻止位置(i1, j1 - 1, k1)) && this.worldObj.getLightFromNeighbors(new 阻止位置(i1, j1, k1)) < 10)
                    {
                        entityzombie.setPosition((double)i1, (double)j1, (double)k1);

                        if (!this.worldObj.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0D) && this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.worldObj.getCollidingBoundingBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox()))
                        {
                            this.worldObj.spawnEntityInWorld(entityzombie);
                            entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new 阻止位置(entityzombie)), (IEntityLivingData)null);
                            this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void onUpdate()
    {
        if (!this.worldObj.isRemote && this.isConverting())
        {
            int i = this.getConversionTimeBoost();
            this.conversionTime -= i;

            if (this.conversionTime <= 0)
            {
                this.convertToVillager();
            }
        }

        super.onUpdate();
    }

    public boolean attackEntityAsMob(实体 实体In)
    {
        boolean flag = super.attackEntityAsMob(实体In);

        if (flag)
        {
            int i = this.worldObj.getDifficulty().getDifficultyId();

            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F)
            {
                实体In.setFire(2 * i);
            }
        }

        return flag;
    }

    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }

    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }

    protected void playStepSound(阻止位置 pos, Block blockIn)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    protected Item getDropItem()
    {
        return Items.rotten_flesh;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(3))
        {
            case 0:
                this.dropItem(Items.iron_ingot, 1);
                break;

            case 1:
                this.dropItem(Items.carrot, 1);
                break;

            case 2:
                this.dropItem(Items.potato, 1);
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (this.rand.nextFloat() < (this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
        {
            int i = this.rand.nextInt(3);

            if (i == 0)
            {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            }
            else
            {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.isChild())
        {
            tagCompound.setBoolean("IsBaby", true);
        }

        if (this.isVillager())
        {
            tagCompound.setBoolean("IsVillager", true);
        }

        tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        tagCompound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }

        if (tagCompund.getBoolean("IsVillager"))
        {
            this.setVillager(true);
        }

        if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1)
        {
            this.startConversion(tagCompund.getInteger("ConversionTime"));
        }

        this.setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
    }

    public void onKillEntity(实体LivingBase entityLivingIn)
    {
        super.onKillEntity(entityLivingIn);

        if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof 实体Villager)
        {
            if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean())
            {
                return;
            }

            实体Living entityliving = (实体Living)entityLivingIn;
            实体Zombie entityzombie = new 实体Zombie(this.worldObj);
            entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
            this.worldObj.removeEntity(entityLivingIn);
            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new 阻止位置(entityzombie)), (IEntityLivingData)null);
            entityzombie.setVillager(true);

            if (entityLivingIn.isChild())
            {
                entityzombie.setChild(true);
            }

            entityzombie.setNoAI(entityliving.isAIDisabled());

            if (entityliving.hasCustomName())
            {
                entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
                entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
            }

            this.worldObj.spawnEntityInWorld(entityzombie);
            this.worldObj.playAuxSFXAtEntity((实体Player)null, 1016, new 阻止位置((int)this.X坐标, (int)this.Y坐标, (int)this.Z坐标), 0);
        }
    }

    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }

    protected boolean func_175448_a(ItemStack stack)
    {
        return stack.getItem() == Items.egg && this.isChild() && this.isRiding() ? false : super.func_175448_a(stack);
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

        if (livingdata == null)
        {
            livingdata = new 实体Zombie.GroupData(this.worldObj.rand.nextFloat() < 0.05F, this.worldObj.rand.nextFloat() < 0.05F);
        }

        if (livingdata instanceof 实体Zombie.GroupData)
        {
            实体Zombie.GroupData entityzombie$groupdata = (实体Zombie.GroupData)livingdata;

            if (entityzombie$groupdata.isVillager)
            {
                this.setVillager(true);
            }

            if (entityzombie$groupdata.isChild)
            {
                this.setChild(true);

                if ((double)this.worldObj.rand.nextFloat() < 0.05D)
                {
                    List<实体Chicken> list = this.worldObj.<实体Chicken>getEntitiesWithinAABB(实体Chicken.class, this.getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);

                    if (!list.isEmpty())
                    {
                        实体Chicken entitychicken = (实体Chicken)list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.mountEntity(entitychicken);
                    }
                }
                else if ((double)this.worldObj.rand.nextFloat() < 0.05D)
                {
                    实体Chicken entitychicken1 = new 实体Chicken(this.worldObj);
                    entitychicken1.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, 0.0F);
                    entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
                    entitychicken1.setChickenJockey(true);
                    this.worldObj.spawnEntityInWorld(entitychicken1);
                    this.mountEntity(entitychicken1);
                }
            }
        }

        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        if (this.getEquipmentInSlot(4) == null)
        {
            Calendar calendar = this.worldObj.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0F;
            }
        }

        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

        if (d0 > 1.0D)
        {
            this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.rand.nextFloat() < f * 0.05F)
        {
            this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
            this.setBreakDoorsAItask(true);
        }

        return livingdata;
    }

    public boolean interact(实体Player player)
    {
        ItemStack itemstack = player.getCurrentEquippedItem();

        if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getMetadata() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness))
        {
            if (!player.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }

            if (!this.worldObj.isRemote)
            {
                this.startConversion(this.rand.nextInt(2401) + 3600);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void startConversion(int ticks)
    {
        this.conversionTime = ticks;
        this.getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
        this.removePotionEffect(Potion.weakness.id);
        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
        this.worldObj.setEntityState(this, (byte)16);
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 16)
        {
            if (!this.isSilent())
            {
                this.worldObj.playSound(this.X坐标 + 0.5D, this.Y坐标 + 0.5D, this.Z坐标 + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    protected boolean canDespawn()
    {
        return !this.isConverting();
    }

    public boolean isConverting()
    {
        return this.getDataWatcher().getWatchableObjectByte(14) == 1;
    }

    protected void convertToVillager()
    {
        实体Villager entityvillager = new 实体Villager(this.worldObj);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new 阻止位置(entityvillager)), (IEntityLivingData)null);
        entityvillager.setLookingForHome();

        if (this.isChild())
        {
            entityvillager.setGrowingAge(-24000);
        }

        this.worldObj.removeEntity(this);
        entityvillager.setNoAI(this.isAIDisabled());

        if (this.hasCustomName())
        {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }

        this.worldObj.spawnEntityInWorld(entityvillager);
        entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        this.worldObj.playAuxSFXAtEntity((实体Player)null, 1017, new 阻止位置((int)this.X坐标, (int)this.Y坐标, (int)this.Z坐标), 0);
    }

    protected int getConversionTimeBoost()
    {
        int i = 1;

        if (this.rand.nextFloat() < 0.01F)
        {
            int j = 0;
            阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置();

            for (int k = (int)this.X坐标 - 4; k < (int)this.X坐标 + 4 && j < 14; ++k)
            {
                for (int l = (int)this.Y坐标 - 4; l < (int)this.Y坐标 + 4 && j < 14; ++l)
                {
                    for (int i1 = (int)this.Z坐标 - 4; i1 < (int)this.Z坐标 + 4 && j < 14; ++i1)
                    {
                        Block block = this.worldObj.getBlockState(blockpos$mutableblockpos.set(k, l, i1)).getBlock();

                        if (block == Blocks.iron_bars || block == Blocks.bed)
                        {
                            if (this.rand.nextFloat() < 0.3F)
                            {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }

    protected final void setSize(float width, float height)
    {
        boolean flag = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
        this.zombieWidth = width;
        this.zombieHeight = height;

        if (!flag)
        {
            this.multiplySize(1.0F);
        }
    }

    protected final void multiplySize(float size)
    {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }

    public double getYOffset()
    {
        return this.isChild() ? 0.0D : -0.35D;
    }

    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (cause.getEntity() instanceof 实体Creeper && !(this instanceof 实体PigZombie) && ((实体Creeper)cause.getEntity()).getPowered() && ((实体Creeper)cause.getEntity()).isAIEnabled())
        {
            ((实体Creeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
        }
    }

    class GroupData implements IEntityLivingData
    {
        public boolean isChild;
        public boolean isVillager;

        private GroupData(boolean isBaby, boolean isVillagerZombie)
        {
            this.isChild = false;
            this.isVillager = false;
            this.isChild = isBaby;
            this.isVillager = isVillagerZombie;
        }
    }
}
