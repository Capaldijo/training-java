package fr.ebiz.computerdatabase.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

/**
 * Computer is the class representing a computer in the database. A computer got
 * the following information: A unique identifier that stay still. A name, that
 * can be changed. An introduced date, that can be changed. A discontinued date,
 * that can be changed. And a company referencing the company's ID which
 * built it.
 * @see LocalDateTime
 * @see Company
 * @author capaldijo
 */
@Entity
@Table(name = "computer")
public class Computer {

    /**
     * The Computer's ID, that can't be changed.
     * @see Computer#Computer(Builder b)
     * @see Computer#getId()
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Computer's name, that can be change.
     * @see Computer#getName()
     * @see Computer#setName(String)
     */
    private String name;

    /**
     * The Computer's introduced date, that can be change.
     * @see Computer#getIntroduced()
     * @see Computer#setIntroduced(LocalDate)
     */
    private LocalDate introduced;

    /**
     * The Computer's discontinued date, that can be change.
     * @see Computer#getDiscontinued()
     * @see Computer#setDiscontinued(LocalDate)
     */
    private LocalDate discontinued;

    /**
     * The Computer's referenced company's id, that can be change.
     * @see Computer#setCompany(Company)
     */
    @ManyToOne(targetEntity = Company.class)
    private Company company;

    /**
     * Default empty constructor for Hibernate.
     */
    public Computer() {

    }

    /**
     * Contructor for HQL Query.
     * @param id computer's id.
     * @param name computer's name.
     * @param introduced computer's introduced date.
     * @param discontinued computer's discontinued date.
     * @param company computer's company.
     */
    public Computer(Long id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
    }

    /**
     * Computer Constructor.
     * Each field is instantiated through the parameters. No default values.
     * @param builder builder than contains or not all the fields
     */
    private Computer(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company = builder.company_id;
    }

    /**
     * Return the Computer's ID.
     * @return Computer's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Return the Computer's name.
     * @return Computer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the Computer's name.
     * @param name The new Computer's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the Computer's introduced date.
     * @return Computer's introduced date.
     */
    public LocalDate getIntroduced() {
        return introduced;
    }

    /**
     * Update the Computer's introduced date..
     * @param introduced name The new Computer's introduced date.
     */
    public void setIntroduced(LocalDate introduced) {
        this.introduced = introduced;
    }

    /**
     * Return the Computer's discontinued date.
     * @return Computer's discontinued date.
     */
    public LocalDate getDiscontinued() {
        return discontinued;
    }

    /**
     * Update the Computer's discontinued date.
     * @param discontinued The new Computer's discontinued date.
     */
    public void setDiscontinued(LocalDate discontinued) {
        this.discontinued = discontinued;
    }

    /**
     * Return the company's computer.
     * @return company.
     */
    public Company getCompany() {
       return this.company;
    }

    /**
     * Update the Company referenced ID.
     * @param company The new Company referenced ID.
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Return the object Computer through String.
     * @return Computer to String.
     */
    @Override
    public String toString() {
        return "ID: " + this.id + "\nName: " + this.name + "\nIntroduced: " + this.introduced + "\nDiscontinued: "
                + this.discontinued + "\nCompany: " + this.company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Computer computer = (Computer) o;

        if (id != null ? !id.equals(computer.id) : computer.id != null) {
            return false;
        }
        if (name != null ? !name.equals(computer.name) : computer.name != null) {
            return false;
        }
        if (introduced != null ? !introduced.equals(computer.introduced) : computer.introduced != null) {
            return false;
        }
        if (discontinued != null ? !discontinued.equals(computer.discontinued) : computer.discontinued != null) {
            return false;
        }
        return company != null ? company.equals(computer.company) : computer.company == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (introduced != null ? introduced.hashCode() : 0);
        result = 31 * result + (discontinued != null ? discontinued.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    public static class Builder {

        private final String name;

        private Long id;

        private LocalDate introduced;

        private LocalDate discontinued;

        private Company company_id;

        /**
         * Builder for computer.
         * @param name new name
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Set new id.
         * @param id new id
         * @return builder
         */
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        /**
         * Set new introduced date.
         * @param introduced new date
         * @return builder
         */
        public Builder introduced(LocalDate introduced) {
            this.introduced = introduced;
            return this;
        }

        /**
         * Set new discontinued date.
         * @param discontinued new date
         * @return builder
         */
        public Builder discontinued(LocalDate discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        /**
         * Set new company id ref.
         * @param companyId new company id
         * @return builder
         */
        public Builder company(Company companyId) {
            this.company_id = companyId;
            return this;
        }

        /**
         * Build computer.
         * @return computer
         */
        public Computer build() {
            return new Computer(this);
        }
    }

}
