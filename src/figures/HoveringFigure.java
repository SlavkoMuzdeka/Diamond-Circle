package figures;

import java.awt.Color;
import java.util.ArrayList;

import interfaces.Hover;

public class HoveringFigure extends Figure implements Hover {

	public HoveringFigure() {
		super();
	}

	public HoveringFigure(Color color, Integer brojPolja) {
		super(color, brojPolja);
	}

	public HoveringFigure(Color color, ArrayList<Integer> path, Integer br) {
		super(color, path, br);
	}

}
