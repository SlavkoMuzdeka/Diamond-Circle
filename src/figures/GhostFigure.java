package figures;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import models.Diamond;
import models.Hole;
import models.Map;
import services.PlayerService;

public class GhostFigure extends Figure {
	private Object[][] m;
	private JPanel[][] mPanela;

	private Integer rows;
	private Integer columns;
	private Integer numOfDiamonds;
	private Integer[] diamondPositions;

	public static ArrayDeque<Integer> path = PlayerService.path;
	public static Handler handler;

	{
		try {
			handler = new FileHandler("GhostFigure.log");
			Logger.getLogger(GhostFigure.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(GhostFigure.class.getName()).addHandler(handler);
		} catch (IOException ex) {
			Logger.getLogger(Map.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}

	public GhostFigure(Object[][] m, JPanel[][] mPanela) {
		super();
		this.m = m;
		this.mPanela = mPanela;
		this.rows = PlayerService.NUM_OF_ROWS;
		this.columns = PlayerService.NUM_OF_COLUMNS;
	}

	private void determineDiamondPositions(Integer brD, ArrayDeque<Integer> pom) {
		Integer[] pomNiz = new Integer[pom.size()];
		for (Integer i = 0; i < pomNiz.length; i++) {
			pomNiz[i] = pom.poll();
		}

		diamondPositions = new Integer[brD];
		for (Integer i = 0; i < brD; i++) {
			Integer selectedNumber = pomNiz[PlayerService.rand.nextInt(pomNiz.length)];
			for (Integer j = 0; j < diamondPositions.length; j++) {
				while (selectedNumber == diamondPositions[j]) {
					selectedNumber = pomNiz[PlayerService.rand.nextInt(pomNiz.length)];
				}
			}
			diamondPositions[i] = selectedNumber;
		}
	}

	private void refresh() {
		for (Integer i = 0; i < rows; i++) {
			for (Integer j = 0; j < columns; j++) {
				if (m[i][j] instanceof Diamond) {
					mPanela[i][j].setBackground(new Color(238, 238, 238));
					m[i][j] = null;
				}
			}
		}
	}

	private void finishTheDiamond() {
		for (Integer i = 0; i < rows; i++) {
			for (Integer j = 0; j < columns; j++) {
				for (Integer k = 0; k < diamondPositions.length; k++) {
					if ((i * rows) + (j + 1) == diamondPositions[k] && m[i][j] instanceof Diamond) {
						mPanela[i][j].setBackground(new Color(238, 238, 238));
						m[i][j] = null;
					}
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			while (Map.END == false) {
				refresh();
				this.numOfDiamonds = PlayerService.rand.nextInt(rows) + 2;
				ArrayDeque<Integer> pom = path.clone();
				determineDiamondPositions(numOfDiamonds, pom);
				for (Integer i = 0; i < rows; i++) {
					for (Integer j = 0; j < columns; j++) {
						for (Integer k = 0; k < diamondPositions.length; k++) {
							if ((i * rows) + (j + 1) == diamondPositions[k] && !(m[i][j] instanceof Hole)
									&& !(m[i][j] instanceof Figure)) {
								mPanela[i][j].setBackground(Color.orange);
								m[i][j] = new Diamond();
							}
						}
					}
				}
				sleep(5000);
			}
			finishTheDiamond();
		} catch (Exception ex) {
			Logger.getLogger(GhostFigure.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
}
