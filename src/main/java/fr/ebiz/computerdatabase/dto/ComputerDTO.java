package fr.ebiz.computerdatabase.dto;

public class ComputerDTO {

    private String id;

    private String name;

    private String introduced;

    private String discontinued;

    private CompanyDTO company;

    /**
     * Default constructor.
     */
    public ComputerDTO() {
    }

    /**
     * Constructor dto.
     *
     * @param builder to build computerDTO
     */
    private ComputerDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company = builder.company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduced() {
        return introduced;
    }

    public void setIntroduced(String introduced) {
        this.introduced = introduced;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public static class Builder {
        private final String name;
        private String id;
        private String introduced;
        private String discontinued;
        private CompanyDTO company;

        /**
         * Constructor for builder.
         *
         * @param name required name
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Set new id.
         *
         * @param id new id
         * @return Builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set new introduced date.
         *
         * @param introduced new date
         * @return Builder
         */
        public Builder introduced(String introduced) {
            this.introduced = introduced;
            return this;
        }

        /**
         * Set new discontinued date.
         *
         * @param discontinued new date
         * @return Builder
         */
        public Builder discontinued(String discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        /**
         * Set a new company id ref.
         *
         * @param company new company id
         * @return Builder
         */
        public Builder company(CompanyDTO company) {
            this.company = company;
            return this;
        }

        /**
         * Build a computerDTO.
         *
         * @return computerDTO
         */
        public ComputerDTO build() {
            return new ComputerDTO(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComputerDTO that = (ComputerDTO) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        if (introduced != null ? !introduced.equals(that.introduced) : that.introduced != null) {
            return false;
        }
        if (discontinued != null ? !discontinued.equals(that.discontinued) : that.discontinued != null) {
            return false;
        }
        return company != null ? company.equals(that.company) : that.company == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (introduced != null ? introduced.hashCode() : 0);
        result = 31 * result + (discontinued != null ? discontinued.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComputerDTO [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
                + discontinued + ", company=" + company + "]";
    }

}
