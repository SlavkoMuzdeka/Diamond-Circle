package models;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import figures.*;
import services.PlayerService;

public class Map extends Thread implements ActionListener {
	private PlayerService playerService;

	private JFrame frame, frame1, frame2;
	private JPanel panel1, panel2, panel3, p;
	private JButton startBtn, stopBtn, showBtn;
	private JButton[] figureBtn;
	private JLabel label1, label2, label3, label4;
	public static JTextArea numOfGames = new JTextArea(), cardDescription = new JTextArea();
	public static JTextPane totalTimePane = new JTextPane();;
	public static JPanel[][] panels = new JPanel[PlayerService.NUM_OF_ROWS][PlayerService.NUM_OF_COLUMNS];
	public static Object[][] matrix = new Object[PlayerService.NUM_OF_ROWS][PlayerService.NUM_OF_COLUMNS];
	public static JPanel panelImageNumber = new JPanel();
	public static ArrayList<JButton> pathBtns = new ArrayList<>();

	public static Handler handler;
	public static Deck deck;

	public static Integer NUM_OF_PLAYED_GAMES = 0;
	public static Integer TOTAL_TIME_OF_GAME = 0;
	public static Boolean GAME_HAS_STOPPED = false;
	public static Integer clickedToClose = 0;
	public static Boolean END = false;

	public static final String DIAMANT1_IMAGE = ".\\DiamondImages\\Diamond1.png";
	public static final String DIAMANT2_IMAGE = ".\\SlikeDijamanata\\Diamond2.png";
	public static final String RESULTS = ".\\Results";

	{
		try {
			handler = new FileHandler("Map.log");
			Logger.getLogger(Map.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(Map.class.getName()).addHandler(handler);
		} catch (IOException ex) {
			Logger.getLogger(PlayerService.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

	public Map(PlayerService playerService) throws Exception {
		this.playerService = playerService;

		frame = new JFrame("Diamond Circle");
		frame.setSize(900, 800);
		frame.setLocation(400, 20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		panel1 = new JPanel(new BorderLayout(20, 15));
		panel2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 25));
		panel2.setBorder(BorderFactory.createLineBorder(Color.black));
		panel3 = new JPanel(new BorderLayout(20, 15));

		makePanel2(panel2);
		makePanel3(panel3);

		panel1.add(panel2, BorderLayout.NORTH);
		panel1.add(panel3, BorderLayout.CENTER);
		frame.add(panel1);
		frame.setVisible(true);
	}

	private void makePanel2(JPanel panel2) throws Exception {
		numOfGames.setText("Current number of played                  games: " + NUM_OF_PLAYED_GAMES);
		numOfGames.setEditable(false);
		numOfGames.setPreferredSize(new Dimension(200, 50));
		numOfGames.setBackground(new Color(238, 238, 238));
		numOfGames.setForeground(Color.blue);
		numOfGames.setFont(new Font("Verdana", Font.PLAIN, 15));
		numOfGames.setLineWrap(true);
		numOfGames.setWrapStyleWord(true);
		panel2.add(numOfGames);

		JTextPane pane1 = new JTextPane();
		pane1.setText("     DiamondCircle");
		pane1.setForeground(Color.red);
		pane1.setBackground(new Color(255, 229, 204));
		pane1.setEditable(false);
		pane1.setPreferredSize(new Dimension(220, 30));
		pane1.setFont(new Font("Verdana", Font.PLAIN, 20));
		pane1.setBorder(BorderFactory.createLineBorder(Color.black));
		panel2.add(pane1);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		p.setPreferredSize(new Dimension(200, 40));

		startBtn = new JButton("Start");
		startBtn.setFocusable(false);
		startBtn.addActionListener(this);
		startBtn.setBackground(new Color(238, 238, 238));
		startBtn.setBorder(BorderFactory.createEmptyBorder());
		startBtn.setForeground(Color.blue);
		p.add(startBtn);

		JLabel l = new JLabel("/");
		l.setForeground(Color.blue);
		p.add(l);

		stopBtn = new JButton("Stop");
		stopBtn.setFocusable(false);
		stopBtn.addActionListener(this);
		stopBtn.setBackground(new Color(238, 238, 238));
		stopBtn.setBorder(BorderFactory.createEmptyBorder());
		stopBtn.setForeground(Color.blue);
		stopBtn.setEnabled(false);
		p.add(stopBtn);

		panel2.add(p);
	}

	private void makePanel3(JPanel panel3) throws Exception {
		JPanel p = addNorthPanel();
		panel3.add(p, BorderLayout.NORTH);
		JPanel p1 = addWestPanel();
		panel3.add(p1, BorderLayout.WEST);
		JPanel p2 = addCenterPanel();
		panel3.add(p2, BorderLayout.CENTER);
		JPanel p3 = addEastPanel();
		panel3.add(p3, BorderLayout.EAST);
	}

	private void showPlayerNames(JPanel p, JLabel l1, JLabel l2, JLabel l3, JLabel l4) {
		if (PlayerService.NUM_OF_PLAYERS == 2) {
			p.add(l1);
			p.add(l2);
		} else if (PlayerService.NUM_OF_PLAYERS == 3) {
			p.add(l1);
			p.add(l2);
			p.add(l3);
		} else if (PlayerService.NUM_OF_PLAYERS == 4) {
			p.add(l1);
			p.add(l2);
			p.add(l3);
			p.add(l4);
		}
	}

	private JPanel addNorthPanel() throws Exception {
		JPanel pan = new JPanel(new BorderLayout(10, 10));

		p = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 25));
		p.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pom1 = new JPanel();
		JPanel pom2 = new JPanel();

		label1 = new JLabel("Igrac 1");
		label1.setFont(new Font("Verdana", Font.PLAIN, 15));
		label1.setForeground(PlayerService.PLAYER_COLOR1);

		label2 = new JLabel("Igrac 2");
		label2.setFont(new Font("Verdana", Font.PLAIN, 15));
		label2.setForeground(PlayerService.PLAYER_COLOR2);

		label3 = new JLabel("Igrac 3");
		label3.setFont(new Font("Verdana", Font.PLAIN, 15));
		label3.setForeground(PlayerService.PLAYER_COLOR3);

		label4 = new JLabel("Igrac 4");
		label4.setFont(new Font("Verdana", Font.PLAIN, 15));
		label4.setForeground(PlayerService.PLAYER_COLOR4);

		showPlayerNames(p, label1, label2, label3, label4);

		pan.add(p, BorderLayout.CENTER);
		pan.add(pom1, BorderLayout.WEST);
		pan.add(pom2, BorderLayout.EAST);
		return pan;
	}

	private JPanel addWestPanel() throws Exception {
		JPanel pan = new JPanel(new BorderLayout(10, 10));
		JPanel pom1 = new JPanel();
		JPanel pom2 = new JPanel();

		JPanel p1 = new JPanel(new GridLayout(PlayerService.NUM_OF_PLAYERS * 4, 0, 0, 5));
		p1.setBorder(BorderFactory.createLineBorder(Color.black));
		p1.setPreferredSize(new Dimension(100, 50));
		figureBtn = new JButton[PlayerService.NUM_OF_PLAYERS * 4];
		for (Integer i = 0; i < figureBtn.length; i++) {
			figureBtn[i] = new JButton("Figure " + (i + 1));
			figureBtn[i].setFocusable(false);
			figureBtn[i].addActionListener(this);
			figureBtn[i].setBorder(BorderFactory.createEmptyBorder());
			figureBtn[i].setBackground(new Color(238, 238, 238));
			p1.add(figureBtn[i]);
		}
		pan.add(p1, BorderLayout.CENTER);
		pan.add(pom1, BorderLayout.WEST);
		pan.add(pom2, BorderLayout.SOUTH);
		return pan;
	}

	private JPanel addCenterPanel() throws Exception {
		JPanel pan = new JPanel(new BorderLayout(20, 15));

		JPanel panel = new JPanel(new GridLayout(PlayerService.NUM_OF_ROWS, PlayerService.NUM_OF_COLUMNS, 0, 0));
		for (Integer i = 0; i < PlayerService.NUM_OF_ROWS; i++) {
			for (Integer j = 0; j < PlayerService.NUM_OF_COLUMNS; j++) {
				panels[i][j] = new JPanel();
				panels[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				String broj = String.valueOf((i * PlayerService.NUM_OF_ROWS) + (j + 1));
				JLabel label = new JLabel(broj);
				panels[i][j].add(label);
				panel.add(panels[i][j]);
			}
		}
		JPanel pane = new JPanel(new BorderLayout(10, 10));

		JPanel pom = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
		pom.setBorder(BorderFactory.createLineBorder(Color.black));
		pom.setPreferredSize(new Dimension(200, 120));
		JLabel l = new JLabel("Card description");
		cardDescription.setEditable(false);
		cardDescription.setPreferredSize(new Dimension(250, 50));
		cardDescription.setBackground(new Color(238, 238, 238));
		cardDescription.setFont(new Font("Verdana", Font.PLAIN, 12));
		cardDescription.setLineWrap(true);
		cardDescription.setWrapStyleWord(true);
		pom.add(l);
		pom.add(cardDescription);

		pane.add(pom, BorderLayout.NORTH);
		JPanel pa = new JPanel();
		pane.add(pa, BorderLayout.CENTER);

		pan.add(panel, BorderLayout.CENTER);
		pan.add(pane, BorderLayout.SOUTH);
		return pan;
	}

	private JPanel addEastPanel() throws Exception {
		JPanel p3 = new JPanel(new BorderLayout(10, 10));

		JPanel pom1 = new JPanel(new BorderLayout(10, 10));
		JPanel pom11 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pom12 = new JPanel();
		pom1.add(pom11, BorderLayout.CENTER);
		pom1.add(pom12, BorderLayout.EAST);
		pom11.setBorder(BorderFactory.createLineBorder(Color.black));
		pom11.setPreferredSize(new Dimension(150, 350));

		JPanel pom111 = new JPanel(new BorderLayout(10, 10));
		JPanel pom1111 = new JPanel();
		JPanel pom1112 = new JPanel(new BorderLayout(10, 10));
		panelImageNumber = pom1112;

		JTextPane pane2 = new JTextPane();
		pane2.setText("    Current card");
		pane2.setBackground(new Color(153, 204, 255));
		pane2.setEditable(false);
		pane2.setPreferredSize(new Dimension(150, 30));
		pane2.setFont(new Font("Verdana", Font.PLAIN, 15));
		pane2.setBorder(BorderFactory.createLineBorder(Color.black));
		pom1111.add(pane2);
		pom111.add(pom1111, BorderLayout.NORTH);
		pom111.add(pom1112, BorderLayout.CENTER);

		pom11.add(pom111);

		JPanel pom2 = new JPanel(new BorderLayout(10, 10));
		JPanel pom21 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pom22 = new JPanel();
		pom2.add(pom21, BorderLayout.CENTER);
		pom2.add(pom22, BorderLayout.EAST);
		totalTimePane.setText(" Duration time of game: " + TOTAL_TIME_OF_GAME + "s");
		totalTimePane.setBackground(new Color(255, 229, 204));
		totalTimePane.setEditable(false);
		totalTimePane.setPreferredSize(new Dimension(200, 30));
		totalTimePane.setFont(new Font("Verdana", Font.PLAIN, 15));
		totalTimePane.setBorder(BorderFactory.createLineBorder(Color.black));
		pom21.add(totalTimePane);

		JPanel pom3 = new JPanel(new BorderLayout(10, 10));
		JPanel pom31 = new JPanel();
		JPanel pom32 = new JPanel();
		JPanel pom33 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pom3.add(pom31, BorderLayout.SOUTH);
		pom3.add(pom32, BorderLayout.EAST);
		pom3.add(pom33, BorderLayout.CENTER);
		pom33.setBorder(BorderFactory.createLineBorder(Color.black));
		pom33.setPreferredSize(new Dimension(200, 120));
		JPanel pom331 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pom331.setPreferredSize(new Dimension(100, 30));
		pom33.add(pom331);
		JPanel pom332 = new JPanel();
		pom332.setBorder(BorderFactory.createLineBorder(Color.black));
		pom332.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		pom33.add(pom332);
		showBtn = new JButton("Show list of files");
		showBtn.setFocusable(false);
		showBtn.addActionListener(this);
		showBtn.setBackground(new Color(238, 238, 238));
		showBtn.setBorder(BorderFactory.createEmptyBorder());
		pom332.add(showBtn);
		pom33.add(pom332);

		p3.add(pom1, BorderLayout.NORTH);
		p3.add(pom2, BorderLayout.CENTER);
		p3.add(pom3, BorderLayout.SOUTH);
		return p3;
	}

	private void updateTextArea() throws Exception {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (END == false) {
					numOfGames.setText("Current number of played                 games: " + NUM_OF_PLAYED_GAMES);
					numOfGames.revalidate();
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	private void updateCardDescription() throws Exception {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (END == false) {
					cardDescription.setText("               Player's turn " + Deck.CURR_PLAYER
							+ ".\t            Figure " + Player.CURR_FIGURE + ", passes "
							+ Deck.NUMBER_OF_SQUARES_TO_PASS + " fields,                  is moving from field "
							+ Player.CURR_FIELD + ". to " + Player.NEXT_FIELD + ". field");
					cardDescription.revalidate();
					try {
						sleep(1000);
					} catch (Exception ex) {
						Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	private void updateTextPane(Integer time) throws Exception {
		TOTAL_TIME_OF_GAME = 0;
		totalTimePane.setText(" Duration of the game: " + TOTAL_TIME_OF_GAME + "s");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (END == false) {
					totalTimePane.setText("Duration of the game:" + (++TOTAL_TIME_OF_GAME) + "s");
					totalTimePane.revalidate();
					try {
						sleep(1000);
					} catch (Exception ex) {
						Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public void showFilesList() throws Exception {
		frame1 = new JFrame("Results list");
		frame1.setSize(300, 200);
		frame1.setLocation(650, 200);
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.setResizable(false);

		File file = new File(RESULTS);
		String[] putanje = file.list();
		JPanel panel = new JPanel(new GridLayout(putanje.length, 0, 1, 1));

		for (Integer i = 0; i < putanje.length; i++) {
			if (putanje[i].endsWith(".txt")) {
				JButton bt = new JButton(putanje[i]);
				bt = new JButton(putanje[i]);
				bt.setFocusable(false);
				bt.addActionListener(this);
				panel.add(bt);
				pathBtns.add(bt);
			}
		}
		JScrollPane pane = new JScrollPane(panel);
		frame1.add(pane);
		frame1.setVisible(true);
	}

	private ArrayList<Player> determinePlayerOrder() throws Exception {
		ArrayList<Player> players = new ArrayList<>();
		ArrayList<Player> pomPlayers = new ArrayList<>();
		for (Integer i = 0; i < PlayerService.players.size(); i++) {
			pomPlayers.add(PlayerService.players.get(i));
		}
		for (Integer i = 0; i < PlayerService.NUM_OF_PLAYERS; i++) {
			Integer br = PlayerService.rand.nextInt(pomPlayers.size());
			players.add(pomPlayers.get(br));
			pomPlayers.remove(pomPlayers.get(br));
		}
		return players;
	}

	private void refreshFields() {
		PlayerService.players = new ArrayList<>();
		PlayerService.pomPlayers = new ArrayList<>();
		PlayerService.pathOfMovement = new ArrayList<>();
		PlayerService.path = new ArrayDeque<>();

		Map.TOTAL_TIME_OF_GAME = 0;
		Map.clickedToClose = 0;

		Player.CURR_FIGURE = 1;
		Player.CURR_FIELD = 0;
		Player.NEXT_FIELD = 0;
		Player.FINISHED_GAME = 0;

		Deck.NUM_OF_FIELDS = 0;
		Deck.CURR_PLAYER = 1;
		Deck.NUMBER_OF_SQUARES_TO_PASS = 0;
		Deck.finishedGame = new ArrayList<>();

		label1.setBackground(PlayerService.PLAYER_COLOR1);
		label2.setBackground(PlayerService.PLAYER_COLOR2);
		label3.setBackground(PlayerService.PLAYER_COLOR3);
		label4.setBackground(PlayerService.PLAYER_COLOR4);
		showPlayerNames(p, label1, label2, label3, label4);
		p.revalidate();
	}

	private void showFigureMovementPath(Figure f) {
		frame2 = new JFrame("Results list");
		frame2.setSize(400, 400);
		frame2.setLocation(30, 200);
		frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame2.setResizable(false);

		JPanel panel = new JPanel(new GridLayout(PlayerService.NUM_OF_ROWS, PlayerService.NUM_OF_COLUMNS, 0, 0));
		JPanel[][] matricaP = new JPanel[PlayerService.NUM_OF_ROWS][PlayerService.NUM_OF_COLUMNS];
		for (Integer i = 0; i < PlayerService.NUM_OF_ROWS; i++) {
			for (Integer j = 0; j < PlayerService.NUM_OF_COLUMNS; j++) {
				matricaP[i][j] = new JPanel();
				matricaP[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				String broj = String.valueOf((i * PlayerService.NUM_OF_ROWS) + (j + 1));
				JLabel label = new JLabel(broj);
				matricaP[i][j].add(label);
				panel.add(matricaP[i][j]);
			}
		}
		MovementHistory ik = f.getMovementHistory();
		ArrayList<Integer> koordinate = ik.getCoordinates();
		for (Integer i = 0; i < PlayerService.NUM_OF_ROWS; i++) {
			for (Integer j = 0; j < PlayerService.NUM_OF_COLUMNS; j++) {
				for (Integer k = 0; k < koordinate.size(); k++) {
					if ((i * PlayerService.NUM_OF_ROWS) + (j + 1) == koordinate.get(k)) {
						matricaP[i][j].setBackground(f.getColor());
					}
				}
			}
		}
		frame2.add(panel);
		frame2.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startBtn) {
			try {
				if (clickedToClose != 0) {
					END = false;
					GAME_HAS_STOPPED = false;
					refreshFields();
					playerService.initSize();
					playerService.loadFigurePath();
					playerService.initPlayers();
					Player.NUM_OF_PLAYERS = PlayerService.NUM_OF_PLAYERS;
				}
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);

				updateTextArea();
				updateTextPane(TOTAL_TIME_OF_GAME);

				deck = new Deck(matrix, panels);
				deck.start();

				updateCardDescription();

				ArrayList<Player> players;
				players = determinePlayerOrder();
				for (Integer i = 0; i < players.size(); i++) {
					players.get(i).start();
				}

				GhostFigure ghostFigure = new GhostFigure(matrix, panels);
				ghostFigure.start();
			} catch (Exception ex) {
				Logger.getLogger(PlayerService.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
			}
		} else if (e.getSource() == stopBtn) {
			clickedToClose++;
			GAME_HAS_STOPPED = true;
			stopBtn.setEnabled(false);
			startBtn.setEnabled(true);
		} else if (e.getSource() == showBtn) {
			try {
				showFilesList();
			} catch (Exception ex) {
				Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
			}
		} else if (e.getSource() == figureBtn[0]) {
			Player igr = PlayerService.pomPlayers.get(0);
			Figure f = igr.getPomFigures().get(0);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[1]) {
			Player igr = PlayerService.pomPlayers.get(0);
			Figure f = igr.getPomFigures().get(1);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[2]) {
			Player igr = PlayerService.pomPlayers.get(0);
			Figure f = igr.getPomFigures().get(2);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[3]) {
			Player igr = PlayerService.pomPlayers.get(0);
			Figure f = igr.getPomFigures().get(3);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[4]) {
			Player igr = PlayerService.pomPlayers.get(1);
			Figure f = igr.getPomFigures().get(0);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[5]) {
			Player igr = PlayerService.pomPlayers.get(1);
			Figure f = igr.getPomFigures().get(1);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[6]) {
			Player igr = PlayerService.pomPlayers.get(1);
			Figure f = igr.getPomFigures().get(2);
			showFigureMovementPath(f);
		} else if (e.getSource() == figureBtn[7]) {
			Player igr = PlayerService.pomPlayers.get(1);
			Figure f = igr.getPomFigures().get(3);
			showFigureMovementPath(f);
		} else if (Player.NUM_OF_PLAYERS == 3) {
			if (e.getSource() == figureBtn[8]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(0);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[9]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(1);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[10]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(2);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[11]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(3);
				showFigureMovementPath(f);
			}
		} else if (Player.NUM_OF_PLAYERS == 4) {
			if (e.getSource() == figureBtn[8]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(0);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[9]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(1);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[10]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(2);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[11]) {
				Player igr = PlayerService.pomPlayers.get(2);
				Figure f = igr.getPomFigures().get(3);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[12]) {
				Player igr = PlayerService.pomPlayers.get(3);
				Figure f = igr.getPomFigures().get(0);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[13]) {
				Player igr = PlayerService.pomPlayers.get(3);
				Figure f = igr.getPomFigures().get(1);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[14]) {
				Player igr = PlayerService.pomPlayers.get(3);
				Figure f = igr.getPomFigures().get(2);
				showFigureMovementPath(f);
			} else if (e.getSource() == figureBtn[15]) {
				Player igr = PlayerService.pomPlayers.get(3);
				Figure f = igr.getPomFigures().get(3);
				showFigureMovementPath(f);
			}
		}
		if (pathBtns.size() != 0) {
			for (Integer i = 0; i < pathBtns.size(); i++) {
				if (pathBtns.get(i) == e.getSource()) {
					try {
						JFrame pomF = new JFrame("Content " + pathBtns.get(i).getText());
						pomF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						BufferedReader bf = new BufferedReader(
								new FileReader(RESULTS + File.separator + pathBtns.get(i).getText()));
						String s = "";
						JPanel panel = new JPanel();
						JTextArea area = new JTextArea();
						area.setEditable(false);
						while ((s = bf.readLine()) != null) {
							area.append(s + "\n");
						}
						bf.close();
						panel.add(area);
						pomF.add(panel);
						pomF.pack();
						pomF.setVisible(true);
					} catch (Exception ex) {
						Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
					}
				}
			}
		}
	}
}
