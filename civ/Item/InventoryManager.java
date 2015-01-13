package civ.Item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import civ.TileEntity.TileEntityShopTable;

public class InventoryManager {

	//Static Helpers	
	public static int NextMatchingInventoryStackIndex(ItemStack stack, IInventory inv, int startIndex)
	{
		for (int i = startIndex; i < inv.getSizeInventory(); i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			
			//If both match null
			if (stack == null && slot == null)
				return i;
			
			//If only one null, since previous case occurred
			if (stack == null || slot == null)
				continue;
			
			//Handle Matching Items
			Item i1 = slot.getItem(), i2 = stack.getItem();
			if (Item.getIdFromItem(i1) == Item.getIdFromItem(i2))
				return i;
		}
		
		return -1;
	}
	public static int NextMatchingInventoryStackIndex(Class itemClass, IInventory inv, int startIndex)
	{
		if (inv == null)
			return -1;
		
		for (int i = startIndex; i < inv.getSizeInventory(); i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			
			//If both match null
			if (itemClass == null && slot == null)
				return i;
			
			//If only one null, since previous case occurred
			if (itemClass == null || slot == null)
				continue;
			
			//Handle Matching Items
			if (itemClass.isInstance(slot.getItem()))
				return i;
		}
		
		return -1;
	}
	public static int NextMatchingInventoryStacksIndex(ItemStack[] stacks, IInventory inv, int startIndex)
	{
		for (int i = startIndex; i < inv.getSizeInventory(); i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			
			for (int j = 0; j < stacks.length; j++)
			{
				ItemStack stack = stacks[j];
				
				//If both match null
				if (stack == null && slot == null)
					return i;
				
				//If only one null, since previous case occurred
				if (stack == null || slot == null)
					continue;
				
				//Handle Matching Items
				Item i1 = slot.getItem(), i2 = stack.getItem();
				if (Item.getIdFromItem(i1) == Item.getIdFromItem(i2))
					return i;
			}
		}
		
		return -1;
	}
	public static ItemStack NextMatchingInventoryStack(Class itemClass, IInventory inv)
	{
		int i = InventoryManager.NextMatchingInventoryStackIndex(itemClass, inv, 0);
		
		if (i == -1)
			return null;
		
		return inv.getStackInSlot(i);
	}
	
  	public static boolean HasInventorySpaceFor(ItemStack stack, IInventory inv)
  	{
  		int index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, 0);
  		int remSize = stack.stackSize;
  		
  		while (index != -1)
  		{
  			int spaceAv = inv.getStackInSlot(index).getMaxStackSize() - inv.getStackInSlot(index).stackSize;
  			
  			if (spaceAv >= remSize)
  				return true;
  			
  			if (spaceAv < remSize)
  				remSize = remSize - spaceAv;  		
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, index + 1); 			
  		}
  		
  		index = InventoryManager.NextMatchingInventoryStackIndex((ItemStack)null, inv, 0);
  		
  		while (index != -1)
  		{
  			int spaceAv = stack.getMaxStackSize();
  			
  			if (spaceAv >= remSize)
  				return true;
  			
  			if (spaceAv < remSize)
  				remSize = remSize - spaceAv;  		
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex((ItemStack)null, inv, index + 1); 			
  		}
  		
  		return false;
  	}
  	public static boolean ContainsInventory(ItemStack stack, IInventory inv, int count)
  	{
  		Currency c = Currency.GetCurrency(stack);
  		
  		if (c != null)
  			return c.GetBaseWealth(inv) > count;  		
  		
  		int index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, 0);
  		int remSize = count;
  		
  		while (index != -1)
  		{
  			int curAmt = inv.getStackInSlot(index).stackSize;
  			
  			if (curAmt >= remSize)
  				return true;
  			
  			if (curAmt < remSize)
  				remSize = remSize - curAmt;  		
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, index + 1); 			
  		}  	
  		
  		if (remSize > 0)
  			return false;
  		
  		return true;
  	}  	
  	public static boolean ContainsInventory(ItemStack stack, IInventory inv)
  	{
  		return InventoryManager.ContainsInventory(stack, inv, stack.stackSize);
  	}
  	public static boolean AddToInventory(ItemStack stack, IInventory inv)
  	{
  		int index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, 0);
  		int remSize = stack.stackSize;
  		
  		while (index != -1)
  		{
  			int spaceAv = inv.getStackInSlot(index).getMaxStackSize() - inv.getStackInSlot(index).stackSize;
  			
  			if (spaceAv >= remSize)
  			{
  				inv.getStackInSlot(index).stackSize += remSize;
  				return true;
  			}

  			if (spaceAv < remSize)
  			{
  				remSize = remSize - spaceAv;  
  				inv.getStackInSlot(index).stackSize += spaceAv;
  			}		
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, index + 1); 			
  		}
  		
  		index = InventoryManager.NextMatchingInventoryStackIndex((ItemStack)null, inv, 0);
  		
  		while (index != -1)
  		{
  			int spaceAv = stack.getMaxStackSize();

  			if (spaceAv >= remSize)
  			{
  				ItemStack newStack = ItemStack.copyItemStack(stack);
  				newStack.stackSize = remSize;
  				inv.setInventorySlotContents(index, newStack);
  				return true;
  			}

  			if (spaceAv < remSize)
  			{
  				remSize = remSize - spaceAv;  
  				ItemStack newStack = ItemStack.copyItemStack(stack);
  				newStack.stackSize = spaceAv;
  				inv.setInventorySlotContents(index, newStack);	
  				
  			}
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex((ItemStack)null, inv, index + 1); 			
  		}
  		
  		stack.stackSize = remSize;
  		return false;
  		
  		
  	}
  	public static boolean RemoveFromInventory(ItemStack stack, IInventory inv, boolean DisableCurrency)
  	{
  		int index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, 0);
  		List<Integer> indexes = new ArrayList<Integer>();
  		int remSize = stack.stackSize;
  		
  		Currency c = Currency.GetCurrency(stack);
  		if (!DisableCurrency && c != null)
  			return c.RemoveBaseWealthFrom(inv, c.GetBaseWealth(stack));
  		
  		while (index != -1)
  		{
  			int curAmt = inv.getStackInSlot(index).stackSize;
  			
  			if (curAmt > remSize)
  			{
  				remSize = 0;
  				indexes.add(index);
  				break;  				
  			}
  			if (curAmt == remSize)
  			{
  				remSize -= inv.getStackInSlot(index).stackSize;
  				indexes.add(index);
  				break;  				
  			}
  			
  			if (curAmt < remSize)
  			{
  				remSize -= curAmt;  
  				indexes.add(index);
  			}
  			
  	  		index = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, index + 1); 			
  		}  		
  		
  		if (remSize > 0)
  			return false;
  		
  		remSize = stack.stackSize;
  		for (int i = 0; i < indexes.size(); i++)
  		{
  			int curAmt = inv.getStackInSlot(indexes.get(i)).stackSize;
  			
  			if (curAmt > remSize)
  			{
  				inv.getStackInSlot(indexes.get(i)).stackSize -= remSize;
  				remSize = 0;
  				return true;  				
  			}
  			if (curAmt == remSize)
  			{
  				remSize -= inv.getStackInSlot(indexes.get(i)).stackSize;
  				inv.setInventorySlotContents(indexes.get(i), null);
  				return true;  				
  			}
  			
  			if (curAmt < remSize)
  			{
  				remSize -= curAmt;  
  				inv.setInventorySlotContents(indexes.get(i), null);
  			}  			
  		}
  		
  		if (remSize > 0)
  			return false;
  		
  		return true;
  	}
  	public static boolean RemoveFromInventory(ItemStack stack, IInventory inv)
  	{
  		return InventoryManager.RemoveFromInventory(stack, inv, true);
  	}
  	
  	public static int TotalInventoryContained(ItemStack stack, IInventory inv)
  	{
  		boolean cont = true;
  		int count = 0;
  		int i = -1;
  		while (cont)
  		{
  			i = InventoryManager.NextMatchingInventoryStackIndex(stack, inv, i + 1);
  			
  			if (i == -1)
  				break;
  			
  			if (stack != null)
  				count += inv.getStackInSlot(i).stackSize;
  			
  			i++;
  		}
  		
  		return count;
  	}  
  	public static int TotalEmptySlots(IInventory inv)
  	{
		int sum = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++)
			if (inv.getStackInSlot(i) == null)
				sum++;
		
		return sum;
  	}

}
