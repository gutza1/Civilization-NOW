package civ.Core;

import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import civ.Core.*;
import civ.Entity.EntityCivHorse;
import civ.Entity.Render.RenderCivVillager;
import civ.Entity.Render.RenderEvoAnimal;
import civ.TileEntity.TileEntityJobPost;
import civ.TileEntity.Render.BaseSpecialRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers()
        {
           //Special Tile Entity Renderer           
        	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJobPost.class, new BaseSpecialRenderer());
           
            //Render Entities
            RenderingRegistry.registerEntityRenderingHandler(EntityVillager.class, new RenderCivVillager());
            RenderingRegistry.registerEntityRenderingHandler(EntityCivHorse.class, new RenderHorse(new ModelHorse(), 1.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityCow.class, new RenderEvoAnimal(new ModelCow(), 1.0f));
            RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, new RenderEvoAnimal(new ModelPig(), 1.0f));
            RenderingRegistry.registerEntityRenderingHandler(EntityChicken.class, new RenderEvoAnimal(new ModelChicken(), 1.0f));  
            
        }
        
}