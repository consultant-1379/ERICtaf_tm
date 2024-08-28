package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.inject.Inject;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * Created by ezhigci on 05/09/2017.
 */
public class CreateDropCommand implements Command<DropInfo> {

    @Inject
    private DropRepository dropRepository;

    @Inject
    private DropMapper dropMapper;

    @Inject
    private ProductRepository productRepository;

    @Override
    public Response apply(DropInfo input) {
        dropRepository.disableFilter();
        Drop entity = dropMapper.mapDto(input, Drop.class);
        if (input.getProduct() == null) {
            throw new BadRequestException(Responses.badRequest("Product Information not attached"));
        }
        Drop drop = dropRepository.findByProductIDAndDropName(input.getProduct().getId(), input.getName());
        if (drop != null) {
            if (!drop.isDeleted()) {
                productRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Drop already exists"));
            } else {
                drop.undelete();
                DropInfo dropDto = dropMapper.mapEntity(drop, new DropInfo());
                productRepository.enableFilter();
                return Responses.created(dropDto);
            }
        }
        entity.setProduct(productRepository.find(input.getProduct().getId()));
        dropRepository.persist(entity);
        dropRepository.enableFilter();
        DropInfo dropDto = dropMapper.mapEntity(entity, new DropInfo());
        return Responses.created(dropDto);

    }
}

