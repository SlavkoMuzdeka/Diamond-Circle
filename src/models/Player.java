package models;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import figures.*;
import services.PlayerService;

public class Player extends Thread {
	private String playerName;
	private ArrayList<Figure> figures;
	private ArrayList<Figure> pomFigures;

	private Deck deckOfCards;
	private Integer rows, columns;
	private boolean hasGameFinished;

	public static Handler handler;
	public static Integer CURR_FIGURE = 1;
	public static Integer CURR_FIELD;
	public static Integer NEXT_FIELD;
	public static JLabel label = new JLabel();
	public static Integer FINISHED_GAME = 0;
	public static Integer NUM_OF_PLAYERS = PlayerService.NUM_OF_PLAYERS;

	{
		try {
			handler = new FileHandler("Player.log");
			Logger.getLogger(Player.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(Player.class.getName()).addHandler(handler);
		} catch (IOException ex) {
			Logger.getLogger(PlayerService.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

	public Player() {
		super();
		figures = new ArrayList<>();
	}

	public Player(String playerName, ArrayList<Figure> figures) {
		super();
		this.playerName = playerName;
		this.figures = new ArrayList<>();
		this.pomFigures = new ArrayList<>();
		for (Integer i = 0; i < figures.size(); i++) {
			this.figures.add(figures.get(i));
			this.pomFigures.add(figures.get(i));
		}
		deckOfCards = new Deck();
		rows = PlayerService.NUM_OF_ROWS;
		columns = PlayerService.NUM_OF_COLUMNS;
		this.hasGameFinished = false;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public synchronized ArrayList<Figure> getFigures() {
		return figures;
	}

	public synchronized void setFigures(ArrayList<Figure> figures) {
		this.figures = figures;
	}

	public synchronized boolean hasGameFinished() {
		return hasGameFinished;
	}

	public synchronized void setHasGameFinished(boolean hasGameFinished) {
		this.hasGameFinished = hasGameFinished;
	}

	public ArrayList<Figure> getPomFigures() {
		return pomFigures;
	}

	public void setPomFigures(ArrayList<Figure> pomFigures) {
		this.pomFigures = pomFigures;
	}

	@Override
	public String toString() {
		String s = "";
		for (Integer i = 0; i < figures.size(); i++) {
			s += "Player name: " + playerName + ", his figures are:\n" + figures.get(i) + "\n";
		}
		return s;
	}

	private synchronized void setDescription(Figure f, Integer br) throws Exception {
		if (f.getPomPath().size() != 0) {
			f.setPrevField(f.getPomPath().get(0));
			if ((br - 1) != 0) {
				f.getPomPath().remove(0);
			}
			for (Integer i = 0; i < br - 1; i++) {
				if ((i + 1) == br - 1 && f.getPomPath().size() != 0) {
					f.setNextField(f.getPomPath().get(0));
				} else if (i < br - 1 && f.getPomPath().size() != 0) {
					f.getPomPath().remove(0);
				}
			}
			CURR_FIELD = f.getPrevField();
			if (Deck.NUM_OF_FIELDS > f.getPomPath().size() && PlayerService.pathOfMovement.size() != 0) {
				NEXT_FIELD = PlayerService.path.peekLast();
			} else {
				NEXT_FIELD = f.getNextField();
			}
		}
	}

	private void mannerOfMovement(Figure f) throws Exception {
		if (f instanceof OrdinaryFigure || f instanceof HoveringFigure) {
			if (f.getPath().size() == PlayerService.path.size()) {
				f.setNumberOfFields(Deck.NUM_OF_FIELDS + 1);
				setDescription(f, f.getNumberOfFileds());
			} else {
				f.setNumberOfFields(Deck.NUM_OF_FIELDS);
				setDescription(f, f.getNumberOfFileds() + 1);
			}
			Deck.NUMBER_OF_SQUARES_TO_PASS = Deck.NUM_OF_FIELDS;
			CURR_FIGURE = f.getFigureNumber();
		} else if (f instanceof SuperFastFigure) {
			if (f.getPath().size() == PlayerService.pathOfMovement.size()) {
				f.setNumberOfFields((2 * Deck.NUM_OF_FIELDS) + 1);
				setDescription(f, f.getNumberOfFileds());
			} else {
				f.setNumberOfFields(2 * Deck.NUM_OF_FIELDS);
				setDescription(f, f.getNumberOfFileds() + 1);
			}
			Deck.NUMBER_OF_SQUARES_TO_PASS = 2 * Deck.NUM_OF_FIELDS;
			CURR_FIGURE = f.getFigureNumber();
		}
	}

	private void setColor(Integer fieldNumber, Figure f, Color color) throws Exception {
		color = new Color(238, 238, 238);
		for (Integer i = 0; i < rows; i++) {
			for (Integer j = 0; j < columns; j++) {
				if ((i * rows) + (j + 1) == fieldNumber && Map.matrix[i][j] == f) {
					Map.panels[i][j].setBackground(color);
					Map.matrix[i][j] = null;
				}
				Map.panels[i][j].remove(label);
				Map.panels[i][j].revalidate();
			}
		}
	}

	private void updateNumberOfFields(Figure f) throws Exception {
		Integer numberOfFields = f.getNumberOfFileds();
		numberOfFields--;
		f.setNumberOfFields(numberOfFields);
	}

	private void setFigureType(Figure f, Integer i, Integer j) throws Exception {
		if (f instanceof OrdinaryFigure) {
			label.setText("o");
		} else if (f instanceof HoveringFigure) {
			label.setText("l");
		} else if (f instanceof SuperFastFigure) {
			label.setText("s");
		}
		Map.panels[i][j].add(label);
		Map.panels[i][j].revalidate();
	}

	private synchronized void makeAMove() throws Exception {
		if (Deck.NUM_OF_FIELDS == 0) {
			CURR_FIGURE = 0;
			Deck.NUMBER_OF_SQUARES_TO_PASS = 0;
			CURR_FIELD = 0;
			NEXT_FIELD = 0;
			sleep(2000);
		} else {
			Figure f = null;
			if (figures.size() != 0) {
				f = figures.get(0);
			} else {
				this.hasGameFinished = true;
			}
			Color color = new Color(238, 238, 238);
			mannerOfMovement(f);
			while (f != null && f.getNumberOfFileds() != 0 && f.getPath().size() != 0) {
				Integer currP = f.getPath().get(0);
				f.getMovementHistory().addCoordinate(currP);
				f.setCurrField(currP);
				for (Integer i = 0; i < rows; i++) {
					for (Integer j = 0; j < columns; j++) {
						if ((i * rows) + (j + 1) == currP) {
							if (Map.matrix[i][j] == null) {
								setColor(f.getPrevField(), f, color);
								color = Map.panels[i][j].getBackground();
								Map.panels[i][j].setBackground(f.getColor());
								setFigureType(f, i, j);
								Map.matrix[i][j] = f;
								updateNumberOfFields(f);
							} else if (Map.matrix[i][j] instanceof Figure) {
								if (f.getNumberOfFileds() == 1) {
									setColor(f.getPrevField(), f, color);
									if (f.getPomPath().size() != 0) {
										f.getPomPath().remove(0);
										NEXT_FIELD = f.getPomPath().get(0);
									}
								} else {
									setColor(f.getPrevField(), f, color);
									updateNumberOfFields(f);
								}
							} else if (Map.matrix[i][j] instanceof Diamond) {
								setColor(f.getPrevField(), f, color);
								Map.panels[i][j].setBackground(f.getColor());
								Map.matrix[i][j] = f;
								if (f.getPomPath().size() != 0) {
									f.getPomPath().remove(0);
									if (f.getPomPath().size() != 0) {
										NEXT_FIELD = f.getPomPath().get(0);
										Deck.NUMBER_OF_SQUARES_TO_PASS++;
									} else {
										NEXT_FIELD = PlayerService.path.peekLast();
									}
								} else {
									if (PlayerService.path.size() != 0) {
										NEXT_FIELD = PlayerService.path.peekLast();
									}
									Deck.NUMBER_OF_SQUARES_TO_PASS++;
								}
								setFigureType(f, i, j);
							}
						}
					}
				}
				sleep(1000);
				f.setPrevField(currP);
				f.getPath().remove(0);
				if (f.getPath().size() == 0) {
					f.setHasArrived(true);
					f.setNumberOfFields(0);
					setColor(f.getPrevField(), f, color);
					ArrayList<Figure> pomLista = this.getFigures();
					pomLista.remove(0);
					this.setFigures(pomLista);
				}
			}
		}
	}

	private void deletePlayer(String name) {
		for (Integer i = 0; i < PlayerService.players.size(); i++) {
			if (PlayerService.players.get(i).getName().equals(name)) {
				PlayerService.players.remove(PlayerService.players.get(i));
			}
		}
	}

	@Override
	public synchronized void run() {
		try {
			deckOfCards = Map.deck;
			while (hasGameFinished == false) {
				wait();
				if (hasGameFinished == false) {
					makeAMove();
				}
				if (hasGameFinished == true) {
					FINISHED_GAME++;
					PlayerService.NUM_OF_PLAYERS--;
					if (FINISHED_GAME == NUM_OF_PLAYERS) {
						if (Map.GAME_HAS_STOPPED == false) {
							Map.NUM_OF_PLAYED_GAMES++;
						}
						synchronized (deckOfCards) {
							deckOfCards.notify();
						}
						Map.END = true;
					} else {
						synchronized (deckOfCards) {
							deckOfCards.notify();
						}
					}
				} else {
					synchronized (deckOfCards) {
						deckOfCards.notify();
					}
				}
			}
			deletePlayer(this.getName());
			Deck.finishedGame.add(this);
		} catch (Exception ex) {
			Logger.getLogger(Player.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

}
