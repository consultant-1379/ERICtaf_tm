package com.ericsson.cifwk.tm.domain.repository.search;

import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.EnumQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.NameWithIdQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class QueryTest {

    @Test
    public void testParse() {
        Query query = Query.fromQueryString("key1=tom&jerry&a.key2!=3&invalid=invalid&any~aa");
        Map<String, QueryField> fieldMap = mockFieldMap("key1", "a.key2", "key3");

        Search convertedSearch = query.convertToSearch(TestCaseVersion.class, fieldMap);
        assertEquals(3, convertedSearch.getFilters().size());
        assertEquals(3, query.convertToQueryInfo(fieldMap).getCriteria().size());
        verify(fieldMap.get("key1")).toFilter(QueryOperator.EQUAL, "tom&jerry");
        verify(fieldMap.get("a.key2")).toFilter(QueryOperator.NOT_EQUAL, "3");
        verify(fieldMap.get("key3")).toFilter(QueryOperator.LIKE_CI, "aa");
    }

    @Test
    public void testEmpty() {
        Query query = Query.fromQueryString("");
        Map<String, QueryField> fieldMap = mockFieldMap("key1", "key2");

        assertEquals(0, query.convertToSearch(TestCaseVersion.class, fieldMap).getFilters().size());
        assertEquals(0, query.convertToQueryInfo(fieldMap).getCriteria().size());
    }

    @Test
    public void shouldConvertEnumsPersistedAsString() {
        Map<String, QueryField> fieldMap = Maps.newHashMap();
        fieldMap.put("priority", new EnumQueryField<>("priority", Priority.class));

        Query query = Query.fromQueryString("priority~Blocker");

        QueryInfo queryInfo = query.convertToQueryInfo(fieldMap);
        assertThat(queryInfo.getCriteria().size(), equalTo(1));
        Search search = query.convertToSearch(TestCaseVersion.class, fieldMap);
        assertThat(search.getFilters().size(), equalTo(1));
        Filter filter = search.getFilters().get(0);
        assertThat(filter.getProperty(), equalTo("priority"));
        assertThat((List<Priority>) filter.getValue(), hasItem(Priority.BLOCKER));
        assertThat(filter.getOperator(), equalTo(Filter.OP_IN));
    }

    @Test
    public void shouldConvertEnumsPersistedAsInteger() {
        Map<String, QueryField> fieldMap = Maps.newHashMap();
        fieldMap.put("executionType", new NameWithIdQueryField<>("executionType", TestExecutionType.class));
        Query query = Query.fromQueryString("executionType=manual");
        QueryInfo queryInfo = query.convertToQueryInfo(fieldMap);
        assertThat(queryInfo.getCriteria().size(), equalTo(1));
        Search search = query.convertToSearch(TestCaseVersion.class, fieldMap);
        assertThat(search.getFilters().size(), equalTo(1));
        Filter filter = search.getFilters().get(0);
        assertThat(filter.getProperty(), equalTo("executionType"));
        assertThat(filter.getOperator(), equalTo(Filter.OP_EQUAL));
        Integer filterValue = (Integer) filter.getValue();
        assertThat(filterValue, equalTo(1));
    }
    
    @Test
    public void shouldIgnoreMissingEnums() {
        Map<String, QueryField> fieldMap = Maps.newHashMap();
        fieldMap.put("priority", new EnumQueryField<>("priority", Priority.class));
        
        Query query = Query.fromQueryString("priority=yesterday");
        QueryInfo queryInfo = query.convertToQueryInfo(fieldMap);
        assertThat(queryInfo.getCriteria().size(), equalTo(1));
        Search search = query.convertToSearch(TestCaseVersion.class, fieldMap);
        assertThat(search.getFilters().size(), equalTo(1));
    }

    @Test
    public void shouldGroupOrType() {
        Query query = Query.fromQueryString("key1=A&key1=B&key1~C&key1!=D|key1!=E|key2=F");
        Map<String, QueryField> fieldMap = mockFieldMap("key1", "key2");
        Search search = query.convertToSearch(Object.class, fieldMap);
        List<Filter> filters = search.getFilters();

        assertThat(filters, hasSize(5));

        List<Filter> filterGroups = Lists.newArrayList();
        for (Filter filter : filters) {
            if (filter.getOperator() == Filter.OP_OR) {
                filterGroups.add(filter);
            }
        }

        assertThat(filterGroups, hasSize(1));

    }

    @Test
    public void shouldConvertAnyField() {
        Map<String, QueryField> fieldMap = mockFieldMap("key1", "key2");
        List<Filter> searchAny = Query.fromQueryString("any=A")
                .convertToSearch(Object.class, fieldMap)
                .getFilters();

        assertThat(searchAny, hasSize(1));
        assertThat(searchAny, Matchers.<Filter>hasItems(
                not(hasProperty("operator", is(Filter.OP_EQUAL))),
                hasProperty("operator", is(Filter.OP_OR))
        ));

        List<Filter> searchOne = Query.fromQueryString("key1=A&any=B")
                .convertToSearch(Object.class, fieldMap)
                .getFilters();

        assertThat(searchOne, hasSize(2));
        assertThat(searchOne, Matchers.<Filter>hasItems(
                hasProperty("operator", is(Filter.OP_EQUAL)),
                hasProperty("operator", is(Filter.OP_OR))
        ));

        List<Filter> searchAll = Query.fromQueryString("key1=A&key2=B&any=C")
                .convertToSearch(Object.class, fieldMap)
                .getFilters();

        assertThat(searchAll, hasSize(2));
        assertThat(searchOne, Matchers.<Filter>hasItems(
                hasProperty("operator", is(Filter.OP_EQUAL)),
                not(hasProperty("operator", is(Filter.OP_OR)))
        ));
    }

    private Map<String, QueryField> mockFieldMap(String... fieldNames) {
        Map<String, QueryField> fieldMap = Maps.newHashMap();
        for (String fieldName : fieldNames) {
            QueryField field = mockQueryField(fieldName);
            fieldMap.put(fieldName, field);
        }
        return fieldMap;
    }

    private QueryField mockQueryField(final String property) {
        QueryField field = mock(QueryField.class);
        when(field.toFilter(any(QueryOperator.class), anyString())).then(new Answer<Filter>() {
            @Override
            public Filter answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                Filter filter = mock(Filter.class);
                when(filter.getProperty()).thenReturn(property);
                when(filter.getValue()).thenReturn(arguments[1]);
                return filter;
            }
        });
        return field;
    }

}
