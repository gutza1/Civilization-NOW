package civ.Entity.AI;

import civ.Entity.AI.AIHelper.AIState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;

public class AICivIdle extends EntityAIBase 
{
	//Static Getter
	public static AICivIdle GetAI(EntityVillager ev)
	{
		AICivIdle ai = null;
		for (int i = 0; i < ev.tasks.taskEntries.size(); i++)
			if (AICivIdle.class.isInstance(ev.tasks.taskEntries.get(i)))
			{
				if (ai == null)
					ai = (AICivIdle)ev.tasks.taskEntries.get(i);
				else
				{
					ai = null;
					break;		
				}
			}
		
		if (ai == null)
			ai = AIHelper.SetAIState(ev, AIState.Idle, false);
		
		return ai;
	}
	
	//Fields
	protected EntityVillager _myVillager = null;	
	protected AIState _myState = null;
	
	//Setters
	public void SetState(AIState state)
	{
		this._myState = state;
	}
	
	//Constructors
	public AICivIdle(EntityVillager v)
	{
		this._myVillager = v;
	}

	//Overridden Mod-Functions
	@Override
	public boolean shouldExecute()
	{
		return false;
	}
	@Override
    public boolean continueExecuting()
    {
        return this.shouldExecute();
    }
	

}
