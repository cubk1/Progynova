package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.实体XPOrb;
import net.minecraft.entity.monster.实体Witch;
import net.minecraft.entity.monster.实体Zombie;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.实体LightningBolt;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class 实体Villager extends 实体Ageable implements IMerchant, INpc
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;
    private 实体Player buyingPlayer;
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;
    private String lastBuyingPlayer;
    private int careerId;
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private InventoryBasic villagerInventory;
    private static final 实体Villager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new 实体Villager.ITradeList[][][][] {{{{new 实体Villager.EmeraldForItems(Items.wheat, new 实体Villager.PriceInfo(18, 22)), new 实体Villager.EmeraldForItems(Items.potato, new 实体Villager.PriceInfo(15, 19)), new 实体Villager.EmeraldForItems(Items.carrot, new 实体Villager.PriceInfo(15, 19)), new 实体Villager.ListItemForEmeralds(Items.bread, new 实体Villager.PriceInfo(-4, -2))}, {new 实体Villager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new 实体Villager.PriceInfo(8, 13)), new 实体Villager.ListItemForEmeralds(Items.pumpkin_pie, new 实体Villager.PriceInfo(-3, -2))}, {new 实体Villager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new 实体Villager.PriceInfo(7, 12)), new 实体Villager.ListItemForEmeralds(Items.apple, new 实体Villager.PriceInfo(-5, -7))}, {new 实体Villager.ListItemForEmeralds(Items.cookie, new 实体Villager.PriceInfo(-6, -10)), new 实体Villager.ListItemForEmeralds(Items.cake, new 实体Villager.PriceInfo(1, 1))}}, {{new 实体Villager.EmeraldForItems(Items.string, new 实体Villager.PriceInfo(15, 20)), new 实体Villager.EmeraldForItems(Items.coal, new 实体Villager.PriceInfo(16, 24)), new 实体Villager.ItemAndEmeraldToItem(Items.fish, new 实体Villager.PriceInfo(6, 6), Items.cooked_fish, new 实体Villager.PriceInfo(6, 6))}, {new 实体Villager.ListEnchantedItemForEmeralds(Items.fishing_rod, new 实体Villager.PriceInfo(7, 8))}}, {{new 实体Villager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new 实体Villager.PriceInfo(16, 22)), new 实体Villager.ListItemForEmeralds(Items.shears, new 实体Villager.PriceInfo(3, 4))}, {new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new 实体Villager.PriceInfo(1, 2)), new 实体Villager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new 实体Villager.PriceInfo(1, 2))}}, {{new 实体Villager.EmeraldForItems(Items.string, new 实体Villager.PriceInfo(15, 20)), new 实体Villager.ListItemForEmeralds(Items.arrow, new 实体Villager.PriceInfo(-12, -8))}, {new 实体Villager.ListItemForEmeralds(Items.bow, new 实体Villager.PriceInfo(2, 3)), new 实体Villager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new 实体Villager.PriceInfo(10, 10), Items.flint, new 实体Villager.PriceInfo(6, 10))}}}, {{{new 实体Villager.EmeraldForItems(Items.paper, new 实体Villager.PriceInfo(24, 36)), new 实体Villager.ListEnchantedBookForEmeralds()}, {new 实体Villager.EmeraldForItems(Items.book, new 实体Villager.PriceInfo(8, 10)), new 实体Villager.ListItemForEmeralds(Items.compass, new 实体Villager.PriceInfo(10, 12)), new 实体Villager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new 实体Villager.PriceInfo(3, 4))}, {new 实体Villager.EmeraldForItems(Items.written_book, new 实体Villager.PriceInfo(2, 2)), new 实体Villager.ListItemForEmeralds(Items.clock, new 实体Villager.PriceInfo(10, 12)), new 实体Villager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new 实体Villager.PriceInfo(-5, -3))}, {new 实体Villager.ListEnchantedBookForEmeralds()}, {new 实体Villager.ListEnchantedBookForEmeralds()}, {new 实体Villager.ListItemForEmeralds(Items.name_tag, new 实体Villager.PriceInfo(20, 22))}}}, {{{new 实体Villager.EmeraldForItems(Items.rotten_flesh, new 实体Villager.PriceInfo(36, 40)), new 实体Villager.EmeraldForItems(Items.gold_ingot, new 实体Villager.PriceInfo(8, 10))}, {new 实体Villager.ListItemForEmeralds(Items.redstone, new 实体Villager.PriceInfo(-4, -1)), new 实体Villager.ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new 实体Villager.PriceInfo(-2, -1))}, {new 实体Villager.ListItemForEmeralds(Items.ender_eye, new 实体Villager.PriceInfo(7, 11)), new 实体Villager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new 实体Villager.PriceInfo(-3, -1))}, {new 实体Villager.ListItemForEmeralds(Items.experience_bottle, new 实体Villager.PriceInfo(3, 11))}}}, {{{new 实体Villager.EmeraldForItems(Items.coal, new 实体Villager.PriceInfo(16, 24)), new 实体Villager.ListItemForEmeralds(Items.iron_helmet, new 实体Villager.PriceInfo(4, 6))}, {new 实体Villager.EmeraldForItems(Items.iron_ingot, new 实体Villager.PriceInfo(7, 9)), new 实体Villager.ListItemForEmeralds(Items.iron_chestplate, new 实体Villager.PriceInfo(10, 14))}, {new 实体Villager.EmeraldForItems(Items.diamond, new 实体Villager.PriceInfo(3, 4)), new 实体Villager.ListEnchantedItemForEmeralds(Items.diamond_chestplate, new 实体Villager.PriceInfo(16, 19))}, {new 实体Villager.ListItemForEmeralds(Items.chainmail_boots, new 实体Villager.PriceInfo(5, 7)), new 实体Villager.ListItemForEmeralds(Items.chainmail_leggings, new 实体Villager.PriceInfo(9, 11)), new 实体Villager.ListItemForEmeralds(Items.chainmail_helmet, new 实体Villager.PriceInfo(5, 7)), new 实体Villager.ListItemForEmeralds(Items.chainmail_chestplate, new 实体Villager.PriceInfo(11, 15))}}, {{new 实体Villager.EmeraldForItems(Items.coal, new 实体Villager.PriceInfo(16, 24)), new 实体Villager.ListItemForEmeralds(Items.iron_axe, new 实体Villager.PriceInfo(6, 8))}, {new 实体Villager.EmeraldForItems(Items.iron_ingot, new 实体Villager.PriceInfo(7, 9)), new 实体Villager.ListEnchantedItemForEmeralds(Items.iron_sword, new 实体Villager.PriceInfo(9, 10))}, {new 实体Villager.EmeraldForItems(Items.diamond, new 实体Villager.PriceInfo(3, 4)), new 实体Villager.ListEnchantedItemForEmeralds(Items.diamond_sword, new 实体Villager.PriceInfo(12, 15)), new 实体Villager.ListEnchantedItemForEmeralds(Items.diamond_axe, new 实体Villager.PriceInfo(9, 12))}}, {{new 实体Villager.EmeraldForItems(Items.coal, new 实体Villager.PriceInfo(16, 24)), new 实体Villager.ListEnchantedItemForEmeralds(Items.iron_shovel, new 实体Villager.PriceInfo(5, 7))}, {new 实体Villager.EmeraldForItems(Items.iron_ingot, new 实体Villager.PriceInfo(7, 9)), new 实体Villager.ListEnchantedItemForEmeralds(Items.iron_pickaxe, new 实体Villager.PriceInfo(9, 11))}, {new 实体Villager.EmeraldForItems(Items.diamond, new 实体Villager.PriceInfo(3, 4)), new 实体Villager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new 实体Villager.PriceInfo(12, 15))}}}, {{{new 实体Villager.EmeraldForItems(Items.porkchop, new 实体Villager.PriceInfo(14, 18)), new 实体Villager.EmeraldForItems(Items.chicken, new 实体Villager.PriceInfo(14, 18))}, {new 实体Villager.EmeraldForItems(Items.coal, new 实体Villager.PriceInfo(16, 24)), new 实体Villager.ListItemForEmeralds(Items.cooked_porkchop, new 实体Villager.PriceInfo(-7, -5)), new 实体Villager.ListItemForEmeralds(Items.cooked_chicken, new 实体Villager.PriceInfo(-8, -6))}}, {{new 实体Villager.EmeraldForItems(Items.leather, new 实体Villager.PriceInfo(9, 12)), new 实体Villager.ListItemForEmeralds(Items.leather_leggings, new 实体Villager.PriceInfo(2, 4))}, {new 实体Villager.ListEnchantedItemForEmeralds(Items.leather_chestplate, new 实体Villager.PriceInfo(7, 12))}, {new 实体Villager.ListItemForEmeralds(Items.saddle, new 实体Villager.PriceInfo(8, 10))}}}};

    public 实体Villager(World worldIn)
    {
        this(worldIn, 0);
    }

    public 实体Villager(World worldIn, int professionId)
    {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
        this.setProfession(professionId);
        this.setSize(0.6F, 1.8F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, 实体Zombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, 实体Player.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, 实体Living.class, 8.0F));
        this.setCanPickUpLoot(true);
    }

    private void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true;

            if (this.isChild())
            {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            }
            else if (this.getProfession() == 0)
            {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    protected void onGrowingAdult()
    {
        if (this.getProfession() == 0)
        {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.onGrowingAdult();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    protected void updateAITasks()
    {
        if (--this.randomTickDivider <= 0)
        {
            阻止位置 blockpos = new 阻止位置(this);
            this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                阻止位置 blockpos1 = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockpos1, (int)((float)this.villageObj.getVillageRadius() * 1.0F));

                if (this.isLookingForHome)
                {
                    this.isLookingForHome = false;
                    this.villageObj.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    for (MerchantRecipe merchantrecipe : this.buyingList)
                    {
                        if (merchantrecipe.isRecipeDisabled())
                        {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.populateBuyingList();
                    this.needsInitilization = false;

                    if (this.villageObj != null && this.lastBuyingPlayer != null)
                    {
                        this.worldObj.setEntityState(this, (byte)14);
                        this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
                    }
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }

        super.updateAITasks();
    }

    public boolean interact(实体Player player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild())
        {
            if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0))
            {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }

            player.triggerAchievement(StatList.timesTalkedToVillagerStat);
            return true;
        }
        else
        {
            return super.interact(player);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Profession", this.getProfession());
        tagCompound.setInteger("Riches", this.wealth);
        tagCompound.setInteger("Career", this.careerId);
        tagCompound.setInteger("CareerLevel", this.careerLevel);
        tagCompound.setBoolean("Willing", this.isWillingToMate);

        if (this.buyingList != null)
        {
            tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (itemstack != null)
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }

        tagCompound.setTag("Inventory", nbttaglist);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setProfession(tagCompund.getInteger("Profession"));
        this.wealth = tagCompund.getInteger("Riches");
        this.careerId = tagCompund.getInteger("Career");
        this.careerLevel = tagCompund.getInteger("CareerLevel");
        this.isWillingToMate = tagCompund.getBoolean("Willing");

        if (tagCompund.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }

        NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));

            if (itemstack != null)
            {
                this.villagerInventory.func_174894_a(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected String getLivingSound()
    {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    protected String getHurtSound()
    {
        return "mob.villager.hit";
    }

    protected String getDeathSound()
    {
        return "mob.villager.death";
    }

    public void setProfession(int professionId)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(professionId));
    }

    public int getProfession()
    {
        return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean mating)
    {
        this.isMating = mating;
    }

    public void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public void setRevengeTarget(实体LivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (this.villageObj != null && livingBase != null)
        {
            this.villageObj.addOrRenewAgressor(livingBase);

            if (livingBase instanceof 实体Player)
            {
                int i = -1;

                if (this.isChild())
                {
                    i = -3;
                }

                this.villageObj.setReputationForPlayer(livingBase.getName(), i);

                if (this.isEntityAlive())
                {
                    this.worldObj.setEntityState(this, (byte)13);
                }
            }
        }
    }

    public void onDeath(DamageSource cause)
    {
        if (this.villageObj != null)
        {
            实体 实体 = cause.getEntity();

            if (实体 != null)
            {
                if (实体 instanceof 实体Player)
                {
                    this.villageObj.setReputationForPlayer(实体.getName(), -2);
                }
                else if (实体 instanceof IMob)
                {
                    this.villageObj.endMatingSeason();
                }
            }
            else
            {
                实体Player entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

                if (entityplayer != null)
                {
                    this.villageObj.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    public void setCustomer(实体Player p_70932_1_)
    {
        this.buyingPlayer = p_70932_1_;
    }

    public 实体Player getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    public boolean getIsWillingToMate(boolean updateFirst)
    {
        if (!this.isWillingToMate && updateFirst && this.func_175553_cp())
        {
            boolean flag = false;

            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

                if (itemstack != null)
                {
                    if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3)
                    {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot) && itemstack.stackSize >= 12)
                    {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }

                if (flag)
                {
                    this.worldObj.setEntityState(this, (byte)18);
                    this.isWillingToMate = true;
                    break;
                }
            }
        }

        return this.isWillingToMate;
    }

    public void setIsWillingToMate(boolean willingToTrade)
    {
        this.isWillingToMate = willingToTrade;
    }

    public void useRecipe(MerchantRecipe recipe)
    {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0)
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getName();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }

            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.emerald)
        {
            this.wealth += recipe.getItemToBuy().stackSize;
        }

        if (recipe.getRewardsExp())
        {
            this.worldObj.spawnEntityInWorld(new 实体XPOrb(this.worldObj, this.X坐标, this.Y坐标 + 0.5D, this.Z坐标, i));
        }
    }

    public void verifySellingItem(ItemStack stack)
    {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();

            if (stack != null)
            {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else
            {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    public MerchantRecipeList getRecipes(实体Player p_70934_1_)
    {
        if (this.buyingList == null)
        {
            this.populateBuyingList();
        }

        return this.buyingList;
    }

    private void populateBuyingList()
    {
        实体Villager.ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[this.getProfession()];

        if (this.careerId != 0 && this.careerLevel != 0)
        {
            ++this.careerLevel;
        }
        else
        {
            this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
            this.careerLevel = 1;
        }

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        int i = this.careerId - 1;
        int j = this.careerLevel - 1;
        实体Villager.ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];

        if (j >= 0 && j < aentityvillager$itradelist1.length)
        {
            实体Villager.ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];

            for (实体Villager.ITradeList entityvillager$itradelist : aentityvillager$itradelist2)
            {
                entityvillager$itradelist.modifyMerchantRecipeList(this.buyingList, this.rand);
            }
        }
    }

    public void setRecipes(MerchantRecipeList recipeList)
    {
    }

    public IChatComponent getDisplayName()
    {
        String s = this.getCustomNameTag();

        if (s != null && s.length() > 0)
        {
            交流组分文本 chatcomponenttext = new 交流组分文本(s);
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttext;
        }
        else
        {
            if (this.buyingList == null)
            {
                this.populateBuyingList();
            }

            String s1 = null;

            switch (this.getProfession())
            {
                case 0:
                    if (this.careerId == 1)
                    {
                        s1 = "farmer";
                    }
                    else if (this.careerId == 2)
                    {
                        s1 = "fisherman";
                    }
                    else if (this.careerId == 3)
                    {
                        s1 = "shepherd";
                    }
                    else if (this.careerId == 4)
                    {
                        s1 = "fletcher";
                    }

                    break;

                case 1:
                    s1 = "librarian";
                    break;

                case 2:
                    s1 = "cleric";
                    break;

                case 3:
                    if (this.careerId == 1)
                    {
                        s1 = "armor";
                    }
                    else if (this.careerId == 2)
                    {
                        s1 = "weapon";
                    }
                    else if (this.careerId == 3)
                    {
                        s1 = "tool";
                    }

                    break;

                case 4:
                    if (this.careerId == 1)
                    {
                        s1 = "butcher";
                    }
                    else if (this.careerId == 2)
                    {
                        s1 = "leather";
                    }
            }

            if (s1 != null)
            {
                ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("entity.Villager." + s1, new Object[0]);
                chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
                chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
                return chatcomponenttranslation;
            }
            else
            {
                return super.getDisplayName();
            }
        }
    }

    public float getEyeHeight()
    {
        float f = 1.62F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 12)
        {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(particleType, this.X坐标 + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.Y坐标 + 1.0D + (double)(this.rand.nextFloat() * this.height), this.Z坐标 + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2, new int[0]);
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setProfession(this.worldObj.rand.nextInt(5));
        this.setAdditionalAItasks();
        return livingdata;
    }

    public void setLookingForHome()
    {
        this.isLookingForHome = true;
    }

    public 实体Villager createChild(实体Ageable ageable)
    {
        实体Villager entityvillager = new 实体Villager(this.worldObj);
        entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new 阻止位置(entityvillager)), (IEntityLivingData)null);
        return entityvillager;
    }

    public boolean allowLeashing()
    {
        return false;
    }

    public void onStruckByLightning(实体LightningBolt lightningBolt)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            实体Witch entitywitch = new 实体Witch(this.worldObj);
            entitywitch.setLocationAndAngles(this.X坐标, this.Y坐标, this.Z坐标, this.旋转侧滑, this.rotationPitch);
            entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new 阻止位置(entitywitch)), (IEntityLivingData)null);
            entitywitch.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }

            this.worldObj.spawnEntityInWorld(entitywitch);
            this.setDead();
        }
    }

    public InventoryBasic getVillagerInventory()
    {
        return this.villagerInventory;
    }

    protected void updateEquipmentIfNeeded(实体Item itemEntity)
    {
        ItemStack itemstack = itemEntity.getEntityItem();
        Item item = itemstack.getItem();

        if (this.canVillagerPickupItem(item))
        {
            ItemStack itemstack1 = this.villagerInventory.func_174894_a(itemstack);

            if (itemstack1 == null)
            {
                itemEntity.setDead();
            }
            else
            {
                itemstack.stackSize = itemstack1.stackSize;
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn)
    {
        return itemIn == Items.bread || itemIn == Items.potato || itemIn == Items.carrot || itemIn == Items.wheat || itemIn == Items.wheat_seeds;
    }

    public boolean func_175553_cp()
    {
        return this.hasEnoughItems(1);
    }

    public boolean canAbondonItems()
    {
        return this.hasEnoughItems(2);
    }

    public boolean func_175557_cr()
    {
        boolean flag = this.getProfession() == 0;
        return flag ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
    }

    private boolean hasEnoughItems(int multiplier)
    {
        boolean flag = this.getProfession() == 0;

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (itemstack != null)
            {
                if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3 * multiplier || itemstack.getItem() == Items.potato && itemstack.stackSize >= 12 * multiplier || itemstack.getItem() == Items.carrot && itemstack.stackSize >= 12 * multiplier)
                {
                    return true;
                }

                if (flag && itemstack.getItem() == Items.wheat && itemstack.stackSize >= 9 * multiplier)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (itemstack != null && (itemstack.getItem() == Items.wheat_seeds || itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot))
            {
                return true;
            }
        }

        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int i = inventorySlot - 300;

            if (i >= 0 && i < this.villagerInventory.getSizeInventory())
            {
                this.villagerInventory.setInventorySlotContents(i, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    static class EmeraldForItems implements 实体Villager.ITradeList
    {
        public Item sellItem;
        public 实体Villager.PriceInfo price;

        public EmeraldForItems(Item itemIn, 实体Villager.PriceInfo priceIn)
        {
            this.sellItem = itemIn;
            this.price = priceIn;
        }

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.price != null)
            {
                i = this.price.getPrice(random);
            }

            recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
        }
    }

    interface ITradeList
    {
        void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random);
    }

    static class ItemAndEmeraldToItem implements 实体Villager.ITradeList
    {
        public ItemStack buyingItemStack;
        public 实体Villager.PriceInfo buyingPriceInfo;
        public ItemStack sellingItemstack;
        public 实体Villager.PriceInfo field_179408_d;

        public ItemAndEmeraldToItem(Item p_i45813_1_, 实体Villager.PriceInfo p_i45813_2_, Item p_i45813_3_, 实体Villager.PriceInfo p_i45813_4_)
        {
            this.buyingItemStack = new ItemStack(p_i45813_1_);
            this.buyingPriceInfo = p_i45813_2_;
            this.sellingItemstack = new ItemStack(p_i45813_3_);
            this.field_179408_d = p_i45813_4_;
        }

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.buyingPriceInfo != null)
            {
                i = this.buyingPriceInfo.getPrice(random);
            }

            int j = 1;

            if (this.field_179408_d != null)
            {
                j = this.field_179408_d.getPrice(random);
            }

            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
        }
    }

    static class ListEnchantedBookForEmeralds implements 实体Villager.ITradeList
    {
        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
            int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;

            if (j > 64)
            {
                j = 64;
            }

            recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
        }
    }

    static class ListEnchantedItemForEmeralds implements 实体Villager.ITradeList
    {
        public ItemStack enchantedItemStack;
        public 实体Villager.PriceInfo priceInfo;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, 实体Villager.PriceInfo p_i45814_2_)
        {
            this.enchantedItemStack = new ItemStack(p_i45814_1_);
            this.priceInfo = p_i45814_2_;
        }

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.priceInfo != null)
            {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
            ItemStack itemstack1 = new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata());
            itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class ListItemForEmeralds implements 实体Villager.ITradeList
    {
        public ItemStack itemToBuy;
        public 实体Villager.PriceInfo priceInfo;

        public ListItemForEmeralds(Item par1Item, 实体Villager.PriceInfo priceInfo)
        {
            this.itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }

        public ListItemForEmeralds(ItemStack stack, 实体Villager.PriceInfo priceInfo)
        {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.priceInfo != null)
            {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(Items.emerald, 1, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            }
            else
            {
                itemstack = new ItemStack(Items.emerald, i, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }

            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
        }

        public int getPrice(Random rand)
        {
            return ((Integer)this.getFirst()).intValue() >= ((Integer)this.getSecond()).intValue() ? ((Integer)this.getFirst()).intValue() : ((Integer)this.getFirst()).intValue() + rand.nextInt(((Integer)this.getSecond()).intValue() - ((Integer)this.getFirst()).intValue() + 1);
        }
    }
}
