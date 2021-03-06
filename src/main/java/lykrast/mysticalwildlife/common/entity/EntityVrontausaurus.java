package lykrast.mysticalwildlife.common.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import lykrast.mysticalwildlife.common.init.ModPotions;
import lykrast.mysticalwildlife.common.init.ModSounds;
import lykrast.mysticalwildlife.common.util.LootUtil;
import lykrast.mysticalwildlife.common.util.ResourceUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

public class EntityVrontausaurus extends EntityFurzard {
    public static final ResourceLocation LOOT = ResourceUtil.getEntityLootTable("vrontausaurus");
    public static final ResourceLocation LOOT_BRUSH = ResourceUtil.getSpecialLootTable("brush_vrontausaurus");
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.BEEF, Items.COOKED_BEEF, Items.MUTTON, Items.COOKED_MUTTON);
	
	public EntityVrontausaurus(World worldIn)
	{
		super(worldIn);
        this.setSize(2.2F, 1.4F);
	}

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIPanic(1.2D));
        this.tasks.addTask(2, new AIAttackMeleeShortrange(this, 1.2D, false));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new AITempt(this, 1.2D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    }
    
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F))
        {
            if (entityIn instanceof EntityLivingBase)
            {
                int i = -1;

                if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    i = 0;
                }
                else if (this.world.getDifficulty() == EnumDifficulty.HARD)
                {
                    i = 1;
                }

                if (i >= 0)
                {
                    playSound(ModSounds.spark, 1.0F, 1.0F);
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(ModPotions.shocked, 60, i));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected void updateAITasks()
    {
    	if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
    	{
        	if (this.getAttackTarget() != null) this.setAttackTarget(null);
        	if (this.getRevengeTarget() != null) this.setRevengeTarget(null);
    	}
    	
    	super.updateAITasks();
    }

	@Override
	public boolean isBrushable(EntityPlayer player, ItemStack item, IBlockAccess world, BlockPos pos) {
		return !isChild() && isBrushable() && getAttackTarget() == null;
	}

	@Override
	public List<ItemStack> onBrushed(EntityPlayer player, ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		if (rand.nextInt(4) == 0) player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2.0F);

        playSound(ModSounds.brushing, 1.0F, 1.0F);
        playSound(ModSounds.spark, 1.0F, 1.0F);
        
        if (rand.nextInt(5) == 0) setBrushTimer(3600 + rand.nextInt(2401));
        
    	LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(LOOT_BRUSH);
		return loottable.generateLootForPools(rand, LootUtil.getBrushingContext(this, player, fortune));
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityVrontausaurus(world);
	}

	@Override
    protected SoundEvent getAmbientSound()
    {
        return ModSounds.lizardIdle;
    }

	@Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return ModSounds.lizardHurt;
    }

	@Override
    protected SoundEvent getDeathSound()
    {
        return ModSounds.lizardDeath;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }

	@Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
    	if (!world.isRaining()) return false;
        return TEMPTATION_ITEMS.contains(stack.getItem());
    }
    
    class AIPanic extends EntityAIPanic
    {
        public AIPanic(double speed)
        {
            super(EntityVrontausaurus.this, speed);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return !EntityVrontausaurus.this.isChild() && !EntityVrontausaurus.this.isBurning() ? false : super.shouldExecute();
        }
    }
    
    private static class AIAttackMeleeShortrange extends EntityAIAttackMelee {

		public AIAttackMeleeShortrange(EntityCreature creature, double speedIn, boolean useLongMemory) {
			super(creature, speedIn, useLongMemory);
		}

		@Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return (double)(9.0F + attackTarget.width);
        }
    	
    }
    
    private static class AITempt extends EntityAITempt {
    	//Private in the super class, damn it
    	private EntityCreature tempted;
    	
		public AITempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<Item> temptItemIn) {
			super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, temptItemIn);
			tempted = temptedEntityIn;
		}

		@Override
	    public boolean shouldExecute() {
			if (!tempted.world.isRaining()) return false;
			return super.shouldExecute();
		}
    }

}
