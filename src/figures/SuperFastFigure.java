package figures;

import java.awt.Color;
import java.util.ArrayList;

import interfaces.DogInTheHole;

public class SuperFastFigure extends Figure implements DogInTheHole {

	public SuperFastFigure() {
		super();
	}

	public SuperFastFigure(Color color, Integer brojPolja) {
		super(color, brojPolja);
	}

	public SuperFastFigure(Color color, ArrayList<Integer> path, Integer br) {
		super(color, path, br);
	}

}
