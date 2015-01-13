package civ.Merchant;

import civ.Item.Currency;
import civ.Item.InventoryManager;
import civ.Merchant.ItemDemand.DemandType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CurrencyDemand extends ItemDemand
{	
	private boolean _isDefault = false, _isCurrency = false;
	private Currency CurrencyDemanded;
	
	@Override
	public boolean isCurrency(Currency c)
	{
		return this.CurrencyDemanded != null && c != null && this.CurrencyDemanded.IsMatch(c);
	}
	@Override
	public boolean isCurrency()
	{
		return true;
	}
	@Override
	public boolean ContainsItemMatch(ItemStack is, boolean matchAll)
	{
		if (matchAll && this.CurrencyDemanded.HierarchyCount() > 1)
			return false;
		
		return this.CurrencyDemanded.ContainsItemMatch(is);
	}
	@Override
	public boolean ContainsItemMatch(ItemDemand id, boolean matchAll)
	{
		if (matchAll)
		{
			if (!id.isCurrency() && this.CurrencyDemanded.HierarchyCount() > 1)
				return false;
			
			return id.isCurrency(this.CurrencyDemanded);
		}
		
		if (id.ItemDemanded != null)
			return this.CurrencyDemanded.ContainsItemMatch(id.ItemDemanded);
		
		return false;
	}
	@Override
	public int GetCurrentMaxDemand(IInventory inv)
	{
		if (this.CurrencyDemanded != null)		
		{
			int a = super.GetCurrentMaxDemand(this.CurrencyDemanded.GetBaseWealth(inv));	
			return a + (int)(.5 + this.CurrencyDemanded.GetBaseWealth(inv) * ((this.RiskAverseDayCount - 1) / this.RiskAverseDayCount));
		}
		
		return 0;
	}
	@Override
	public Item GetItemDemanded()
	{
		if (this.MyDemandType == DemandType.Currency)
			return this.CurrencyDemanded.BaseWealth().getItem();
		
		return super.GetItemDemanded();
	}	
	@Override
	public int GetInventory(IInventory inv)
	{
		return this.CurrencyDemanded.GetBaseWealth(inv);
	}
	
	@Override
	public void AddConsumptionDemand(ItemStack is)
	{
		int bw = this.CurrencyDemanded.GetBaseWealth(is);
		
		this.CurrentDemandPerDay += bw;
	}
	
	@Override
	public NBTTagCompound WriteToTag(NBTTagCompound nbttagcompound1)
	{
		nbttagcompound1 = super.WriteToTag(nbttagcompound1);
        	
		nbttagcompound1 = this.CurrencyDemanded.BaseWealth().writeToNBT(nbttagcompound1);
		nbttagcompound1.setBoolean("isDefault", this._isDefault);
		nbttagcompound1.setBoolean("isCurrency", this._isCurrency);
		
		return nbttagcompound1;
	}
	
	public CurrencyDemand(NBTTagCompound nbt)
	{		
		super(nbt);
		
		if (this.ItemDemanded != null)
		{
			this.CurrencyDemanded = Currency.GetCurrency(this.ItemDemanded);
			this._isCurrency = nbt.getBoolean("isCurrency");
			this._isDefault = nbt.getBoolean("isDefault");
			this.ItemDemanded = null;
		}
	}
	public CurrencyDemand(Currency c, boolean isDefault)
	{
		super(DemandType.Currency, null);
		
		this._isDefault = isDefault;
		this._isCurrency = true;
		this.CurrencyDemanded = c;
	}
		
}
