package com.gamedevies.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.gamedevies.entities.Entity;
import com.gamedevies.entities.Player;
import com.gamedevies.graficos.Spritesheet;
import com.gamedevies.graficos.UI;
import com.gamedevies.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	private BufferedImage image;

	public static World world;
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public static UI ui;
	
	public static int life = 3;
	public static int score = 0;
	
	public static String gameState = "MENU";
	
	public static String curLevel = "level1";
	
	public Menu menu;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		player = new Player(0,0,16,16,1, Game.spritesheet.getSprite(48, 16, 16, 16));
		world = new World("/" + curLevel + ".png");
		
		ui = new UI();
		menu = new Menu();
		
		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Bomberman Clone");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		Image icon = null;
		try {
			icon = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setIconImage(icon);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		
		if (world.numOfEnemies <= 0 && gameState == "JOGANDO") {
			World.nextLevel();
		}
		
		if (gameState == "JOGANDO") {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			if (life <= 0) {
				gameState = "GAMEOVER";
			}
		}else if (gameState == "MENU") {
			menu.tick();
		}
		ui.tick();
		
	}
	
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		/***/
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		if (gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Horizontal
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		// Vertical
		if (gameState == "JOGANDO") {
			if(e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
			}else if(e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
			}
		}else if (gameState == "MENU") {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				menu.up = true;
			}else if (e.getKeyCode() == KeyEvent.VK_S) {
				menu.down = true;
			}
		}
		
		
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.placeBomb = true;
		}
		
		if (gameState == "GAMEOVER") {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				World.restartGame(curLevel);
			}
		}else if (gameState == "MENU") {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu.isPressed = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Horizontal
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			player.curSprite = 0;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
			player.curSprite = 0;
		}
		// Vertical
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			player.curSprite = 0;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			player.curSprite = 0;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
