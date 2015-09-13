package test;

import javafx.scene.paint.Color;
import army.Army;
import actor.*;
import util.Input;


public class TestArmy {
	public static void main (String[]args){
				
		Army forcesOfLight = new Army("Forces of Light", null, Color.RED);
		//forcesOfLight.populate(ActorFactory.Type.RANDOM, 10);
		
		forcesOfLight.populate(ActorFactory.Type.HOBBIT, 4);
		forcesOfLight.populate(ActorFactory.Type.ELF, 3);
		forcesOfLight.populate(ActorFactory.Type.WIZARD, 2);
		forcesOfLight.display();
		
		Army forcesOfDarkness = new Army("Forces of Darkness", null, Color.RED);
		forcesOfDarkness.populate(ActorFactory.Type.ORC, 10);
		forcesOfDarkness.display();
		
		int indexToEdit = Input.instance.getInt("Forces of Light", 0,forcesOfLight.size()-1);
//		forcesOfLight.edit(indexToEdit);
//		forcesOfLight.display();
	}
}
