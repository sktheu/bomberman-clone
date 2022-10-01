package com.gamedevies.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.world.FloorTile;
import com.gamedevies.world.WallTile;
import com.gamedevies.world.World;

public class LifePack extends Entity {
	
	private int tileX = 0, tileY = 0;
	
	public LifePack(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		tileX = (int)(this.getX() / 16);
		tileY = (int)(this.getY() / 16);
	}
	
	public void render(Graphics g) {
		if (World.tiles[tileX + (tileY*World.WIDTH)] instanceof WallTile) {
			sprite = null;
		}else if (World.tiles[tileX + (tileY*World.WIDTH)] instanceof FloorTile) {
			sprite = Entity.LIFEPACK_SPRITE;
		}
		
		super.render(g);
	}

}
