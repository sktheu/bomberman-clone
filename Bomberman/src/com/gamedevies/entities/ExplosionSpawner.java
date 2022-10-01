package com.gamedevies.entities;

import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;
import com.gamedevies.world.FloorTile;
import com.gamedevies.world.Tile;
import com.gamedevies.world.WallTile;
import com.gamedevies.world.World;

public class ExplosionSpawner extends Entity{

	private boolean canRight = false, canLeft = false, canUp = false, canDown = false;
	private int tileX = 0, tileY = 0;
	private int rightTile = 0, leftTile = 0, upTile = 0, downTile = 0;
	
	private int frames = 0, maxFrames = 60;
	private int lifeTime = 0, maxLifeTime = 1;
	
	private boolean cantRight = false, cantLeft = false, cantUp = false, cantDown = false;
	
	
	public ExplosionSpawner(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		tileX = (int)(this.getX()/World.TILE_SIZE);
		tileY = (int)(this.getY()/World.TILE_SIZE);

		rightTile = tileX + 1;
		leftTile = tileX - 1;
		upTile = tileY - 1;
		downTile = tileY + 1;
		
		Explosion explosion = new Explosion(tileX*16, tileY*16, 16, 16, 0, Game.spritesheet.getSprite(0, 96, 16, 16));
		Game.entities.add(explosion);
	}
	
	public void tick() {
		
		checkTile(this.getX(), this.getY());
		
		if (canRight) {
			//System.out.println("DIREITA");
			Explosion explosion = new Explosion(rightTile*16, tileY*16, 16, 16, 0, Game.spritesheet.getSprite(0, 96, 16, 16));
			Game.entities.add(explosion);
		}
		if(canLeft) {
			//System.out.println("ESQUERDA");
			Explosion explosion = new Explosion(leftTile*16, tileY*16, 16, 16, 0, Game.spritesheet.getSprite(0, 96, 16, 16));
			Game.entities.add(explosion);
		}
		if (canUp) {
			//System.out.println("CIMA");
			Explosion explosion = new Explosion(tileX*16, upTile*16, 16, 16, 0, Game.spritesheet.getSprite(0, 96, 16, 16));
			Game.entities.add(explosion);
		}
		if (canDown) {
			//System.out.println("BAIXO");
			Explosion explosion = new Explosion(tileX*16, downTile*16, 16, 16, 0, Game.spritesheet.getSprite(0, 96, 16, 16));
			Game.entities.add(explosion);
		}
		
		destroySelf();
	}
	
	public void destroySelf() {
		frames++;
		if(frames >= maxFrames) {
			frames = 0;
			lifeTime++;
			if(lifeTime >= maxLifeTime) {
				Game.entities.remove(this);
				return;
			}
		}
	}
	
	
	public void checkTile(int x, int y) {
		// RIGHT
		if (World.tiles[rightTile + (tileY * World.WIDTH)] instanceof WallTile || World.tiles[rightTile + (tileY * World.WIDTH)] instanceof FloorTile){
			if (World.tiles[rightTile + (tileY * World.WIDTH)].solid == false) {
				canRight = true;
				if (World.tiles[rightTile + (tileY * World.WIDTH)] instanceof WallTile) {
					World.tiles[rightTile + (tileY * World.WIDTH)] = new FloorTile(rightTile*16, tileY*16, Tile.TILE_FLOOR);
					cantRight = true;
					canRight = false;
				}else if (World.tiles[rightTile + (tileY * World.WIDTH)] instanceof FloorTile && !cantRight) {
					rightTile++;
				}
				
			}
		}else {
			canRight = false;
		}
		// LEFT
		if (World.tiles[leftTile + (tileY * World.WIDTH)] instanceof WallTile || World.tiles[leftTile + (tileY * World.WIDTH)] instanceof FloorTile) {
			if (World.tiles[leftTile + (tileY * World.WIDTH)].solid == false) {
				canLeft = true;
				if (World.tiles[leftTile + (tileY * World.WIDTH)] instanceof WallTile) {
					World.tiles[leftTile + (tileY * World.WIDTH)] = new FloorTile(leftTile*16, tileY*16, Tile.TILE_FLOOR);
					cantLeft = true;
					canLeft = false;
				}else if (World.tiles[leftTile + (tileY * World.WIDTH)] instanceof FloorTile && !cantLeft) {
					leftTile--;
				}
				
			}
		}else {
			canLeft = false;
		}
		// UP
		if (World.tiles[tileX + (upTile * World.WIDTH)] instanceof WallTile || World.tiles[tileX + (upTile * World.WIDTH)] instanceof FloorTile) {
			if (World.tiles[tileX + (upTile * World.WIDTH)].solid == false) {
				canUp = true;
				if (World.tiles[tileX + (upTile * World.WIDTH)] instanceof WallTile) {
					World.tiles[tileX + (upTile * World.WIDTH)] = new FloorTile(tileX*16, upTile*16, Tile.TILE_FLOOR);
					cantUp = true;
					canUp = false;
				}else if (World.tiles[tileX + (upTile * World.WIDTH)] instanceof FloorTile && !cantUp) {
					upTile--;
				}
	
			}
		}else {
			canUp = false;
		}
		// DOWN
		if (World.tiles[tileX + (downTile * World.WIDTH)] instanceof WallTile || World.tiles[tileX + (downTile * World.WIDTH)] instanceof FloorTile) {
			if (World.tiles[tileX + (downTile * World.WIDTH)].solid == false) {
				canDown = true;
				if (World.tiles[tileX + (downTile * World.WIDTH)] instanceof WallTile) {
					World.tiles[tileX + (downTile * World.WIDTH)] = new FloorTile(tileX*16, downTile*16, Tile.TILE_FLOOR);
					cantDown = true;
					canLeft = false;
				}else if (World.tiles[tileX + (downTile * World.WIDTH)] instanceof FloorTile && !cantDown) {
					downTile++;
				}
				
			}
		}else {
			canDown = false;
		}
	}

}
