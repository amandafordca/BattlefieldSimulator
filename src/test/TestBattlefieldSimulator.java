package test;

import static org.junit.Assert.*;
import javafx.scene.paint.Color;

import org.junit.Test;




import util.Input;
import actor.*;
import army.Army;


/** A public class named <i>TestAssignment1</i> used to test independant actors, arrays and skirmishes*/

public class TestBattlefieldSimulator {

	Actor w1 = new Wizard();
	Actor w2 = new Wizard();
	Actor h1 = new Hobbit();
	Actor o1 = new Orc();
	Actor e1 = new Elf();

	/**Test setHealth method for hobbit actors*/
	@Test
	public void testSetHealth(){

		final int NUM_ACTORS = 10;
		Actor[ ] collection = new Actor[NUM_ACTORS];
		for (int i=0; i<NUM_ACTORS; ++i) {
			if (Math.random() < 0.2) // Wizard 20% probability4
				collection[i] = new Wizard();
			else
				collection[i] = new Hobbit();
		}

		collection[3].setHealth(-1);		//tests below boundary of setHealth for actor array object
		assertTrue(collection[3].getHealth()==Actor.MIN_HEALTH);

		h1.setHealth(123456);       //test Above boundary of setHealth                            
		assertTrue(h1.getHealth()==Actor.MAX_HEALTH);	//Evaluates true if it is equal to MAX_HEALTH value from Actor class

		h1.setHealth(-5);		//test Below boundary of setHealth
		assertTrue(h1.getHealth()==Actor.MIN_HEALTH); //Evaluates true if it is equal to MIN_HEALTH value from Actor class

		h1.setHealth(7);		//test within boundary of setHealth
		assertTrue(h1.getHealth()==7);
	}


	/**Test setName method for independent wizard actors*/
	@Test
	public void testSetName(){

		w1.setName("Gandalf");
		assertTrue(w1.getName().equals("Gandalf"));
		assertFalse(w1.getName().equals("Not Gandalf"));
	}


	/**Test setSpeed method for independent hobbit actors*/
	@Test
	public void testSetSpeed(){
		h1.setSpeed(3);		//test below boundary of setSpeed
		assertTrue(h1.getSpeed()==Actor.MIN_SPEED); //Evaluates true if it is equal to MIN_SPEED value from Actor class.

		h1.setSpeed(7);		//test within boundary of setSpeed
		assertTrue(h1.getSpeed()==7);

		h1.setSpeed(100);
		assertTrue(h1.getSpeed()==Actor.MAX_SPEED);	//Evaluates true if it is equal to MAX_SPEED value from Actor class.
	}


	/**Test setStrength method for independent wizard actors*/
	@Test
	public void testSetStrength(){
		w1.setStrength(40);		//tests below boundary of setStrength
		assertTrue(w1.getStrength()==Actor.MIN_STRENGTH); //Evaluates true if it is equal to MIN_STRENGTH value from Actor class

		w1.setStrength(60);		//tests within boundary of setStrength
		assertTrue(w1.getStrength()==60);

		w1.setStrength(222);	//tests above boundary of setStrength
		assertTrue(w1.getStrength()==Actor.MAX_STRENGTH);	//Evaluates true if it is equal to MAX_STRENGTH value from Actor class.
	}

	/**Tests methods specific to Hobbit*/
	@Test
	public void testSetStealth(){
		((Hobbit)h1).setStealth(-5);		//tests below boundary of setStealth
	//	System.out.println(((Hobbit)h1).getStealth());
		assertTrue(((Hobbit)h1).getStealth()==Hobbit.MIN_STEALTH);	//Evaluates true if equal to MIN_STEALTH in Hobbit class.

		((Hobbit)h1).setStealth(25);
		assertTrue(((Hobbit)h1).getStealth()==25);

		((Hobbit)h1).setStealth(200);
		assertTrue(((Hobbit)h1).getStealth()==Hobbit.MAX_STEALTH);
	}
	
	
	/**Tests methods specific to Wizard and Hobbit*/
	@Test
	public void testHasHorseHasStaff(){		
		((Wizard)w1).setHasHorse(true);
		assertEquals(((Wizard)w1).getHasHorse(), true);
	}

	public void testSetHasStaff(){		
		((Wizard)w1).setHasStaff(false);
		assertEquals(((Wizard)w1).getHasStaff(),false);

	}
//	/**Tests to confirm that health is lost after an skirmish between two actors*/
//	@Test
//	public void testActorSkirmishes(){
//		double healthBeforeCombat = w1.getHealth();
//		w1.combatRound(w2);
//		assertTrue(w1.getHealth()<healthBeforeCombat);
//
//	}
//	
	/**Tests <i>inputAllFields</i> by validating <i>toString</i>*/
	@Test
		public void testInputAllFields(){
		
		System.out.println(o1.toString());
		o1.inputAllFields();
		System.out.println(o1.toString());
		
		System.out.println(e1.toString());
		e1.inputAllFields();
		System.out.println(e1.toString());
		
		System.out.println(w2.toString());
		w2.inputAllFields();
		System.out.println(w2.toString());
		
		
		System.out.println(h1.toString());	
		h1.inputAllFields();
		System.out.println(h1.toString());
		
		
		System.out.println(w2.toString());
		w2.inputAllFields();
		System.out.println(w2.toString());
		
	}
	
	/**Tests creating two armies, <i>forcesOfLight</i> and <i>forcesOfDarkness</i>.
	 * Populates them, edits a chosen actor in one and then displays the changes.
	 */
	@Test
	public void testArmy(){
		
//		Army forcesOfLight = new Army("Forces of Light");		//creates an army of Forces of Light allegiance
//		//forcesOfLight.populate(ActorFactory.Type.RANDOM, 10);
//		
//		forcesOfLight.populate(ActorFactory.Type.HOBBIT, 4);	//populates army with 4 hobbits
//		forcesOfLight.populate(ActorFactory.Type.ELF, 3);		//populates army with 3 elves
//		forcesOfLight.populate(ActorFactory.Type.WIZARD, 2);	//populates army with 2 wizards
//		forcesOfLight.display();								//displays army of 4 hobbits, 3 elves, 2 wizards
		
		Army forcesOfDarkness = new Army("Forces of Darkness", null, Color.RED);	//creates an army of Forces of Darkness allegiance
		forcesOfDarkness.populate(ActorFactory.Type.ORC, 10);	//populates army with 10 orcs
		forcesOfDarkness.display();								//displays all 10 orcs
		
		//Prints out Forces of Darkness and valid indexes for that army. Calls for user input or valid int
		int indexToEdit = Input.instance.getInt("Forces of Darkness", 0,forcesOfDarkness.size()-1);
		System.out.println("Enter 9 for health and speed");
		forcesOfDarkness.edit(indexToEdit);	//allows user to edit the values for that specific actor
		forcesOfDarkness.display();			//displays all 10 orcs and chosen orc's values should be changed.
		assertTrue((forcesOfDarkness.getActor(indexToEdit)).getHealth() == 9);
		
		
		
	}
	/**Tests to make sure that auto populated actors have valid values for their variables*/
	@Test
	public void testDefaultConstructorValues(){
		Actor h1 = new Hobbit();
		assertTrue(((Hobbit)h1).getStealth()>=Hobbit.MIN_STEALTH && ((Hobbit)h1).getStealth()<= Hobbit.MAX_STEALTH);
		
		Actor e1 = new Elf();
		assertTrue(((Elf)e1).getArrows()>=Elf.MIN_ARROWS && ((Elf)e1).getArrows()<=Elf.MAX_ARROWS);
		
		Actor w1 = new Wizard();
		assertTrue(((Wizard)w1).getStrength()>=Actor.MIN_STRENGTH && ((Wizard)w1).getStrength()<=Actor.MAX_STRENGTH);
		
		Actor o1 = new Orc();
		assertTrue(((Orc)o1).getRage()>=Orc.MIN_RAGE && ((Orc)o1).getRage()<=Orc.MAX_RAGE);
	}
	
	/**Tests creating an army, populating it with a Hobbit actor and verifying that
	 * actor's army allegiance is equal to the army created.
	 */
	@Test
	public void testArmyAllegiance(){
	Army army1 = new Army("Forces of Darkness", null, Color.RED);
	Actor a1 = new Hobbit(army1);
	
	assertTrue(a1.getArmyAllegiance() == army1);
	
	}
}
