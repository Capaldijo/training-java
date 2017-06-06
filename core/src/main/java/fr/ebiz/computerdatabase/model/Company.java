package fr.ebiz.computerdatabase.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Set;


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
    private Long id;

    /**
     * The Company's name, that can be change.
     * @see Company#getName()
     * @see Company#setName(String)
     */
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "company")
    private Set<Computer> computers;

    /**
     * Default no parameters Company Constructor.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Company company = (Company) o;

        if (id != null ? !id.equals(company.id) : company.id != null) {
            return false;
        }
        return name != null ? name.equals(company.name) : company.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
