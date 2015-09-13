package army;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulator.Simulator;
import actor.*;
/**<i>Army</i> class is a public class used to store an arraylist of Actors with either 
 * <i>force of light</i> or <i>force of darkness</i>
 * @author Amanda
 * @author Rex Woollard
 *
 */
public class Army {

	private String force;	//String used to hold name of force
	private Simulator simulator;
	private Color color;
	private DropShadow dropShadow;
	private Army opposingArmy;
	public static final String FONT_NAME = "Copperplate Gothic Bold";
	private static final Font NOTIFICATION_FONT_SMALL = new Font(FONT_NAME, 14.0);
	private static final Font NOTIFICATION_FONT_LARGE = new Font(FONT_NAME, 36.0);

	/**<i>collectionActors</i> holds an arrayList of actors set to a particular army allegiance*/
	private ObservableList<Actor> collectionActors = FXCollections.observableList(new ArrayList < >());

	/**Method that allows us access to the collection of actors
	 * @return Returns an observable list of actors */
	public ObservableList<Actor> getCollectionActors() { //allows us to access the private ObservableList elsewhere
		return FXCollections.unmodifiableObservableList(collectionActors); //returns a version of the Observable list that can't be modified.
	}

	/**<i>Army</i> class constructor which takes a String to set the army allegiance to the force variable
	 * @param simulator Takes a simulator from the Simulator class
	 * @param color Takes a color to represent which force
	 * @param Force Takes a force to choose army allegiance*/
	public Army(String Force, Simulator simulator, Color color){
		this.force = Force;
		this.simulator = simulator;
		this.color = color;
		dropShadow = new DropShadow(20.0, color);
	}//end Army constructor

	/**Method <i>setForce</i> which takes a String to set the value for force (army allegiance)
	 * @param force Sets the force for the army*/
	public void setForce(String force){
		this.force = force;
	}

	/**Method <i>getForce</i> which returns the value of force.
	 * @return Returns the force of the army*/
	public String getForce(){
		return force;
	}




	/**Method <i>populate</i> to potentially generate thousands of actor objects automatically
	 * @param type Takes which type of actor enum
	 * @param numToAdd The number of actors of specified type to add.*/
	public void populate(ActorFactory.Type type, int numToAdd){
		for (int i=0;i<numToAdd; ++i){
			//collectionActors.add(type.create(this));

			Actor actor = type.create(this);
			actor.createAvatar();
			Node avatar = actor.getAvatar();
			double newX = (simulator.getScene().getWidth()-80)*Math.random();
			double newY = (simulator.getScene().getHeight()-100)*Math.random();
			avatar.setTranslateX(newX);
			avatar.setTranslateY(newY);
			avatar.setEffect(dropShadow);
			Tooltip.install(avatar, new Tooltip(actor.toString())); 
			simulator.getChildren().add(actor.getAvatar());
			collectionActors.add(actor);
			//send "this" so that Actor object can capture its allegiance
		} //ends for
	} //ends populate

	/**Method <i>edit</i> that allows user to adjust the values in a selected actor object.
	 * @param indexOfActorToEdit Specified which actor to edit in the collection*/
	public void edit(int indexOfActorToEdit){
		collectionActors.get(indexOfActorToEdit).inputAllFields();

	}
	/**Method <i>display</i> used to display all Actor objects used in the Army object.*/
	public void display() {
		for (Actor current : collectionActors)
			System.out.println(current);
	}

	/**Method <i>size</i> returns a count of the number of Actor objects currently
	 * stored in the ArrayList object.
	 * @return Returns the size of the collection (How many actors in army)
	 */
	public int size(){
		int numOfActors = collectionActors.size();
		return numOfActors;
	}

	/**Method <i>getActor</i> returns an Actor from the array at a specified index 
	 * @return Returns actor at a specific index in an army collection
	 * @param index The index of the specified actor to access*/
	public Actor getActor(int index){
		if(index<collectionActors.size()){
			return collectionActors.get(index);
		}
		return null;
	}
	/**Method that starts all actor nodes movement
	 * @param engageInCombat Boolean value to state if actors should combat */
	public void startMoving(boolean engageInCombat) {
		for (Actor actor : collectionActors){
			actor.startMoving(true);
		}

	}
	/**Method that stops all actor nodes movements */
	public void suspendMoving() {
		for (Actor actor : collectionActors){
			actor.suspendMoving();
		}

	}
	/**Returns the name of the force that the army is aligned with (Light or Darkness)
	 * @return Returns the army's name */
	public String getName() {
		// TODO Auto-generated method stub
		return force;
	}
	/**Creates a table containing a list of actors in army 
	 * @return Returns a Table View of actors*/
	public TableView<Actor> getTableViewOfActors() {
		TableView<Actor> tableView = Actor.createTable();
		tableView.setItems(collectionActors);
		return tableView;
	}

	/** Finds the nearest actor to current actor
	 * @return Returns nearest actor
	 * @param actorToMove Selects the actor who you want to find who is closest to*/
	public Actor findNearestActor(Actor actorToMove){
		Actor nearest = null;
		double distanceToClosest = Double.MAX_VALUE;

		for(Actor current : collectionActors){
			if (current.isVisible()){
				double calculatedDistance = actorToMove.distanceTo(current);
				if (calculatedDistance < distanceToClosest) {
					distanceToClosest = calculatedDistance;
					nearest = current;
				} //end if closer
			} // end if (isVisible)
		} // end for loop
		return nearest;
	}

	/**Returns the opposing army
	 * @return Returns the opposing army */
	public Army getOpposingArmy(){
		return opposingArmy;
	}

	/**Sets the opposing army
	 * @param opposingArmy The opposing army you want to set */
	public void setOpposingArmy(Army opposingArmy) {
		this.opposingArmy = opposingArmy;
	}
	/**Creates a record in the harddrive of the current actors and their locations
	 * @param out The file you want to write to
	 * @throws IOException If file cannot be written to*/
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeObject(force);
		out.writeDouble(color.getRed());	//returns a primitive value to the hard drive
		out.writeDouble(color.getGreen());
		out.writeDouble(color.getBlue());
		out.writeDouble(color.getOpacity());
		out.writeInt(collectionActors.size());	//find out how many actors you are saving
		for (Actor a : collectionActors)
			out.writeObject(a);
	} // end serialize() to support serialization

	/**Restores actors to the scene from a previously saved state
	 * @param in The file you want to restore from
	 * @throws IOException if file cannot be read
	 * @throws ClassNotFoundException if objects cannot be read */
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		collectionActors.clear();
		force = (String) in.readObject();
		color = new Color(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
		dropShadow = new DropShadow(10.0, color);
		int size = in.readInt();
		for (int i = 0; i < size; ++i) {
			Actor actor = (Actor) in.readObject();
			actor.setArmyAllegiance(this);
			actor.getAvatar().setEffect(dropShadow);
			simulator.getChildren().add(actor.getAvatar());
			collectionActors.add(actor);
		}
	} // end deserialize() to support serialization

	/**Returns the speeedControllerValue from simulator
	 * @return Returns the speed controller value*/
	public double getSpeedControllerValue() {
		return simulator.getSpeedControllerValue();
	}

	/**Method that removes dead actor from table and removes avatar from scene
	 * @param nowDeadActor The actor you would like to remove from scene and table*/
	public void removeNowDeadActor(Actor nowDeadActor) {
		final ObservableList<Node> listJavaFXNodesOnBattlefield = simulator.getChildren(); // creating as a convenience variable, since the removeNowDeadActor() method needs to manage many Node objects in the simulator collection of Node objects
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// START: Create Notification message about the nowDeadActor: Create, then add two Transition Animations, packing in a ParallelTransition
		{ // setup Stack Frame to allow re-use of variable identifiers "tt", "ft" and "pt"
			Text message = new Text(240.0, 100.0, "Dead: " + nowDeadActor.getName()); message.setFont(NOTIFICATION_FONT_SMALL); message.setStroke(color);
			final Duration duration = Duration.seconds(3.0);
			FadeTransition ft = new FadeTransition(duration); ft.setToValue(0.0); // no need to associate with the Text (message) here, that will be done in the ParallelTransition
			TranslateTransition tt = new TranslateTransition(duration); tt.setByY(200.0);  // no need to associate with the Text (message) here, that will be done in the ParallelTransition
			ParallelTransition pt = new ParallelTransition(message, ft, tt); pt.setOnFinished(event->listJavaFXNodesOnBattlefield.remove(message)); pt.play(); // couple both Transitions in the ParallelTransition and associate with Text
			listJavaFXNodesOnBattlefield.add(message); // it will play() and after playing the code in the setOnFinished() method will called to remove the temporay message from the scenegraph.
		}
		// END: Create Notification message about the nowDeadActor: Create, then add two Transition Animations, packing in a ParallelTransition
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		collectionActors.remove(nowDeadActor); // removes nowDeadActor from the collection of active Actor objects that are part of this army.
		listJavaFXNodesOnBattlefield.remove(nowDeadActor.getAvatar()); // removes the avatar from the scenegraph (the Node object). The actor will disappear from the screen.
		System.out.println(nowDeadActor.getName() + " from " + force + " has been killed!");
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// START: Create Final Announcement of Winning Army
		if (collectionActors.size() == 0) { // Army has been wiped out, since no Actor objects remain in the collection. Therefore . . . the opposing Army wins.
			Text winner = new Text(260.0, 300.0, "Winner: " + opposingArmy.getName()); winner.setFont(NOTIFICATION_FONT_LARGE); winner.setStroke(opposingArmy.color); winner.setEffect(opposingArmy.dropShadow);
			final Duration duration = Duration.seconds(1.0);
			FadeTransition ft = new FadeTransition(duration, winner); ft.setToValue(0.2); ft.setCycleCount(10); ft.setAutoReverse(true); ft.setOnFinished(event->listJavaFXNodesOnBattlefield.remove(winner)); ft.play();
			listJavaFXNodesOnBattlefield.add(winner); // it will play() and after playing the code in the setOnFinished() method will called to remove the temporary winner from the scenegraph.
		
		}
		// END: Create Final Announcement of Winning Army
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	} // end removeNowDeadActor()


} //ends Army class
