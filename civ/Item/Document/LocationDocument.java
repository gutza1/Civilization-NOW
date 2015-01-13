package civ.Item.Document;

import javax.vecmath.Point3i;

import civ.Core.CivMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class LocationDocument extends EmptyDocument
{		
	public static Point3i GetLocation(ItemStack is)
	 {    	
		if (is == null)
			return null;
		
    	NBTTagCompound nbt = is.getTagCompound();
    	if (nbt != null)
    	{
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			
			return new Point3i(x, y, z);	
    	}
    	
    	return null;
    }
	public static boolean IsValidLocation(World w, ItemStack is)
	{
		Point3i p = LocationDocument.GetLocation(is);
		
		if (p == null)
			return false;
		
		return LocationDocument.IsValidLocation(w, p);
	}
	public static boolean IsValidLocation(World w, Point3i p)
	{
		TileEntity te = w.getTileEntity(p.x, p.y, p.z);
		
		return LocationDocument.IsValidTileEntity(te);			
	}
	public static boolean IsValidTileEntity(TileEntity te)
	{
		if (TileEntityChest.class.isInstance(te))
			return true;		

		if (TileEntitySign.class.isInstance(te))
			return true;
		
		return false;	
	}
	
    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	String r = "Location Document";
    	
    	NBTTagCompound nbt = par1ItemStack.getTagCompound();
    	if (nbt != null)
    	{
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			
			r = "Location Document (" + x + ", " + y + ", " + z + ")"; 		
    	}
    	
    	return r;
    }
    
	public LocationDocument()
	{
		super();
	}
	
	public static ItemStack CreateLocationDocument(String author, int x, int y, int z)
	{				
		ItemStack nis = new ItemStack(CivMod.LocationDocument);
		
		NBTTagCompound locDocNBT = new NBTTagCompound();
		
		locDocNBT.setString("author", author);
		locDocNBT.setInteger("x", x);
		locDocNBT.setInteger("y", y);
		locDocNBT.setInteger("z", z);
		
		nis.setTagCompound(locDocNBT);
		
		return nis;
	}
}
