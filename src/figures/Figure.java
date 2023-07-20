package figures;

import java.awt.Color;
import java.util.ArrayList;

import models.MovementHistory;

public abstract class Figure extends Thread {
	protected Color color;
	protected Integer numberOfFileds; 	// Number of squares the given figure passes over
	protected Integer figureNumber; 	// Indicates the number of the figure for a player (from 1 to 4)

	protected MovementHistory movementHistory;
	protected ArrayList<Integer> path;
	protected ArrayList<Integer> pomPath;

	protected Integer currField;
	protected Integer prevField;
	protected Integer nextField;

	protected Integer figureId;
	protected Boolean hasArrived;

	public static Integer counter = 1;

	public Figure() {
		super();
	}

	public Figure(Color color, Integer numberOfFileds) {
		super();
		this.color = color;
		this.numberOfFileds = numberOfFileds;
	}

	public Figure(Color color, ArrayList<Integer> path, int br) {
		super();
		this.color = color;
		this.path = new ArrayList<>();
		this.pomPath = new ArrayList<>();
		for (int i = 0; i < path.size(); i++) {
			this.path.add(path.get(i));
			this.pomPath.add(path.get(i));
		}
		this.figureNumber = br;
		movementHistory = new MovementHistory();
		this.figureId = counter++;
		this.hasArrived = false;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public synchronized Integer getNumberOfFileds() {
		return numberOfFileds;
	}

	public synchronized void setNumberOfFields(Integer numberOfFileds) {
		this.numberOfFileds = numberOfFileds;
	}

	public MovementHistory getMovementHistory() {
		return movementHistory;
	}

	public void setMovementHistory(MovementHistory movementHistory) {
		this.movementHistory = movementHistory;
	}

	public synchronized ArrayList<Integer> getPath() {
		return path;
	}

	public synchronized void setPath(ArrayList<Integer> path) {
		this.path = path;
	}

	public synchronized Integer getCurrField() {
		return currField;
	}

	public synchronized void setCurrField(Integer currField) {
		this.currField = currField;
	}

	public synchronized Integer getPrevField() {
		return prevField;
	}

	public synchronized void setPrevField(Integer prevField) {
		this.prevField = prevField;
	}

	public synchronized Integer getNextField() {
		return nextField;
	}

	public synchronized void setNextField(Integer nextField) {
		this.nextField = nextField;
	}

	public synchronized ArrayList<Integer> getPomPath() {
		return pomPath;
	}

	public synchronized void setPomPath(ArrayList<Integer> pomPath) {
		this.pomPath = pomPath;
	}

	public Integer getFigureNumber() {
		return figureNumber;
	}

	public void setFigureNumber(Integer figureNumber) {
		this.figureNumber = figureNumber;
	}

	public Integer getFigureId() {
		return figureId;
	}

	public void setFigureId(Integer figureId) {
		this.figureId = figureId;
	}

	public Boolean hasArrived() {
		return hasArrived;
	}

	public void setHasArrived(Boolean hasArrived) {
		this.hasArrived = hasArrived;
	}

	@Override
	public String toString() {
		return "Figure [color=" + color + ", numberOfFileds=" + numberOfFileds + "]";
	}

}
