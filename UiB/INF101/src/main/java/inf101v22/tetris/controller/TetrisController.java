package inf101v22.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

import inf101v22.tetris.midi.TetrisSong;
import inf101v22.tetris.model.GameScreen;
import inf101v22.tetris.model.GameState;
import inf101v22.tetris.net.client.ClientController;
import inf101v22.tetris.net.server.ServerController;
import inf101v22.tetris.view.TetrisView;

public class TetrisController implements KeyListener, ActionListener {
	
	private TetrisControllable model;
	private TetrisView view;
	private Timer timer;
	private Timer fastTimer;
	private TetrisSong song;
	private boolean multiplayer;
	String rowAnimationSound = "rowAnimationSound.wav";
	ClientController clientController;
	ServerController serverController;

	public TetrisController(TetrisControllable model, TetrisView view) {
		this.model = model;
		this.view = view;
		this.multiplayer = false;
		view.addKeyListener(this);
		timer = new Timer(model.calculateStep(), this);
		fastTimer = new Timer(40, extraListener);
		timer.start();
		fastTimer.start();
		song = new TetrisSong();
		
		
	}
	
	/**
	 * this extra listener is used for multiplayer and doing row animation
	 */
	ActionListener extraListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			    
			if (model.getGameScreen() == GameScreen.GAME_OVER) {
				song.doStopMidiSounds();
			}
			
			// the multiplayer version is still not 100%. Currently set up for use within localhost
			if (multiplayer) {
				
				if (clientController.tilesToBeAdded()) {
					clientController.applyTiles();
					clientController.client.attack = false;
				}
				
				if (model.canSendToEnemy()) {
					clientController.update();
					clientController.sendData();
					model.setSendToEnemy(false);
				}
			}
			
			
			if (model.getGameState() == GameState.VISUALEFFECT ) {
				
				model.doVisualEffect();
				
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(rowAnimationSound).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (Exception err) {
					err.printStackTrace();
				}
				
			} 
			view.repaint();
		}
	};

	@Override
	public void keyPressed(KeyEvent e) {
		if (model.getGameScreen() == GameScreen.WELCOME_SCREEN) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				song.run();
				model.setGameScreen(GameScreen.ACTIVE_GAME);
			}
			if (e.getKeyCode() == KeyEvent.VK_N) {
				serverController = new ServerController(model);
				clientController = new ClientController(model);
				model.setGameScreen(GameScreen.ACTIVE_GAME);
				multiplayer = true;
				song.run();
				
			}
			if (e.getKeyCode() == KeyEvent.VK_C) {
				clientController = new ClientController(model);
				multiplayer = true;
				song.run();
				model.setGameScreen(GameScreen.ACTIVE_GAME);
				
			}
			
		}
		if (model.getGameScreen() == GameScreen.ACTIVE_GAME ) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				model.moveFallingPiece(0, -1);
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				model.moveFallingPiece(0, 1);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(model.moveFallingPiece(1, 0)) {
					timer.restart();
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				model.rotateFallingPiece("right");
			}
			else if (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_META) {
				model.rotateFallingPiece("left");
			}
			
			else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				song.doPauseMidiSounds();
			}
			
			else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				model.dropFallingPiece();
				timer.restart();
				//fastTimer.restart();
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				model.setGameScreen(GameScreen.PAUSE);
				song.doPauseMidiSounds();
			}
			
			view.repaint();
		}
		if (model.getGameScreen() == GameScreen.GAME_OVER) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				song.run();
				model.resetGame();
				updateDelay();
			}
			else if (e.getKeyCode() == KeyEvent.VK_E) {
				 System.exit(0);
			}
		}
		
		if (model.getGameScreen() == GameScreen.PAUSE && model.getGameScreen() != GameScreen.GAME_OVER) {
			 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					model.setGameScreen(GameScreen.ACTIVE_GAME);
					song.doUnpauseMidiSounds();
				}
			 else if (e.getKeyCode() == KeyEvent.VK_E) {
				 System.exit(0);
			}
		}
		
		
	}

	
	private void updateDelay() {
		timer.setDelay(model.calculateStep());
		timer.setInitialDelay(model.calculateStep());
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (model.getGameScreen() == GameScreen.ACTIVE_GAME && model.getGameState() == GameState.NORMAL) {
			model.clockTick();
			
			view.repaint();
			updateDelay();
		}
		
	}
	
	


	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
