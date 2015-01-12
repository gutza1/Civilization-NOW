package civ.Merchant;

import java.util.ArrayList;
import java.util.List;

import civ.Core.CivMod;
import civ.TileEntity.TileEntityAutoShopTable;

import net.minecraft.block.Block;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class TradeManager
{
	private static int _TradeSearchLength = 4;
	
	//Methods
  	@SuppressWarnings("unchecked")
	public static List<IMerchant> GetNearbyMerchants(IMerchant self,
  			World worldObj, boolean includeVillagers, Block[] includeBlocks, int x, int y, int z, int dist)
  	{
  		int xs = x - dist, xf = x + dist;
  		int ys = y - dist, yf = y + dist;
  		int zs = z - dist, zf = z + dist;
  		List<IMerchant> list = null;
  		
  		if (includeVillagers)
  		{
  			list = worldObj.getEntitiesWithinAABB(IMerchant.class, AxisAlignedBB.getBoundingBox(xs, ys, zs, xf, yf, zf));
  			list.remove(self);
  		}
  		else
  			list = new ArrayList<IMerchant>();
  		
  		if (includeBlocks != null && includeBlocks.length > 0)
  		{
	  		for (int xi = xs; xi <= xf; xi++)
	  			for (int yi = ys; yi < yf; yi++)
	  				for (int zi = zs; zi < zf; zi++)
			  		{
	  			  		TileEntity curTE = worldObj.getTileEntity(xi, yi, zi);
	  			  		
	  			  		if (curTE == null || curTE == self || !(curTE instanceof IMerchant))
	  			  			continue;
	  			  		
	  			  		if (self.getClass().isInstance(curTE))
	  			  		{	  					  			  				
  			  				list.add((IMerchant)curTE);
  			  				continue;  	  			  			
	  			  		}
	  			  		
	  			  		
	  			  		for (int i = 0; i < includeBlocks.length; i++)
	  			  		{	  		
	  			  			int cTEtype = Block.getIdFromBlock(curTE.getBlockType());
	  			  			int inclBLtype = Block.getIdFromBlock(includeBlocks[i]);
	  			  			if (curTE != self && cTEtype == inclBLtype)
	  			  			{	  			  					  			  				
	  			  				list.add((IMerchant)curTE);
	  			  				break;  	
	  			  			}
	  			  		}	  			  		
			  		}	  		
  		}

  		return list;
  	}
  	public static List<IMerchant> GetNearbyMerchants(IMerchant self,
  			World worldObj, boolean includeVillagers, int x, int y, int z)
  	{
  		return TradeManager.GetNearbyMerchants(self, worldObj, includeVillagers, new Block[] { 
			CivMod.AutoShopTable, CivMod.ShopTable }, x, y, z, _TradeSearchLength);
  	}
  	
  	//Getters
  	public static double GetPriceOfSale(MerchantRecipe trade)
  	{
  		return (trade.getItemToSell().stackSize * 1.0) / (trade.getItemToBuy().stackSize * 1.0);  	
  	}  	
  	public static double GetPriceOfBuy(MerchantRecipe trade)
  	{
  		return (trade.getItemToBuy().stackSize * 1.0) / (trade.getItemToSell().stackSize * 1.0);  	
  	}
  	public static double GetMatchSalePrice(MerchantRecipe activeTrade, MerchantRecipe otherTrade)
  	{
  		if (Item.getIdFromItem(activeTrade.getItemToBuy().getItem()) != Item.getIdFromItem(otherTrade.getItemToSell().getItem()) || 
  				Item.getIdFromItem(activeTrade.getItemToSell().getItem()) != Item.getIdFromItem(otherTrade.getItemToBuy().getItem()))  		
  			return -1;
  		
  		double priceForBuyInSellUnits = TradeManager.GetPriceOfSale(otherTrade);
  		
  		return priceForBuyInSellUnits;
  		
  	}
  	
  	//Getters
  	public static MerchantTradeRecipe ReturnBestRecipe(ItemStack buy, ItemStack sell)
  	{
  		if (buy.stackSize == 1 || sell.stackSize == 1)
  			return new MerchantTradeRecipe(buy, sell);
  		
  		if (buy.stackSize > sell.stackSize)
  		{
  			buy.stackSize /= sell.stackSize;
  			sell.stackSize = 1;
  		}
  		else
  		{
  			sell.stackSize /= buy.stackSize;
  			buy.stackSize = 1;  			
  		}
  		
  		return new MerchantTradeRecipe(buy, sell);
  	}
  	
  	//Handlers
  	public static boolean HandleVerifiedTransaction(IMerchant buyer, MerchantRecipe buyRecipe, 
  			IMerchant seller, MerchantRecipe saleRecipe, double salePrice)
  	{
  		MerchantRecipe reverseSaleRecipe = new MerchantRecipe(saleRecipe.getItemToSell(), saleRecipe.getItemToBuy());
  		
  		buyer.useRecipe(reverseSaleRecipe);
  		seller.useRecipe(saleRecipe); 	  
  		
  		return true;
  	}
  	
  
  	
  	public static  float[] ReturnItemPrices(MerchantRecipeList offers, ItemStack itemDesired, ItemStack itemToGive)
  	{
  		float[] rf = new float[offers.size()];
  		
  		for (int i = 0; i < rf.length; i++)
  		{
  			MerchantRecipe curMR = (MerchantRecipe)offers.get(i);
  			
  			//Match
  			if (Item.getIdFromItem(curMR.getItemToSell().getItem()) == Item.getIdFromItem(itemDesired.getItem()) && 
  					Item.getIdFromItem(curMR.getItemToBuy().getItem()) == Item.getIdFromItem(itemToGive.getItem()))
  			{
  				rf[i] = (curMR.getItemToBuy().stackSize * 1.0f) / (curMR.getItemToSell().stackSize * 1.0f);
  			}
  			else
  			{
  				rf[i] = -1.0f;
  			}
  		}
  		
  		return rf;  		
  	}
  	public static void AttemtToTradeTrade(IEconMerchant[] merchants, MerchantRecipe desiredRecipe, int maxToGive)
  	{
  		if (desiredRecipe.isRecipeDisabled())
  			return;
  		
  		ItemStack itemDesired = desiredRecipe.getItemToBuy();
  		ItemStack itemToGive = desiredRecipe.getItemToSell();
  		
  		//Find Best Trade amongst villagers
  		float bestPrice = -1.0f;
  		int bestVilIndex = -1;
  		MerchantRecipe bestRecipe = null;
  		
  		for (int i = 0; i < merchants.length; i++)
  		{
  			IEconMerchant curV = merchants[i];
  			MerchantRecipeList curMRL = curV.getRecipes(null);
  			float myPrice = (1f * itemToGive.stackSize) / (1f * itemDesired.stackSize);
  			
  			float[] tvs = TradeManager.ReturnItemPrices(curMRL, itemDesired, itemToGive);
  			
  			for (int j = 0; j < tvs.length; j++)
  			{
  				if ((bestRecipe == null || tvs[j] < bestPrice || (tvs[j] == bestPrice && CivMod.RandomObj.nextFloat() < .5f)) && tvs[j] > 0)
  				{
  					MerchantRecipe tr = (MerchantRecipe)curMRL.get(j); 					
  					
  					if (tr.isRecipeDisabled())
  						continue;
  					
  					if (myPrice < tvs[j])
  						continue;
  					if (maxToGive < tr.getItemToBuy().stackSize)
  						continue;
  					
  					bestPrice = tvs[j];
  					bestVilIndex = i;  
  					bestRecipe = tr;
  				}
  			}
  		}
  		
  		//Attempt to trade
  	}
}
