package civ.Block;

import civ.Core.CivMod;
import civ.GUI.GuiMerchantTable;
import civ.GUI.GuiJobPost;
import civ.Item.Key;
import civ.Item.Document.EmptyDocument;
import civ.Item.Document.LocationDocument;
import civ.TileEntity.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockShopPost extends BlockContainer
{
	//Table Fields
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150034_b;
    private static final String __OBFID = "CL_00000221";

    public BlockShopPost()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
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
    	
    	if (te.getBlockType().getClass() == this.getClass())
    		System.out.println("TILE ENTITY MISMATCH");
    	
    	TileEntityShopPost sp = (TileEntityShopPost)world.getTileEntity(x, y, z);
    	String senderName = entPlayer.getCommandSenderName();

    	
    	//If no owner
    	if (!sp.HasOwner())
    	{
    		sp.setOwner(senderName);
    		
    		if (CivMod.DEBUG)
    			System.out.println("MERCHANT POST OWNER - " + sp.getOwner());
    		
    		entPlayer.addChatMessage(new ChatComponentTranslation("You have taken ownership of the post"));
    		
    		return true;
    	} 


    	//Handle the owner
    	ItemStack heldItem = entPlayer.getHeldItem();
    	boolean holdingNonKey = (heldItem != null && heldItem.getItem() != CivMod.Key);
    	boolean isOwner = sp.getOwner().equalsIgnoreCase(senderName);
    	
    	//Check for location document, owner, and lack of merchant owner
    	if (isOwner && heldItem != null && heldItem.getItem() instanceof LocationDocument)
    	{
    		NBTTagCompound nbt = heldItem.getTagCompound();
    		int x2 = nbt.getInteger("x");
    		int y2 = nbt.getInteger("y");
    		int z2 = nbt.getInteger("z");
    		
			if (sp.setShopTableOwner(entPlayer.worldObj, x2, y2, z2))
			{
				entPlayer.addChatMessage(new ChatComponentTranslation("The post has been set to the document's written location"));    
				return true;
			}
			else
				System.out.println("MAJOR POST SET LINK ERROR");
    	}
    		
    	sp.ActivateMerchantOwner(world, entPlayer, true);
    	
    	return true;

    	//Get Table
    	//TileEntityShopTable TEsp = sp.GetTable(entPlayer.worldObj);

    	//Handle bad stuff that should never happen
		//if (TEsp == null || !(TEsp instanceof TileEntityShopTable))
		//	return true;
		
		
    	/*
		//Handle regular owner open event
		if (holdingNonKey)
		{    
			TEsp.setCustomer(entPlayer);
			entPlayer.displayGUIMerchant(TEsp, "Merchant Table");
			return true;
		}
		else
		{				
			//Check if key held matches, then open
			if ((isOwner && heldItem == null) || (heldItem != null && Key.IsKeyMatch(entPlayer.getHeldItem(), TEsp)))
			{
				GuiMerchantTable.DisplayGUIMerchTable_ForPlayer_FromServer((EntityPlayerMP)entPlayer, TEsp, x, y, z, 1); 
				return true;
			}
			else
			{					
				TEsp.setCustomer(entPlayer);
				entPlayer.displayGUIMerchant(TEsp, "Merchant Table");
				return true;    
			}
		}
		*/	
    }   
       
    //Inventory Functions
    public TileEntity createNewTileEntity(World w, int i)
    {    	
    	return new TileEntityShopPost();
    }
    
}
