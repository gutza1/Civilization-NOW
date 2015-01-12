package civ.Entity.AI;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import civ.Item.Document.IdentityDocument;

import com.sun.xml.internal.stream.Entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class AIHelper {
	
	public static enum AIState { Base, Idle };
	public static AICivIdle SetAIState(EntityVillager v, AIState state)
	{
		return AIHelper.SetAIState(v, state, true);
	}
	public static AICivIdle SetAIState(EntityVillager v, AIState state, boolean preserveAI)
	{     
		AICivIdle ai = null;
		if (preserveAI)
			ai = AICivIdle.GetAI(v);
		
		v.tasks.taskEntries.clear();
		
	    v.tasks.addTask(0, new EntityAISwimming(v));
	    v.tasks.addTask(1, new EntityAIAvoidEntity(v, EntityZombie.class, 8.0F, 0.6D, 0.6D));
	    v.tasks.addTask(1, new EntityAITradePlayer(v));
	    v.tasks.addTask(1, new EntityAILookAtTradePlayer(v));
	    v.tasks.addTask(2, new EntityAIMoveIndoors(v));
	    v.tasks.addTask(3, new EntityAIRestrictOpenDoor(v));
	    v.tasks.addTask(4, new EntityAIOpenDoor(v, true));
	    v.tasks.addTask(5, new EntityAIMoveTowardsRestriction(v, 0.6D));
	    v.tasks.addTask(9, new EntityAIWatchClosest2(v, EntityPlayer.class, 3.0F, 1.0F));
	    v.tasks.addTask(9, new EntityAIWatchClosest2(v, EntityVillager.class, 5.0F, 0.02F));
	    v.tasks.addTask(10, new EntityAIWatchClosest(v, EntityLiving.class, 8.0F));
	    
	    if (state == AIState.Idle)
	    {
		    //v.tasks.addTask(6, new EntityAIVillagerMate(v));
		    //v.tasks.addTask(7, new EntityAIFollowGolem(v));
		    v.tasks.addTask(8, new EntityAIPlay(v, 0.32D));
		    v.tasks.addTask(9, new EntityAIWander(v, 0.6D));
		    
		    v.tasks.addTask(6, new AIVillagerMate(v, .6D));
	    }	    

	    
	    if (ai == null)
	    	ai = new AICivIdle(v);
	    
	    ai.SetState(state);

	    v.tasks.addTask(12, ai);
	    
	    return ai;
	}	
	
	public static EntityVillager GetVillager(long searchId, World w, int x, int y, int z, int dXZ, int dY)
	{
		//Find villager
		//int x = p.x, y = p.y, z = p.z, 
		int distXZ = dXZ, distY = dY;				
		
  		int xs = x - distXZ, xf = x + distXZ;
  		int ys = y - distY, yf = y + distY;
  		int zs = z - distXZ, zf = z + distXZ;
  		
		List<Entity> le = w.getEntitiesWithinAABB(EntityVillager.class, AxisAlignedBB.getBoundingBox(xs, ys, zs, xf, yf, zf));
		EntityVillager ev = null;
		
		for (int j = 0; j < le.size(); j++)
			if (EntityVillager.class.isInstance(le.get(j)))
			{
				IdentityDocument id = null;
				EntityVillager t = EntityVillager.class.cast(le.get(j));
				long l1 = t.getEntityData().getLong("civid");
				
				if (l1 == searchId)
				{
					ev = t;
					break;
				}
			}
		
		return ev;
	}	
	public static EntityVillager GetVillager(long searchId, List<EntityVillager> le)
	{
		EntityVillager ev = null;
		
		for (int j = 0; j < le.size(); j++)
			if (EntityVillager.class.isInstance(le.get(j)))
			{
				IdentityDocument id = null;
				EntityVillager t = EntityVillager.class.cast(le.get(j));
				long l1 = t.getEntityData().getLong("civid");
				
				if (l1 == searchId)
				{
					ev = t;
					break;
				}
			}
		
		return ev;
	}
	public static List<EntityVillager> GetVillagers(World w, int x, int y, int z, int dXZ, int dY)
	{
		//Find villager
		//int x = p.x, y = p.y, z = p.z, 
		int distXZ = dXZ, distY = dY;				
		
  		int xs = x - distXZ, xf = x + distXZ;
  		int ys = y - distY, yf = y + distY;
  		int zs = z - distXZ, zf = z + distXZ;
  		
		List<Entity> le = w.getEntitiesWithinAABB(EntityVillager.class, AxisAlignedBB.getBoundingBox(xs, ys, zs, xf, yf, zf));
		List<EntityVillager> r = new ArrayList<EntityVillager>();
		
		for (int j = 0; j < le.size(); j++)
			if (EntityVillager.class.isInstance(le.get(j)))
			{
				IdentityDocument id = null;
				EntityVillager t = EntityVillager.class.cast(le.get(j));
				r.add(t);
			}
		
		return r;
	}
	public static boolean IsWithinDistance(Point3i pi, Point3d pd, double dist)
	{
		double dx = pi.x - pd.x;
		double dy = pi.y - pd.y;
		double dz = pi.z - pd.z;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}
	public static boolean IsWithinDistance(Point3i pi, Point3i pd, double dist)
	{
		double dx = pi.x - pd.x;
		double dy = pi.y - pd.y;
		double dz = pi.z - pd.z;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}	
	public static boolean IsWithinDistance(Point3i pi, Point3d pd, double dist, boolean excludeY)
	{
		double dx = pi.x - pd.x;
		double dy = pi.y - pd.y;
		double dz = pi.z - pd.z;
		
		if (excludeY)
			dy = 0;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}	
	public static boolean IsWithinDistance(Point3i pi, double pdx, double pdy, double pdz, double dist, boolean excludeY)
	{
		double dx = pi.x - pdx;
		double dy = pi.y - pdy;
		double dz = pi.z - pdz;
		
		if (excludeY)
			dy = 0;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}
	public static boolean IsWithinDistance(double x1, double y1, double z1, double x2, double y2, double z2, double dist, boolean excludeY)
	{
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;
		
		if (excludeY)
			dy = 0;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}
	
	public static double Distance(Point3i p1, Point3i p2)
	{
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		double dz = p1.z - p2.z;
		
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	public static double Distance(Point3d p1, Point3d p2)
	{
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		double dz = p1.z - p2.z;
		
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	public static double Distance(double p1x, double p1y, double p1z, int p2x, int p2y, int p2z)
	{
		double dx = p1x - p2x;
		double dy = p1y - p2y;
		double dz = p1z - p2z;
		
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	public static double Distance(int p1x, int p1y, int p1z, int p2x, int p2y, int p2z)
	{
		double dx = p1x - p2x;
		double dy = p1y - p2y;
		double dz = p1z - p2z;
		
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static Point3d GetLocation(EntityVillager v)
	{
		return new Point3d(v.posX, v.posY, v.posZ);
	}	
	public static Point3i GetLocation(TileEntity v)
	{
		return new Point3i(v.xCoord, v.yCoord, v.zCoord);
	}
	public static Point3i ToPoint3i(Point3d p)
	{
		return new Point3i((int)p.x, (int)p.y, (int)p.z);
	}
	public static Block GetBlock(World w, Point3i p)
	{
		if (p == null)
			return null;
		
		return w.getBlock(p.x, p.y, p.z);
	}	
	public static TileEntity GetTileEntity(World w, Point3i p)
	{
		if (p == null)
			return null;
		
		return w.getTileEntity(p.x, p.y, p.z);
	}
}
