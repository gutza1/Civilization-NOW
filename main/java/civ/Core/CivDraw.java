package civ.Core;

import javax.vecmath.Point3i;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class CivDraw {

	public static void DrawLine(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		GL11.glPushMatrix();
		
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(3.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		GL11.glColor4d(1, 1, 1, 1);

		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(x1, y1, z1);
			GL11.glVertex3d(x2, y2, z2);
			//GL11.glVertex3d(ep.posX, ep.posY, ep.posZ);
		}
		GL11.glEnd();
		
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		
		GL11.glPopMatrix();
	}	
	public static void DrawLines(Point3i[] ps)
	{			
		GL11.glPushMatrix();
		
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(3.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		GL11.glColor4d(1, 1, 1, 1);

		GL11.glBegin(GL11.GL_LINES);
		{
			for (int i = 0; i < ps.length; i++)
				GL11.glVertex3d(ps[i].x, ps[i].y, ps[i].z);
			//GL11.glVertex3d(x2, y2, z2);
			//GL11.glVertex3d(ep.posX, ep.posY, ep.posZ);
		}
		GL11.glEnd();
		
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		
		GL11.glPopMatrix();
	}
	//public static void DrawBox(Point3i topNE, Point3i botSW)
	public static void DrawBox2(double topNEx, double topNEy, double topNEz, double botSWx, double botSWy, double botSWz)
	{
		GL11.glPushMatrix();
		
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(3.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		GL11.glColor4d(1, 1, 1, 1);

		//Draw First WireFrame
		
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(topNEx, topNEy, topNEz);
			GL11.glVertex3d(topNEx, topNEy, botSWz);
			GL11.glVertex3d(topNEx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, topNEy, botSWz);
			
			GL11.glVertex3d(botSWx, topNEy, topNEz);
			GL11.glVertex3d(botSWx, botSWy, topNEz);
			GL11.glVertex3d(topNEx, botSWy, topNEz);
			GL11.glVertex3d(topNEx, topNEy, topNEz);
		}
		GL11.glEnd();

		//GL11.glBegin(GL11.GL_LINES);
		{
			//GL11.glVertex3d(x2, y2, z2);
		}
		//GL11.glEnd();
		
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		
		GL11.glPopMatrix();
		
	}
	public static void DrawBox(double topNEx, double topNEy, double topNEz, double botSWx, double botSWy, double botSWz)
	{
		//GL11.glColor4f(1.0F, 0.0F, 0.0F, 1F);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glPushMatrix();
		//GL11.glPushAttrib(GL11.)
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);		
		//GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);		

		//Draw First WireFrame
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glBegin(GL11.GL_LINES);
		{
			//Top Box			
			GL11.glVertex3d(topNEx, topNEy, topNEz);
			GL11.glVertex3d(topNEx, topNEy, botSWz);
			GL11.glVertex3d(topNEx, topNEy, botSWz);
			GL11.glVertex3d(botSWx, topNEy, botSWz);
			GL11.glVertex3d(botSWx, topNEy, botSWz);
			GL11.glVertex3d(botSWx, topNEy, topNEz);
			GL11.glVertex3d(botSWx, topNEy, topNEz);
			GL11.glVertex3d(topNEx, topNEy, topNEz);

			//Bottom Box			
			GL11.glVertex3d(topNEx, botSWy, topNEz);
			GL11.glVertex3d(topNEx, botSWy, botSWz);
			GL11.glVertex3d(topNEx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, botSWy, topNEz);
			GL11.glVertex3d(botSWx, botSWy, topNEz);
			GL11.glVertex3d(topNEx, botSWy, topNEz);
			
			//Legs
			GL11.glVertex3d(topNEx, topNEy, topNEz);
			GL11.glVertex3d(topNEx, botSWy, topNEz);
			GL11.glVertex3d(topNEx, topNEy, botSWz);
			GL11.glVertex3d(topNEx, botSWy, botSWz);
			GL11.glVertex3d(botSWx, topNEy, botSWz);
			GL11.glVertex3d(botSWx, botSWy, botSWz);	
			GL11.glVertex3d(botSWx, topNEy, topNEz);
			GL11.glVertex3d(botSWx, botSWy, topNEz);
			
			
		}
		GL11.glEnd();
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		//GL11.glBegin(GL11.GL_LINES);
		{
			//GL11.glVertex3d(x2, y2, z2);
		}
		//GL11.glEnd();
		
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);

		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glPopMatrix();
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		
	}
}
