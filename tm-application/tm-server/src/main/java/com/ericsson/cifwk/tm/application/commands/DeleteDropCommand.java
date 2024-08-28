package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Created by ezhigci on 08/11/2017.
 */
public class DeleteDropCommand implements Command<Long> {

    @Inject
    private DropRepository dropRepository;

    @Override
    public Response apply(Long id) {
        Drop drop = dropRepository.find(id);
        if (drop == null || drop.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        drop.delete();
        return Responses.noContent();
    }
}
