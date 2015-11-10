package marques.ctrl;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;

import javafx.scene.image.Image;

public class CardDTO implements Serializable {

	private static final long serialVersionUID = -171103021043509188L;
	private int amount;
	private String name;
	private File imageFile;
	private Image image = new Image(CardDTO.class.getResourceAsStream("/marques/gui/not-found.jpg"));
	
	

	public CardDTO(int amount, String name) {
		super();
		this.amount = amount;
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
		try {
			image = new Image(imageFile.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public Image getImage() {
		return image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((imageFile == null) ? 0 : imageFile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardDTO other = (CardDTO) obj;
		if (amount != other.amount)
			return false;
		if (imageFile == null) {
			if (other.imageFile != null)
				return false;
		} else if (!imageFile.equals(other.imageFile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + amount + "x) " + name;
	}

}
