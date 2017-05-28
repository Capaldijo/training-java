package fr.ebiz.computerdatabase.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;


/**
 * Company is the class representing a company in the database. A company got
 * the following information: A unique identifier that stay still. A name, that
 * can be changed.
 * @author capaldijo
 */
@Entity
@Table(name = "company")
public class Company {

    /**
     * The Company's ID, that can't be changed.
     * @see Company#Company(Long, String)
     * @see Company#getId()
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * The Company's name, that can be change.
     * @see Company#getName()
     * @see Company#setName(String)
     */
    @Column(name = "name")
    private String name;

    /**
     * .
     */
    public Company() {

    }

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
