package civ.Block;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import civ.Core.CivMod;
import civ.GUI.GuiMerchantTable;
import civ.Item.Key;
import civ.Item.Document.EmptyDocument;
import civ.Item.Document.LocationDocument;
import civ.TileEntity.TileEntityShopTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockShopTable extends BlockContainer
{
	//Table Fields
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150034_b;

    public BlockShopTable()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    //Gets the block's texture. Args: side, meta
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_150035_a : (p_149691_1_ == 0 ? Blocks.planks.getBlockTextureFromSide(p_149691_1_) : (p_149691_1_ != 2 && p_149691_1_ != 4 ? this.blockIcon : this.field_150034_b));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.field_150035_a = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.field_150034_b = p_149651_1_.registerIcon(this.getTextureName() + "_side");
    }
  
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {    	
    	super.onBlockActivated(world, x, y, z, entPlayer, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    	
    	if (world.isRemote)
    		return true;
    	
    	TileEntity te = world.getTileEntity(x, y, z);
    	
    	//int b1 = Block.getIdFromBlock(te.getBlockType());
    	//int b2 = Block.getIdFromBlock(this);
    	
    	//if (b1 != b2)
    	//	System.out.println("TILE ENTITY MISMATCH");\
    	
    	if (!this.getUnlocalizedName().contains("Merchant Table"))
    		System.out.println("TILE ENTITY MISMATCH");	
    	
    	TileEntityShopTable mte = (TileEntityShopTable)world.getTileEntity(x, y, z);
    	String senderName = entPlayer.getCommandSenderName();
    	
    	if (mte.isTrading())
    		return true;
    	
    	//Check for paper, return location document
    	ItemStack heldItem = entPlayer.getHeldItem();
    	if (heldItem != null && heldItem.getItem() == CivMod.UnwrittenDocument && mte.getOwner().equalsIgnoreCase(senderName))
    	{
    		entPlayer.inventory.setInventorySlotContents(entPlayer.inventory.currentItem,
    			LocationDocument.CreateLocationDocument(senderName, mte.xCoord,  mte.yCoord,  mte.zCoord));
    		return true;
    	}    	
    	
    	//Handle does not have owner, set to first person
    	if (!mte.HasOwner())
    	{
    		mte.setOwner(senderName);
    		
    		if (CivMod.DEBUG)
    			System.out.println("MERCHANT TABLE OWNER - " + mte.getOwner());
    		
    		entPlayer.addChatMessage(new ChatComponentTranslation("You have taken ownership of the merchant table"));
    	}  

    	//Handle the owner
    	boolean holdingNonKey = (heldItem != null && heldItem.getItem() != CivMod.Key);
    	boolean isOwner = mte.getOwner().equalsIgnoreCase(senderName);
    	
		//Handle regular owner open event
		if (holdingNonKey)
		{    
			mte.setCustomer(entPlayer);
			entPlayer.displayGUIMerchant(mte, "Merchant Table");
			return true;
		}
		else
		{	

			if (mte.GetLockValue() == null && !holdingNonKey && heldItem != null)
			{
				Key.SetNewKeyValue(heldItem, 0, mte);
			}
			
			//Check if key held matches, then open
			if ((isOwner && heldItem == null) || (heldItem != null && Key.IsKeyMatch(entPlayer.getHeldItem(), mte)))
			{
				GuiMerchantTable.DisplayGUIMerchTable_ForPlayer_FromServer((EntityPlayerMP)entPlayer, mte, x, y, z); 
				return true;
			}
			else
			{					
				mte.setCustomer(entPlayer);
				entPlayer.displayGUIMerchant(mte, "Merchant Table");
				return true;    
			}
		}	
    	
    	
    	/*
    	
    	//Handle if Item is held, then open for trade 
    	if (entPlayer.getHeldItem() != null && entPlayer.getHeldItem().getItem() != CivMod.Key)
    	{
    		holdingNonKey = true;
    		if (!mte.isTrading())
    		{
				mte.setCustomer(entPlayer);
				entPlayer.displayGUIMerchant(mte, "Merchant Table");
				return true;
    		}
    	}
    	
    	if (holdingNonKey && this.lockValue != null &&
    			Key.HandleKeyAttempt(entPlayer, entPlayer.getHeldItem(), this.lockValue))
    	{
    		if (!mte.isTrading())
    		{
				mte.setCustomer(entPlayer);
				entPlayer.displayGUIMerchant(mte, "Merchant Table");
				return true;
    		}
    	}
		
		//Since no chosen option (held item or sneaking), check if user is owner of table
		if (true)// && entPlayer.getCommandSenderName() == mte.getOwner())
		{
			GuiMerchantTable.DisplayGUIMerchTable_ForPlayer_FromServer((EntityPlayerMP)entPlayer, mte, x, y, z); 
			return true;
		}
    	
		//Since user is not owner, and is not choosing an action, open trade option
    	if (!mte.isTrading())
    	{
			mte.setCustomer(entPlayer);
			entPlayer.displayGUIMerchant(mte, "Merchant Table");
    	}
    	*/
    	
        //return true;
    }   
    
    public boolean ActivateBlock(World w, int x, int y, int z, EntityPlayer entPlayer, boolean TradeOnly)
    {    	
    	TileEntity te = w.getTileEntity(x, y, z);
	
		int b1 = Block.getIdFromBlock(te.getBlockType());
		int b2 = Block.getIdFromBlock(this);
		
		//if (b1 != b2)
		//	System.out.println("TILE ENTITY MISMATCH");\
		
		if (!this.getUnlocalizedName().contains("Merchant Table"))
			System.out.println("TILE ENTITY MISMATCH");	
		
		TileEntityShopTable mte = (TileEntityShopTable)te;
		String senderName = entPlayer.getCommandSenderName();
		
		if (mte.isTrading())
			return true;
		
		if (TradeOnly)
		{			
			mte.setCustomer(entPlayer);
			entPlayer.displayGUIMerchant(mte, "Merchant Table");
			return true;    			
		}
		
		//Check for paper, return location document
		ItemStack heldItem = entPlayer.getHeldItem();
		if (heldItem != null && heldItem.getItem() == CivMod.UnwrittenDocument && mte.getOwner().equalsIgnoreCase(senderName))
		{
			entPlayer.inventory.setInventorySlotContents(entPlayer.inventory.currentItem,
				LocationDocument.CreateLocationDocument(senderName, mte.xCoord,  mte.yCoord,  mte.zCoord));
			return true;
		}    	
		
		//Handle does not have owner, set to first person
		if (!mte.HasOwner())
		{
			mte.setOwner(senderName);
			
			if (CivMod.DEBUG)
				System.out.println("MERCHANT TABLE OWNER - " + mte.getOwner());
			
			entPlayer.addChatMessage(new ChatComponentTranslation("You have taken ownership of the merchant table"));
		}  
	
		//Handle the owner
		boolean holdingNonKey = (heldItem != null && heldItem.getItem() != CivMod.Key);
		boolean isOwner = mte.getOwner().equalsIgnoreCase(senderName);
		
		//Handle regular owner open event
		if (holdingNonKey)
		{    
			mte.setCustomer(entPlayer);
			entPlayer.displayGUIMerchant(mte, "Merchant Table");
			return true;
		}
		else
		{	
	
			if (mte.GetLockValue() == null && !holdingNonKey && heldItem != null)
			{
				Key.SetNewKeyValue(heldItem, 0, mte);
			}
			
			//Check if key held matches, then open
			if ((isOwner && heldItem == null) || (heldItem != null && Key.IsKeyMatch(entPlayer.getHeldItem(), mte)))
			{
				GuiMerchantTable.DisplayGUIMerchTable_ForPlayer_FromServer((EntityPlayerMP)entPlayer, mte, x, y, z); 
				return true;
			}
			else
			{					
				mte.setCustomer(entPlayer);
				entPlayer.displayGUIMerchant(mte, "Merchant Table");
				return true;    
			}
		}	
    }
    
    //Inventory Functions
    public TileEntity createNewTileEntity(World w, int i)
    {    	
    	return new TileEntityShopTable();
    }
    
}
