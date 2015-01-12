package civ.TileEntity;

import java.util.ArrayList;
import java.util.List;

import civ.Block.BlockShopTable;
import civ.Core.CivMod;
import civ.Merchant.IEconMerchant;
import civ.Merchant.MerchantTradeRecipe;
import civ.Merchant.MerchantTradeRecipeList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TileEntityShopTable extends TileEntity implements IEconMerchant, IInventory
{
	//Static Helpers	
	public static int NextEmptyInventoryStackIndex(TileEntityShopTable temt, int startIndex, boolean includeTrades)
	{
		int x = 0;
		if (includeTrades)
		{
			x = 3;
		}
		
		int ai = startIndex % 12;
		int aj = (int)(startIndex / 12.0d);
		
		for (int i = ai; i < 9 + x; i++)
			for (int j = aj; j < 6; j++)
			{
				ItemStack i1 = temt.chestContents[i + j * 12];
				
				if (i1 == null)
				{
					return i + j * 12;
				}
			}
		
		return -1;
	}	
	public static int NextMatchingInventoryStackIndex(ItemStack type, TileEntityShopTable temt, int startIndex, boolean includeTrades)
	{
		int x = 0;
		if (includeTrades)
		{
			x = 3;
		}
		
		int ai = startIndex % 12;
		int aj = (int)(startIndex / 12.0d);
		
		for (int i = ai; i < 9 + x; i++)
			for (int j = aj; j < 6; j++)
			{
				ItemStack i1 = temt.chestContents[i + j * 12];
				
				if (i1 == null)
				{
					continue;
				}
				
				String s1 = i1.getDisplayName();
				String s2 = type.getDisplayName();
				
				if (s1.matches(s2))
				{
					return i + j * 12;
				}
			}
		
		return -1;
	}	
	public static int NextMatchingInventoryStackIndex(ItemStack type, TileEntityShopTable temt, boolean includeTrades)
	{
		return TileEntityShopTable.NextMatchingInventoryStackIndex(type, temt, 0, includeTrades);
	}
	public static int NextMatchingSaleStackIndex(ItemStack type, TileEntityShopTable temt)
	{		
		//for (int i = 9; i < 12; i++)
			for (int j = 0; j < 6; j++)
				if (temt.chestContents[11 + j * 12].getDisplayName().matches(type.getDisplayName()))
					return 11 + j * 12;	
		
		return -1;
	}	
	public static int NextMatchingTradeBySaleIndex(MerchantRecipe mr, TileEntityShopTable temt)
	{		
		//for (int i = 9; i < 12; i++)
			for (int j = 0; j < 6; j++)
			{
				ItemStack buy1 = temt.chestContents[9 + j * 12];
				ItemStack buy2 = temt.chestContents[10 + j * 12];
				ItemStack sell = temt.chestContents[11 + j * 12];
				
				if (buy1 == null || sell == null)
					continue;
				
				String b1 = buy1.getDisplayName();
				String s1 = sell.getDisplayName();
				String b2 = null;
				
				if (buy2 != null)
					b2 = buy2.getDisplayName();
				
				/*if (buy1.getDisplayName() == mr.getItemToBuy().getDisplayName() && 
					(!mr.hasSecondItemToBuy() || temt.chestContents[10 + j * 12].getDisplayName() == mr.getSecondItemToBuy().getDisplayName()) &&
					temt.chestContents[11 + j * 12].getDisplayName() == mr.getItemToSell().getDisplayName()						
						)
					return j;*/
				
				String x1 = mr.getItemToBuy().getDisplayName();
				String x2 = null;
				String x3 = mr.getItemToSell().getDisplayName();
				
				if (b2 != null)
					x2 = mr.getSecondItemToBuy().getDisplayName();
				
				boolean bl1 = b1.matches(x1);
				boolean bl2 = (b2 == null || b2.matches(x2));
				boolean bl3 = s1.matches(x3);
				
				if (bl1 && bl2 && bl3)
					return j;	
			}
		
		return -1;
	}
		
	//Fields
    private int numPlayersUsing = 0;
    private ItemStack[] chestContents = new ItemStack[72];
    
    //Merchant Fields
	protected MerchantTradeRecipeList _myTrades = new MerchantTradeRecipeList(6);
	protected String myOwner = "", customName, lockValue;
    protected EntityPlayer buyingPlayer;
    protected IMerchant buyingMerchant;
	
	//Getters
	public boolean HasOwner()
	{
		return this.myOwner != null && this.myOwner != "";
	}
	public void setOwner(String newOwner)
	{
		this.myOwner = newOwner;
	}
	public String getOwner()
	{
		return this.myOwner;
	}
	public Block getBlockType()
	{
		return CivMod.ShopTable;
	}
	public int NumEmptyInventorySlots()
	{
		int r = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 6; j++)
				if (this.chestContents[i + j * 12] == null)
					r++;
		
		return r;
	}
	public String getLockValue()
	{
		return this.lockValue;
	}
	
	public TileEntityShopTable()
	{
		super();
		
		this.blockType = CivMod.ShopTable;			
	}
	
	//NBT
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        
        //Merchant NBT
        par1NBTTagCompound.setString("merchantOwner", this.myOwner);
        
        if (this.lockValue != null)
        	par1NBTTagCompound.setString("lockValue", this.lockValue);
        
        if (this._myTrades != null)
        {
            par1NBTTagCompound.setTag("Offers", this._myTrades.getRecipiesAsTags());
        }
        
        //Storage NBT
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        	par1NBTTagCompound.setString("CustomName", this.customName);
    }
  	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.myOwner = par1NBTTagCompound.getString("merchantOwner");
        
        if (par1NBTTagCompound.hasKey("lockValue"))
        	this.lockValue = par1NBTTagCompound.getString("lockValue");

        //Merchant NBT
        if (par1NBTTagCompound.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("Offers");
            this._myTrades = new MerchantTradeRecipeList(nbttagcompound1);
        }
        //Storage NBT
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if (par1NBTTagCompound.hasKey("CustomName", 8))
            this.customName = par1NBTTagCompound.getString("CustomName");
    }

  	//Vanilla-Civ Functions
  	public boolean HasInventorySpaceFor(ItemStack stack)
  	{
  		int index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, 0, false);
  		int remSize = stack.stackSize;
  		
  		while (index != -1)
  		{
  			int spaceAv = this.chestContents[index].getMaxStackSize() - this.chestContents[index].stackSize;
  			
  			if (spaceAv >= remSize)
  				return true;
  			
  			if (spaceAv < remSize)
  				remSize = remSize - spaceAv;  		
  			
  	  		index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, index + 1, false); 			
  		}
  		
  		index = TileEntityShopTable.NextEmptyInventoryStackIndex(this, 0, false);
  		
  		while (index != -1)
  		{
  			int spaceAv = stack.getMaxStackSize();
  			
  			if (spaceAv >= remSize)
  				return true;
  			
  			if (spaceAv < remSize)
  				remSize = remSize - spaceAv;  		
  			
  	  		index = TileEntityShopTable.NextEmptyInventoryStackIndex(this, index + 1, false); 			
  		}
  		
  		return false;
  	}
  	public boolean ContainsInventory(ItemStack stack, int count)
  	{
  		int index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, 0, false);
  		int remSize = stack.stackSize;
  		
  		while (index != -1)
  		{
  			int curAmt = this.chestContents[index].stackSize;
  			
  			if (curAmt >= remSize)
  				return true;
  			
  			if (curAmt < remSize)
  				remSize = remSize - curAmt;  		
  			
  	  		index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, index + 1, false); 			
  		}  	
  		
  		if (remSize > 0)
  			return false;
  		
  		return true;
  	}  	
  	public boolean ContainsInventory(ItemStack stack)
  	{
  		return this.ContainsInventory(stack, stack.stackSize);
  	}
  	public boolean AddToInventory(ItemStack stack)
  	{
  		int index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, 0, false);
  		int remSize = stack.stackSize;
  		
  		while (index != -1)
  		{
  			int spaceAv = this.chestContents[index].getMaxStackSize() - this.chestContents[index].stackSize;
  			
  			if (spaceAv >= remSize)
  			{
  				this.chestContents[index].stackSize += remSize;
  				return true;
  			}

  			if (spaceAv < remSize)
  			{
  				remSize = remSize - spaceAv;  
  				this.chestContents[index].stackSize += spaceAv;
  			}		
  			
  	  		index = TileEntityShopTable.NextMatchingInventoryStackIndex(stack, this, index + 1, false); 			
  		}
  		
  		index = TileEntityShopTable.NextEmptyInventoryStackIndex(this, 0, false);
  		
  		while (index != -1)
  		{
  			int spaceAv = stack.getMaxStackSize();

  			if (spaceAv >= remSize)
  			{
  				ItemStack newStack = ItemStack.copyItemStack(stack);
  				newStack.stackSize = remSize;
  				this.chestContents[index] = newStack;
  				return true;
  			}

  			if (spaceAv < remSize)
  			{
  				remSize = remSize - spaceAv;  
  				ItemStack newStack = ItemStack.copyItemStack(stack);
  				newStack.stackSize = spaceAv;
  				this.chestContents[index] = newStack;	
  				
  			}
  			
  	  		index = TileEntityShopTable.NextEmptyInventoryStackIndex(this, index + 1, false); 			
  		}
  		
  		stack.stackSize = remSize;
  		return false;
  		
  		
  	}
  	
  	//Lockable Functions
  	public void SetLockValue(String key)
  	{
  		this.lockValue = key;
  	}
  	public String GetLockValue()
  	{
  		return this.lockValue;
  	}
  	
    //Merchant Functions
    public void setCustomer(EntityPlayer par1EntityPlayer)
    {
        this.buyingPlayer = par1EntityPlayer;
    }
    public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }
    public boolean isTrading()
    {
        return this.buyingPlayer != null || this.buyingMerchant != null;
    }
    public void useRecipe(MerchantRecipe par1MerchantRecipe)
    {    	
    	//Check if any inventory is available for sale, if not, then remove the last item for sale from trade inventory
    	ItemStack is = par1MerchantRecipe.getItemToSell();
    	
    	int saleAmount = is.stackSize;
    	int remSaleAmt = saleAmount;
    	int nextSt = TileEntityShopTable.NextMatchingInventoryStackIndex(is, this, false);
    	
    	//Handle Remaining Payment
    	while(remSaleAmt > 0 && nextSt != -1)
    	{	    
        	nextSt = TileEntityShopTable.NextMatchingInventoryStackIndex(is, this, false);

        	int a = 7;
        	
        	//If null, check for final sale and remove final sale item
        	if (false && nextSt == -1 && remSaleAmt > 0 && TileEntityShopTable.NextMatchingTradeBySaleIndex(par1MerchantRecipe, this) > -1) //MOD BOOL
        	{
        		nextSt = TileEntityShopTable.NextMatchingTradeBySaleIndex(par1MerchantRecipe, this);
        		
        		//If Trade index exists, remove saleitem
        		if (nextSt > -1)
        		{
        			this.chestContents[11 + nextSt * 12] = null;
    				if (MerchantTradeRecipe.class.isInstance(par1MerchantRecipe))
    					((MerchantTradeRecipe)par1MerchantRecipe).IsDisabled = true;

        			this.AddToInventory(par1MerchantRecipe.getItemToBuy());
        			
        			if (par1MerchantRecipe.hasSecondItemToBuy())
            			this.AddToInventory(par1MerchantRecipe.getSecondItemToBuy());
        			
        			return;
        		}
        		else
        		{
        			//Print nonexistence of trade item when trade was used
            		System.out.println("ERROR - CIVMOD - MAJOR TRANSACTION ERROR");
            		
            		//Search for single trade sale item
            		nextSt = TileEntityShopTable.NextMatchingSaleStackIndex(is, this);
            		
            		//If Item nonexistent entirely, then return
            		if (nextSt > -1)
            		{
            			this.chestContents[11 + nextSt * 12] = null;
            		}
            		else
            		{
                		System.out.println("ERROR - CIVMOD - MAJOR TRANSACTION ERROR");
                		return;
            		}
        		}
        	}
        	
        	//If null
        	if (nextSt == -1 && remSaleAmt > 0)
        	{
        		System.out.println("ERROR - CIVMOD - MAJOR TRANSACTION ERROR");
        		return;
        	}
        	
        	//Handle all transaction options
    		if (this.chestContents[nextSt].stackSize > remSaleAmt)
    		{
    			ItemStack curSt = this.getStackInSlot(nextSt);
    			//int newSize = curSt.stackSize - remSaleAmt;
    			curSt.stackSize = curSt.stackSize - remSaleAmt;
    			remSaleAmt = 0;
    		}    		
    		else if (this.chestContents[nextSt].stackSize == remSaleAmt)
    		{
    			this.chestContents[nextSt] = null;
    			remSaleAmt = 0;
    		}
    		else if (this.chestContents[nextSt].stackSize < remSaleAmt)
    		{
    			remSaleAmt = remSaleAmt - this.chestContents[nextSt].stackSize;
    			this.chestContents[nextSt] = null;
    		}    
    		
    		if (remSaleAmt == 0)
    		{
    			this.AddToInventory(par1MerchantRecipe.getItemToBuy());
    			
    			if (par1MerchantRecipe.hasSecondItemToBuy())
        			this.AddToInventory(par1MerchantRecipe.getSecondItemToBuy());
    			
    			if (TileEntityShopTable.NextMatchingInventoryStackIndex(is, this, false) == -1)
    				if (MerchantTradeRecipe.class.isInstance(par1MerchantRecipe))
    					((MerchantTradeRecipe)par1MerchantRecipe).IsDisabled = true;
    		
    			
    			return;
    		}
    	}    	
    }
    public void setRecipes(MerchantRecipeList mrl)
    {
    
    }
    public void func_110297_a_(ItemStack par1ItemStack)
    {
        /*if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();

            if (par1ItemStack != null)
            {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else
            {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }*/
    }
    @SuppressWarnings("unchecked")
	public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer)
    {
    	MerchantRecipeList mrl = new MerchantRecipeList();

    	//Iterate through chest components in the designatated slots (8 - 11 X 0 - 5)
    	for (int i = 0; i < 6; i++)
    	{    		
    		ItemStack i1 = this.getStackInSlot(9 + 9 * i);
    		ItemStack i2 = this.getStackInSlot(10 + 9 * i);
    		ItemStack i3 = this.getStackInSlot(11 + 9 * i);

    		if (i1 == null || i3 == null || i1 == i3)
    			continue;
    		    		    		
			if (!this.HasInventorySpaceFor(i1))
				continue;
			if (!this.ContainsInventory(i3))
				continue;
			
    		int i1id = Item.getIdFromItem(i1.getItem());
    		int i3id = Item.getIdFromItem(i3.getItem());
    		
    		MerchantTradeRecipe mr = null;
    		
    		if (i2 == null)
    			mr = new MerchantTradeRecipe(i1, i3);
    		else
    			mr = new MerchantTradeRecipe(i1, i2, i3);   
    		
    		if (this._myTrades.get(i) == null)
    			this._myTrades.set(i, mr);
    		else
    		{
    			MerchantTradeRecipe mtr = (MerchantTradeRecipe)this._myTrades.get(i);
	    		int mtri1 = Item.getIdFromItem(mtr.getItemToBuy().getItem());    		
	    		int mtri3 = Item.getIdFromItem(mtr.getItemToSell().getItem());
	    		
	    		if (i1id != mtri1 || i3id != mtri3)
	    			this._myTrades.set(i, mr);
    		}
    		
    		mrl.addToListWithCheck(mr);
    	}    	
    	
        return mrl;
    }
    public void updateRecipes()
    {
    	
    }
    
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
        return 72;
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
        if (this.getBlockType() instanceof BlockShopTable)
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
