package services;

import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import exceptions.MyException;
import figures.*;
import models.Player;

public class PlayerService {

	public static Integer NUM_OF_ROWS; // Min number of rows is 7, max is 10
	public static Integer NUM_OF_COLUMNS; // Min number of columns is 7, max is 10
	public static Integer NUM_OF_PLAYERS; // Min number of players is 2, max 4

	public static final String CONFIG_PATH = ".\\src\\resources\\config.properties";

	public static String PLAYER_NAME1, PLAYER_NAME2, PLAYER_NAME3, PLAYER_NAME4;
	public static Color PLAYER_COLOR1, PLAYER_COLOR2, PLAYER_COLOR3, PLAYER_COLOR4;

	public static ArrayList<Player> players = new ArrayList<>();
	public static ArrayList<Player> pomPlayers = new ArrayList<>();
	public static ArrayDeque<Integer> path = new ArrayDeque<>();
	public static ArrayList<Integer> pathOfMovement = new ArrayList<>();

	public static Random rand = new Random();
	public static Handler handler;
	public static String[] typeOfFigure = { "ordinary", "superFast", "hovering" };

	{
		try {
			handler = new FileHandler("Init.log");
			Logger.getLogger(PlayerService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(PlayerService.class.getName()).addHandler(handler);
		} catch (IOException ex) {
			Logger.getLogger(PlayerService.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

	public PlayerService() throws MyException, IOException {
		initSize();
		validateUserInput();
		loadFigurePath();
		initPlayers();
		validatePlayerNames();
		validateColors();
	}

	public void initSize() throws IOException, MyException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(CONFIG_PATH));
		NUM_OF_ROWS = Integer.parseInt(prop.getProperty("NUM_OF_ROWS"));
		NUM_OF_COLUMNS = Integer.parseInt(prop.getProperty("NUM_OF_COLUMNS"));
		NUM_OF_PLAYERS = Integer.parseInt(prop.getProperty("NUM_OF_PLAYERS"));
	}

	private void validateUserInput() throws MyException {
		if (NUM_OF_ROWS != NUM_OF_COLUMNS) {
			throw new MyException(
					"The number of columns in the matrix must be equal to the number of rows in the matrix.");
		} else if (NUM_OF_ROWS < 7 || NUM_OF_ROWS > 10) {
			throw new MyException("The minimum number of rows must be 7, and the maximum 10.");
		} else if (NUM_OF_COLUMNS < 7 || NUM_OF_COLUMNS > 10) {
			throw new MyException("The minimum number of columns must be 7, and the maximum 10.");
		} else if (NUM_OF_PLAYERS < 2 || NUM_OF_PLAYERS > 4) {
			throw new MyException("The minimum number of players must be 2, and the maximum 4.");
		}
	}

	public void loadFigurePath() throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(CONFIG_PATH));
		String loadedPath = "";
		if (NUM_OF_ROWS == 7) {
			loadedPath = prop.getProperty("MATRIX7x7");
		} else if (NUM_OF_ROWS == 8) {
			loadedPath = prop.getProperty("MATRIX8x8");
		} else if (NUM_OF_ROWS == 9) {
			loadedPath = prop.getProperty("MATRIX9x9");
		} else if (NUM_OF_ROWS == 10) {
			loadedPath = prop.getProperty("MATRIX10x10");
		}
		String[] s = loadedPath.split(",");
		for (Integer i = 0; i < s.length; i++) {
			path.add(Integer.parseInt(s[i]));
			pathOfMovement.add(Integer.parseInt(s[i]));
		}
	}

	public void initPlayers() throws MyException, IOException {
		ArrayList<Figure> f1 = new ArrayList<>();
		ArrayList<Figure> f2 = new ArrayList<>();
		ArrayList<Figure> f3 = new ArrayList<>();
		ArrayList<Figure> f4 = new ArrayList<>();
		ArrayList<Color> boje = new ArrayList<>();

		boje.add(Color.blue);
		boje.add(Color.red);
		boje.add(new Color(240, 240, 0));
		boje.add(new Color(0, 153, 0));

		Properties prop = new Properties();
		prop.load(new FileInputStream(CONFIG_PATH));

		PLAYER_NAME1 = prop.getProperty("PLAYER_NAME1");
		PLAYER_NAME2 = prop.getProperty("PLAYER_NAME2");
		PLAYER_NAME3 = prop.getProperty("PLAYER_NAME3");
		PLAYER_NAME4 = prop.getProperty("PLAYER_NAME4");
		if (NUM_OF_PLAYERS == 2) {
			init2Players(f1, f2, PLAYER_NAME1, PLAYER_NAME2, boje);
		} else if (NUM_OF_PLAYERS == 3) {
			init3Players(f1, f2, f3, PLAYER_NAME1, PLAYER_NAME2, PLAYER_NAME3, boje);
		} else if (NUM_OF_PLAYERS == 4) {
			init4Players(f1, f2, f3, f4, PLAYER_NAME1, PLAYER_NAME2, PLAYER_NAME3, PLAYER_NAME4, boje);
		}
	}

	private void init2Players(ArrayList<Figure> f1, ArrayList<Figure> f2, String name1, String name2,
			ArrayList<Color> colors) throws MyException {
		Integer firstColorNumber = rand.nextInt(colors.size());
		Color firstColor = colors.get(firstColorNumber);
		colors.remove(firstColor);
		PLAYER_COLOR1 = firstColor;

		Integer secondColorNumber = rand.nextInt(colors.size());
		Color secondColor = colors.get(secondColorNumber);
		colors.remove(secondColor);
		PLAYER_COLOR2 = secondColor;

		createFigures(f1, firstColor);
		createFigures(f2, secondColor);
		players.add(new Player(name1, f1));
		players.add(new Player(name2, f2));
		pomPlayers.add(new Player(name1, f1));
		pomPlayers.add(new Player(name2, f2));
	}

	private void init3Players(ArrayList<Figure> f1, ArrayList<Figure> f2, ArrayList<Figure> f3, String name1, String name2,
			String name3, ArrayList<Color> colors) throws MyException {
		init2Players(f1, f2, name1, name2, colors);

		Integer thirdColorNumber = rand.nextInt(colors.size());
		Color thirdColor = colors.get(thirdColorNumber);
		colors.remove(thirdColor);
		
		PLAYER_COLOR3 = thirdColor;
		createFigures(f3, thirdColor);
		players.add(new Player(name3, f3));
		pomPlayers.add(new Player(name3, f3));
	}

	private void init4Players(ArrayList<Figure> f1, ArrayList<Figure> f2, ArrayList<Figure> f3, ArrayList<Figure> f4,
			String name1, String name2, String name3, String name4, ArrayList<Color> colors) throws MyException {
		init3Players(f1, f2, f3, name1, name2, name3, colors);

		Integer fourthColorNumber = rand.nextInt(colors.size());
		Color fourthColor = colors.get(fourthColorNumber);
		colors.remove(fourthColor);
		PLAYER_COLOR4 = fourthColor;
		createFigures(f4, fourthColor);
		players.add(new Player(name4, f4));
		pomPlayers.add(new Player(name4, f4));
	}

	private void validatePlayerNames() throws MyException {
		for (Integer i = 0; i < players.size(); i++) {
			for (Integer j = i + 1; j < players.size(); j++) {
				if (players.get(i).getName().equals(players.get(j).getName())) {
					throw new MyException("Name of players must be unique.");
				}
			}
		}
	}

	private void validateColors() throws MyException {
		for (Player p : players) {
			for (Figure f1 : p.getFigures()) {
				for (Figure f2 : p.getFigures()) {
					if (!(f1.getColor().getRGB() == f2.getColor().getRGB())) {
						throw new MyException("All pieces of one player must be the same color.");
					}
				}
			}
		}
	}

	private void createFigures(ArrayList<Figure> figures, Color color) throws MyException {
		for (Integer i = 0; i < 4; i++) {
			String tip = typeOfFigure[rand.nextInt(typeOfFigure.length)];
			if ("ordinary".equals(tip)) {
				figures.add(new OrdinaryFigure(color, pathOfMovement, (i + 1)));
			} else if ("superFast".equals(tip)) {
				figures.add(new SuperFastFigure(color, pathOfMovement, (i + 1)));
			} else if ("hovering".equals(tip)) {
				figures.add(new HoveringFigure(color, pathOfMovement, (i + 1)));
			}
		}
	}

}
