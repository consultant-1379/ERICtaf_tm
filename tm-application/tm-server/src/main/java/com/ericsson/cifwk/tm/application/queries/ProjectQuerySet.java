/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@QuerySet
public class ProjectQuerySet {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectMapper projectMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectInfo> infos = Lists.transform(projects, new Function<Project, ProjectInfo>() {
            @Override
            public ProjectInfo apply(Project input) {
                return projectMapper.mapEntity(input, ProjectInfo.class);
            }
        });
        return Responses.ok(infos);
    }

    public Response getProject(String projectId) {
        Project project = projectRepository.findByExternalId(projectId);
        if (project != null) {
            return Responses.ok(projectMapper.mapEntity(project, ProjectInfo.class));
        } else {
            return Responses.notFound();
        }
    }

    public Response getProjectsByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> featureFields = searchMapping.getProductFields();
        QueryInfo queryInfo = query.convertToQueryInfo(featureFields);
        Search search = query.convertToSearch(Project.class, featureFields);
        Paginated<Project> paginated = projectRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<Project, ProjectInfo>() {
                    @Override
                    public ProjectInfo apply(Project project) {
                        return projectMapper.mapEntity(project, new ProjectInfo());
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }
}
