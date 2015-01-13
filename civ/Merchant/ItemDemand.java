package civ.Merchant;

import civ.Item.Currency;
import civ.Item.InventoryManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemDemand {

	public static enum DemandType { Hunger, Shelter, Pleasure, Security, Currency }
	
	//protected int lastAmountDemanded2 = -1;
	//protected int lastInvCount = -1;
	
	protected DemandType MyDemandType;
	protected ItemStack ItemDemanded;
	protected double CurrentDemandPerDay = 0;
	protected double InventoryDemand = 0;
	protected double RiskAverseDayCount = 7;

	public boolean Update = true;
	
	public boolean matchesType(DemandType dt)
	{
		return dt == this.MyDemandType;
	}
	public boolean isCurrency(Currency c)
	{
		return false;
	}
	public boolean isCurrency()
	{
		return false;
	}
	public boolean ContainsItemMatch(ItemStack is, boolean matchAll)
	{
		if (is == null || this.ItemDemanded == null)
			return false;		
		
		if (is.isItemEqual(this.ItemDemanded))
			return true;
		
		return false;
	}
	public boolean ContainsItemMatch(ItemDemand id, boolean matchAll)
	{
		ItemStack is = id.ItemDemanded;
		
		if (is == null || this.ItemDemanded == null)
			return false;		
		
		if (is.isItemEqual(this.ItemDemanded))
			return true;
		
		return false;
	}
	public int GetInventory(IInventory inv)
	{
		if (this.MyDemandType == DemandType.Currency)
			return Currency.GetCurrency(new ItemStack(this.GetItemDemanded())).GetBaseWealth(inv);
		
		return InventoryManager.TotalInventoryContained(this.ItemDemanded, inv);
	}
	public Item GetItemDemanded()
	{
		if (this.ItemDemanded == null)
			return null;
		
		return this.ItemDemanded.getItem();
	}
	
	public MerchantTradeRecipe GenerateCurrentRecipe()
	{
		return null;
	}
	public double ItemTypePracticalValue()
	{
		if (this.MyDemandType == DemandType.Hunger)
		{
			ItemFood f = null;
			Item i = this.ItemDemanded.getItem();
			if (i instanceof ItemFood)			
				return (f = (ItemFood)i).func_150905_g(null);
		}
		
		return 1;
	}
	public double ItemTypeTimeValue(double currentInventory)
	{
		return 1.0;
	}
	public void AddConsumptionDemand(ItemStack is)
	{
		this.CurrentDemandPerDay += is.stackSize;
	}
	public int GetCurrentMaxDemand(int invCount)
	{
		//if (!this.Update || invCount != this.lastInvCount)
		//	return this.lastAmountDemanded;
		
		double numD = ((1 + RiskAverseDayCount) * (InventoryDemand + CurrentDemandPerDay));	
		
		//if (numD < 0)
		//	numD /= (1 + RiskAverseDayCount);
	
		this.Update = false;
		//this.lastAmountDemanded = (int)(numD + .5 * Math.signum(numD));
		//this.lastInvCount = invCount;
		
		return (int)(numD + .5 * Math.signum(numD));//this.lastAmountDemanded;
	}
	public int GetCurrentMaxDemand(IInventory inv)
	{
		if (this.ItemDemanded != null)		
			return this.GetCurrentMaxDemand(InventoryManager.TotalInventoryContained(this.ItemDemanded, inv));
		
		return 0;
	}
	public double EstimatePrice(double demand, double inv)
	{
		double val = this.ItemTypePracticalValue() * this.ItemTypeTimeValue(inv);
		return val * Math.exp(((demand - inv) / (demand)) - 1);
		//double cdx = demand - inv;
		//double cdx2 = (cdx * cdx) * Math.signum(cdx);
		//return this.ItemTypePracticalValue() * Math.exp(cdx2 / ((demand + 1) * (inv + 1)));
	}

	public NBTTagCompound WriteToTag(NBTTagCompound nbttagcompound1)
	{
        //NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        
        nbttagcompound1.setInteger("DemandType", this.MyDemandType.ordinal());
        
        //nbttagcompound1.setByte("Slot", (byte)index);
        
        if (this.ItemDemanded != null)
        	nbttagcompound1 = this.ItemDemanded.writeToNBT(nbttagcompound1);
        
        nbttagcompound1.setDouble("CurrentDemandPerDay", this.CurrentDemandPerDay);

        return nbttagcompound1;
	}
	
	public static ItemDemand CreateFromNBT(NBTTagCompound nbt)
	{
		DemandType dt = DemandType.values()[nbt.getInteger("DemandType")];
		
		if (dt == DemandType.Currency)
			return new CurrencyDemand(nbt);
		
		if (dt == DemandType.Hunger)
			return new ItemDemand(nbt);
		
		return null;
	}
	public ItemDemand(NBTTagCompound nbt)
	{				
		this.MyDemandType = DemandType.values()[nbt.getInteger("DemandType")];
		this.CurrentDemandPerDay = nbt.getDouble("CurrentDemandPerDay");
		this.ItemDemanded = ItemStack.loadItemStackFromNBT(nbt);
	}
	public ItemDemand(DemandType dt, ItemStack itemDemanded)
	{
		this.MyDemandType = dt;
		if (itemDemanded != null)
		{
			this.ItemDemanded = new ItemStack(itemDemanded.getItem());
			this.ItemDemanded.stackSize = 1;
		}
	}
	
	public void DecayCurrentDemand(double portOfDay)
	{
		this.CurrentDemandPerDay = this.CurrentDemandPerDay * (1 - (
				(RiskAverseDayCount / (RiskAverseDayCount + 1)) * portOfDay
						));
		return;
	}
}
