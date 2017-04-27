package fr.ebiz.computerdatabase.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import fr.ebiz.computerdatabase.persistence.Operator;


public class PaginationFilters {

    private HashMap<String, Operator> filters;

    private String orderBy;

    private boolean asc;

    /**
     * Constructor.
     */
    private PaginationFilters() {
        this.filters = new HashMap<>();
    }

    // Methods
    public Set<String> getFilteredColumns() {
        return filters.keySet();
    }

    public Collection<Operator> getFilterValues() {
        return filters.values();
    }

    /**
     * filters.
     * @param col col.
     * @return operator
     */
    public Operator getFilterValue(String col) {
        return filters.get(col);
    }

    public HashMap<String, Operator> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, Operator> filters) {
        this.filters = filters;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public static class Builder {

        private PaginationFilters filter;

        /**
         * Builder.
         */
        public Builder() {
            filter = new PaginationFilters();
        }

        /**
         * search.
         * @param s s
         * @param op op
         * @return builder
         */
        public Builder search(String s, Operator op) {
            filter.filters.put(s, op);
            return this;
        }

        /**
         * order by.
         * @param orderBy ob
         * @param asc asc
         * @return builder
         */
        public Builder orderBy(String orderBy, boolean asc) {
            filter.orderBy = orderBy;
            filter.asc = asc;
            return this;
        }

        /**
         * build.
         * @return PaginationFilter.
         */
        public PaginationFilters build() {
            return filter;
        }
    }
}
