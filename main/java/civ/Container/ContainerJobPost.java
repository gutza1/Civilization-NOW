package civ.Container;

import civ.Core.CivMod;
import civ.Item.Document.JobDocument;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ContainerJobPost extends Container
{
    private IInventory lowerChestInventory;
    private int numRows;
    private static final String __OBFID = "CL_00001742";

    public ContainerJobPost(IInventory par1IInventory, IInventory par2IInventory)
    {
        this.lowerChestInventory = par2IInventory;
        this.numRows = 2;//1;//par2IInventory.getSizeInventory() / 12;
        par2IInventory.openInventory();
        int j;
        int k;
        int curSlotIndex = 0;
        
        for (j = 0; j < 2; j++)
        {
            for (k = 0; k < 8; k++)
            {            	
            	Slot ns = null;    
            	
            	int x = 12 + k * 18;
            	int y = 11 + j * 27;
            	
            	if (k == 0 && j == 0)   
            	{
            		ns = new CustomSlot(par2IInventory, new Class[] { JobDocument.class },
            				//{ CivMod.FarmContract.getClass(), CivMod.LumberContract.getClass(), 
            				//CivMod.QuarryContract.getClass(), CivMod.CaravanContract.getClass() }, 
            			curSlotIndex, x - 4, y);  
            	}
        		
	        	if (k == 0 && j == 1)
	        	{
	        		ns = new CustomSlot(par2IInventory, new Class[] { CivMod.LocationDocument.getClass() }, 
	        			curSlotIndex, x - 4, y); 
	        	}

            	int xOff = 5, yOff = 0; 
            	if (ns == null && k < 7)
            	{               	
            		if (j == 0)
            			yOff = 5;
            		else
            			yOff = -4;
            		
            		ns = new CustomSlot(par2IInventory, new Class[] { ItemHoe.class, ItemAxe.class, ItemPickaxe.class, ItemTool.class,
            				CivMod.LocationDocument.getClass(), CivMod.IdentityDocument.getClass() },
            				curSlotIndex, x + xOff, y + yOff);   
            	}

            	if (ns == null)
            		ns = new Slot(par2IInventory, curSlotIndex, x + xOff + 9, y);
            	
                this.addSlotToContainer(ns);      
                curSlotIndex++;
            }
        }

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(par1IInventory, k + j * 9 + 9, 8 + k * 18, 65 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(par1IInventory, j, 8 + j * 18, 123));
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

            if (par2 < this.numRows * 9)
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

