package cards;

import java.awt.image.BufferedImage;

public abstract class Card {

	protected BufferedImage image;

	public Card() {
		super();
	}

	public Card(BufferedImage image) {
		super();
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
