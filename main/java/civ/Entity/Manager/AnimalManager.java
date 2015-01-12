package civ.Entity.Manager;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import civ.Core.CivMod;

public class AnimalManager {

	public static double UpdateTickProbablity = .01;
	public static int UpdateTickWait = 100, UpdateRepTickWait = 36000, TicksToAdult = 48000,
			RepPotentialReq = 5;
		
	public static double HungerCostPerDay = 2.5;
	public static float StarveDamagePerDay = 10.0f;
	
	//Core Update
	public static void HandleAnimalUpdate(EntityAgeable ev)
	{
		if (CivMod.RandomObj.nextDouble() > AnimalManager.UpdateTickProbablity)
			return;
		
		NBTTagCompound ed = ev.getEntityData();
		
		if (!ed.hasKey("lastTick"))
			ed = AnimalManager.SetVillagerNBTToDefault(ev);
		
		long lastTick = ed.getLong("lastTick");
		
		long curTime = ev.worldObj.getWorldTime();
		long curWaited = Math.abs(curTime - lastTick);
		
		if (curWaited < AnimalManager.UpdateTickWait)
			return;
		
		ed.setLong("lastTick", curTime);
		
		double PortionOfDay = ((curWaited % 24000) * 1.0) / 24000.0;
		
		AnimalManager.HandleHungerUpdate(ev, ed, PortionOfDay);
		
		AnimalManager.HandleReproductionUpdate(ev, ed);
	}
	
	//Hunger Functions
	public static void HandleReproductionUpdate(EntityAgeable ev, NBTTagCompound ed)
	{	
		if (!ev.isChild())
			return;
		long lastRepTick = ed.getLong("lastRepTick");
		long curTime = ev.worldObj.getWorldTime();
		long curWaited = Math.abs(curTime - lastRepTick);
		
		if (curWaited < AnimalManager.UpdateRepTickWait || ed.getDouble("repPot") < AnimalManager.RepPotentialReq)
			return;
		
		ed.setLong("lastRepTick", curTime);
		ed.setDouble("repPot", 0);
		
		if (EntityVillager.class.isInstance(ev))
			((EntityVillager)ev).setMating(true);
		if (EntityAnimal.class.isInstance(ev))
			((EntityAnimal)ev).func_146082_f(null);
		
		
	}
	public static void HandleHungerUpdate(EntityAgeable ev, NBTTagCompound ed, double portionOfDay)
	{
		//Create variables
		double curHunger = ed.getDouble("hunger");
		
		//Get current change in hunger
		double delHunger = HungerCostPerDay * portionOfDay;		
		
		//Handle Reproduction && Healing
		if (curHunger > 20)
		{
			ed.setDouble("repPot", ed.getDouble("repPot") + delHunger);
			ev.heal((float)(curHunger - 19));
		}
		
		//Change hunger
		if (curHunger > 0)
			curHunger -= delHunger;
		
		//Handle Negative Hunger
		if (curHunger < 0)
			curHunger = 0;
		
		//Handle Starvation
		if (curHunger == 0)
			ev.attackEntityFrom(DamageSource.starve, StarveDamagePerDay * (float)portionOfDay + 1);
		
		//Set Hunger
		ed.setDouble("hunger", curHunger);
	}
	public static boolean AddHunger(EntityAgeable ev, float hungerFill)
	{
		NBTTagCompound ed = ev.getEntityData();
		double curHunger = ed.getDouble("hunger");
		
		if (curHunger < 20)
		{
			ed.setDouble("hunger", curHunger + hungerFill);			
			return true;
		}

		return false;			
	}	
	public static boolean Reproduce(EntityAgeable v1, EntityVillager v2)
	{
		if (EntityVillager.class.isInstance(v1))
		{
			if (!((EntityVillager)v1).isMating() || !((EntityVillager)v2).isMating())
				return false;
		}
		if (EntityAnimal.class.isInstance(v1))
			{ return false; }
		//{
		//	if (!((EntityAnimal)v1).isInLove() || !((EntityAnimal)v2).isInLove())
		//		return false;
		//}
		
		EntityAgeable child = v1.createChild(null);
		/*if (EntityVillager.class.isInstance(v1))
			((EntityVillager)v1).createChild(null);
		else if (EntityAnimal.class.isInstance(v1))
			((EntityAnimal)v1).createChild(null);*/
		
        child.copyLocationAndAnglesFrom(v1);
        //child.setGrowingAge(-1 * AnimalManager.TicksToAdult);
        v1.worldObj.spawnEntityInWorld(child);
        
        ((EntityVillager)v1).setMating(false);
        ((EntityVillager)v2).setMating(false);
		//if (EntityVillager.class.isInstance(ev))
		//	((EntityVillager)ev).setMating(true);
		//if (EntityAnimal.class.isInstance(ev))
		//	((EntityAnimal)ev).func_146082_f(null);
		
		return true;
	}
	
	public static NBTTagCompound SetVillagerNBTToDefault(EntityAgeable ev)
	{	
		NBTTagCompound ed = ev.getEntityData();
		
		ed.setLong("lastTick", Long.MIN_VALUE / 2);
		ed.setLong("lastRepTick", Long.MIN_VALUE / 2);
		ed.setDouble("hunger", 20.0);
		ed.setDouble("repPot", 0);
		
		return ed;
	}
	
	
}
