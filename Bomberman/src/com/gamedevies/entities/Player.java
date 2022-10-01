package com.gamedevies.entities;



import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;
import com.gamedevies.world.Camera;
import com.gamedevies.world.World;


public class Player extends Entity{

	
	public static BufferedImage[] PLAYER_SPRITE_RIGHT;

	public static BufferedImage[] PLAYER_SPRITE_LEFT;
	
	public static BufferedImage[] PLAYER_SPRITE_UP;
	
	public static BufferedImage[] PLAYER_SPRITE_DOWN;
	
	
	public boolean right,left,up,down;
	
	
	private int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	private int dir = right_dir;
	
	private int frames = 0, maxFrames = 12;
	public int curSprite = 0; 
	private int maxSprite = 4;
	
	private boolean moved = false;
	
	public boolean placeBomb = false;
	
	private int time = 0, coolDownTime = 60*2;
	
	public int spawnPointX = 0, spawnPointY = 0;
	
	private boolean isDamaged = false;
	private int timeIsDamaged = 0, coolDownIsDamaged = 3*60;
	
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		
		PLAYER_SPRITE_UP = new BufferedImage[4];
		PLAYER_SPRITE_DOWN = new BufferedImage[4];
		PLAYER_SPRITE_RIGHT = new BufferedImage[4];
		PLAYER_SPRITE_LEFT = new BufferedImage[4];
		
		
		for (int i = 0; i < 4; i++) {
			PLAYER_SPRITE_UP[i] = Game.spritesheet.getSprite(48 + (i*16), 16, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			PLAYER_SPRITE_DOWN[i] = Game.spritesheet.getSprite(48 + (i*16), 0, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			PLAYER_SPRITE_RIGHT[i] = Game.spritesheet.getSprite(48 + (i*16), 32, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			PLAYER_SPRITE_LEFT[i] = Game.spritesheet.getSprite(48 + (i*16), 48, 16, 16);
		}
		
		
		
	}
	
	public void tick(){
		depth = 2;
		
		// Movement
		moved = false;
		
		if(right && World.isFree((int)(this.getX()+speed), this.getY())) {	
			x+=speed;
			moved = true;
			dir = right_dir;
		}
		else if(left && World.isFree((int)(this.getX()-speed), this.getY())) {
			x-=speed;
			moved = true;
			dir = left_dir;
		}
		
		if (up && World.isFree(this.getX(), (int)(this.getY()-speed))) {
			y-=speed;
			moved = true;
			dir = up_dir;
		}else if (down && World.isFree(this.getX(), (int)(this.getY()+speed))) {
			y+=speed;
			moved = true;
			dir = down_dir;
		}
		
		// Bomb System
		
		if (placeBomb) {
			placeBomb = false;
			if (time == 0) {
				int tileX = this.getX()/World.TILE_SIZE;
				int tileY = this.getY()/World.TILE_SIZE;
				
				Bomb bomb = new Bomb(tileX*16, tileY*16, 16, 16, 0, Game.spritesheet.getSprite(0, 16, 16, 16));
				Game.entities.add(bomb);
				time = coolDownTime;
			}
		}
		
		if (time > 0) {
			time--;
			if (time <= 0) {
				time = 0;
			}
		}
		
		
		// Animation
		if (moved) {
			frames++;
			if (frames >= maxFrames) {
				frames = 0;
				curSprite++;
				if (curSprite == maxSprite) {
						curSprite = 0;
				}
			}
		}
		
		// Collision
		checkCollisionWithEnemy();
		checkCollisionWithExplosion();
		checkCollisionWithLifeItem();
		
		if (isDamaged) {
			if (timeIsDamaged > 0) {
				timeIsDamaged--;
			}else {
				timeIsDamaged = 0;
				isDamaged = false;
			}
		}
		
		Camera.x = Camera.clamp((int)x - Game.WIDTH / 2, 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp((int)y - Game.HEIGHT / 2, 0, World.HEIGHT * 16 - Game.HEIGHT);
		
		
	}
	
	public void checkCollisionWithLifeItem() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColidding(this, atual)) {
					Game.life++;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionWithExplosion() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Explosion) {
				if (Entity.isColidding(this, atual) && !isDamaged) {
					this.setX(spawnPointX);
					this.setY(spawnPointY);
					Game.life--;
					isDamaged = true;
					timeIsDamaged = coolDownIsDamaged;
					if (Game.life <= 0) {
						// Game Over :(
					}
				}
			}
		}
	}
	
	public void checkCollisionWithEnemy() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Enemy) {
				if (Entity.isColidding(this, atual) && !isDamaged) {
					this.setX(spawnPointX);
					this.setY(spawnPointY);
					Game.life--;
					isDamaged = true;
					timeIsDamaged = coolDownIsDamaged;
					if (Game.life <= 0) {
						//Game Over :(
					}
				}
			}
		}
	}
	
	public void render(Graphics g){
		
		if (dir == right_dir) {
			sprite = PLAYER_SPRITE_RIGHT[curSprite];
		}else if (dir == left_dir) {
			sprite = PLAYER_SPRITE_LEFT[curSprite];
		}
		if (dir == up_dir) {
			sprite = PLAYER_SPRITE_UP[curSprite];
		}else if (dir == down_dir) {
			sprite = PLAYER_SPRITE_DOWN[curSprite];
		}
		
		super.render(g);
	}
	

	


}
