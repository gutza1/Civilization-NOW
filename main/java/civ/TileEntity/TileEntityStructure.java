package civ.TileEntity;

import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;

public abstract class TileEntityStructure extends TileEntityPost {
    
	private long _myFounding;
	private String _myFounder;
	private String _myOwner;

	public void SetStructureProps(EntityPlayer entityPlayer) 
	{	
		this._myFounding = entityPlayer.worldObj.getWorldTime();
		this._myFounder = entityPlayer.getCommandSenderName();
		this._myOwner = entityPlayer.getCommandSenderName();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
        if (nbt.hasKey("myFounding"))
        	this._myFounding = nbt.getLong("myFounding");

        if (nbt.hasKey("myFounder"))
        	this._myFounder = nbt.getString("myFounder");

        if (nbt.hasKey("myOwner"))
        	this._myOwner = nbt.getString("myOwner");
        
        if (nbt.hasKey("blockType"))
        	this.blockType = (Block)Block.blockRegistry.getObjectById(nbt.getInteger("blockType"));      
        	
        return;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {    	
    	if (this._myFounding >= 0)
            nbt.setLong("myFounding", this._myFounding);

        if (this._myFounder != null && this._myFounder != "")
        	nbt.setString("myFounder", this._myFounder);
        
        if (this._myOwner != null && this._myOwner != "")
        	nbt.setString("myOwner", this._myOwner);
    	
    	if (this.getBlockType() != null)
    		nbt.setInteger("blockType", Block.blockRegistry.getIDForObject(this.getBlockType()));
        
        super.writeToNBT(nbt);    
    }

    @Override
    public void updateEntity() 
    {
    	    	
    }

}
