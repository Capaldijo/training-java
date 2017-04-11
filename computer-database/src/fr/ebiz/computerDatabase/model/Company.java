package fr.ebiz.computerDatabase.model;

/**
  * <b>Company is the class representing a company in the database.</b>
  * <p>
  * A company got the following information:
  * <ul>
  * <li>A unique identifier that stay still.</li>
  * <li>A name, that can be changed.</li>
  * </ul>
  * </p>
  * 
  * @author jojo
  */
public class Company {
	
	/**
      * The Company's ID, that can't be changed.
      * 
      * @see Company#Company(int, String)
      * @see Company#getId()
      */
	private int id;
	
	/**
      * The Company's name, that can be change.
      * 
      * @see Company#getName()
      * @see Company#setName(String)
      */
	private String name;
	
	/**
     * Company Constructor.
     * <p>
     * Each field is instantiated through the parameters. No default values.
     * </p>
     * 
     * @param id
     *           Unique Company's identifier.
     * @param name
     *           The Company's name.
     * 
     * @see Company#id
     * @see Company#name
     */
	public Company(int id, String name) {
		this.name = name;
		this.id = id;
	}

	/**
     * Return the Company's ID.
     * 
     * @return Company's ID. 
     */
	public int getId() {
		return id;
	}

	/**
     * Return the Company's name.
     * 
     * @return Company's name. 
     */
	public String getName() {
		return name;
	}

    /**
     * Update the Company's name.
     * 
     * @param name
     *            The new Company's name.
     */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * Return the object Company through String.
     * 
     * @return Company to String. 
     */
	public String toString() {
		return "id: " + this.id + ", name: "
				+ this.name;
	}
}
