package civ.Container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot
{
	private Class[] _allowableItems;
	
    public CustomSlot(IInventory par1IInventory, Class[] allowableItems, int par2, int par3, int par4)
    {
    	super(par1IInventory, par2, par3, par4);
    	
    	this._allowableItems = allowableItems;
    }
    
    public boolean isItemValid(ItemStack par1ItemStack)
    {
    	if (this._allowableItems == null || this._allowableItems.length == 0)    	
    		return true;
    	    	
    	for (int i = 0; i < this._allowableItems.length; i++)
    		if (this._allowableItems[i].isInstance(par1ItemStack.getItem()))
    			return true;
    	
    	return false;
    }
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return true;
    }  

    public void onSlotChanged()
    {
        super.onSlotChanged();
    }
}
