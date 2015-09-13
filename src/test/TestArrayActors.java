/*package test;

import actor.Actor;
import java.util.Scanner;

/**
 * 
 * @author Amanda
 * 
 * Creates an array of actor objects.
 * Prints out their characteristics and asks user to choose one to edit.
 * Edits the chosen actor and reprints out all actors to show changes have taken affect.
 *
 */
/*
public class TestArrayActors {	//creates an array of references to actor

	public static void main (String[]args){


		Actor[] actors = new Actor[10];		//Create new array of 10 actors

		for (int i=0; i<10; i++){

			actors[i]= new Actor(); 		//populate array with objects of actor

		}

		for (int i=0; i<10; i++){			//loop prints out all actors and their characteristics
			System.out.println((i+1)+": " +actors[i]);
		}

		System.out.print("\nEnter the number of the character you would like to edit: ");	//Asks user to choose one to edit.
		Scanner input = new Scanner(System.in);
		int character = input.nextInt();
			
		while (character<1||character>10){
			System.out.print("Number must be between 1 and 10. Enter a number: ");
			character=input.nextInt();
		}

		actors[character-1].inputAllFields();		//Allows for user to edit chosen actor.
		input.close();
		
		System.out.println("Here is the new array of actors: \n");	
		
		for (int i=0; i<10; i++){
			System.out.println((i+1)+": " +actors[i]);			//Reprints out all actors again to show changes have taken affect.
		}

	}
}
*/