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
     * Get filters value from a columnName.
     * @param columnName columnName.
     * @return operator
     */
    public Operator getFilterValue(String columnName) {
        return filters.get(columnName);
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
         * Set a new search.
         * @param columnName columnName to apply search on
         * @param op Like or =
         * @return builder
         */
        public Builder search(String columnName, Operator op) {
            filter.filters.put(columnName, op);
            return this;
        }

        /**
         * Set new order by.
         * @param orderBy name of column to order by
         * @param asc true if ASC else false
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
