package fr.ebiz.computerDatabase.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.utils.Utils;

public class Cli {
	
	public Cli() {}
	
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
					this.print("Please, choose between 1 to 4.\n");
			}
		}
	}
	
	public void printSubMenuCompanies(ResultSet companies) throws SQLException {
		boolean stop = false;
		while(!stop){
			this.print("\n---- SubMenu Companies Listing ----");
			this.print("1) List\n" + "2) Quit");
			Scanner sc = new Scanner(System.in);
			switch(sc.next()){
				case "1":
					while(companies.next()) {
						int id = companies.getInt(Utils.COLUMN_ID);
						String name = companies.getString(Utils.COLUMN_NAME);
						
						this.print("id: " + id + ", name: " + name);
					}
					break;
				case "2":
					this.print("Returning to top menu");;
					stop = true;
					break;
			default:
					this.print("Please, choose a correct answer.");
			}
		}
	}
	
	public int printSubMenuComputers(ResultSet computers) throws SQLException {
		while(true){
			this.print("\n---- SubMenu Computer Listing ----");
			this.print("1) List\n" +"2) Show computer details\n"+ 
					"3) Update computer\n" + "4) Delete computer\n" + "5) Quit");
			Scanner sc = new Scanner(System.in);
			switch(sc.next()){
				case "1":
					while(computers.next()) {
						int id = computers.getInt(Utils.COLUMN_ID);
						String name = computers.getString(Utils.COLUMN_NAME);
						String intro = computers.getString(Utils.COLUMN_INTRODUCED);
						String discon = computers.getString(Utils.COLUMN_DISCONTINUED);
						int company_id = computers.getInt(Utils.COLUMN_COMPANYID);
						
						this.print("id: " + id + ", name: " + name + 
								", introduced: " + intro + ", discontinued: " +
								discon + ", company_id: " + company_id);
					}
					break;
				case "2":
					return 1;
				case "3":
					return 2;
				case "4":
					return 3;
				case "5":
					this.print("Returning to top menu");;
					return 4;
				default:
					this.print("Please, choose a correct answer.");
			}
		}
	}
	
	public Computer printInsertComputerAction() {
		Computer computer = null;;
		
		this.print("\n---- Create a Computer ----");
		
		String name = this.getStringChoice("\nEnter a name:");
		LocalDateTime intro = stringToDate("\nIntroduced date:");
		LocalDateTime discon = stringToDate("\nDiscontinued date:");
		while(discon.isBefore(intro)) {
			this.print("\nDiscontinued date can not be before introduce one.");
			discon = stringToDate("\nEnter the discontinued date again:");
		}
		int compIdRed = this.getIntChoice("\nEnter a company id reference: ");
		
		computer = new Computer(name, intro, discon, compIdRed);
		
		return computer;
	}
	
	public int printShowDetailsAction() {
		return this.getIntChoice("\nChoose a computer id to show details: ");
	}
	
	public int printUpdateComputerAction() {
		return 0;
	}
	
	public int printDeleteComputerAction() {
		return this.getIntChoice("\nChoose a computer id to delete: ");
	}
	
	public void print(String msg){
		System.out.println(msg);
	}
	
	
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
	
	public String getStringChoice(String msg) {
		this.print(msg);
		Scanner sc = new Scanner(System.in);
		return sc.next();
	}
	
	public LocalDateTime stringToDate(String msg) {
		this.print(msg);
		LocalDateTime time = null;
		while(time == null) {
			int year = getIntChoice("Enter the year:");
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
