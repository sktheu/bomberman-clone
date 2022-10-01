package com.gamedevies.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;

public class Bomb extends Entity{
	
	private int frames = 0, maxFrames = 60;
	private int lifeTime = 0, maxLifeTime = 2;
	
	private int curSprite = 0, maxSprite = 3;
	private int framesAnim = 0, maxFramesAnim = 10;
	
	
	public static BufferedImage[] BOMB_SPRITE;

	public Bomb(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		BOMB_SPRITE = new BufferedImage[3];
		
		for (int i = 0; i < 3; i++) {
			BOMB_SPRITE[i] = Game.spritesheet.getSprite(0 + (i*16), 16, 16, 16);
		}
	}
	
	public void tick() {
		
		destroySelf();
		tickAnim();
	}
	
	public void destroySelf() {
		frames++;
		if (frames >= maxFrames) {
			frames = 0;
			lifeTime++;
			if (lifeTime >= maxLifeTime) {
				ExplosionSpawner explosionSpawner = new ExplosionSpawner(this.getX(), this.getY(), 16, 16, 0, null);
				Game.entities.add(explosionSpawner);
				Game.entities.remove(this);
				return;
			}
		}
	}
	
	public void tickAnim() {
		framesAnim++;
		if (framesAnim >= maxFramesAnim) {
			framesAnim = 0;
			curSprite++;
			if (curSprite == maxSprite) {
				curSprite = 0;
			}
		}
	}
	
	public void render(Graphics g) {
		sprite = BOMB_SPRITE[curSprite];
		
		super.render(g);
	}

}
