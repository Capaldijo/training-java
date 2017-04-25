package fr.ebiz.computerdatabase.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Company is the class representing a company in the database. A company got
 * the following information: A unique identifier that stay still. A name, that
 * can be changed.
 * @author capaldijo
 */
public class Company {

    final Logger logger = LoggerFactory.getLogger(Company.class);

    /**
     * The Company's ID, that can't be changed.
     * @see Company#Company(int, String)
     * @see Company#getId()
     */
    private Long id;

    /**
     * The Company's name, that can be change.
     * @see Company#getName()
     * @see Company#setName(String)
     */
    private String name;

    /**
     * Company Constructor. Each field is instantiated through the parameters.
     * No default values.
     * @param id Unique Company's identifier.
     * @param name The Company's name.
     * @see Company#id
     * @see Company#name
     */
    public Company(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    /**
     * Return the Company's ID.
     * @return Company's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Return the Company's name.
     * @return Company's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the Company's name.
     * @param name The new Company's name.
     */
    public void setName(String name) {
        logger.debug("Name set to {}. Old name was {}.", name, this.name);
        this.name = name;
    }

    /**
     * Return the object Company through String.
     * @return Company to String.
     */
    @Override
    public String toString() {
        return "id: " + this.id + ", name: " + this.name;
    }
}
