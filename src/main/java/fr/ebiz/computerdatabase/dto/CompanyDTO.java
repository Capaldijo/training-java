package fr.ebiz.computerdatabase.dto;

public class CompanyDTO {

    private String id;

    private String name;

    /**
     * Constructor CompanyDTO.
     */
    public CompanyDTO() {
    }

    /**
     * Contructor CompanyDTO.
     * @param id of the company
     * @param name of the company
     */
    public CompanyDTO(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "CompanyDTO id=" + id + ", name=" + name;
    }
}
