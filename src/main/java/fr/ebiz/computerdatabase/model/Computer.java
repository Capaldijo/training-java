package fr.ebiz.computerdatabase.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Computer is the class representing a computer in the database. A computer got
 * the following information: A unique identifier that stay still. A name, that
 * can be changed. An introduced date, that can be changed. A discontinued date,
 * that can be changed. And a company_id referencing the company's ID which
 * built it.
 * @see LocalDateTime
 * @see Company
 * @author capaldijo
 */
public class Computer {

    final Logger logger = LoggerFactory.getLogger(Computer.class);

    /**
     * The Computer's ID, that can't be changed.
     * @see Computer#Computer(Builder b)
     * @see Computer#getId()
     */
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
     * @see Computer#getCompanyId()
     * @see Computer#setCompanyId(int)
     */
    private int company_id;

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
        this.company_id = builder.company_id;
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
        logger.debug("Name set to {}. Old name was {}.", name, this.name);
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
        logger.debug("Introduced date set to {}. Old date was {}.", introduced, this.introduced);
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
        logger.debug("Discontinued date set to {}. Old date was {}.", discontinued, this.discontinued);
        this.discontinued = discontinued;
    }

    /**
     * Return the Company's referenced ID.
     * @return Company's referenced ID.
     */
    public int getCompanyId() {
        return company_id;
    }

    /**
     * Update the Company referenced ID.
     * @param companyId The new Company referenced ID.
     */
    public void setCompanyId(int companyId) {
        logger.debug("Company_id set to {}. Old company_id was {}.", company_id, this.company_id);
        this.company_id = companyId;
    }

    /**
     * Return the object Computer through String.
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Computer other = (Computer) obj;
        if (company_id != other.company_id) {
            return false;
        }
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public static class Builder {

        private final String name;

        private Long id;

        private LocalDate introduced;

        private LocalDate discontinued;

        private int company_id;

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
        public Builder companyId(int companyId) {
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
