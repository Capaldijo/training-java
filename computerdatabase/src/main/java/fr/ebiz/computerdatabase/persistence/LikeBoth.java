package fr.ebiz.computerdatabase.persistence;

public class LikeBoth extends Operator {

    /**
     * Build part like for query.
     * @param value search value
     */
    public LikeBoth(String value) {
        super(" LIKE ", "%" + value + "%");
    }

}
