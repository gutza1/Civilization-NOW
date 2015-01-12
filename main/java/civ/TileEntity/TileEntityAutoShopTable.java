package civ.TileEntity;

import java.util.ArrayList;
import java.util.List;

import civ.Block.BlockShopTable;
import civ.Core.CivMod;
import civ.Merchant.IEconMerchant;
import civ.Merchant.MerchantTradeRecipe;
import civ.Merchant.MerchantTradeRecipeList;
import civ.Merchant.TradeManager;
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

public class TileEntityAutoShopTable extends TileEntityShopTable
{	
	//Private static fields
	private static int _AITradeSearchDelayTickWaitBase = 60;
	
	//Private Fields
    private int _AITradeSearchDelayTickCount = 0;
    private List<int[]> neighborTables = new ArrayList<int[]>();

    //Constructor
    public TileEntityAutoShopTable()
    {
		this.blockType = CivMod.AutoShopTable;
		this._AITradeSearchDelayTickCount = CivMod.RandomObj.nextInt(TileEntityAutoShopTable._AITradeSearchDelayTickWaitBase);
    }
  	
  	//Vanilla-Civ Functions
    public void updateEntity()
    {
    	this.updateEntity(null);
    }
  	public void updateEntity(List<IMerchant> merchants)
  	{  	  		
  		if (this.worldObj.isRemote)
  			return;
  		
  		//System.out.println("" + this.worldObj.getTotalWorldTime());
  		
  		//Handle AI Tick Delay
  		if (this._AITradeSearchDelayTickCount > TileEntityAutoShopTable._AITradeSearchDelayTickWaitBase)
  			this._AITradeSearchDelayTickCount = CivMod.RandomObj.nextInt(40) - 20;
  		else
  		{
  	  		this._AITradeSearchDelayTickCount++;
  			return;
  		}
  		
		//Get All merchants available
		if (this.isTrading())
			return;
		
		//this._isTrading = true;
		
		if (merchants == null)
			merchants = TradeManager.GetNearbyMerchants(this, this.worldObj, true,
				this.xCoord, this.yCoord, this.zCoord);
		
		if (merchants.size() == 0)
			return;

		//Get My Trades
  		MerchantRecipeList myTrades = this.getRecipes(null);
  		
  		if (myTrades.size() == 0)
  			return;
  		
		//Cyle through trades
		for (int i = 0; i < myTrades.size(); i++)
		{	
			int numAttempts = 0;
			MerchantRecipe myRec = (MerchantRecipe)myTrades.get(i);	
			double myPrice = 1.0 / TradeManager.GetPriceOfSale(myRec);

  			//////////////////////////////////////
			//List<Double> prices = new ArrayList<Double>();
			//List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
			double bestPrice = 0;//Double.MAX_VALUE;
			MerchantRecipe bestRecipe = null;
			IMerchant bestMerchant = null;  							
			
	  		//Cycle through all merchants
  			for (int j = 0; j < merchants.size(); j++)
  			{
  				IMerchant curOther = merchants.get(j);
  				
  				if (this == curOther)
  					continue;
  				
  				MerchantRecipeList otherTrades = curOther.getRecipes(null);  	  	
  				
  				if (otherTrades.size() == 0)
  					continue;  	

				//Get Best trade from merchants
				for (int k = 0; k < otherTrades.size(); k++)
				{  					
					MerchantRecipe otherRec = (MerchantRecipe)otherTrades.get(k);
					
					double curPriceOfBuyItem = TradeManager.GetMatchSalePrice(myRec, otherRec);  		
					
					boolean AcceptablePrice = curPriceOfBuyItem > 0 && curPriceOfBuyItem > bestPrice && curPriceOfBuyItem >= myPrice;
					
					if (!AcceptablePrice)
					{
						if (MerchantTradeRecipeList.class.isInstance(otherTrades))
						{
							MerchantTradeRecipeList mtrl = (MerchantTradeRecipeList)otherTrades;
							//((MerchantTradeRecipe)mtrl.get(k)).HandleTrade(false);  								
						}
						numAttempts++;
						continue; 							
					}
					
					if (!this.HasInventorySpaceFor(otherRec.getItemToSell()))
						continue;
					if (!this.ContainsInventory(otherRec.getItemToBuy()))
						continue;
					
					IEconMerchant othEcon = null;
					 
					//if (IAgentEntity.class.isInstance(curOther))
					//	othEcon = ((IAgentEntity)curOther).getAgent();
					
					if (othEcon == null && IEconMerchant.class.isInstance(curOther))
						othEcon = (IEconMerchant)curOther;
					
					if (othEcon != null)
					{
						if (!othEcon.HasInventorySpaceFor(otherRec.getItemToBuy()))
							continue;
						if (!othEcon.ContainsInventory(otherRec.getItemToSell()))
							continue;
					}  						
					
					//if (!curOther.HasInventorySpaceFor(otherRec.getItemToBuy()))
					//	return;  	
					
					//this.setCustomer(par1EntityPlayer);
					
					if (bestMerchant != null && MerchantTradeRecipeList.class.isInstance(otherTrades)) 
					{
						MerchantTradeRecipeList mtrl = (MerchantTradeRecipeList)otherTrades;						
						//((MerchantTradeRecipe)mtrl.get(k)).HandleTrade(false); 
					}
					
					bestPrice = curPriceOfBuyItem;
					bestRecipe = otherRec;
					bestMerchant = curOther;
				
					
				}
			}
  			//////////////////////////////////////
  			
  			//Perform Transaction
  			if (bestMerchant != null)
  			{
  				if (TradeManager.HandleVerifiedTransaction(this, myRec, bestMerchant, bestRecipe, bestPrice))
  				{
  					//this.updateRecipes();
  					//if (IEconMerchant.class.isInstance(this))
  					//	((IEconMerchant)bestMerchant).updateRecipes();
  				}
  				//else
  					//((MerchantTradeRecipe)this._myTrades.get(i)).HandleTrade(false);
  			}
  			
  			//if (bestMerchant == null && numAttempts > 0)
  				//((MerchantTradeRecipe)this._myTrades.get(i)).HandleTrade(false);
		}

  		this._AITradeSearchDelayTickCount++;
  		
  		return;
  	}
  	
  	
  	
  	
}
