package civ.Block;

import civ.Core.CivMod;
import civ.GUI.GuiJobPost;
import civ.GUI.GuiMerchantTable;
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

public class BlockJobPost extends BlockPost
{
    public BlockJobPost()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
  
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {    	
    	super.onBlockActivated(world, x, y, z, entPlayer, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    	
    	if (world.isRemote)
    		return true;
    	
    	TileEntity te = world.getTileEntity(x, y, z);
    	
    	TileEntityJobPost jp = (TileEntityJobPost)world.getTileEntity(x, y, z);
    	String senderName = entPlayer.getCommandSenderName();

    	
    	//If no owner
    	if (!jp.HasOwner())
    	{
    		jp.setOwner(senderName);
    		
    		if (CivMod.DEBUG)
    			System.out.println("JOB POST OWNER - " + jp.getOwner());
    		
    		entPlayer.addChatMessage(new ChatComponentTranslation("You have taken ownership of the job post"));
    	} 

    	//Handle the owner
    	ItemStack heldItem = entPlayer.getHeldItem();
    	//boolean holdingNonKey = (heldItem != null && heldItem.getItem() != CivMod.Key);
    	boolean isOwner = jp.getOwner().equalsIgnoreCase(senderName);
    	
    	//Check for location document, owner, and lack of merchant owner
    	/*if (isOwner && heldItem != null && heldItem.getItem() instanceof LocationDocument)
    	{
    		NBTTagCompound nbt = heldItem.getTagCompound();
    		int x2 = nbt.getInteger("x");
    		int y2 = nbt.getInteger("y");
    		int z2 = nbt.getInteger("z");
    		
			if (jp.setShopTableOwner(entPlayer.worldObj, x2, y2, z2))
			{
				entPlayer.addChatMessage(new ChatComponentTranslation("The post has been set to the document's written location"));    
				return true;
			}
			else
				System.out.println("MAJOR POST SET LINK ERROR");
    	}*/
    	
    	//if (isOwner)
    	{
    		//if (heldItem == null || (heldItem != null && heldItem.getItem() == CivMod.Key))
    			GuiJobPost.DisplayGUIJobPost_ForPlayer_FromServer((EntityPlayerMP)entPlayer, jp, x, y, z);
    		//else
    			//jp.ActivateMerchantOwner(world, entPlayer, true);
    	}
    	//else
    	{    		
    		//if (heldItem != null && heldItem.getItem() == CivMod.Key && jp.UnlocksTable(heldItem))
    		//	GuiJobPost.DisplayGUIJobPost_ForPlayer_FromServer((EntityPlayerMP)entPlayer, jp, x, y, z);
    		//else
    		//	jp.ActivateMerchantOwner(world, entPlayer, true);
    	}
    	
    	return true;
    }   
    
    //Inventory Functions
    public TileEntity createNewTileEntity(World w, int i)
    {    	
    	return new TileEntityJobPost();
    }
    
}
