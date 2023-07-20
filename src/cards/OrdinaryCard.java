package cards;

import java.awt.image.BufferedImage;

public class OrdinaryCard extends Card {

	private int numOfFields;

	public OrdinaryCard() {
		super();
	}

	public OrdinaryCard(BufferedImage image, Integer numOfFields) {
		super(image);
		this.numOfFields = numOfFields;
	}

	public Integer getNumOfFields() {
		return numOfFields;
	}

	public void setNumOfFields(Integer numOfFields) {
		this.numOfFields = numOfFields;
	}

}
