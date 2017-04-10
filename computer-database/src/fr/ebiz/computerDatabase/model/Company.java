package fr.ebiz.computerDatabase.model;

public class Company {
	
	private int id;
	
	private String name;
	
	public Company() {}
	
	public Company(int id, String name) {
		this.name = name;
		this.id = id;
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
	
	public String toString() {
		return "id: " + this.id + ", name: "
				+ this.name;
	}
}
