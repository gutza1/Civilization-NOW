package civ.Entity.Render;

import org.lwjgl.opengl.GL11;

import civ.Entity.EntityCivHorse;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class RenderCivHorse extends RenderLiving
{	    	
	public RenderCivHorse(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);			
	}

	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        //return this._evoCowTexture;
		return new ResourceLocation(((EntityCivHorse)entity).getHorseTexture());
    }
	@Override
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.preRenderCallback((EntityCivHorse)par1EntityLivingBase, par2);
		
    }
    protected void preRenderCallback(EntityCivHorse e, float par2)
    {		
    }
}
