package fr.ebiz.computerdatabase.model;

public class ComputerDTO {

    private String id;

    private String name;

    private String introduced;

    private String discontinued;

    private String company_id;

    /**
     * Constructor dto.
     * @param builder to build computerDTO
     */
    private ComputerDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company_id = builder.company_id;
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

    public String getCompanyId() {
        return company_id;
    }

    public void setCompanyId(String companyId) {
        this.company_id = companyId;
    }

    public static class Builder {
        private final String name;
        private String id;
        private String introduced;
        private String discontinued;
        private String company_id;

        /**
         * Constructor for builder.
         * @param name required name
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Set new id.
         * @param id new id
         * @return Builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set new introduced date.
         * @param introduced new date
         * @return Builder
         */
        public Builder introduced(String introduced) {
            this.introduced = introduced;
            return this;
        }

        /**
         * Set new discontinued date.
         * @param discontinued new date
         * @return Builder
         */
        public Builder discontinued(String discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        /**
         * Set a new company id ref.
         * @param companyId new company id
         * @return Builder
         */
        public Builder companyId(String companyId) {
            this.company_id = companyId;
            return this;
        }

        /**
         * Build a computerDTO.
         * @return computerDTO
         */
        public ComputerDTO build() {
            return new ComputerDTO(this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((company_id == null) ? 0 : company_id.hashCode());
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
        ComputerDTO other = (ComputerDTO) obj;
        if (company_id == null) {
            if (other.company_id != null) {
                return false;
            }
        } else if (!company_id.equals(other.company_id)) {
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

    @Override
    public String toString() {
        return "ComputerDTO [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
                + discontinued + ", company_id=" + company_id + "]";
    }

}
