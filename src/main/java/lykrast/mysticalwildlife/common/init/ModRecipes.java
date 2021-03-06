package lykrast.mysticalwildlife.common.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber
public class ModRecipes {
	public static final String meatRaw = "listAllmeatraw",
			meatCooked = "listAllmeatcooked",
			leather = "leather";
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		initSmelting();
	}
	
	public static void initOreDict()
	{
		//Meat
		OreDictionary.registerOre(meatRaw, ModItems.vrontausaurusRaw);
		OreDictionary.registerOre(meatCooked, ModItems.vrontausaurusCooked);
		OreDictionary.registerOre(meatRaw, ModItems.yagaHogRaw);
		OreDictionary.registerOre(meatCooked, ModItems.yagaHogCooked);
		OreDictionary.registerOre(meatRaw, ModItems.duskLurkerRaw);
		OreDictionary.registerOre(meatCooked, ModItems.duskLurkerCooked);
		OreDictionary.registerOre(meatRaw, ModItems.cicapteraRaw);
		OreDictionary.registerOre(meatCooked, ModItems.cicapteraCooked);
		OreDictionary.registerOre(meatRaw, ModItems.plumperRaw);
		OreDictionary.registerOre(meatCooked, ModItems.plumperCooked);
		OreDictionary.registerOre(meatRaw, ModItems.krillRaw);
		OreDictionary.registerOre(meatCooked, ModItems.krillCooked);
		
		//Leather
		OreDictionary.registerOre(leather, ModItems.vrontausaurusFur);
		OreDictionary.registerOre(leather, ModItems.duskLurkerFur);
		
		//Dye
		OreDictionary.registerOre("dyeBlack", ModItems.duskAsh);
		OreDictionary.registerOre("dyeBlue", new ItemStack(ModItems.cicapteraHusk, 1, 0));
		OreDictionary.registerOre("dyeGreen", new ItemStack(ModItems.cicapteraHusk, 1, 1));
		OreDictionary.registerOre("dyeRed", new ItemStack(ModItems.cicapteraHusk, 1, 2));
		OreDictionary.registerOre("dyeYellow", new ItemStack(ModItems.cicapteraHusk, 1, 3));
		OreDictionary.registerOre("dyeWhite", new ItemStack(ModItems.cicapteraHusk, 1, 4));
		
		//Other
		OreDictionary.registerOre("dustAsh", ModItems.duskAsh);
		OreDictionary.registerOre("ash", ModItems.duskAsh);
		
		OreDictionary.registerOre("materialPressedwax", ModItems.plumperBlubber);
		OreDictionary.registerOre("wax", ModItems.plumperBlubber);
		OreDictionary.registerOre("tallow", ModItems.plumperBlubber);
	}
	
	public static void initSmelting()
	{
		GameRegistry.addSmelting(ModItems.vrontausaurusRaw, new ItemStack(ModItems.vrontausaurusCooked), 0.35F);
		GameRegistry.addSmelting(ModItems.yagaHogRaw, new ItemStack(ModItems.yagaHogCooked), 0.35F);
		GameRegistry.addSmelting(ModItems.duskLurkerRaw, new ItemStack(ModItems.duskLurkerCooked), 0.35F);
		GameRegistry.addSmelting(ModItems.cicapteraRaw, new ItemStack(ModItems.cicapteraCooked), 0.35F);
		GameRegistry.addSmelting(ModItems.plumperRaw, new ItemStack(ModItems.plumperCooked), 0.35F);
		GameRegistry.addSmelting(ModItems.krillRaw, new ItemStack(ModItems.krillCooked), 0.35F);
	}
}
