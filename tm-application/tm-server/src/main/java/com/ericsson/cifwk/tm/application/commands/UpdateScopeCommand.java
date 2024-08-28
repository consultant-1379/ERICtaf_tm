/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ScopeMapper;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class UpdateScopeCommand implements Command<GroupInfo> {

    @Inject
    private ScopeRepository scopeRepository;

    @Inject
    private ScopeMapper scopeMapper;

    @Override
    public Response apply(GroupInfo input) {
        scopeRepository.disableFilter();
        Scope scope = scopeRepository.findByNameAndProduct(input.getName(), input.getProduct().getId());
        if (scope != null) {
            if (scope.isDeleted()) {
                scopeRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted group of the same name still exists." +
                        " Please try again using a different name"));
            } else if (!scope.getId().equals(input.getId())) {
                scopeRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Group already exists"));
            }
        }
        Scope entity = scopeRepository.find(input.getId());
        if (entity == null) {
            scopeRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        scopeRepository.enableFilter();
        scopeMapper.mapDto(input, entity);
        scopeRepository.save(entity);
        return Responses.ok();
    }

}
