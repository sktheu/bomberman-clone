package com.gamedevies.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamedevies.world.World;

public class Menu {
	
	private Image background;
	private String[] options = {"jogar", "sair"};
	private int currentOption = 0;
	private int maxOption = options.length-1;
	
	public boolean up = false, down = false;
	
	public boolean isPressed = false;
	
	public Menu() {
		try {
			background = ImageIO.read(getClass().getResource("/backgroundMenu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void tick() {
		if (up) {
			up = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}else if (down) {
			down = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if (isPressed) {
			if (options[currentOption] == "jogar") {
				Game.world.numOfEnemies = 0;
				World.restartGame(Game.curLevel);
			}else if (options[currentOption] == "sair") {
				System.exit(0);
			}
		}
	}
	
	public void render(Graphics g) {
		// Background
		g.drawImage(background, 0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE, null);
		
		
		// Credits
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Feito por: Matheus Santos Duca", 32, (Game.HEIGHT*Game.SCALE)-32);
		
		// Inputs
		g.setColor(Color.white);
		g.drawRect((Game.WIDTH*Game.SCALE)-172, ((Game.HEIGHT*Game.SCALE)/2)+60, 150, 60);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("WASD = Mover", (Game.WIDTH*Game.SCALE)-156, ((Game.HEIGHT*Game.SCALE)/2)+80);
		g.drawString("SPACE = Bomba", (Game.WIDTH*Game.SCALE)-162, ((Game.HEIGHT*Game.SCALE)/2)+110);
		
		// Menu options
		if (options[currentOption] != "jogar") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("JOGAR", ((Game.WIDTH*Game.SCALE)/2)-64, ((Game.HEIGHT*Game.SCALE)/2)+80);
		}else {
			g.setColor(Color.yellow);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("JOGAR", ((Game.WIDTH*Game.SCALE)/2)-64, ((Game.HEIGHT*Game.SCALE)/2)+80);
		}
		
		if (options[currentOption] != "sair") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("SAIR", ((Game.WIDTH*Game.SCALE)/2)-46, ((Game.HEIGHT*Game.SCALE)/2)+136);
		}else {
			g.setColor(Color.yellow);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("SAIR", ((Game.WIDTH*Game.SCALE)/2)-46, ((Game.HEIGHT*Game.SCALE)/2)+136);
		}
		
	}
	
}
