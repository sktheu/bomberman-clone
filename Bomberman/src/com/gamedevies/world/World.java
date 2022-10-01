package com.gamedevies.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamedevies.entities.Enemy;
import com.gamedevies.entities.Entity;
import com.gamedevies.entities.LifePack;
import com.gamedevies.entities.Player;
import com.gamedevies.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public  int numOfEnemies = 0;
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); // Localiza a imagem do mapa e armazena nessa BufferedImage.
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH*HEIGHT]; // Array que irá armazenar todos os pixels da imagem do mapa, tendo o tamanho definido da largura X altura do mapa.
			tiles = new Tile[WIDTH*HEIGHT]; 
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH); // Armazena todos os pixels da imagem do mapa no array pixels.
			
			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int pixelAtual = pixels[xx+(yy*WIDTH)];
					
					switch(pixelAtual) {
						case 0xFFFFFFFF:
							// Wall / Solid
							tiles[xx+(yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_SOLID);	
							tiles[xx+(yy*WIDTH)].solid = true;
						break;
						case 0xFF000000:
							// Floor
							tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
						break;
						case 0xFF2200FF:
							// Player
							tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
							Game.player.spawnPointX = xx*16;
							Game.player.spawnPointY = yy*16;
						break;
						case 0xFF484848:
							// Wall / Brick
							tiles[xx+(yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_BRICK);
						break;
						case 0xFFFF0000:
							// Enemy / Water Guy
							tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
							Enemy enemyWater = new Enemy(xx*16, yy*16, 16, 16, 1, null, "WATER");
							Game.entities.add(enemyWater);
							numOfEnemies++;
						break;
						case 0xFF7300FF:
							// Enemy / Bomb Guy
							tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
							Enemy enemyBomb = new Enemy(xx*16, yy*16, 16, 16, 1, null, "BOMB");
							numOfEnemies++;
							Game.entities.add(enemyBomb);
						break;
						case 0xFF00FF22:
							// LifePack
							tiles[xx+(yy*World.WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_BRICK);
							LifePack lifePack = new LifePack(xx*16, yy*16, 16, 16, 0, Entity.LIFEPACK_SPRITE);
							Game.entities.add(lifePack);
						break;
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void nextLevel() {
		Game.player = new Player(0, 0, 16, 16, 1, Game.spritesheet.getSprite(48, 0, 16, 16));
		Game.entities.clear();
		Game.entities.add(Game.player);
		Game.ui.resetTime();
		if (Game.curLevel == "level1") {
			Game.world = new World("/level2.png");
			Game.curLevel = "level2";
		}else if (Game.curLevel == "level2") {
			Game.world = new World("/level3.png");
			Game.curLevel = "level3";
		}else if (Game.curLevel == "level3") {
			Game.gameState = "FINAL";
		}
	}
	
	public static void restartGame(String level) {
		Game.gameState = "JOGANDO";
		Game.player = new Player(0, 0, 16, 16, 1, Game.spritesheet.getSprite(48, 0, 16, 16));
		Game.entities.clear();
		Game.entities.add(Game.player);
		Game.life = 3;
		Game.score = 0;
		Game.ui.resetTime();
		Game.world = new World("/"+Game.curLevel+".png");
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(){
		//TODO: Aplicar método para reiniciar o jogo corretamente.
		return;
	}
	
	public void render(Graphics g){
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx+yy*WIDTH].render(g);
			}
		}
		
	}
	
}
