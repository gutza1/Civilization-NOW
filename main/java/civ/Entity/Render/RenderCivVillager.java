package civ.Entity.Render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderCivVillager extends RenderBiped
{  
    private static final ResourceLocation farmerVillagerTexture = new ResourceLocation("civmod", "/textures/entity/villager.png");
	
	public RenderCivVillager()
	{
		super(new ModelBiped(0.0F), 0.5F);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return this.farmerVillagerTexture;
    }

}
