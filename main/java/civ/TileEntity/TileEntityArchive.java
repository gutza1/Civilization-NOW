package civ.TileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityArchive extends TileEntity implements IInventory
{
	//Fields
    private int numPlayersUsing = 0;
    protected ItemStack[] chestContents = new ItemStack[0];
    protected String myOwner = "", customName;
	
	//Storage Functions   
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
    public boolean hasCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }
    public int getSizeInventory()
    {
        return 0;
    }    
    public int getInventoryStackLimit()
    {
       return 64;
    }
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.customName : "Merchant Table";
    }
    public ItemStack getStackInSlot(int par1)
    {
        return this.chestContents[par1];
    }   
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack; 

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2); 

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
 
        this.markDirty();
              
    }
    public void openInventory()
    {
        if (this.numPlayersUsing < 0)
        {
            this.numPlayersUsing = 0;
        }

        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }
    public void closeInventory()
    {
        //if (this.getBlockType() instanceof ShopPost)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());

            if (this.worldObj.isRemote)
            {
            	//NBTTagCompound nbtTag = new NBTTagCompound();
            	//this.writeToNBT(nbtTag);
            	//CivMod.network.sendToServer(new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag));  
            }
            
        }
    }

}
