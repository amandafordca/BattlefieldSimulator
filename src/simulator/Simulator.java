package simulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import army.Army;
import actor.Actor;
import actor.ActorFactory;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**Class <i>Simulator</i> is a class that creates the Stage for armies to be populated onto. It also manages the
 * ability to create Army Lists and Tables*/

public class Simulator extends Group {

	private Stage primaryStage;
	private Army forcesOfLight;
	private Army forcesOfDarkness;
	private Stage stageListControllerWindow;
	private Stage stageTableControllerWindow;
	private double speedControllerValue = 1.0;
	private static final double MAX_SPEEDUP = 50.0; //highest value that movement can be sped up to
	private static final double MIN_SPEEDUP = 1.0;	//lowest value that 

	/**Constructor that takes a Stage to build the stage and armies
	 * @param primaryStage Takes a Stage*/
	public Simulator(Stage primaryStage) {
		this.primaryStage = primaryStage;
		forcesOfLight = new Army("Forces of Light", this, Color.BLUE);
		forcesOfDarkness = new Army("Forces of Darkness", this, Color.RED);
		forcesOfDarkness.setOpposingArmy(forcesOfLight);
		forcesOfLight.setOpposingArmy(forcesOfDarkness);
		buildListViewWindow();
		buildTableViewWindow();
	}

	/**Method called when <i>Populate</i> menu item is selected, that populates armies
	 * with specific amounts of subclasses of Actor*/
	public void populate() {
		forcesOfLight.populate(ActorFactory.Type.HOBBIT,3);
		forcesOfLight.populate(ActorFactory.Type.ELF,3);
		forcesOfDarkness.populate(ActorFactory.Type.ORC,8);
		forcesOfLight.populate(ActorFactory.Type.WIZARD,2);
		forcesOfDarkness.populate(ActorFactory.Type.WIZARD, 1);
	}

	/**Method that is called when <i>Run</i> is selected from menu. Starts movement on actors of both armies*/
	public void run() {
		forcesOfLight.startMoving(true);
		forcesOfDarkness.startMoving(true);
	}

	/**Method that is called when <i>Suspend</i> is selected from menu. Suspends movement on actors of both
	 *  armies and stops combatRounds*/
	public void suspend() {
		forcesOfDarkness.suspendMoving();
		forcesOfLight.suspendMoving();
	}

	/**Method that builds the list view menu */
	public final void buildListViewWindow() { // final because of its use in the constructor
		//		  ListView<Actor> listView = new ListView<Actor>();
		VBox vBoxLightArmy = new VBox(5.0, new Text(forcesOfLight.getName()), new ListView<Actor>(forcesOfLight.getCollectionActors()));
		VBox vBoxDarkArmy = new VBox(5.0, new Text(forcesOfDarkness.getName()), new ListView<Actor>(forcesOfDarkness.getCollectionActors()));
		HBox hBoxSceneGraphRoot = new HBox(5.0, vBoxLightArmy, vBoxDarkArmy);

		if (stageListControllerWindow != null) {
			stageListControllerWindow.close();
			stageListControllerWindow.setScene(null);
		}
		stageListControllerWindow = new Stage(StageStyle.UTILITY);
		stageListControllerWindow.initOwner(primaryStage);
		stageListControllerWindow.setScene(new Scene(hBoxSceneGraphRoot));
	} // end buildListViewWindow()

	/**Method that builds the table view menu */
	public final void buildTableViewWindow() { // final because of its use in the constructor
		VBox vBoxLightArmy = new VBox(5.0, new Text(forcesOfLight.getName()), forcesOfLight.getTableViewOfActors());
		VBox vBoxDarkArmy = new VBox(5.0, new Text(forcesOfDarkness.getName()), forcesOfDarkness.getTableViewOfActors());
		HBox hBoxSceneGraphRoot = new HBox(5.0, vBoxLightArmy, vBoxDarkArmy);

		if (stageTableControllerWindow != null) {
			stageTableControllerWindow.close();
			stageTableControllerWindow.setScene(null);
		}
		stageTableControllerWindow = new Stage(StageStyle.UTILITY);
		stageTableControllerWindow.initOwner(primaryStage);
		stageTableControllerWindow.setScene(new Scene(hBoxSceneGraphRoot));
	} // end buildTableViewWindow()

	/**Method to open the list view window*/
	public void openListViewWindow() { stageListControllerWindow.show();}
	/**Method to close the list view window */
	public void closeListViewWindow() {	stageListControllerWindow.hide();}
	/**Method to open the table view window*/
	public void openTableViewWindow() { stageTableControllerWindow.show();}
	/**Method to close the table view window*/
	public void closeTableViewWindow() {  stageTableControllerWindow.hide();}

	/*Start Serialization*/

	/**Saves to disk state of Actors' locations and values of attributes*/
	public void save() {
		// Using a try block in case there is a file I/O error. Open a file that is configured for binary output.
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("battlefield.ser"))) {
			forcesOfLight.serialize(out);// "normal" method call that I created. Army class NOT serializable. Actor class and ALL its subclasses are serializable.
			forcesOfDarkness.serialize(out);// same
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end save()

	/**Restores the location and value of actors' attributes from a previously saved state*/
	public void restore() {
		// Using a try block in case there is a file I/O error. Open a file that is configured for binary input.
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("battlefield.ser"))) {
			forcesOfLight.deserialize(in);// "normal" method call that I created. Army class NOT serializable. Actor class and ALL its subclasses are serializable.
			forcesOfDarkness.deserialize(in); // same
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end restore()

	/**Method to speed up the movement of all actors*/
	public void speedUp() {
		++speedControllerValue;
		if(speedControllerValue > MAX_SPEEDUP){
			speedControllerValue = MAX_SPEEDUP;
		}
	} // end speedUp

	/**Method to slow down the movement of all actors*/
	public void slowDown() {
		--speedControllerValue;
		if(speedControllerValue < MIN_SPEEDUP){
			speedControllerValue = MIN_SPEEDUP;
		}
	} // end slowDown

	/**Method that returns the value of speedControllerValue
	 * @return Returns the speed controller value*/
	public double getSpeedControllerValue() {
		return speedControllerValue;
	}
} // end Simulator

