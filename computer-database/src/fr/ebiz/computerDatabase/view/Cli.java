package fr.ebiz.computerDatabase.view;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import fr.ebiz.computerDatabase.model.Computer;

public class Cli {
	
	private Scanner sc;
	
	public Cli() {
		sc = new Scanner(System.in);
	}
	
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
			switch(sc.next()){
				case "1": // List Companies
					return 1;
				case "2": // List computers
					return 2;
				case "3": // Create computer
					return 3;
				case "4": // Quit
					sc.close();
					return 4;
			default:
					this.print("Please, choose between 1 to 4.");
			}
		}
	}
	
	/*
	 * SubMenu for Computers. User can choose between list them
	 * show details of one computer chosen 
	 * update a computer chosen, delete a computer  
	 * and return a int depending the user's choice
	 */
	public int printSubMenuComputers() throws SQLException {
		while(true){
			this.print("\n---- SubMenu Computer Listing ----");
			this.print("1) List\n" +"2) Show computer details\n"+ 
					"3) Update computer\n" + "4) Delete computer\n" + "5) Quit");
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
		while((intro != null && discon != null) && discon.isBefore(intro)) {
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
	 * Ask the user for each fields except the ID,
	 * if he wants to change them. After that, return
	 * the computer
	 * 
	 * return the Computer
	 */
	public Computer printUpdateComputerAction(Computer computer) {
		
		this.print("Here is the computer's info you want to update:");
		this.print(computer.toString());
		String choice = null;
		
		String name = null;
		LocalDateTime intro = null, discon = null;
		int compIdRef = 0;
		do{
			choice = getStringChoice("\nDo you want to change the name ?");
			if(choice.toLowerCase().equals("yes")){
				name = getStringChoice("\nEnter a new name:");
				computer.setName(name);
			}
		}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
		
		do{ // check in the db if introduced date is before discontinued one			
			choice = null;
			do{
				choice = getStringChoice("\nDo you want to change the introduced date ?");
				if(choice.toLowerCase().equals("yes")){
					intro = stringToDate("\nEnter a new introduced date:");
					computer.setIntroduced(intro);
				}
			}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
			
			choice = null;
			do{
				choice = getStringChoice("\nDo you want to change the discontinued date ?");
				if(choice.toLowerCase().equals("yes")){
					discon = stringToDate("\nEnter a new discontinued date:");
					if(computer.getIntroduced() != null && discon.isBefore(intro)){
						this.print("Discontinued can not be before introduced one");
						choice=null;
					}
					computer.setDiscontinued(discon);
				}
			}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
		}while((computer.getIntroduced() != null && computer.getDiscontinued() != null)
				&& computer.getIntroduced().isAfter(computer.getDiscontinued()));
		
		choice = null;
		do{
			choice = getStringChoice("\nDo you want to change the company ref id ?");
			if(choice.toLowerCase().equals("yes")){
				compIdRef = getIntChoice("\nEnter a new company ref id (for a null company: 0):");
				computer.setCompany_id(compIdRef);
			}
		}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
		
		return computer;
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
			try{
				if(sc.hasNextInt()){
					response = sc.nextInt();
					correct=true;
				}
				else{
					this.print("Please, choose a correct answer.");
					sc.nextLine();
				}
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
		return sc.nextLine();
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
