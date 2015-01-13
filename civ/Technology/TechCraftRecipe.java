package civ.Technology;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class TechCraftRecipe implements IRecipe
{	
	protected IRecipe _original;
	
	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		
		//if (!var2.isRemote || TechnologyManager.PlayerHasTechnology(this._original))
		
		return this._original.matches(var1, var2);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return this._original.getCraftingResult(var1);
	}

	@Override
	public int getRecipeSize() {
		return this._original.getRecipeSize();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this._original.getRecipeOutput();
	}

	public TechCraftRecipe(IRecipe original)
	{
		this._original = original;
	}
	
}
