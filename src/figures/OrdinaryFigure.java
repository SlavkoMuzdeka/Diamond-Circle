package figures;

import java.awt.Color;
import java.util.ArrayList;

import interfaces.DogInTheHole;

public class OrdinaryFigure extends Figure implements DogInTheHole {

	public OrdinaryFigure() {
		super();
	}

	public OrdinaryFigure(Color color, Integer brojPolja) {
		super(color, brojPolja);
	}

	public OrdinaryFigure(Color color, ArrayList<Integer> path, Integer br) {
		super(color, path, br);
	}

}
