package civ.Container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShopTable extends Container
{
    private IInventory lowerChestInventory;
    private int numRows;
    private static final String __OBFID = "CL_00001742";

    public ContainerShopTable(IInventory par1IInventory, IInventory par2IInventory)
    {
        this.lowerChestInventory = par2IInventory;
        this.numRows = par2IInventory.getSizeInventory() / 12;
        par2IInventory.openInventory();
        int i = (this.numRows - 4) * 18;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j)
        {
        	int xOff = 0;
            for (k = 0; k < 12; ++k)
            {
            	if (k == 9)
            		xOff = 14;
            	
            	if (k == 11)
            		xOff = 21;
            	
                this.addSlotToContainer(new Slot(par2IInventory, k + j * 12, xOff + 8 + k * 18, 18 + j * 18));         
            }
        }

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(par1IInventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(par1IInventory, j, 8 + j * 18, 162 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.lowerChestInventory.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {                  	
    	ItemStack itemstack = null;    
	    Slot slot = (Slot)this.inventorySlots.get(par2);
	
	    if (slot != null && slot.getHasStack())
	    {
	        ItemStack itemstack1 = slot.getStack();
	        itemstack = itemstack1.copy();      
	    	if (par2 < this.numRows * 12)
		    {
		        if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
		        {
		            return null;
		        }
		    }
            else 
            {
            	for (int i = 0; i < 3; i++)
		        	if (this.mergeItemStack(itemstack1, (i * 12), (i * 12) + 9, false))
		        		break;
            	
            	return null;
            }
	    	
            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
	    }
	    
	    return itemstack;
    	/*
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.numRows * 12)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
        */
    	
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.lowerChestInventory.closeInventory();
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }
}

