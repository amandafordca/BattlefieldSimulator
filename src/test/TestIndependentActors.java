/*package test;
import actor.Actor;
import java.util.Scanner;

/**
 * 
 * @author Amanda
 * Has at least two reference-to-Actor variables.
 * Creates two actual Actor objects and tests the random initialization and subsequent editing.
 *
 */

/*
public class TestIndependentActors {

	public static void main (String[]args){
		Actor a1 = new Actor();		//Creates two new actor objects
		Actor a2 = new Actor();

		System.out.println("Player 1: " + a1.toString());		//prints out both of the newly created actor objects characteristics.
		System.out.println("Player 2: " + a2.toString());

		System.out.println("Enter the number of the player you would like to edit: ");	//Asks user which actor they would like to edit.
		Scanner input = new Scanner(System.in);

		int player = input.nextInt();

		while (player <1 || player >2){		//If the user input is incorrect it asks again.
			System.out.println("You must select either 1 or 2 for player. \nEnter player to edit: ");
			player = input.nextInt();
		}

		if (player == 1){
			a1.inputAllFields();
		}
		else {
			a2.inputAllFields();
		}

		System.out.println("\nHere are the players: ");			//Reprints both actor's characteristics to make sure the changes have taken place.
		System.out.println("\nPlayer 1: " + a1.toString());
		System.out.println("Player 2: " + a2.toString());

		input.close();
	}


}
*/