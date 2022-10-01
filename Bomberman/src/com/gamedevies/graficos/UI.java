package com.gamedevies.graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.gamedevies.main.Game;

public class UI {
	
	public static BufferedImage LIFE_ICON = Game.spritesheet.getSprite(0, 32, 16, 16);

	private int frames = 0, maxFrames = 60;
	private int seconds = 0;
	private int minutes = 0;
	private boolean showInputGameOver = true;
	private int inputGameOverFrames = 0, inputGameOverMaxFrames = 30;
	private boolean showFinalMessage = true;
	private int finalMessageFrames = 0, finalMessageMaxFrames = 30;
	
	public void tick() {
		if (Game.gameState == "JOGANDO") {
			frames++;
			if (frames >= maxFrames) {
				frames = 0;
				seconds++;
				if (seconds >= 60) {
					seconds = 0;
					minutes++;
				}
			}
		}else if (Game.gameState == "GAMEOVER") {
			inputGameOverFrames++;
			if (inputGameOverFrames >= inputGameOverMaxFrames) {
				inputGameOverFrames = 0;
				if (showInputGameOver) {
					showInputGameOver = false;
				}else {
					showInputGameOver = true;
				}
			}
		}else if (Game.gameState == "FINAL") {
			finalMessageFrames++;
			if (finalMessageFrames >= finalMessageMaxFrames) {
				finalMessageFrames = 0;
				if (showFinalMessage) {
					showFinalMessage = false;
				}else {
					showFinalMessage = true;
				}
			}
		}
	
	}
	
	public void resetTime() {
		frames = 0;
		seconds = 0;
		minutes = 0;
	}
	
	public void render(Graphics g) {
		if (Game.gameState == "JOGANDO") {
			// BackGround UI
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, 48);
			
			// Life
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString("VIDAS: " + Game.life, 292, 32);
			
			// Time
			String formatTime = "";
			formatTime += "0"+minutes+":";
			if (seconds >= 10) {
				formatTime += seconds;
			}else {
				formatTime += "0"+seconds;
			}
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString("TEMPO: " + formatTime, 32, 32);
			
			// Score
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString("PONTOS: " + Game.score, Game.WIDTH*Game.SCALE - 184, 32);
		}
		else if (Game.gameState == "GAMEOVER") {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("GAME OVER!", ((Game.WIDTH*Game.SCALE)/2)-112, ((Game.HEIGHT*Game.SCALE)/2)-64);
			if(showInputGameOver) {
				g.setFont(new Font("Arial", Font.BOLD, 24));
				g.drawString("Pressione enter para reiniciar...", ((Game.WIDTH*Game.SCALE)/2)-180, (Game.HEIGHT*Game.SCALE)/2+8);
			}
		}else if (Game.gameState == "FINAL") {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("FIM DE JOGO", ((Game.WIDTH*Game.SCALE)/2)-112, ((Game.HEIGHT*Game.SCALE)/2)-64);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString("Obrigado por jogar!", ((Game.WIDTH*Game.SCALE)/2)-120, ((Game.HEIGHT*Game.SCALE)/2)-16);
			if (showFinalMessage) {
				g.setFont(new Font("Arial", Font.BOLD, 24));
				g.drawString("Pontuação final: " + Game.score, ((Game.WIDTH*Game.SCALE)/2)-122, ((Game.HEIGHT*Game.SCALE)/2)+32);
			}
		}
	}
	
}
