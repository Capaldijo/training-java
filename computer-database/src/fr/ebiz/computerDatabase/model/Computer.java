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
 * <li>And a company_id referencing the company's ID which built it.</li>
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
	private Long id;

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

	/**
	 * Computer Constructor.
	 * <p>
	 * Each field is instantiated through the parameters. No default values.
	 * </p>
	 * 
	 * @param builder
	 *            builder than contains or not all the fields
	 */
	private Computer(ComputerBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.introduced = builder.introduced;
		this.discontinued = builder.discontinued;
		this.company_id = builder.company_id;
	}

	/**
	 * Return the Computer's ID.
	 * 
	 * @return Computer's ID.
	 */
	public Long getId() {
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
	@Override
	public String toString() {
		return "ID: " + this.id + "\nName: " + this.name + "\nIntroduced: " + this.introduced + "\nDiscontinued: "
				+ this.discontinued + "\nCompany_id: " + this.company_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + company_id;
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company_id != other.company_id)
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public static class ComputerBuilder {
		
		private final String name;
		
		private Long id;
		
		private LocalDateTime introduced;
		
		private LocalDateTime discontinued;
		
		private int company_id;
		
		public ComputerBuilder(String name) {
			this.name = name;
		}
		
		public ComputerBuilder id(Long id) {
			this.id = id;
			return this;
		}
		
		public ComputerBuilder introduced(LocalDateTime introduced) {
			this.introduced = introduced;
			return this;
		}
		
		public ComputerBuilder discontinued(LocalDateTime discontinued) {
			this.discontinued = discontinued;
			return this;
		}
		
		public ComputerBuilder companyId(int companyId) {
			this.company_id = companyId;
			return this;
		}
		
		public Computer build() {
			return new Computer(this);
		}
	}

}
