package civ.Entity.AI.Job;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import civ.Core.CivMod;
import civ.TileEntity.TileEntityJobPost;

public abstract class AIArtisan extends AIWorker
{	
	public AIArtisan(EntityVillager ev, TileEntityJobPost te) 
	{
		super(ev, te);
		this._workSiteBlockTypes = new Block[] { Blocks.crafting_table, CivMod.JobPost };
	}
	
	@Override
	public Point3i NextWorkPosition() {
		
		List<Point3i> ps = this._myJobOwner.GetWorkSites(this._workSiteBlockTypes);	
		
		if (ps.size() == 0)
			return null;
		
		return ps.get(CivMod.RandomObj.nextInt(ps.size()));
	}

}
