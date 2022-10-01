package com.gamedevies.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamedevies.main.Game;

public class Tile {
	
	// Floor
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0,0,16,16);
	
	// Wall
	public static BufferedImage TILE_SOLID = Game.spritesheet.getSprite(16,0,16,16);
	public static BufferedImage TILE_BRICK = Game.spritesheet.getSprite(32, 0, 16, 16);

	public BufferedImage sprite;
	private int x,y;
	
	public boolean solid = false;
	
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
