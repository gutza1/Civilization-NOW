package civ.TileEntity.Render;

import org.lwjgl.opengl.GL11;

import civ.Core.CivDraw;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BaseSpecialRenderer extends TileEntitySpecialRenderer
{
    //This method is called when minecraft renders a tile entity
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) 
    { 
        //CivDraw.DrawLine(x, y, z, x+5, y, z);
    	CivDraw.DrawBox(x + 1, y + 1, z + 1, x, y, z);
    }
}
