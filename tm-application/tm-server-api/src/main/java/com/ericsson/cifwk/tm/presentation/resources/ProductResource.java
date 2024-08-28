package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;
import com.ericsson.cifwk.tm.presentation.validation.HasId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 */
@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductResource {

    /**
     * Request all Products.
     *
     * @return Array with Products objects.
     */
    @GET
    Response getProducts();

    /**
     * Request Product by specified ID.<br>
     * If Product doesn't exist then Response with status code 404 (Not Found) is returned.
     *
     * @param productId Existing Product ID.
     * @return Product object.
     */
    @GET
    @Path("{productId}")
    Response getProduct(@PathParam("productId") String productId);

    /**
     * Request all Drops for the specified Product.
     *
     * @param productId The id of the Product.
     * @return List of DropInfo objects.
     */
    @GET
    @Path("{productId}/drops")
    Response getDrops(@PathParam("productId") Long productId);

    /**
     * Request all Features for the specified Product.
     *
     * @param productId The id of the product.
     * @return List of FeatureInfo objects.
     */
    @GET
    @Path("{productId}/features")
    Response getFeatures(@PathParam("productId") Long productId);

    /**
     * Request all Components for the specified list of Features.
     *
     * @param featureIds The id of the feature.
     * @return the list of components for that feature
     */
    @GET
    @Path("/features/components")
    Response getComponents(@QueryParam("featureId") @NotNull List<Long> featureIds);

    /**
     * Request to create product with specified ID.<br>
     * If product with such ID exists, checks to see if product was deleted.
     * If product with specified ID exists and is deleted, it is re-created.
     * If product with specified ID exists and is not deleted, exception is thrown.
     *
     * @param productInfo for the product to create.
     * @return Created product Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid ProductInfo productInfo);

    /**
     * Request to update product with specified ID.<br>
     * If product entity with specified ID doesn't exist, then it's created.
     *
     * @param id          product ID.
     * @param productInfo ProductInfo featureInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @Valid @HasId ProductInfo productInfo);

    /**
     * Request to delete product with specified ID.<br>
     * If product entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id product ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
