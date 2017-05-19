package fr.ebiz.computerdatabase.models;

public abstract class Operator {

    protected String filter;

    protected String value;

    /**
     * Constructor.
     * @param filter fil
     * @param value val
     */
    public Operator(String filter, String value) {
        this.filter = filter;
        this.value = value;
    }

    public String getOperator() {
        return filter;
    }

    public void setOperator(String operator) {
        this.filter = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
