package civ.Entity.Render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class RenderEvoAnimal extends RenderLiving
{	
    private static final ResourceLocation aurochTexture = new ResourceLocation("civmod", "/textures/entity/auroch.png");    
    private static final ResourceLocation boarTexture = new ResourceLocation("civmod", "/textures/entity/boar.png");
    private static final ResourceLocation wildChickenTexture = new ResourceLocation("civmod", "/textures/entity/wild_chicken.png");
    
    private static final ResourceLocation animalTexture = null;
	
	public RenderEvoAnimal(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);	
	}

	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		if (EntityCow.class.isInstance(entity))
			return RenderEvoAnimal.aurochTexture;
		if (EntityPig.class.isInstance(entity))
			return RenderEvoAnimal.boarTexture;
		if (EntityChicken.class.isInstance(entity))
			return RenderEvoAnimal.wildChickenTexture;
		
		return null;
    }
	/*
	@Override
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.preRenderCallback((EntityEvoAnimal)par1EntityLivingBase, par2);
		
    }
    protected void preRenderCallback(EntityEvoAnimal e, float par2)
    {
    	float gs = e.DerivedVisualSizeFactor(true);
    	
        GL11.glScalef(gs, gs, gs);
    }*/
}
