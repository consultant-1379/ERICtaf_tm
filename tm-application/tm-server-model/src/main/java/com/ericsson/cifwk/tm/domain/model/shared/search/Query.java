package com.ericsson.cifwk.tm.domain.model.shared.search;

import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.presentation.dto.CriterionInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.SortInfo;
import com.ericsson.cifwk.tm.presentation.dto.SortOrderInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.googlecode.genericdao.search.Search;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {

    private static final Pattern QUERY_RE = Pattern.compile(
            String.format("(?<type>&|\\||$)*(?<field>(?:\\w+?\\.)*\\w+)(?<operator>%1$s)(?<value>(?:(?!%1$s).)*(?:.(?=&|\\||$)))",
                    Joiner.on("|").join(QueryOperator.values())));

    private final Multimap<String, CriterionInfo> criteria;
    private SortInfo sortBy = new SortInfo("id", SortOrderInfo.DESC);

    Query() {
        this.criteria = LinkedListMultimap.create();
    }

    public static Query fromQueryString(String queryString) {
        String nonNullQuery = Strings.nullToEmpty(queryString);

        Query query = new Query();
        Matcher matcher = QUERY_RE.matcher(nonNullQuery);
        while (matcher.find()) {
            String fieldName = matcher.group("field");
            String operatorName = matcher.group("operator");
            String value = matcher.group("value");
            String type = Strings.nullToEmpty(matcher.group("type"));
            query.add(new CriterionInfo(fieldName, operatorName, value, type));
        }

        return query;
    }

    public QueryInfo convertToQueryInfo(Map<String, QueryField> fieldMap) {
        QueryInfo info = new QueryInfo();
        for (CriterionInfo criterion : criteria.values()) {
            if (fieldMap.containsKey(criterion.getField()) || QueryConverter.ANY_FIELD.equals(criterion.getField())) {
                info.getCriteria().add(criterion);
            }
        }
        if (fieldMap.containsKey(sortBy.getField())) {
            info.setSortBy(sortBy);
        }
        return info;
    }

    public void add(CriterionInfo criterion) {
        criteria.put(criterion.getField(), criterion);
    }

    public void remove(String fieldName) {
        criteria.removeAll(fieldName);
    }

    public void remove(CriterionInfo criterion) {
        criteria.remove(criterion.getField(), criterion);
    }

    public void sortBy(String field, String order) {
        if (Strings.isNullOrEmpty(field)) {
            return;
        }
        SortOrderInfo orderInfo = SortOrderInfo.parse(order);
        sortBy = new SortInfo(field, orderInfo);
    }

    public boolean isEmpty() {
        return criteria.isEmpty();
    }

    public <T> Search convertToSearch(Class<T> searchClass, Map<String, QueryField> fieldMap) {
        QueryConverter<T> converter = new QueryConverter<>(criteria.values(), sortBy);
        return converter.convert(searchClass, fieldMap);
    }

}
