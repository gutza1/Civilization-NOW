package civ.Entity.AI.Job;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import civ.Core.CivMod;
import civ.Item.InventoryManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class AIAnimalHerder extends AIWorker
{
	protected Entity _entityToWork = null;
	protected Class[] _pickupItemClasses = new Class[] {
			ItemFood.class, Items.bone.getClass(), Items.egg.getClass(), Items.feather.getClass(), 
			Item.getItemFromBlock(Blocks.wool).getClass(), Items.leather.getClass() };
	
	public AIAnimalHerder(EntityVillager ev, IJobOwner te)
	{
		super(ev, te);
	} 

	@Override
	public Point3i NextWorkPosition() {
			  	
		int x = (int)(this._myVillager.posX), y = (int)(this._myVillager.posY), z = (int)(this._myVillager.posZ), dist = 8;
  		int xs = x - dist, xf = x + dist;
  		int ys = y - dist, yf = y + dist;
  		int zs = z - dist, zf = z + dist;
  		
  		this._entityToWork = null;
  		List<Entity> list = this._myVillager.worldObj.getEntitiesWithinAABB(IMerchant.class, AxisAlignedBB.getBoundingBox(xs, ys, zs, xf, yf, zf));
  		
  		int index = 0;
  		while (index < 10)
  		{
  			Entity e = list.get(CivMod.RandomObj.nextInt(list.size()));
  			boolean go = true;
  			  			
  			while (true)
  			{
	  			if (EntityItem.class.isInstance(e))
	  			{
	  				EntityItem eItem = ((EntityItem)e);
	  				
	  				for (int i = 0; i < this._pickupItemClasses.length; i++)
	  					if (this._pickupItemClasses[i].isInstance(eItem.getEntityItem().getItem()))
	  						break;
	  				
	  			}
	  			
	  			if (EntitySheep.class.isInstance(e) && 
	  					((EntitySheep)e).isShearable(null, null, 0, 0, 0) &&
	  					InventoryManager.ContainsInventory(new ItemStack(Items.shears), this._myJobOwner.GetRepository()))
	  				break;
	  			
	  			go = false;
	  			break;
  			}  			

  			if (go)
  			{
				this._entityToWork = e;
				return new Point3i((int)e.posX, (int)e.posY, (int)e.posZ); 
  			}
  		}
  		
  		return null;
	}

	@Override
	public boolean DoWorkOnPosition()
	{		
		if (this._entityToWork == null)
			return true;
		
		List<ItemStack> drops = new ArrayList<ItemStack>();
		
		if (EntityItem.class.isInstance(this._entityToWork))
		{
			EntityItem eItem = ((EntityItem)this._entityToWork);
			boolean collect = false;

				for (int i = 0; i < this._pickupItemClasses.length; i++)
					if (this._pickupItemClasses[i].isInstance(eItem.getEntityItem().getItem()))
						collect = true;
			
			if (collect)
			{
				drops.add(eItem.getEntityItem());
				this._entityToWork.setDead();				
			}
			
		}
		
		//if (EntityChicken.class.isInstance(e))
			//return new Point3i((int)e.posX, (int)e.posY, (int)e.posZ);
		
		if (EntitySheep.class.isInstance(this._entityToWork) && 
				((EntitySheep)this._entityToWork).isShearable(null, null, 0, 0, 0) &&
				InventoryManager.ContainsInventory(new ItemStack(Items.shears), this._myJobOwner.GetRepository()))
		{
			drops.addAll(((EntitySheep)this._entityToWork).onSheared(null, null, 0, 0, 0, 0));
			boolean toolUse = this.UseCurrentTool(Items.shears.getClass());
		}

		if (drops.size() > 0)
			this._myJobOwner.intakeWorkerProduct(drops);
		
		this._entityToWork = null;
		return true;
	}

}
