package actor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import army.Army;
import util.Input;
import util.SingletonRandom;

/**
 * <i>Wizard</i> class is a subclass of Actor which adds the booleans <i>hasStaff</i> and <i>hasHorse</i> 
 * @author Amanda Ford
 */

public class Wizard extends Actor{
	/**The boolean variable <i>hasStaff</i> stores whether this wizard character has a staff or
	 * not which will later make changes in their combat behaviour */
	private boolean hasStaff;
	/**The boolean variable <i>hasHorse</i> stores whether this wizard character has a horse or
	 * not which will later make changes in their combat behaviour */
	private boolean hasHorse;
	private ImageView avatar;
	private static int numOfWizards=0;
	private static final double NORMAL_SIZE = 8.0;

	/**No argument constructor for Wizard objects.*/
	public Wizard(){
		super();
		hasStaff = SingletonRandom.instance.getRandomBoolean();
		hasHorse = SingletonRandom.instance.getRandomBoolean();
		if (hasHorse){	//if wizard has a horse, set speed to MAX_SPEED
			speed.set(MAX_SPEED);
		}
		setName("Wizard " + (numOfWizards+=1));

	}	//ends Wizard constructor without arguments

	/**Saves state of the Wizard specific attributes for this Wizard
	 * @param out File to write data to
	 * @throws IOException Unable to write to file */
	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {out.writeBoolean(hasStaff); out.writeBoolean(hasHorse);}
	/**Restores state of Wizard specific attributes from previously saved state.
	 * @param in File to write restore from
	 * @throws IOException Unable to read from file
	 * @throws ClassNotFoundException Unable to read String*/
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException{ hasStaff= in.readBoolean(); hasHorse= in.readBoolean(); }


	/**A Wizard constructor that takes armyAllegiance
	 * @param armyAllegiance Takes an army allegiance*/
	public Wizard(Army armyAllegiance) {
		super(armyAllegiance);
		hasStaff = SingletonRandom.instance.getRandomBoolean();
		hasHorse = SingletonRandom.instance.getRandomBoolean();
		setName("Wizard " + (numOfWizards+=1));
		if (hasHorse){	//if wizard has a horse, set speed to MAX_SPEED
			speed.set(MAX_SPEED);
		}
	}
	/**Public method <i>setHasStaff</i> that sets the variable hasStaff
	 * @param hasStaff Takes whether or not the wizard has a staff.*/
	public void setHasStaff(boolean hasStaff){
		this.hasStaff=hasStaff;
	}
	/**Public method <i>getHasStaff</i> that returns hasStaff
	 * @return Returns if the wizard has a staff or not*/
	public boolean getHasStaff(){
		return hasStaff;
	}
	/**Public method <i>setHasHorse</i> that sets the variable hasHorse
	 * @param hasHorse Sets whether the wizard has a horse or not*/
	public void setHasHorse(boolean hasHorse){
		this.hasHorse = hasHorse;
	}
	/**Public method <i>getHasHorse</i> that returns hasHorse
	 * @return Returns whether the wizard has a horse or not*/
	public boolean getHasHorse(){
		return hasHorse;
	}
	/**A public method <i>inputAllFields</i> which uses <i>inputAllFields</i> from the super class
	 * as well as adding inputs for the <i>hasStaff</i> and <i>hasHorse</i> booleans. */
	@Override
	public void inputAllFields() {
		super.inputAllFields ();	
		setHasStaff(Input.instance.getBoolean("Does this wizard have a staff? "+getHasStaff()+" (true/false): "));
		setHasHorse(Input.instance.getBoolean("Does this wizard have a horse? "+getHasHorse()+" (true/false): "));
	}


	/**A public method <i>toString</i> which uses the<i>toString</i> method from the actor superclass
	 * and adds the new variables from Wizard
	 * @return Returns a string with info about the wizard*/
	@Override
	public String toString(){
		return String.format(super.toString()+" Staff: %b Horse: %b ", hasStaff, hasHorse);
	}

	/**Returns Wizard avatar
	 * @return Returns the wizard's avatar*/
	@Override
	public Node getAvatar() {
		return avatar;
	}

	/**Creates wizard avatar*/
	@Override
	public void createAvatar() {

		//		try { // try-catch block implemented to manage potential file loading issues.
		//			avatar = new ImageView("Wizard.gif");
		//		} catch (FileNotFoundException e1) {
		//			e1.printStackTrace();
		//			System.err.println("Cannot find ");
		//			System.exit(0);
		//		}

		//		avatar.setFitWidth(NORMAL_SIZE * 6.0);
		//		avatar.setPreserveRatio(true);

		//		 avatar = new ImageView("Wizard.gif");
		//	      avatar.setFitWidth(NORMAL_SIZE*6.0);
		//	      avatar.setPreserveRatio(true);		
		try { // try-catch block implemented to manage potential file loading issues.
			avatar = new ImageView(new Image(new FileInputStream("Wizard.gif")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Cannot find ");
			System.exit(0);
		}

		avatar.setFitWidth(NORMAL_SIZE * 6.0);
		avatar.setPreserveRatio(true);
	}

	/**Calculates new location for wizard to move to. Wizards move towards opponents.
	 * @return Returns the Point2D location for the actor to move to
	 * @param opponent Takes the opponent whose location you will use to determine move.
	 */
	@Override
	protected Point2D calculateNewLocation(Actor opponent) {
		//return new Point2D(opponent.getAvatar().getTranslateX(), opponent.getAvatar().getTranslateY()); 
		//return new Point2D(Math.abs(opponent.getAvatar().getTranslateX()-((opponent.getAvatar().getTranslateX()-getAvatar().getTranslateX())/2)),
		//	Math.abs(opponent.getAvatar().getTranslateY()-((opponent.getAvatar().getTranslateY()-getAvatar().getTranslateY())/2)));
		Point2D startLocation = new Point2D (getAvatar().getTranslateX(), getAvatar().getTranslateY());
		Point2D opponentLocation = new Point2D (opponent.getAvatar().getTranslateX(),opponent.getAvatar().getTranslateY());
		//	if (startLocation.midpoint(opponentLocation).getX*()>)
		return (startLocation.midpoint(opponentLocation));
	}

	/**Returns if the wizard is visible or not. Wizards that have a staff are invisible
	 * @return Returns if the wizard is visible or not*/
	@Override
	public boolean isVisible() {
		return ! hasStaff;
	}

}	//ends Wizard class
