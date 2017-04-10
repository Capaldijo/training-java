package fr.ebiz.computerDatabase.model;

import java.time.LocalDateTime;

public class Computer {

	private int id;
	
	private String name;
	
	private LocalDateTime introduced;
	
	private LocalDateTime discontinued;
	
	private int company_id;
	
	public Computer() {}
	
	public Computer(int id, String name, LocalDateTime intro,
			LocalDateTime discon, int company_id) {
		this.id = id;
		this.name = name;
		this.introduced = intro;
		this.discontinued = discon;
		this.company_id = company_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}

	public LocalDateTime getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	
	public String toString() {
		return "id: " + this.id + ",name: " + this.name + ", introduced: "
				+ this.introduced + ", discontinued: " + this.discontinued +
				", company_id: " + this.company_id;
	}
}
