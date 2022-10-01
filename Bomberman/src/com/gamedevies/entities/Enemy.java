package com.gamedevies.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;
import com.gamedevies.world.World;

public class Enemy extends Entity{
	
	public boolean right = true,left = false, down = false, up = false;

	public BufferedImage[] ENEMY1_SPRITE_RIGHT;
	public BufferedImage[] ENEMY1_SPRITE_LEFT;
	public BufferedImage[] ENEMY2_SPRITE_RIGHT;
	public BufferedImage[] ENEMY2_SPRITE_LEFT;
	
	private boolean moved = false;
	
	private int frames = 0, maxFrames = 12;
	private int curSprite = 0, maxSprite = 4;
	
	private int right_dir = 1, left_dir = -1;
	private int dir = right_dir;
	
	private String who;
	
	private int points = 0;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite, String who) {
		super(x, y, width, height, speed, null);
		
		this.who = who;
		
		if (who == "WATER") {
			ENEMY1_SPRITE_RIGHT = new BufferedImage[4];
			ENEMY1_SPRITE_LEFT = new BufferedImage[4];
			for (int i = 0; i < 4; i++) {
				ENEMY1_SPRITE_RIGHT[i] = Game.spritesheet.getSprite(64 + (i*16), 80, 16, 16);
			}
			for (int i = 0; i < 4; i++) {
				ENEMY1_SPRITE_LEFT[i] = Game.spritesheet.getSprite(64 + (i*16), 64, 16, 16);
			}
			points = 100;
		}else if (who == "BOMB") {
			ENEMY2_SPRITE_RIGHT = new BufferedImage[4];
			ENEMY2_SPRITE_LEFT = new BufferedImage[4];
			
			for (int i = 0; i < 4; i++) {
				ENEMY2_SPRITE_RIGHT[i] = Game.spritesheet.getSprite(64 + (i*16), 96, 16, 16);
			}
			for (int i = 0; i < 4; i++) {
				ENEMY2_SPRITE_LEFT[i] = Game.spritesheet.getSprite(64 + (i*16), 112, 16, 16);
			}
			points = 200;
		}
		
		
	}
	
	public void tick() {
		// Movement
		moved = false;
		
		if(right) {
			if(World.isFree((int)(this.getX()+speed), this.getY())) {
				x+=speed;
				moved = true;
				if (Entity.rand.nextInt(100) < 35) {
					if (World.isFree(this.getX(), (int)(this.getY()+speed))) {
						right = false;
						down = true;
					}else if (World.isFree(this.getX(), (int)(this.getY()-speed))) {
						right = false;
						up = true;
					}
				}
			}else {
				right = false;
				dir = left_dir;
				left = true;
			}
		}
		
		if(left) {
			if(World.isFree((int)(this.getX()-speed), this.getY())) {
				x-=speed;
				dir = left_dir;
				moved = true;
				if (Entity.rand.nextInt(100) < 35) {
					if (World.isFree(this.getX(), (int)(this.getY()+speed))) {
						left = false;
						down = true;
					}else if (World.isFree(this.getX(), (int)(this.getY()-speed))) {
						left = false;
						up = true;
					}
				}
			}else {
				left = false;
				dir = right_dir;
				right = true;
			}
		}
		
		if (down) {
			if (World.isFree(this.getX(), (int)(this.getY()+speed))){
				y+=speed;
				moved = true;
				if (Entity.rand.nextInt(100) < 35) {
					if (World.isFree((int)(this.getX()+speed), this.getY())) {
						down = false;
						right = true;
					}else if (World.isFree((int)(this.getX()-speed), this.getY())) {
						down = false;
						left = true;
					}
				}
			}else {
				down = false;
				up = true;
			}
		}
		
		if (up) {
			if (World.isFree(this.getX(), (int)(this.getY()-speed))){
				y-=speed;
				moved = true;
				if (Entity.rand.nextInt(100) < 35) {
					if (World.isFree((int)(this.getX()+speed), this.getY())){
						up = false;
						right = true;
					}else if (World.isFree((int)(this.getX()-speed), this.getY())) {
						up = false;
						left = true;
					}
				}
			}else {
				up = false;
				down = true;
			}
		}
		
		//Collision
		checkCollisionWithExplosion();
		
		// Animation
		if (moved) {
			frames++;
			if (frames >= maxFrames) {
				frames = 0;
				curSprite++;
				if(curSprite == maxSprite) {
					curSprite = 0;
				}
			}
		}else {
			curSprite = 0;
		}
		
	}
	
	public void checkCollisionWithExplosion() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Explosion) {
				if (Entity.isColidding(this, atual)) {
					Game.score += points;
					if (who == "BOMB") {
						int tileX = (int)(this.getX()/World.TILE_SIZE);
						int tileY = (int)(this.getY()/World.TILE_SIZE);
						Bomb bomb = new Bomb(tileX*16, tileY*16, 16, 16, 0, Game.spritesheet.getSprite(0, 16, 16, 16));
						Game.entities.add(bomb);
					}
					Game.world.numOfEnemies--;
					Game.entities.remove(this);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		
		if (dir == right_dir) {
			if (who == "WATER") {
				sprite = ENEMY1_SPRITE_RIGHT[curSprite];
			}else if (who == "BOMB") {
				sprite = ENEMY2_SPRITE_RIGHT[curSprite];
			}
		}else if (dir == left_dir) {
			if (who == "WATER") {
				sprite = ENEMY1_SPRITE_LEFT[curSprite];
			}else if (who == "BOMB") {
				sprite = ENEMY2_SPRITE_LEFT[curSprite];
			}
		}
		
		super.render(g);
	}

}
