package fr.ebiz.computerDatabase.model;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <b>Computer is the class representing a computer in the database.</b>
 * <p>
 * A computer got the following information:
 * <ul>
 * <li>A unique identifier that stay still.</li>
 * <li>A name, that can be changed.</li>
 * <li>An introduced date, that can be changed.</li>
 * <li>A discontinued date, that can be changed.</li>
 * <li>And a company_id referencing the company's ID which built it. </li> 
 * </ul>
 * </p>
 * 
 * @see LocalDateTime
 * @see Company
 * @author jojo
 */
public class Computer {

	final Logger logger = LoggerFactory.getLogger(Computer.class);
	
	/**
     * The Computer's ID, that can't be changed.
     * 
     * @see Computer#Computer(int, String, LocalDateTime, LocalDateTime, int)
     * @see Computer#getId()
     */
	private int id;
	
	/**
     * The Computer's name, that can be change.
     * 
     * @see Computer#getName()
     * @see Computer#setName(String)
     */
	private String name;
	
	/**
     * The Computer's introduced date, that can be change.
     * 
     * @see Computer#getIntroduced()
     * @see Computer#setIntroduced(LocalDateTime)
     */
	private LocalDateTime introduced;
	
	/**
     * The Computer's discontinued date, that can be change.
     * 
     * @see Computer#getDiscontinued()
     * @see Computer#setDiscontinued(LocalDateTime)
     */
	private LocalDateTime discontinued;
	
	/**
     * The Computer's referenced company's id, that can be change.
     * 
     * @see Computer#getCompany_id()
     * @see Computer#setCompany_id(int)
     */
	private int company_id;
	
	public Computer(String name, LocalDateTime intro,
			LocalDateTime discon, int company_id) {
		this.name = name;
		this.introduced = intro;
		this.discontinued = discon;
		this.company_id = company_id;
	}
	
	/**
     * Computer Constructor.
     * <p>
     * Each field is instantiated through the parameters. No default values.
     * </p>
     * 
     * @param id
     *           Unique Computer's identifier.
     * @param name
     *           The Computer's name.
     * @param intro
     *           The date when the computer was introduced.
     * @param discon
     *           The date when the computer will be discontinued.
     * @param company_id
     * 			 The company referenced id.
     * 
     * @see Company#id
     * @see Company#name
     */
	public Computer(int id, String name, LocalDateTime intro,
			LocalDateTime discon, int company_id) {
		this.id = id;
		this.name = name;
		this.introduced = intro;
		this.discontinued = discon;
		this.company_id = company_id;
	}
	
	/**
     * Return the Computer's ID.
     * 
     * @return Computer's ID. 
     */
	public int getId() {
		return id;
	}

	/**
     * Return the Computer's name.
     * 
     * @return Computer's name. 
     */
	public String getName() {
		return name;
	}

	/**
     * Update the Computer's name.
     * 
     * @param name
     *            The new Computer's name.
     */
	public void setName(String name) {
		logger.debug("Name set to {}. Old name was {}.", name, this.name);
		this.name = name;
	}

	/**
     * Return the Computer's introduced date.
     * 
     * @return Computer's introduced date.
     */
	public LocalDateTime getIntroduced() {
		return introduced;
	}

	/**
     * Update the Computer's introduced date..
     * 
     * @param name
     *            The new Computer's introduced date.
     */
	public void setIntroduced(LocalDateTime introduced) {
		logger.debug("Introduced date set to {}. Old date was {}.", introduced, this.introduced);
		this.introduced = introduced;
	}

	/**
     * Return the Computer's discontinued date.
     * 
     * @return Computer's discontinued date. 
     */
	public LocalDateTime getDiscontinued() {
		return discontinued;
	}

	/**
     * Update the Computer's discontinued date..
     * 
     * @param name
     *            The new Computer's discontinued date.
     */
	public void setDiscontinued(LocalDateTime discontinued) {
		logger.debug("Discontinued date set to {}. Old date was {}.", discontinued, this.discontinued);
		this.discontinued = discontinued;
	}

	/**
     * Return the Company's referenced ID.
     * 
     * @return Company's referenced ID. 
     */
	public int getCompany_id() {
		return company_id;
	}

	/**
     * Update the Company referenced ID.
     * 
     * @param pseudo
     *            The new Company referenced ID.
     */
	public void setCompany_id(int company_id) {
		logger.debug("Company_id set to {}. Old company_id was {}.", company_id, this.company_id);
		this.company_id = company_id;
	}
	
	/**
     * Return the object Computer through String.
     * 
     * @return Computer to String. 
     */
	public String toString() {
		return "ID: " + this.id + "\nName: " + this.name + "\nIntroduced: "
				+ this.introduced + "\nDiscontinued: " + this.discontinued ;
	}
}
