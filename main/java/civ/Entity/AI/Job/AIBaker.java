package civ.Entity.AI.Job;

import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import civ.Core.CivMod;
import civ.TileEntity.TileEntityJobPost;

public class AIBaker extends AICrafter
{

	public AIBaker(EntityVillager ev, TileEntityJobPost te) 
	{
		super(ev, te);

		this._workSiteBlockTypes = new Block[] { Blocks.crafting_table, Blocks.furnace };
		
		//Bread Recipe		
		this._myRecipes.add(AICrafter.GetRecipeByOutput(new ItemStack(Items.bread)));
	}

}
