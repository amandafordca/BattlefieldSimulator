package actor;
import java.io.IOException;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import army.Army;
import util.Input;
import util.SingletonRandom;

/**
 * <i>Hobbit</i> class is a subclass of Actor which adds the double <i>stealth</i>. 
 * @author Amanda Ford
 */


public class Hobbit extends Actor{

	/**Public final static variable to record and maintain a minimum stealth value of 0.*/
	public final static double MIN_STEALTH = 0.0;
	/**Public final static variable to record and maintain a maximum stealth value of 50.*/
	public final static double MAX_STEALTH = 50.0;
	
	/**A private double variable named <i>stealth</i> used to store stealth for <i>Hobbit</i> objects.*/
	private double stealth;
	private Circle avatar;
	private static int numOfHobbits;

/**A public no argument constructor for <i>Hobbit</i> class objects. */
	public Hobbit(){
		super();
		setStealth(SingletonRandom.instance.getNormalDistribution(MIN_STEALTH, MAX_STEALTH, 2.0));
		setName("Hobbit " + (numOfHobbits+=1));

	}	//ends no argument constructor
	/**Saves the state of stealth to disk for that actor
	 * @param out File to write data to
	 * @throws IOException Unable to write to file*/
	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {out.writeDouble(stealth); }
	/**Restores the state of stealth for that actor from previous save to disk
	 * @param in File to write restore from
	 * @throws IOException Unable to read from file
	 * @throws ClassNotFoundException Unable to read String*/
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException{ stealth= in.readDouble(); }
	
	/**Hobbit constructor that takes armyAllegiance
	 * @param armyAllegiance Takes an army allegiance*/
	public Hobbit(Army armyAllegiance) {
	super(armyAllegiance);
	setStealth(SingletonRandom.instance.getNormalDistribution(MIN_STEALTH, MAX_STEALTH, 2.0));
	setName("Hobbit " + (numOfHobbits+=1));
	
}

	/**A public method <i>setStealth</i> which sets the stealth
	 * @param stealth Sets the stealth for hobbit*/
	public void setStealth(double stealth){
		
		if(stealth<MIN_STEALTH){
			this.stealth=MIN_STEALTH;
		}
		else if(stealth>MAX_STEALTH){
			this.stealth=MAX_STEALTH;
		}
		else {
			this.stealth = stealth;
		}
	}
	
	/**A public method <i>getStealth</i> that returns the value of stealth.
	 * @return Returns the hobbit's stealth value.*/
	public double getStealth(){
		return stealth;
	}
	
	/**A public method <i>inputAllFields</i> which uses <i>inputAllFields</i> from the super class
	 * as well as adding inputs for the <i>stealth</i> variable. */
	@Override
	public void inputAllFields() {
	super.inputAllFields();
	setStealth(Input.instance.getDouble(String.format("Stealth:%.1f", getStealth()), MIN_STEALTH, MAX_STEALTH));
	}
	
	/**A public method <i>toString</i> which uses the<i>toString</i> method from the actor superclass
	 * and adds the new variables from <i>Hobbit</i> class.
	 * @return Returns a string containing info about the hobbit*/
	@Override
	public String toString(){
		return String.format(super.toString()+" Stealth: %.1f ", stealth);
	}
	/**Returns the Hobbit's avatar
	 * @return Returns the Avatar for that hobbit*/
	@Override
	public Circle getAvatar() {
		return avatar;
	}
	/**Creates an avatar to represent the Hobbit*/
	@Override
	public void createAvatar() {
		avatar = new Circle(8.0, Color.GREEN);	
	}
	/**Calculates the new location for Hobbit to move to. Hobbits move differently
	 * from other actors. Rather than moving towards their opponent, they will move to 
	 * a location away from their opponent.
	 * @return Returns the Point2D location for the actor to move to
	 * @param opponent Takes the opponent whose location you will use to determine move.
	 */
	@Override
	protected Point2D calculateNewLocation(Actor opponent) {
		//return new Point2D (((opponent.getAvatar().getTranslateX()-((opponent.getAvatar().getTranslateX()-getAvatar().getTranslateX())/2))*-1),
			//	((opponent.getAvatar().getTranslateY()-((opponent.getAvatar().getTranslateY()-getAvatar().getTranslateY())/2)))*-1);
	double x;
	double y;
	Point2D startLocation = new Point2D (getAvatar().getTranslateX(), getAvatar().getTranslateY());
	Point2D opponentLocation = new Point2D (opponent.getAvatar().getTranslateX(),opponent.getAvatar().getTranslateY());
	if (startLocation.getX()>opponentLocation.getX()){
		x = 200;		
	}
	else {
		x = -200;
	}
	if (startLocation.getY()>opponentLocation.getY()){
		y = 200;
	}
	else {
		y = -200;
	}
	return  (opponentLocation.add(x, y));
	
	}
	/**Uses the hobbit's stealth to determine if he will be visible or not 
	 * @return Returns if the hobbit is visible or not.*/
	@Override
	public boolean isVisible() {
		return getStealth() < (MIN_STEALTH+MAX_STEALTH)/2.0;
	}

}	//ends Hobbit class
