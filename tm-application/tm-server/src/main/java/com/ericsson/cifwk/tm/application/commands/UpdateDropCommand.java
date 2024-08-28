package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Created by ezhigci on 08/11/2017.
 */
public class UpdateDropCommand implements Command<DropInfo> {

    @Inject
    private DropRepository dropRepository;

    @Inject
    private DropMapper dropMapper;

    @Override
    public Response apply(DropInfo input) {
        dropRepository.disableFilter();
        Drop drop = dropRepository.findByProductIDAndDropName(input.getProduct().getId(), input.getName());
        if (drop != null) {
            if (drop.isDeleted()) {
                dropRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Drop with this name already exists but was deleted." +
                        " Please re-create deleted drop or choose a different name."));
            } else if (!drop.getId().equals(input.getId())) {
                dropRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Drop already exists"));
            }
        }
        Drop entity = dropRepository.find(input.getId());
        if (entity == null) {
            dropRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        dropRepository.enableFilter();
        dropMapper.mapDto(input, entity);
        dropRepository.save(entity);
        return Responses.ok();
    }
}
