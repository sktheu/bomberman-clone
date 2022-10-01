package com.gamedevies.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;
import com.gamedevies.world.World;

public class Explosion extends Entity {

	public static BufferedImage[] EXPLOSION_ANIM;
	
	private int frames = 0, maxFrames = 8;
	private int curSprite = 0, maxSprite = 4;
	
	
	public Explosion(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		EXPLOSION_ANIM = new BufferedImage[4];
		
		for (int i = 0; i < 4; i++) {
			EXPLOSION_ANIM[i] = Game.spritesheet.getSprite(0 + (i*16), 96, 16, 16);
		}
		
	}
	
	public void tick() {
		depth = 1;
		destroySelf();
	}
	
	public void destroySelf() {
		frames++;
		if(frames >= maxFrames) {
			frames = 0;
			curSprite++;
			if (curSprite == maxSprite) {
				Game.entities.remove(this);
				return;
			}
		}
	}
	
	public void render(Graphics g) {
		
		sprite = EXPLOSION_ANIM[curSprite];
		
		super.render(g);
	}

}
