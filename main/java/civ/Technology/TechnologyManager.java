package civ.Technology;

import java.util.ArrayList;
import java.util.List;

import civ.Core.CivMod;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class TechnologyManager {

	private static List<List<String>> craftPlayersAllowed = new ArrayList<List<String>>();
	
	public static void InitializeFarmingAchievements()
	{
		Achievement gathering = new Achievement("achievement.gathering", "gathering", -2, -6, Items.carrot, null);
		LanguageRegistry.instance().addStringLocalization("achievement.gathering", "en_US", "Gathering");
		LanguageRegistry.instance().addStringLocalization("achievement.gathering.desc", "en_US", "Villagers can gather and replant wheat, carrots, and potatoes.");		

		Achievement hunting = new Achievement("achievement.hunting", "hunting", -6, -6, Items.feather, null);
		LanguageRegistry.instance().addStringLocalization("achievement.hunting", "en_US", "Hunting");
		LanguageRegistry.instance().addStringLocalization("achievement.hunting.desc", "en_US", "Villagers can hunt small game.");

		Achievement flintTools = new Achievement("achievement.flintworking", "flintworking", 2, -6, Items.flint, null);
		LanguageRegistry.instance().addStringLocalization("achievement.flintworking", "en_US", "Flint Working");
		LanguageRegistry.instance().addStringLocalization("achievement.flintworking.desc", "en_US", "Villagers can create and use flint tools.");
		
		AchievementPage ap = new AchievementPage("Technology", //new Achievement[] { 
				gathering, hunting, flintTools 
				//}
		);
		
		for (int i = 0; i < ap.getAchievements().size(); i++)
			ap.getAchievements().get(i).registerStat();
		
		AchievementPage.registerAchievementPage(ap);
		
		
	}
	
	public static void CollectCraftingRecipes()
	{
		CraftingManager cm = CraftingManager.getInstance();
		List<IRecipe> recipes = cm.getRecipeList();
		
		for (int i = 0; i < recipes.size(); i++)
		{
			recipes.set(i, new TechCraftRecipe(recipes.get(i)));
			TechnologyManager.craftPlayersAllowed.add(new ArrayList<String>());
		}
	}
	
	public static boolean PlayerHasRecipeTechnology(IInventory ep, IRecipe ir)
	{
		//NBTTagCompound nbt = ep.getEntityData();
		//if (ep.getEn)
		return true;
	}
	
}
