package fr.ebiz.computerdatabase.model.utils;

public class SearchFilter {

    private String filter;

    private String value;
    /**
     * Build part like for query.
     * @param value search value.
     */
    public SearchFilter(String value) {
        this.filter = " LIKE ";
        this.value = "%" + value + "%";
    }

    public String getOperator() {
        return filter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
