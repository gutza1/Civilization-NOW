package civ.GUI;

import civ.Container.ContainerJobPost;
import civ.Container.ContainerShopTable;
import civ.TileEntity.TileEntityJobPost;
import civ.TileEntity.TileEntityShopPost;
import civ.TileEntity.TileEntityShopTable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int posX, int posY, int posZ)
	{	      	
		switch (guiId)
      	{
		  	case 0: 
		  	{
		  		TileEntity te = world.getTileEntity(posX, posY, posZ);
		  		TileEntityShopTable temt= ((TileEntityShopTable)te);
		  		
		  		ContainerShopTable cc = new ContainerShopTable(player.inventory, temt);
		  		
		    	return cc;
		  	}     
		  	case 1: 
		  	{
		  		TileEntityJobPost te = (TileEntityJobPost)world.getTileEntity(posX, posY, posZ);
		  		//TileEntityJobPost temt= te.GetEntity(world);
		  		
		  		ContainerJobPost cc = new ContainerJobPost(player.inventory, te);
		  		
		    	return cc;
		  	}     
      	}
		
		return null;
	}
  
	@Override
  	public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int posX, int posY, int posZ)
  	{		
      	switch (guiId)
      	{
		  	case 0: 
		  	{
		  		TileEntity te = world.getTileEntity(posX, posY, posZ);
		  		TileEntityShopTable temt= ((TileEntityShopTable)te);
		  		
		    	return new GuiMerchantTable(player.inventory, temt);
		  	}
		  	case 1: 
		  	{
		  		TileEntityJobPost te = (TileEntityJobPost)world.getTileEntity(posX, posY, posZ);
		  		//TileEntityJobPost temt= ((TileEntityJobPost)te);
		  		
		    	return new GuiJobPost(player.inventory, te);
		  	}
        	
      	}
	    
      	return null;
  	}
}