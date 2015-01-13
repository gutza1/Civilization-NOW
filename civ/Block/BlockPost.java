package civ.Block;

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
import civ.Core.CivMod;
import civ.GUI.GuiJobPost;
import civ.Item.Document.LocationDocument;
import civ.TileEntity.TileEntityJobPost;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockPost extends BlockContainer {
	//Table Fields
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150034_b;
    @SideOnly(Side.CLIENT)
    private IIcon bottom;
    private static final String __OBFID = "CL_00000221";

    public BlockPost(Material m)
    {
        super(m);
    }

    //Gets the block's texture. Args: side, meta
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_150035_a : (p_149691_1_ == 0 ? this.bottom : (p_149691_1_ != 2 && p_149691_1_ != 4 ? this.blockIcon : this.field_150034_b));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.field_150035_a = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.field_150034_b = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        
        if (this.getMaterial() == Material.rock)
        	this.bottom = Blocks.cobblestone.getBlockTextureFromSide(0);
        if (this.getMaterial() == Material.wood)
        	this.bottom = Blocks.planks.getBlockTextureFromSide(0);        
        if (this.bottom == null)
        	this.bottom = Blocks.planks.getBlockTextureFromSide(0);
    }  

    @Override
    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
    	TileEntity te = w.getTileEntity(x, y, z);

    	super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    	
    	w.removeTileEntity(x, y, z);
    	
    	if (!BlockJobPost.class.isInstance(te))
    		return;
    	
    	//((TileEntityJobPost)te).UnloadAll();
    	
    }    
}
