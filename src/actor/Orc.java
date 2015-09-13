
package actor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import army.Army;
import util.Input;
import util.SingletonRandom;

/**
 * <i>Orc</i> class is a subclass of Actor which adds the double <i>rage</i> and boolean <i>hasWarg</i>. 
 * @author Amanda Ford
 */


public class Orc extends Actor{

	/**Public final static variable to record and maintain a minimum rage value of 0.*/
	public final static double MIN_RAGE = 0.0;
	/**Public final static variable to record and maintain a maximum rage value of 50.*/
	public final static double MAX_RAGE = 50.0;

	/**A private double variable named <i>rage</i> used to store rage level for <i>Orc</i> objects.*/
	private double rage;
	/**A private boolean variable named <i>hasWarg</i> used to store if the orc object is riding a warg.*/
	private boolean hasWarg;
	private ImageView avatar;
	private static int numOfOrcs;
	private static final double NORMAL_SIZE = 9.0;

	/**A public no argument constructor for <i>Orc</i> class objects. */
	public Orc(){
		super();
		setRage(SingletonRandom.instance.getNormalDistribution(MIN_RAGE, MAX_RAGE, 2.0));
		hasWarg = SingletonRandom.instance.getRandomBoolean();
		setName("Orc " + (numOfOrcs+=1));
		if (rage>25){	//If orc's rage is greater than half, set speed to MAX_SPEED
			speed.set(MAX_SPEED);
		}
		if (hasWarg){	//if orc has a warg, set speed to MAX_SPEED
			speed.set(MAX_SPEED);
		}

	}	//ends no argument constructor

	/**A constructor for <i>Orc</i> that takes armyAllegiance
	 * @param armyAllegiance Takes an army allegiance*/
	public Orc(Army armyAllegiance) {
		super(armyAllegiance);
		setRage(SingletonRandom.instance.getNormalDistribution(MIN_RAGE, MAX_RAGE, 2.0));
		hasWarg = SingletonRandom.instance.getRandomBoolean();
		setName("Orc " + (numOfOrcs+=1));
		if (hasWarg){	//if orc has a warg, set speed to MAX_SPEED
			speed.set(MAX_SPEED);
		}

	}
	/**Writes the Orc specific variables to disk when state is saved
	 * @param out File to write data to
	 * @throws IOException Unable to write to file*/
	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {out.writeBoolean(hasWarg); out.writeDouble(rage);}
	/**Reads the Orc specific variables for a specific Orc that was saved to state and restores it 
	 * @param in File to write restore from
	 * @throws IOException Unable to read from file
	 * @throws ClassNotFoundException Unable to read String*/
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException{ hasWarg= in.readBoolean(); rage= in.readDouble(); }

	/**A public method <i>setRage</i> which sets the rage level for the Orc object
	 * @param rage Sets the value of orc's rage*/
	public void setRage(double rage){
		this.rage = rage;
	}

	/**A public method <i>getRage</i> that returns the value of rage.
	 * @return Returns the value of the orcs rage*/
	public double getRage(){
		return rage;
	}

	/**A public method <i>setHasWarg</i> which sets whether the Orc object has a warg or not.
	 * @param hasWarg Sets if the orc has a warg or not*/
	public void setHasWarg(boolean hasWarg){
		this.hasWarg = hasWarg;
	}

	/**A public method <i>getHasWarg</i> that returns if hasWarg is true or false.
	 * @return Returns if the orc has a warg or not*/
	public boolean getHasWarg(){
		return hasWarg;
	}

	/**A public method <i>inputAllFields</i> which uses <i>inputAllFields</i> from the super class
	 * as well as adding inputs for the <i>rage</i> and <i>hasWarg</i> variables. */
	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setRage(Input.instance.getDouble(String.format("Rage:%.1f", getRage()), MIN_RAGE, MAX_RAGE));
		setHasWarg(Input.instance.getBoolean("Does this Orc have a warg? " +getHasWarg()+ " (true/false): "));
	}

	/**A public method <i>toString</i> which uses the<i>toString</i> method from the actor superclass
	 * and adds the new variables from <i>Orc</i> class.
	 * @return Returns a String containing info about the orc*/
	@Override
	public String toString(){
		return String.format(super.toString()+" Rage: %.1f Warg: %b ", rage, hasWarg);
	}

	/**Returns the Orc's avatar
	 * @return Returns the Orc's avatar*/
	@Override
	public Node getAvatar() {
		return avatar;
	}
	/**Creates an avatar for the Orc*/
	@Override
	public void createAvatar() {
		try { // try-catch block implemented to manage potential file loading issues.
			avatar = new ImageView(new Image(new FileInputStream("orc.gif")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Cannot find ");
			System.exit(0);
		}

		avatar.setFitWidth(NORMAL_SIZE * 6.0);
		if(rage>=0 && rage<20){				// if rage is less than 20, avatar scale is normal
			avatar.setFitWidth(NORMAL_SIZE * 6.0);
		}
		else if (rage>=20 && rage<40){		// if rage is within 20 to 40, avatar is midsize
			avatar.setFitWidth(NORMAL_SIZE * 8.0);
		}
		else{
			avatar.setFitWidth(NORMAL_SIZE * 10.0);	//if rage is greater than 40, avatar is large
		}
		avatar.setPreserveRatio(true);

		//avatar = new Circle(8.0, Color.RED);
		//avatar.setStrokeWidth(2.0);
	}
	
	/**Calculates the new location for the Orc to move. Orcs always move towards their opponent.
	 * @return Returns the Point2D location for the actor to move to
	 * @param opponent Takes the opponent whose location you will use to determine move.*/
	@Override
	protected Point2D calculateNewLocation(Actor opponent) {
		//return new Point2D(Math.abs(opponent.getAvatar().getTranslateX()-((opponent.getAvatar().getTranslateX()-getAvatar().getTranslateX())/2)),
		//Math.abs(opponent.getAvatar().getTranslateY()-((opponent.getAvatar().getTranslateY()-getAvatar().getTranslateY())/2)));
		//return new Point2D(opponent.getAvatar().getTranslateX(), opponent.getAvatar().getTranslateY()); 
		Point2D startLocation = new Point2D (getAvatar().getTranslateX(), getAvatar().getTranslateY());
		Point2D opponentLocation = new Point2D (opponent.getAvatar().getTranslateX(),opponent.getAvatar().getTranslateY());
		return  (startLocation.midpoint(opponentLocation));
	}

	/**Returns if orc is visible. Orcs are always visible 
	 * @return Returns if the orc is visible or not*/
	@Override
	public boolean isVisible() {
		return true;
	}

}	//ends Orc class
