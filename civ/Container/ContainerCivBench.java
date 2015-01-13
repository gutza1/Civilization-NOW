package civ.Container;

import civ.Technology.TechnologyManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ContainerCivBench extends ContainerWorkbench
{
	private World worldObj;
	private InventoryPlayer player;
	
	public ContainerCivBench(InventoryPlayer player, World w, int x, int y, int z)
	{
		super(player, w, x, y, z);
		
		this.worldObj = w;
		this.player = player;
	}
	
	@Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
		ItemStack is = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj);
		
		//if (this.player != null && EntityPlayerMP.class.isInstance(this.player))// && TechnologyManager.PlayerHasRecipeTechnology(this.player, is))	
		//{
			//EntityPlayerMP ep = null;
			this.craftResult.setInventorySlotContents(0, is);
		//}
    }
}
