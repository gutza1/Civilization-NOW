package civ.Block;

import civ.Core.CivMod;
import civ.GUI.GuiMerchantTable;
import civ.TileEntity.TileEntityAutoShopTable;
import civ.TileEntity.TileEntityShopTable;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockAutoShopTable extends BlockShopTable
{
    public BlockAutoShopTable()
    {
		super();
    }

    
    //Inventory Functions
    public TileEntity createNewTileEntity(World w, int i)
    {    	
    	return new TileEntityAutoShopTable();
    }
    
}
