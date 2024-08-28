package com.ericsson.cifwk.tm.domain.model.shared.search;

import com.ericsson.cifwk.tm.domain.model.shared.search.field.CompositeQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.presentation.dto.CriterionInfo;
import com.ericsson.cifwk.tm.presentation.dto.SortInfo;
import com.google.common.collect.Lists;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import jersey.repackaged.com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryConverter<T> {

    public static final String ANY_FIELD = "any";
    public static final String AND = "&";
    public static final String OR = "|";

    private final Iterable<CriterionInfo> criteria;
    private final SortInfo sortBy;
    private Search search;
    private Map<String, QueryField> fieldMap;
    private List<QueryComparison> anyCriteria;
    private List<Filter> orFilters;
    private Set<String> anyFieldNames;

    public QueryConverter(Iterable<CriterionInfo> criteria, SortInfo sortBy) {
        this.criteria = criteria;
        this.sortBy = sortBy;
    }

    public Search convert(Class<T> searchClass, Map<String, QueryField> fieldMap) {
        search = new Search(searchClass);
        this.fieldMap = fieldMap;
        anyCriteria = Lists.newArrayList();
        orFilters = Lists.newArrayList();
        anyFieldNames = Sets.newHashSet(fieldMap.keySet());
        parseCriteria(criteria);
        addRemainingAny();
        addSorting();
        return search;
    }

    private void parseCriteria(Iterable<CriterionInfo> criteria) {
        for (CriterionInfo criterion : criteria) {
            QueryOperator operator = QueryOperator.parse(criterion.getOperator());
            if (operator != null) {
                String fieldName = criterion.getField();
                String value = criterion.getValue();
                String type = criterion.getType();
                QueryField field = fieldMap.get(fieldName);

                anyFieldNames.remove(fieldName);
                addCriteriaFilter(operator, fieldName, value, field, type);
            }
        }
        if (!orFilters.isEmpty()) {
            int size = orFilters.size();
            search.addFilter(Filter.or(orFilters.toArray(new Filter[size])));
        }

    }

    private void addCriteriaFilter(QueryOperator operator, String fieldName, String value, QueryField field, String type) {
        if (ANY_FIELD.equals(fieldName)) {
            anyCriteria.add(new QueryComparison(operator, value));
        } else if (field != null) {
            Filter filter = field.toFilter(operator, value);
            if (filter != null) {
                if (type.equals(OR)) {
                    orFilters.add(filter);
                } else {
                    search.addFilter(filter);
                }
            }
        }
    }

    private void addRemainingAny() {
        if (anyCriteria.isEmpty() || anyFieldNames.isEmpty()) {
            return;
        }
        CompositeQueryField anyField = new CompositeQueryField();
        for (String fieldName : anyFieldNames) {
            anyField.add(fieldMap.get(fieldName));
        }
        for (QueryComparison comparison : anyCriteria) {
            QueryOperator operator = comparison.getOperator();
            String value = comparison.getValue();
            Filter anyFilter = anyField.toFilter(operator, value);
            search.addFilter(anyFilter);
        }
    }

    private void addSorting() {
        QueryField queryField = fieldMap.get(sortBy.getField());
        if (queryField == null) {
            return;
        }
        String property = queryField.property();
        if (property == null) {
            return;
        }
        search.addSort(property, sortBy.isDescending());
    }

}
