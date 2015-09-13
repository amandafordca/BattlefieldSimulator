package actor;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import army.Army;
import util.Input;
import util.SingletonRandom;

/**
 * Actor class used to store the characteristics of each player in the Battlefield Simulator.
 * @author Amanda Ford
 * @author Rex Woollard for inputAllFields() and SingletonRandom references
 */

public abstract class Actor implements Serializable {

	public final static double MIN_HEALTH = 1.0;// static variable: one and only one instance shared by all objects
	public final static double MAX_HEALTH = 100.0;
	public final static double MIN_STRENGTH = 50.0;
	public final static double MAX_STRENGTH = 100.0;
	public final static double MIN_SPEED = 5.0;
	public final static double MAX_SPEED = 10.0;

	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleDoubleProperty strength = new SimpleDoubleProperty(); //Stores a primitive in a reference variable
	protected SimpleDoubleProperty speed = new SimpleDoubleProperty();
	private SimpleDoubleProperty health = new SimpleDoubleProperty();
	private TranslateTransition tt;
	private Tooltip tooltip; //used to display onscreen information about the actors

	private Army armyAllegiance;
	private boolean allowCombat = true;

	/** A constructor used to generate random values for strength, health and speed and a sequential ID for the name.  */
	public Actor() {

		setStrength(SingletonRandom.instance.getNormalDistribution(MIN_STRENGTH, MAX_STRENGTH, 2.0));
		setHealth(SingletonRandom.instance.getNormalDistribution(MIN_HEALTH, MAX_HEALTH, 2.0));
		setSpeed(SingletonRandom.instance.getNormalDistribution(MIN_SPEED, MAX_SPEED, 2.0));

	}

	/**Constructor that sets whether the actor is evil or good
	 * @param armyAllegiance Takes an army allegiance*/	
	public Actor(Army armyAllegiance) {
		this();
		this.armyAllegiance = armyAllegiance;

	}
	/**Returns the army allegiance of actor
	 * @return returns army allegiance*/
	public Army getArmyAllegiance(){
		return armyAllegiance;
	}

	/**Set's the actor's army allegiance
	 * @param army Sets the actor's army allegiance*/
	public void setArmyAllegiance(Army army) {
		this.armyAllegiance = army;
	}

	/**Method which allows health to be set but stay within the boundaries.
	 * @param health Sets the actor's health*/
	public void setHealth(double health){
		if (health < MIN_HEALTH){
			health = MIN_HEALTH;
		}
		else if (health > MAX_HEALTH){
			health = MAX_HEALTH;
		}
		this.health.set(health);
	}

	/**Method which returns health
	 * @return Returns actor's health*/
	public double getHealth(){ return health.get(); }

	/**Method which adjusts health value
	 * @param changeToValue Adjusts the actor's health*/	
	public void adjustHealth(double changeToValue){
		health.set(health.get()- changeToValue);
	}

	/**Method which allows strength to be set but stay within the boundaries.
	 * @param strength Sets the actor's strength*/
	public void setStrength(double strength){
		if (strength < MIN_STRENGTH ){
			strength = MIN_STRENGTH;
		}
		else if (strength > MAX_STRENGTH){
			strength = MAX_STRENGTH;
		}
		this.strength.set(strength); //sets a double value (stack strength) to the SimpleDoubleProperty (heap strength)
	}

	/**Method which returns strength value
	 * @return Returns actor's strength*/
	public double getStrength(){ return strength.get(); } //returns heap strength as a double

	/**Method which allows speed to be set but stay within the boundaries.
	 * @param speed Sets the actor's speed*/
	public void setSpeed(double speed){
		if (speed < MIN_SPEED){
			speed = MIN_SPEED;
		}
		else if (speed > MAX_SPEED){
			speed = MAX_SPEED;
		}
		this.speed.set(speed);
	}

	/**Method which returns speed value
	 * @return Returns actor's speed*/
	public double getSpeed(){ return speed.get(); }

	/**Method which allows name to be set
	 * @param name Sets the actor's name*/
	public void setName (String name){
		this.name.set(name);
	}
	/**Method which returns name
	 * @return Returns actor's name*/
	public String getName(){ return name.get(); }

	/**Method used to allow the user to set all the values for the actor object*/
	public void inputAllFields() {
		setName(Input.instance.getString("Current Name:"+name+" New Name:"));
		setStrength(Input.instance.getDouble(String.format("Strength:%.1f",strength), MIN_STRENGTH, MAX_STRENGTH));
		setHealth(Input.instance.getDouble(String.format("Health:%.1f", health), MIN_HEALTH, MAX_HEALTH));
		setSpeed(Input.instance.getDouble(String.format("Speed:%.1f", speed), MIN_SPEED, MAX_SPEED));

	}
	/**Method used to represent one round of combat between two actor object.
	 * It uses the random class to vary damage. Health, speed and strength are
	 * all decreased accordingly
	 * @param defender A second actor is brought into the combat round.*/
	public void combatRound(Actor defender){		

		final double LOSS_OF_HEALTH_FOR_WINNER = 3.0;
		final double LOSS_OF_HEALTH_FOR_LOSER = 10.0;
		final double RANDOM_VARIATION_RANGE = 10.0;

		//Calculations for current actor winning
		if (this.strength.get()+Math.random()*RANDOM_VARIATION_RANGE > defender.strength.get()+Math.random()*RANDOM_VARIATION_RANGE) {
			this.health.set(this.health.get()-LOSS_OF_HEALTH_FOR_WINNER);
			defender.health.set(defender.health.get()-LOSS_OF_HEALTH_FOR_LOSER); 
			defender.strength.set(defender.strength.get()-5);
			this.strength.set(this.strength.get()-1);
			defender.speed.set(defender.speed.get()-(defender.speed.get()*.025));

			//Calculations for current actor losing
		} else {
			this.health.set(this.health.get()-LOSS_OF_HEALTH_FOR_LOSER);
			defender.health.set(defender.health.get()-LOSS_OF_HEALTH_FOR_WINNER);
			this.strength.set(this.strength.get()-5);
			defender.strength.set(defender.strength.get()-1);
			this.speed.set(this.speed.get()-(this.speed.get()*.025));
		}

		//			 if (this.health.get()<0){
		//				 this.health.set(0);
		//				 this.strength.set(0);
		//				 this.speed.set(0);
		//				 
		//			 }
		//			 if (defender.health.get()<0){
		//				defender.health.set(0);
		//				 defender.strength.set(0);
		//				 defender.speed.set(0);
		//			 }

		//return this.health.get();
	}
	/**Method used to print all of the current characteristics for an actor object
	 * @return Returns a string containing actor's characteristics*/
	public String toString(){
		return String.format("Name: %s Strength: %.1f Health: %.1f Speed: %.1f", getName(), getStrength(), getHealth(), getSpeed());
		//"Army Allegiance: %s", armyAllegiance.getForce();
	}	//end of toString()

	/**Updates the Tooltip for the Avatar */
	public void updateActorAvatar(){
		//have size reflect strength for example
		Tooltip.install(getAvatar(), new Tooltip(toString()));
	}

	public abstract void createAvatar();
	/**@return Returns actor's avatar*/
	public abstract Node getAvatar();


	/**Method to start avatars moving
	 * @param engageInCombat Takes a boolean to determine if engaging in combat is true */
	public void startMoving(boolean engageInCombat) {
		this.allowCombat = engageInCombat;
		tt = new TranslateTransition(Duration.seconds(Math.random()*5.0+1.0), getAvatar());
		//Scene scene = circle.getScene();
		//sets x and y locations relative to the scene constraints. Depending whether it is expanded or minimized.

		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor opponent = opposingArmy.findNearestActor(this);
		
		if (opponent != null){

			final double DISTANCE_FOR_COMBAT = 50.0;
			if (distanceTo(opponent) < DISTANCE_FOR_COMBAT && this.allowCombat){ //If they are close enough to fight.
				System.out.println(this.getName() + " is fighting " + opponent.getName());
				combatRound(opponent); // Engage in combat with opponent

				if (this.health.get() <= 0.0){	//If current actor is dead, remove them from army list and scene
					armyAllegiance.removeNowDeadActor(this);
					return;

				}
				if (opponent.health.get() <= 0.0){ //If opponent is dead, remove them from army list and scene
					opposingArmy.removeNowDeadActor(opponent);
				}
			}
			
//					System.out.printf("ToMove:[%.1f:%.1f] Opponent:[%.1f:%.1f]\n",
//							getAvatar().getTranslateX(),
//							getAvatar().getTranslateY(),
//							opponent.getAvatar().getTranslateX(),
//							opponent.getAvatar().getTranslateY());
			Point2D newLocation = calculateNewLocation(opponent);
			if (tt.getStatus() != Animation.Status.RUNNING){ //if not running start
				//tt.setToX(Math.random()*getAvatar().getScene().getWidth()); tt.setToY(Math.random()*getAvatar().getScene().getHeight());
				//tt.setToX(opponent.getAvatar().getTranslateX()); //need to replace with calculateNewLocation

				/*The following code keeps the actor avatars within the scene*/
				if( getAvatar() != null){	
					if(getAvatar().getScene() != null){
						if(newLocation != null){
							newLocation.getX();
							if (newLocation.getX()< 0){
								tt.setToX(100);
							}
							else if (newLocation.getX() > getAvatar().getScene().getWidth()){
								tt.setToX(getAvatar().getScene().getWidth()-100);
							}else{
								tt.setToX(newLocation.getX());
							}

							newLocation.getY();
							if (newLocation.getY()< 0){
								tt.setToY(100);
							}
							else if (newLocation.getY() > getAvatar().getScene().getHeight()){
								tt.setToY(getAvatar().getScene().getHeight()-100);
							}else{
								tt.setToY(newLocation.getY());
							}
						} // end if getAvatar not null
					} // end if getScene not null
				} //end if newlocation not null
			}//end if opponent not null
			//tt.setToX(Math.abs(newLocation.getX()-((newLocation.getX()-getAvatar().getTranslateX())/2)));
			//tt.setToY(Math.abs(newLocation.getY()-((newLocation.getY()-getAvatar().getTranslateY())/2)));
			if (getHealth()<1){
				tt.setDelay(Duration.seconds(1));
			}
			else{
				tt.setDelay(Duration.seconds(1.0/getHealth()));
			}
			//tt.setToY(opponent.getAvatar().getTranslateY());
			//tt.setDuration(Duration.seconds(Math.random()*5.0+1.0));	//random duration
			//	tt.setDuration(Duration.seconds(50.0/getSpeed()));
			if ((MAX_SPEED+MIN_SPEED)*5.0/(getSpeed()*armyAllegiance.getSpeedControllerValue())<1){
				tt.setDuration(Duration.seconds(1));
			}
			else{
				tt.setDuration(Duration.seconds((MAX_SPEED+MIN_SPEED)*5.0/(getSpeed()*armyAllegiance.getSpeedControllerValue())));
			} //end else

			tt.setOnFinished(event->startMoving(engageInCombat)); // NOT RECURSION!!!! Sets it as finished and calls move again
			tt.play(); // give assembled object to the render engine (of course, play() is an object-oriented method which has access to "this" inside, and it can use "this" to give to the render engine.
		} // end if running
	} // end startMoving()
	
	/**@return Returns the Point2D location for the actor to move to
	 * @param opponent Takes the opponent whose location you will use to determine move.
	 */
	protected abstract Point2D calculateNewLocation(Actor opponent); //abstract method. Must be protected so subclasses have access.
	
	/**Method to stop movement. Also stops combat*/
	public void suspendMoving() {
		allowCombat = false;
		if(tt != null && tt.getStatus()==Animation.Status.RUNNING){
			tt.pause();

		} // end if
	} // end suspendMoving()

	/** createTable is static to allow Army to define a table without having any Actor objects present.
	 * @return Returns an actor table view */
	public static TableView<Actor> createTable() {
		TableView<Actor> table = new TableView<Actor>();
		final double PREF_WIDTH_DOUBLE = 50.0;
		table.setPrefWidth(PREF_WIDTH_DOUBLE*7.5); // 7.0 because there are 6 individual columns, but one of those is DOUBLE-WIDTH, and there is some inter-column spacing
		table.setEditable(true);

		TableColumn<Actor, String> nameCol      = new TableColumn<>("Name");     nameCol.setCellValueFactory     (new PropertyValueFactory<Actor, String>("name"));         nameCol.setPrefWidth(PREF_WIDTH_DOUBLE*2.0);
		TableColumn<Actor, Number> healthCol    = new TableColumn<>("Health");   healthCol.setCellValueFactory   (cell->cell.getValue().health);       healthCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn<Actor, Number> strengthCol  = new TableColumn<>("Strength"); strengthCol.setCellValueFactory (cell->cell.getValue().strength);     strengthCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn<Actor, Number> speedCol     = new TableColumn<>("Speed");    speedCol.setCellValueFactory    (cell->cell.getValue().speed);        speedCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn<Actor, Number> locationXCol = new TableColumn<>("X");        locationXCol.setCellValueFactory(cell-> cell.getValue().getAvatar().translateXProperty()); locationXCol.setPrefWidth(PREF_WIDTH_DOUBLE); 
		TableColumn<Actor, Number> locationYCol = new TableColumn<>("Y");        locationYCol.setCellValueFactory(cell-> cell.getValue().getAvatar().translateYProperty()); locationYCol.setPrefWidth(PREF_WIDTH_DOUBLE); 
		ObservableList<TableColumn<Actor, ?>> c = table.getColumns(); c.add(nameCol); c.add(healthCol); c.add(strengthCol); c.add(speedCol); c.add(locationXCol); c.add(locationYCol);
		// Compare line ABOVE with line BELOW: The BELOW line looks cleaner and does actually work . . . but the compiler spits out a warning. The ABOVE line accomplishes the same thing, less elegantly, but without warnings.
		// table.getColumns().addAll(nameCol, healthCol, strengthCol, speedCol, locationXCol, locationYCol);

		// The following code makes each cell in the selected columns editable (Name, Health, Strength, Speed)
		// We CANNOT implement edit capabilities on the X/Y columns since they are READ-ONLY.
		nameCol.setCellFactory(TextFieldTableCell.<Actor>forTableColumn());
		nameCol.setOnEditCommit(event-> { Actor a = (event.getTableView().getItems().get(event.getTablePosition().getRow())); a.setName(event.getNewValue()); a.resetAvatarAttributes(); });

		healthCol.setCellFactory(TextFieldTableCell.<Actor, Number>forTableColumn(new NumberStringConverter()));
		healthCol.setOnEditCommit(event-> { Actor a = (event.getTableView().getItems().get(event.getTablePosition().getRow())); a.setHealth((Double)event.getNewValue()); a.resetAvatarAttributes(); });

		strengthCol.setCellFactory(TextFieldTableCell.<Actor, Number>forTableColumn(new NumberStringConverter()));
		strengthCol.setOnEditCommit(event-> { Actor a = (event.getTableView().getItems().get(event.getTablePosition().getRow())); a.setStrength((Double)event.getNewValue()); a.resetAvatarAttributes(); });

		speedCol.setCellFactory(TextFieldTableCell.<Actor, Number>forTableColumn(new NumberStringConverter()));
		speedCol.setOnEditCommit(event-> { Actor a = (event.getTableView().getItems().get(event.getTablePosition().getRow())); a.setSpeed((Double)event.getNewValue()); a.resetAvatarAttributes(); });

		return table;
	} // end createTable()

	public void resetAvatarAttributes() { tooltip.setText(toString()); } // Note: This updates the text in the Tooltip that was installed earlier. We re-use the originally installed Tooltip.
	/**@return Returns if the actor is visible or not*/
	public abstract boolean isVisible();

	// Explicit implementation of writeObject, but called implicitly as a result of recursive calls to writeObject() based on Serializable interface
	/**@param out File to write to
	 * @throws IOException Unable to write to file */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(getName());     // SimpleDoubleProperty name is NOT serializable, so I do it manually
		out.writeDouble(getStrength()); // SimpleDoubleProperty strength is NOT serializable, so I do it manually
		out.writeDouble(getHealth());   // SimpleDoubleProperty health is NOT serializable, so I do it manually
		out.writeDouble(getSpeed());    // SimpleDoubleProperty speed is NOT serializable, so I do it manually
		out.writeDouble(getAvatar().getTranslateX()); // Node battlefieldAvatar is NOT serializable. It's TOO BIG anyway, so I extract the elements that I need (here, translateX property) to retain manually.
		out.writeDouble(getAvatar().getTranslateY()); // Node battlefieldAvatar is NOT serializable. It's TOO BIG anyway, so I extract the elements that I need (here, translateY property) to retain manually.
	} // end writeObject() to support serialization

	// Explicit implementation of readObject, but called implicitly as a result of recursive calls to readObject() based on Serializable interface
	/**@param in File to write restore from
	 * @throws IOException Unable to read from file
	 * @throws ClassNotFoundException Unable to read String
	 * */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		name = new SimpleStringProperty((String) in.readObject());
		strength = new SimpleDoubleProperty(in.readDouble());
		health = new SimpleDoubleProperty(in.readDouble());
		speed = new SimpleDoubleProperty(in.readDouble());
		createAvatar();
		getAvatar().setTranslateX(in.readDouble());
		getAvatar().setTranslateY(in.readDouble());
		tooltip = new Tooltip(toString());
		Tooltip.install(getAvatar(), tooltip);
		resetAvatarAttributes();
		tt = new TranslateTransition(); tt.setNode(getAvatar());
	} // end readObject() to support serialization

	//	private readObjectNoData() throws ObjectStreamException {}

	/**Calculates the distance between to actors
	 * @return Returns a double representing the distance between actors
	 * @param current The current actor you are measuring distance with. When used will
	 * cycle through all actors of an army*/
	public double distanceTo(Actor current) {
		double xToMove = getAvatar().getTranslateX();
		double yToMove = getAvatar().getTranslateY();
		double xCurrent = current.getAvatar().getTranslateX();
		double yCurrent = current.getAvatar().getTranslateY();
		double deltaX = xToMove - xCurrent;
		double deltaY = yToMove - yCurrent;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}


}	//end of class Actor
