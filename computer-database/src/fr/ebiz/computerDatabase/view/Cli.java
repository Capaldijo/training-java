package fr.ebiz.computerDatabase.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.utils.Utils;

public class Cli {
	
	public Cli() {}
	
	/* 
	 * Top menu in which we choose between list companies
	 * list computers, create a computer or quit
	 * and return the user's choice
	 */
	public int printMenu() {
		while(true){
			this.print("\n---- Menu Principal ----");
			this.print("1) List companies\n"
					+ "2) List computers\n" + "3) Create Computer\n"
					+ "4) Quit");
			Scanner sc = new Scanner(System.in);
			switch(sc.next()){
				case "1":
					return 1;
				case "2":
					return 2;
				case "3":
					return 3;
				case "4":
					return 4;
			default:
					this.print("Please, choose between 1 to 4.");
			}
		}
	}
	
	/*
	 * SubMenu for Computers. User can choose between list them
	 * show details of one computer choosen 
	 * update a computer choosen, delete a computer  
	 * and return a int depending the user's choice
	 */
	public int printSubMenuComputers() throws SQLException {
		while(true){
			this.print("\n---- SubMenu Computer Listing ----");
			this.print("1) List\n" +"2) Show computer details\n"+ 
					"3) Update computer\n" + "4) Delete computer\n" + "5) Quit");
			Scanner sc = new Scanner(System.in);
			switch(sc.next()){
				case "1":
					return 1;
				case "2":
					return 2;
				case "3":
					return 3;
				case "4":
					return 4;
				case "5":
					this.print("Returning to top menu");;
					return 5;
				default:
					this.print("Please, choose a correct answer.");
			}
		}
	}
	
	/*
	 * Create a computer, enter its name, introduce and 
	 * discontinued date, and its referenced company id
	 * return the computer constructed by those fields
	 */
	public Computer printInsertComputerAction() {
		Computer computer = null;;
		
		this.print("\n---- Create a Computer ----");
		
		String name = this.getStringChoice("\nEnter a name:");
		LocalDateTime intro = stringToDate("\nIntroduced date:");
		LocalDateTime discon = stringToDate("\nDiscontinued date:");
		
		//discontinued date can not be before introduced one
		while(intro != null && discon.isBefore(intro)) {
			this.print("\nDiscontinued date can not be before introduce one.");
			discon = stringToDate("\nEnter the discontinued date again:");
		}
		
		int compIdRed = this.getIntChoice("\nEnter a company id reference (for a null company: 0): ");
		
		computer = new Computer(name, intro, discon, compIdRed);
		
		return computer;
	}
	
	/*
	 * Print all the List's element
	 * And ask the user what he wants to do (Previous, Next, Quit)
	 * 
	 * return the user choice
	 */
	public int printPageableList(List<?> list){
		for(Object elmt : list){
			System.out.println(elmt.toString()+"\n\n--------------------------\n");
		}
		return getIntChoice("1) Previous   | 2) Next    | 3) Quit");
	}
	
	/*
	 * Ask the user to choose the ID of the computer
	 * he wants to show details 
	 * 
	 * return the user's choice
	 */
	public int printShowDetailsAction() {
		return this.getIntChoice("\nChoose a computer id to show details: ");
	}
	
	/*
	 * 
	 */
	public Computer printUpdateComputerAction(Computer computer) {
		
		this.print("Here is the computer's info you want to update:");
		this.print(computer.toString());
		String changeName = null, changeDateIntro = null,
				changeDateDsicon = null, changeCompIdRef = null;
		
		String newName = null;
		LocalDateTime intro = null, discon = null;
		int compIdRef = 0;
		do{
			changeName = getStringChoice("\nDo you want to change the name ?");
			
		}while(changeName == null || (!changeName.toLowerCase().equals("yes") && !changeName.toLowerCase().equals("no")));
		
		do{
			changeDateIntro = getStringChoice("\nDo you want to change the introduced date ?");
		}while(changeDateIntro == null || (!changeDateIntro.toLowerCase().equals("yes") && !changeDateIntro.toLowerCase().equals("no")));
		
		return null;
	}
	
	/*
	 * Ask the user to choose the ID of the computer
	 * he wants to delete
	 * 
	 * return the user's choice
	 */
	public int printDeleteComputerAction() {
		return this.getIntChoice("\nChoose a computer id to delete: ");
	}
	
	/*
	 * only print message when called by other classes
	 * because only View is supposed to print messages
	 */
	public void print(String msg){
		System.out.println(msg);
	}
	
	/*
	 * Ask the user to enter a answer of type int
	 * It keeps asking him if not type int
	 * 
	 * The string parameter is here only here to be print
	 * nothing special
	 * 
	 * return the user's answer
	 */
	public int getIntChoice(String msg) {
		this.print(msg);
		boolean correct = false;
		int response = 0;
		while(!correct){
			Scanner sc = new Scanner(System.in);
			try{
				response = sc.nextInt();
				correct=true;
			}catch (InputMismatchException ime){
				this.print("Please, choose a correct answer.");
			}
		}
		
		return response;
	}
	
	/*
	 * Ask the user to enter a answer of type String
	 * 
	 * The string parameter is here only here to be print
	 * nothing special
	 * 
	 * return the user's answer
	 */
	public String getStringChoice(String msg) {
		this.print(msg);
		Scanner sc = new Scanner(System.in);
		return sc.next();
	}
	
	/*
	 * Method that ask the user the year, month, day,
	 * hour and minute in order to build a LocalDateTime
	 * object for the computer.
	 * 
	 * The string parameter is here only here to be print
	 * nothing special
	 * 
	 * Return the LocalDateTime object built by
	 * the user's given answers
	 */
	public LocalDateTime stringToDate(String msg) {
		this.print(msg);
		LocalDateTime time = null;
		while(time == null) {
			int year = getIntChoice("Enter the year (for a null date: 0):");
			if(year == 0)
				return null;
				
			while( year<1970 || year>2020) {
				this.print("\nYear is too old or too far in the future.");
				year = getIntChoice("Enter the year again:");
			}
			
			int month = getIntChoice("\nEnter the month (between 1 to 12):");
			while( month>12 || month<1) {
				this.print("\nIncorrect answer. Please choose between 1 to 12.");
				month = getIntChoice("Enter the month again:");
			}
			
			int day = getIntChoice("\nEnter the day (between 1 to 31):");
			while( day>31 || day<1) {
				this.print("\nIncorrect answer. Please choose between 1 to 31.");
				day = getIntChoice("Enter the day again:");
			}
			
			int hour = getIntChoice("\nEnter the hour (between 0 to 23):");
			while( hour>23 || hour<0) {
				this.print("\nIncorrect answer. Please choose between 0 to 23.");
				hour = getIntChoice("Enter the hour again:");
			}
			
			int minute = getIntChoice("\nEnter the hour (between 0 to 59):");
			while( minute>59 || minute<0) {
				this.print("\nIncorrect answer. Please choose between 0 to 59.");
				minute = getIntChoice("Enter the minute again:");
			}
			
			try{
				time = LocalDateTime.of(year, month, day, hour, minute);
			} catch(DateTimeException dte) {
				this.print("\nInvalid Date given. Please try again.");
			}
		}		
		return time;
	}
}
