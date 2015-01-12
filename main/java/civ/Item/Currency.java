package civ.Item;

import java.util.ArrayList;
import java.util.List;

import civ.Core.CivMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Currency 
{
	private final static List<Currency> AllCurrencies = new ArrayList<Currency>();
	
	//Static Functions
	private static Currency CreateEmeraldCurrency()
	{
		ItemStack[] h = new ItemStack[] { 
				new ItemStack(Blocks.emerald_block, 1), 
				new ItemStack(Items.emerald, 9),
				new ItemStack(CivMod.EmeraldShard, 9), };
		
		return Currency.CreateCurrency(h);
	}	
	private static Currency CreateGoldCurrency()
	{
		ItemStack[] h = new ItemStack[] { 
				new ItemStack(Blocks.gold_block, 1), 
				new ItemStack(Items.gold_ingot, 9),
				new ItemStack(Items.gold_nugget, 9), };
		
		return Currency.CreateCurrency(h);
	}
	private static Currency CreateCurrency(ItemStack[] h)
	{		
		Currency c = new Currency(h);
	
		Currency.AllCurrencies.add(c);
		
		return c;
	}
	
	public static Currency EmeraldCurrency = Currency.CreateEmeraldCurrency();
	public static Currency GoldCurrency = Currency.CreateEmeraldCurrency();

	public static void InitializeDefaultCurrencies()
	{
		Currency.AllCurrencies.add(Currency.CreateEmeraldCurrency());
		Currency.AllCurrencies.add(Currency.CreateGoldCurrency());
	}
	public static Currency GetCurrency(ItemStack stack)
	{
		for (int i = 0; i < Currency.AllCurrencies.size(); i++)
		{
			Currency c = Currency.AllCurrencies.get(i);
			ItemStack[] h = c._hierarchy;
			for (int j = 0; j < h.length; j++)
			{
				if (h[j].isItemEqual(stack))
					return c;
			}
		}
		return null;
	}
	
	//Private Fields
	private ItemStack[] _hierarchy;
	
	//Getters
	public int HierarchyCount()
	{
		return this._hierarchy.length;
	}
	public ItemStack BaseWealth()
	{
		return this._hierarchy[this._hierarchy.length - 1];
	}
	public int NumberOfBaseWealthByItem(ItemStack stack)
	{
		ItemStack is = new ItemStack(stack.getItem());
		is.stackSize = 1;
		
		return this.GetBaseWealth(stack);
	}
	public boolean IsMatch(Currency c)
	{
		for (int i = 0;i < this._hierarchy.length; i++)
			if (!this._hierarchy[i].isItemEqual(c._hierarchy[i]))
				return false;
		return true;
	}
	public boolean ContainsItemMatch(ItemStack is)
	{
		for (int i = 0;i < this._hierarchy.length; i++)
			if (this._hierarchy[i].isItemEqual(is))
				return true;
		return false;
	}
	
	//Constructor
	public Currency(ItemStack[] hierarchy)
	{		
		this._hierarchy = hierarchy;
	}
	
	//Functions
	public boolean RemoveBaseWealthFrom2(IInventory inv, int count)
	{
		int iW = this.GetBaseWealth(inv);
		
		if (iW < count)
			return false; 
		
		ItemStack s = new ItemStack(this.BaseWealth().getItem());
		s.stackSize = count;
		if (InventoryManager.RemoveFromInventory(s, inv))
			return true;
		
		int i = 0;
		
		int cnt = count;
		while (true)
		{			
			i = InventoryManager.NextMatchingInventoryStacksIndex(this._hierarchy, inv, i);
			
			if (i == -1)
				break;
			
			if (i > inv.getSizeInventory())
				break;
			
			ItemStack cS = inv.getStackInSlot(i);
			int curBW = this.GetBaseWealth(inv.getStackInSlot(i));
			
			int amtToRemove = 0;
			
			double baseToCS = this.GetConversionFactor(cS, this.BaseWealth());
			
			int invToRem = (int)(curBW * baseToCS);
			
			cS.stackSize -= invToRem;
			count -= (invToRem / baseToCS);

			if (count == 0)
				return true;
			
			i++;
		}		
		
		count = cnt;
		while (true)
		{			
			i = InventoryManager.NextMatchingInventoryStacksIndex(this._hierarchy, inv, i);
			
			if (i == -1)
				break;
			
			if (i > inv.getSizeInventory())
				break;
			
			ItemStack cS = inv.getStackInSlot(i);
			int curBW = this.GetBaseWealth(cS);
			
			double baseToCS = this.GetConversionFactor(cS, this.BaseWealth());
			
			if (baseToCS > count)
			{
				//his.
			}
			
			int invToRem = (int)(curBW * baseToCS);
			
			cS.stackSize -= invToRem;
			count -= (invToRem / baseToCS);
			
			i++;
		}
		
		return false;
	}	
	public boolean RemoveBaseWealthFrom(IInventory inv, int count)
	{
		int iW = this.GetBaseWealth(inv);
		
		if (iW < count)
			return false; 
		
		boolean cont = true;		
		while (cont)
		{
			boolean passNoChange = true;
			
			for (int i = this._hierarchy.length - 1; i >= 0; i--)
			{
				double cf = this.GetConversionFactor(i);
				int curToRem = (int)(count / cf);
				int curBase = (int)(curToRem * cf);
				
				ItemStack is = this._hierarchy[i].copy();
				is.stackSize = curToRem;
				boolean success = InventoryManager.RemoveFromInventory(is, inv, true);
				
				if (success)
				{
					count -= curBase;
					//InventoryManager.RemoveFromInventory(is, inv, true);
					passNoChange = false;
				}
	
				if (count == 0)
					return true;
			}		
			
			//int cnt = 0;
			for (int i = this._hierarchy.length - 2; i >= 0; i--)
			{
				double cf = this.GetConversionFactor(i);
				//boolean cont2 = true;
				
				//while (cont2)
				//{
				//	cont2 = false;

				ItemStack is = new ItemStack(this._hierarchy[i].getItem());
				is.stackSize = 0;
				
				if (count < cf)
					is.stackSize = 1;					
				else
				{
					int ti = (int)(InventoryManager.TotalInventoryContained(is, inv) * cf);
					
					if (count < cf * ti)
						 continue;
					
					if (ti > count)						
						is.stackSize = (int)(1 + (count / cf));						
				}

				if (is.stackSize > 0)
				{
					InventoryManager.RemoveFromInventory(is, inv, true);
					ItemStack newBase = new ItemStack(this._hierarchy[i + 1].getItem());
					newBase.stackSize = (int)(cf + .001);
					InventoryManager.AddToInventory(newBase, inv);
					passNoChange = false;
					break;
				}
			}		
			
			if (count == 0)
				return true;
			
			if (passNoChange)
				break;
		}
		
		return false;
		
	}
	public double GetConversionFactor(ItemStack count, ItemStack conv)
	{
		if (count == null || conv == null)
			return 1;
		
		int i = 0;
		for (i = 0; i < this._hierarchy.length; i++)
			if (this._hierarchy[i].isItemEqual(count))
				break;
		
		int j = 0;
		for (j = 0; i < this._hierarchy.length; j++)
			if (this._hierarchy[j].isItemEqual(conv))
				break;
		
		if (i == j)
			return 1;
		
		double f = 1;
		for (int k = 0; k < Math.abs(j - i); k++)
		{
			int d = (int)Math.signum(j - i);
			//int i1 = i + k * (int)Math.signum(j - i);
			//int i2 = i1 + (int)Math.signum(j - i);
			int ii = i + (k + 1) * d;
			
			if (d > 0)
				f *= ii;
			else
				f /= ii;
		}
		
		return f;
	}
	public double GetConversionFactor(ItemStack is)
	{
		int i = 0;
		for (i = 0; i < this._hierarchy.length; i++)
			if (this._hierarchy[i].isItemEqual(is))
				break;
		
		return this.GetConversionFactor(i);
	}
	public double GetConversionFactor(int startLevel)
	{
		if (startLevel >= this._hierarchy.length - 1)
			return 1.0;
		
		double r = 1;
		for (int i = startLevel + 1; i < this._hierarchy.length; i++)
			r *= this._hierarchy[i].stackSize;
		
		return r;
	}
	public int GetBaseWealth(IInventory inv)
	{
		if (inv == null)
			return 0;
		
		int sum = 0;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
			sum += this.GetBaseWealth(inv.getStackInSlot(i));
		
		return sum;
	}
	public int GetBaseWealth(ItemStack[] stacks)
	{
		if (stacks == null)
			return 0;
		
		int sum = 0;
		for (int i = 0; i < stacks.length; i++)
			sum += this.GetBaseWealth(stacks[i]);
		
		return sum;
	}
	public int GetBaseWealth(ItemStack wealthStack)
	{
		if (wealthStack == null)
			return 0;
		
		//Find Matching
		int i = 0;
		boolean match = false;
		for (i = 0; i < this._hierarchy.length; i++)
		{
			if (this._hierarchy[i].isItemEqual(wealthStack))
			{
				match = true;	
				break;
			}
		}
		
		if (!match)
			return 0;
		
		double cf = this.GetConversionFactor(i);
		
		double r = cf * wealthStack.stackSize;
		
		return (int)r;
		
	}
	public int GetBaseWealth(ItemStack stack, int count)
	{
		if (stack == null || count == 0)
			return 0;
		
		ItemStack i = new ItemStack(stack.getItem());
		i.stackSize = count;
		return this.GetBaseWealth(i);
	}
}
