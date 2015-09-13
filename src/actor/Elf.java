
package actor;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import army.Army;
import util.Input;
import util.SingletonRandom;

/**
 * <i>Elf</i> class is a subclass of Actor which adds the double <i>stealth</i>,
 * int <i>arrows</i> and boolean <i>hasBow</i>. 
 * @author Amanda Ford
 */


public class Elf extends Actor{

	/**Public final static variable to record and maintain a minimum stealth value of 0.*/
	public final static double MIN_STEALTH = 0.0;
	/**Public final static variable to record and maintain a maximum stealth value of 50.*/
	public final static double MAX_STEALTH = 50.0;
	/**Public final static variable to record and maintain a minimum arrow amount of 0.*/
	public final static double MIN_ARROWS = 0;
	/**Public final static variable to record and maintain a maximum arrow amount of 30.*/
	public final static double MAX_ARROWS = 30;
	/**A private boolean variable named <i>hasBow</i> used to store if the Elf object has a bow.*/
	private double stealth;		//double used to store Elf stealth
	private double arrows;		//double that stores the number of arrows
	private boolean hasBow;		//boolean that determines whether the Elf has a bow or not.
	private ImageView avatar;
	private static int numOfElves;
	private static final double NORMAL_SIZE = 9.0;


	/**A public no argument constructor for <i>Elf</i> class objects. */
	public Elf(){
		super();
		setStealth(SingletonRandom.instance.getNormalDistribution(MIN_STEALTH, MAX_STEALTH, 2.0));
		hasBow = SingletonRandom.instance.getRandomBoolean();
		setArrows(SingletonRandom.instance.getNormalDistribution(MIN_ARROWS, MAX_ARROWS, 2));
		setName("Elf " + (numOfElves+=1));

	}	//ends no argument constructor

	/**A constructor for <i>Elf</i> that takes armyAllegiance
	 * @param armyAllegiance Takes an army allegiance*/
	public Elf(Army armyAllegiance) {
		super(armyAllegiance);
		setStealth(SingletonRandom.instance.getNormalDistribution(MIN_STEALTH, MAX_STEALTH, 2.0));
		hasBow = SingletonRandom.instance.getRandomBoolean();
		setArrows(SingletonRandom.instance.getNormalDistribution(MIN_ARROWS, MAX_ARROWS, 2));
		setName("Elf " + (numOfElves+=1));

	}
	/**Saves state for Elf specific attributes
	 * @param out File to write data to
	 * @throws IOException Unable to write to file*/
	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {out.writeBoolean(hasBow); out.writeDouble(arrows); out.writeDouble(stealth);}
	/**Restores state of Elf specific attributes that have previously been saved to disk
	 * @param in File to write restore from
	 * @throws IOException Unable to read from file
	 * @throws ClassNotFoundException Unable to read String*/
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException{ hasBow= in.readBoolean();
			arrows= in.readDouble(); stealth=in.readDouble(); }

	/**A public method <i>setStealth</i> which sets the stealth level for an Elf object
	 * @param stealth Sets the value of stealth for elf*/
	public void setStealth(double stealth){
		this.stealth = stealth;
	}

	/**A public method <i>getStealth</i> that returns the value of stealth.
	 * @return Returns the elf's stealth value*/
	public double getStealth(){
		return stealth;
	}

	/**A public method <i>setHasBow</i> which sets whether the Elf object has a bow or not.
	 * @param hasBow Sets whether the elf has a bow or not*/
	public void setHasBow(boolean hasBow){
		this.hasBow = hasBow;
	}

	/**A public method <i>getHasBow</i> that returns if hasBow is true or false.
	 * @return Returns if Elf has bow or not.*/
	public boolean getHasBow(){
		return hasBow;
	}

	/**A public method <i>setArrows</i> which sets the amount of arrows the Elf object has.
	 * @param arrows Sets how many arrows the elf has*/
	public void setArrows(double arrows){
		this.arrows = arrows;
	}

	/**A public method <i>getArrows</i> that returns the number of arrows the Elf object has.
	 * @return Returns the number of arrows the Elf has*/
	public double getArrows(){
		return arrows;
	}
	/**A public method <i>inputAllFields</i> which uses <i>inputAllFields</i> from the super class
	 * as well as adding inputs for the <i>stealth</i>, <i>arrows</i> and <i>hasBow</i> variables. */
	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setStealth(Input.instance.getDouble(String.format("Stealth: %.1f", getStealth()), MIN_STEALTH, MAX_STEALTH));
		setArrows(Input.instance.getDouble(String.format("Arrows: %.0f", getArrows()), MIN_ARROWS, MAX_ARROWS));
		setHasBow(Input.instance.getBoolean(String.format("Does this Elf have a bow? "+getHasBow()+" (true/false): ")));

	}

	/**A public method <i>toString</i> which uses the<i>toString</i> method from the actor superclass
	 * and adds the new variables from <i>Elf</i> class.
	 * @return Returns a String containing info for this Elf*/
	@Override
	public String toString(){
		return String.format(super.toString()+" Stealth: %.1f Arrows: %.0f Warg: %b ", stealth, arrows, hasBow);
	}

	/**Returns Elf avatar
	 * @return Returns the Elf's avatar*/
	@Override
	public Node getAvatar() {
		return avatar;
	}

	/**Creates Elf avatar*/
	@Override
	public void createAvatar() {
		//		avatar = new Rectangle(50.0, 20.0, Color.AQUAMARINE);
		//		avatar.setStrokeWidth(2.0);
		try { // try-catch block implemented to manage potential file loading issues.
			avatar = new ImageView(new Image(new FileInputStream("elf.gif")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Cannot find ");
			System.exit(0);
		}

		avatar.setFitWidth(NORMAL_SIZE * 4.0);
		avatar.setPreserveRatio(true);
	} // end createAvatar()

	/**Method that determines if the Elf is visible depending on his stealth level
	 * @return Returns if the Elf is visible or not */
	@Override
	public boolean isVisible() {
		return getStealth() < (MIN_STEALTH+MAX_STEALTH)/2.0;
	}
	/**Method that determines the location the Elf will move to
	 * @return Returns the Point2D location for the actor to move to
	 * @param opponent Takes the opponent whose location you will use to determine move.
	 */
	@Override
	protected Point2D calculateNewLocation(Actor opponent) {
		//return new Point2D(opponent.getAvatar().getTranslateX(), opponent.getAvatar().getTranslateY()); 
		//return new Point2D(Math.abs(opponent.getAvatar().getTranslateX()-((opponent.getAvatar().getTranslateX()-getAvatar().getTranslateX())/2)),
		//Math.abs(opponent.getAvatar().getTranslateY()-((opponent.getAvatar().getTranslateY()-getAvatar().getTranslateY())/2)));
		Point2D startLocation = new Point2D (getAvatar().getTranslateX(), getAvatar().getTranslateY());
		Point2D opponentLocation = new Point2D (opponent.getAvatar().getTranslateX(),opponent.getAvatar().getTranslateY());
		return  (startLocation.midpoint(opponentLocation));

	}

}	//ends Elf class
