package models;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.swing.*;
import cards.Card;
import cards.OrdinaryCard;
import cards.SpecialCard;
import figures.Figure;
import figures.HoveringFigure;
import figures.OrdinaryFigure;
import figures.SuperFastFigure;
import services.PlayerService;

public class Deck extends Thread {

	private ArrayList<Card> deck;
	private Boolean hasFinished;

	private Object[][] m;
	private JPanel[][] mPanela;
	public JPanel panel = Map.panelImageNumber;

	public static Handler handler;
	public static Integer NUM_OF_FIELDS;
	public static Integer CURR_PLAYER = 1;
	public static Integer NUMBER_OF_SQUARES_TO_PASS;
	public static Integer[] holePosition;
	public static ArrayList<Player> finishedGame = new ArrayList<>();

	{
		try {
			handler = new FileHandler("Deck.log");
			Logger.getLogger(Deck.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(Deck.class.getName()).addHandler(handler);
		} catch (IOException ex) {
			Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

	public Deck() {
		super();
	}

	public Deck(Object[][] m, JPanel[][] mPanela) throws IOException {
		deck = new ArrayList<>();
		initDeck();
		this.m = m;
		this.mPanela = mPanela;
		hasFinished = false;
	}

	public synchronized Boolean hasFinished() {
		return hasFinished;
	}

	public synchronized void setHasFinished(Boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	private void initDeck() throws IOException {
		BufferedImage img1 = ImageIO.read(new File(Map.DIAMANT1_IMAGE));
		BufferedImage img2 = ImageIO.read(new File(Map.DIAMANT1_IMAGE));
		for (Integer i = 0; i < 10; i++) {
			deck.add(new OrdinaryCard(img1, 1));
			deck.add(new OrdinaryCard(img1, 2));
			deck.add(new OrdinaryCard(img1, 3));
			deck.add(new OrdinaryCard(img1, 4));
		}
		for (Integer i = 0; i < 12; i++) {
			deck.add(new SpecialCard(img2));
		}
	}

	private void setOrdinaryCard(Card card, JPanel panel1, JPanel panel2, JPanel panel3) throws Exception {
		configurePanels(card, panel1, panel2, panel);

		JLabel bp = new JLabel("Number of fields: " + ((OrdinaryCard) card).getNumOfFields());
		panel3.add(bp);
		panel3.setPreferredSize(new Dimension(100, 40));

		panel.add(panel1, BorderLayout.NORTH);
		panel.add(panel2, BorderLayout.CENTER);
		panel.add(panel3, BorderLayout.SOUTH);
		panel.validate();
		panel.repaint();
	}

	private void setSpecialCard(Card card, JPanel panel1, JPanel panel2, JPanel panel3) throws Exception {
		configurePanels(card, panel1, panel2, panel);
		panel.add(panel1, BorderLayout.NORTH);
		panel.add(panel2, BorderLayout.CENTER);
		panel.validate();
		panel.repaint();
	}

	private void configurePanels(Card card, JPanel panel1, JPanel panel2, JPanel panel3) throws InterruptedException {
		panel1.removeAll();
		panel2.removeAll();
		panel3.removeAll();
		panel.setBorder(BorderFactory.createEmptyBorder());
		Thread.sleep(500);
		panel.setPreferredSize(new Dimension(100, 270));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		panel1.setPreferredSize(new Dimension(100, 50));

		JLabel pic = new JLabel(new ImageIcon(card.getImage()));
		panel2.add(pic);
	}

	private void determineHolePositions(Integer numOfHoles, ArrayDeque<Integer> pom, Integer[] diamondPositions)
			throws Exception {
		Integer[] pomArr = new Integer[pom.size()];
		for (Integer i = 0; i < pomArr.length; i++) {
			pomArr[i] = pom.poll();
		}
		for (Integer i = 0; i < numOfHoles; i++) {
			Integer selectedNumber = pomArr[PlayerService.rand.nextInt(pomArr.length)];
			for (Integer j = 0; j < diamondPositions.length; j++) {
				while (selectedNumber == diamondPositions[j]) {
					selectedNumber = pomArr[PlayerService.rand.nextInt(pomArr.length)];
				}
			}
			diamondPositions[i] = selectedNumber;
		}
	}

	private void deleteFigure(Figure f) throws Exception {
		for (Integer i = 0; i < PlayerService.players.size(); i++) {
			Player player = PlayerService.players.get(i);
			for (Integer j = 0; j < player.getFigures().size(); j++) {
				Figure fig = player.getFigures().get(j);
				if (f.getFigureId() == fig.getFigureId()) {
					player.getFigures().remove(0);
				}
			}
		}
	}

	private void setHoles() throws Exception {
		ArrayDeque<Integer> pom = PlayerService.path.clone();
		Integer numOfHoles = PlayerService.NUM_OF_ROWS;
		holePosition = new Integer[numOfHoles];
		determineHolePositions(numOfHoles, pom, holePosition);
		for (Integer i = 0; i < PlayerService.NUM_OF_ROWS; i++) {
			for (Integer j = 0; j < PlayerService.NUM_OF_COLUMNS; j++) {
				for (Integer k = 0; k < holePosition.length; k++) {
					if ((i * PlayerService.NUM_OF_ROWS) + (j + 1) == holePosition[k]) {
						mPanela[i][j].setBackground(new Color(64, 64, 64));
						if (m[i][j] instanceof OrdinaryFigure || m[i][j] instanceof SuperFastFigure) {
							Figure f = (Figure) m[i][j];
							deleteFigure(f);
						} else if (m[i][j] instanceof Diamond) {
							m[i][j] = null;
						}
					}
				}
			}
		}
	}

	private void deleteHoles() throws Exception {
		for (Integer i = 0; i < PlayerService.NUM_OF_ROWS; i++) {
			for (Integer j = 0; j < PlayerService.NUM_OF_COLUMNS; j++) {
				if (Map.panels[i][j].getBackground().getRGB() == new Color(64, 64, 64).getRGB()) {
					mPanela[i][j].setBackground(new Color(238, 238, 238));
					mPanela[i][j].remove(panel);
					mPanela[i][j].revalidate();
					if (!(m[i][j] instanceof HoveringFigure)) {
						m[i][j] = null;
					} else if (m[i][j] instanceof HoveringFigure) {
						Figure f = (Figure) m[i][j];
						mPanela[i][j].setBackground(f.getColor());
						Player.label.setText("l");
						mPanela[i][j].add(Player.label);
					}
				}
			}
		}
	}

	private void saveResults() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
		Date date = new Date();
		PrintWriter pw = new PrintWriter(
				Map.RESULTS + File.separator + "GAME_" + dateFormat.format(date) + ".txt");
		for (Integer i = 0; i < finishedGame.size(); i++) {
			Player player = finishedGame.get(i);
			pw.print("Player " + (i + 1) + " - " + player.getName());
			for (Integer j = 0; j < player.getPomFigures().size(); j++) {
				Figure f = player.getPomFigures().get(j);
				String tipFigure = "";
				if (f instanceof HoveringFigure) {
					tipFigure = "Hover";
				} else if (f instanceof OrdinaryFigure) {
					tipFigure = "Ordinart";
				} else if (f instanceof SuperFastFigure) {
					tipFigure = "SuperFast";
				}
				MovementHistory ik = f.getMovementHistory();
				String kretanje = "";
				for (Integer k = 0; k < ik.getCoordinates().size(); k++) {
					kretanje += ik.getCoordinates().get(k) + "-";
				}
				String stiglaDoCilja = "";
				if (f.hasArrived() == true) {
					stiglaDoCilja = "yes";
				} else {
					stiglaDoCilja = "no";
				}
				String boja = "";
				if (f.getColor().getRGB() == new Color(240, 240, 0).getRGB()) {
					boja = "Zuta";
				} else if (f.getColor().getRGB() == new Color(0, 153, 0).getRGB()) {
					boja = "Zelena";
				} else if (f.getColor().getRGB() == new Color(0, 0, 255).getRGB()) {
					boja = "Plava";
				} else if (f.getColor().getRGB() == new Color(255, 0, 0).getRGB()) {
					boja = "Crvena";
				}
				pw.print("       Figure " + (j + 1) + " (" + tipFigure + "," + boja + " ) - " + " spun path ("
						+ kretanje + ") - has arrived ` " + stiglaDoCilja + "`");
			}
		}
		pw.print("Total time of game is: " + Map.TOTAL_TIME_OF_GAME + "s");
		pw.close();
	}

	@Override
	public void run() {
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		ArrayList<Player> players = PlayerService.players;
		Integer i = 0;
		try {
			while (Map.END == false) {
				if (i == PlayerService.NUM_OF_PLAYERS) {
					i = 0;
					CURR_PLAYER = 1;
				}
				Player player = players.get(i);
				Integer numOfCard = PlayerService.rand.nextInt(deck.size());
				Card card = deck.get(numOfCard);
				deck.remove(card);
				if (card instanceof OrdinaryCard) {
					setOrdinaryCard(card, panel1, panel2, panel3);
					NUM_OF_FIELDS = ((OrdinaryCard) card).getNumOfFields();
					synchronized (player) {
						player.notify();
					}
					synchronized (this) {
						this.wait();
					}
				} else if (card instanceof SpecialCard) {
					setSpecialCard(card, panel1, panel2, panel3);
					NUM_OF_FIELDS = 0;
					setHoles();
					synchronized (player) {
						player.notify();
					}
					synchronized (this) {
						this.wait();
					}
					deleteHoles();
				}
				if (i < PlayerService.NUM_OF_PLAYERS) {
					i++;
					if (i == PlayerService.NUM_OF_PLAYERS) {
						i = 0;
					}
					CURR_PLAYER = (i + 1);
				}
				deck.add(card);
				if (Map.GAME_HAS_STOPPED == true) {
					for (Integer k = 0; k < PlayerService.players.size(); k++) {
						PlayerService.players.get(k).setHasGameFinished(true);
						synchronized (PlayerService.players.get(k)) {
							PlayerService.players.get(k).notify();
						}
					}
					for (Integer k = 0; k < PlayerService.NUM_OF_ROWS; k++) {
						for (Integer l = 0; l < PlayerService.NUM_OF_COLUMNS; l++) {
							Map.matrix[k][l] = null;
							Map.panels[k][l].setBackground(new Color(238, 238, 238));
						}
					}
					Map.END = true;
				}
			}
			Map.clickedToClose++;
			Map.panelImageNumber.removeAll();
			if (Map.GAME_HAS_STOPPED == false) {
				saveResults();
			}
		} catch (Exception ex) {
			Logger.getLogger(Deck.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
}
