package lykrast.mysticalwildlife.common.item;

import java.util.List;
import java.util.Random;

import lykrast.mysticalwildlife.common.entity.IBrushable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ItemBrush extends Item {
    public ItemBrush(int damage) {
        this.setMaxStackSize(1);
        this.setMaxDamage(damage);
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
    {
        if (entity instanceof IBrushable)
        {
        	IBrushable target = (IBrushable)entity;
            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (target.isBrushable(player, itemstack, entity.world, pos))
            {
                if (!entity.world.isRemote)
                {
                	List<ItemStack> drops = target.onBrushed(player, itemstack, entity.world, pos,
                            EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

                    if (!drops.isEmpty())
                    {
                        Random rand = new Random();
                        for(ItemStack stack : drops)
                        {
                            EntityItem ent = entity.entityDropItem(stack, 1.0F);
                            ent.motionY += rand.nextFloat() * 0.05F;
                            ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                            ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        }
                    }
                    itemstack.damageItem(1, entity);
                }
                else {
                	player.swingArm(hand);
                }
            }
            return true;
        }
        return false;
    }
	
	@Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
		if (enchantment == Enchantments.FORTUNE) return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
	
	@Override
    public int getItemEnchantability() {
        return 1;
    }
}
