package fr.ebiz.computerdatabase.models;

public class LikeBoth extends Operator {

    /**
     * Build part like for query.
     * @param value search value
     */
    public LikeBoth(String value) {
        super(" LIKE ", "%" + value + "%");
    }

}
