package civ.Block;

import civ.Container.ContainerCivBench;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.world.World;

public class BlockCivBench extends BlockWorkbench
{

	public BlockCivBench()
	{
		super();
	}
	
	@Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (w.isRemote)
        {
            return true;
        }
        else
        {
        	
        	p.displayGUIWorkbench(x, y, z);
            //if (ep.openContainer != null)
            {
            	//ep.closeScreen();
            	//ep.closeContainer();
            }
               /*     	
        	ep.getNextWindowId();
        	ep.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(ep.currentWindowId, 1, "Crafting", 9, true));
        	ep.openContainer = new ContainerCivBench(ep.inventory, ep.worldObj, x, y, z);
        	ep.openContainer.windowId = ep.currentWindowId;
        	ep.openContainer.addCraftingToCrafters(ep);
        	//ep.openContainer.*/
            return true;
        }
    }
}
