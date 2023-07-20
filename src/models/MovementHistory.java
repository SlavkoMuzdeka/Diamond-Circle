package models;

import java.util.ArrayList;

public class MovementHistory {
	private Long time;
	private ArrayList<Integer> coordinates;

	public MovementHistory() {
		super();
		this.coordinates = new ArrayList<>();
	}

	public long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public ArrayList<Integer> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Integer> coordinates) {
		this.coordinates = coordinates;
	}

	public void addCoordinate(Integer movement) {
		this.coordinates.add(movement);
	}

	@Override
	public String toString() {
		return "MovementHistory [time=" + time + ", coordinates=" + coordinates + "]";
	}

}
