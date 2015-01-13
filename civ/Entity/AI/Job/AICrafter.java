package civ.Entity.AI.Job;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import civ.Core.CivMod;
import civ.Item.InventoryManager;
import civ.TileEntity.TileEntityJobPost;

public class AICrafter extends AIArtisan
{
	public static List<ItemStack> RecipeInputs(IRecipe rec)
	{
		if (ShapelessRecipes.class.isInstance(rec))
			return ((ShapelessRecipes)rec).recipeItems;
		
		if (ShapedRecipes.class.isInstance(rec))
		{
			ItemStack[] stacks = ((ShapedRecipes)rec).recipeItems;
			List<ItemStack> rl = new ArrayList<ItemStack>();
			for (int i = 0; i < stacks.length; i++)
				rl.add(stacks[i]);
			
			return rl;
		}
		
		return null;
	}
	public static IRecipe GetRecipeByOutput(ItemStack output)
	{
		for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++)
			if (((IRecipe)CraftingManager.getInstance().getRecipeList().get(i)).getRecipeOutput().isItemEqual(output))
				return ((IRecipe)CraftingManager.getInstance().getRecipeList().get(i));
		
		return null;
	}
	
	protected List<IRecipe> _myRecipes = new ArrayList<IRecipe>();
	//protected List<ItemStack> _myRecipeOutputs = new ArrayList<ItemStack>();
	//protected List<ItemStack[]> _myRecipeInputs = new ArrayList<ItemStack[]>();
	
	public AICrafter(EntityVillager ev, TileEntityJobPost te) 
	{
		super(ev, te);
		
		return;
	}

	@Override
	public boolean DoWorkOnPosition() {
		
		IInventory inv = this._myJobOwner.GetRepository();
		
		if (inv == null)
			return true;
		
		//Cycle through random recipes until one found
		int index = 0, max = 10;
		while (index < max)
		{
			IRecipe rec = this._myRecipes.get(CivMod.RandomObj.nextInt(this._myRecipes.size()));
			List<ItemStack> inputs = AICrafter.RecipeInputs(rec);

			while (true)
			{
				boolean fullBreak = false;
				for (int i = 0; i < inputs.size(); i++)
					if (!InventoryManager.ContainsInventory(inputs.get(i), inv))
					{
						fullBreak = true;
						break;
					}
				
				if (fullBreak)
					break;
				
				if (!InventoryManager.HasInventorySpaceFor(rec.getRecipeOutput(), inv))
					break;			
			
				boolean removeSuccess = false;
				for (int i = 0; i < inputs.size(); i++)
					removeSuccess = InventoryManager.RemoveFromInventory(inputs.get(i), inv);
				
				if (removeSuccess)
				{
					InventoryManager.AddToInventory(rec.getRecipeOutput(), inv);
					index = max;
				}
				
				break;
			}
			
			index++;
		}
		
		return true;
	}

}
