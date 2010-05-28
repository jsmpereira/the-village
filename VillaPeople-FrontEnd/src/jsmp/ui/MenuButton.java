package jsmp.ui;

import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import viglaglepeopgle.renderable.Renderable;

public class MenuButton implements Renderable {
	
	private float x1, y1, x2, y2;
	private Font font;
	private String caption;
	private Texture texture;
	private int pickID;
	private boolean clicked = false;
	
	public MenuButton(float x1, float y1, float x2, float y2, String caption, int pickID) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.caption = caption;
		this.pickID = pickID;
		texture = null;
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 16);
		font = new TrueTypeFont(awtFont, false);
	}
	
	public void setTexture(String textureName) {
		String path = "textures/"+textureName;
		try {
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		if (this.pickID != 0) {
			System.out.println("Caption: "+this.caption+" => "+this.pickID);
			GL11.glLoadName(this.pickID);
		}
			
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		if (clicked)
			GL11.glColor3f(1.0f, 0.0f, 0.0f);
		else
			GL11.glColor3f(1.0f, 1.0f, 0.0f);
		
		// no texture for now
		if (this.texture != null) {
			this.texture.bind();
			//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT); 
		} else
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		//double scaleAmt = 10.0 * Math.sin(System.currentTimeMillis());
		//GL11.glScaled(scaleAmt, scaleAmt, 1.0);

		GL11.glBegin(GL11.GL_QUADS);
			if (this.texture != null)
				GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(this.x1,this.y1,0);
			if (this.texture != null)
				GL11.glTexCoord2f(this.texture.getWidth(),0);
			GL11.glVertex3f(this.x2, this.y1, 0);
			if (this.texture != null)
				GL11.glTexCoord2f(this.texture.getWidth(),this.texture.getHeight());
			GL11.glVertex3f(this.x2, this.y2, 0);
			if (this.texture != null)
				GL11.glTexCoord2f(0,this.texture.getHeight());
			GL11.glVertex3f(this.x1, this.y2 , 0);
		GL11.glEnd();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		//GL11.glRectf(this.x1, this.y1, this.x2, this.y2);
		
		if (!clicked)
			font.drawString(this.x1+100, this.y1+15, caption, Color.yellow);
		else
			font.drawString(this.x1+100, this.y1+15, caption, Color.red);
		this.clicked = false;
	}
	
	public void over(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		this.texture = null;
		this.clicked = true;
	}
}
