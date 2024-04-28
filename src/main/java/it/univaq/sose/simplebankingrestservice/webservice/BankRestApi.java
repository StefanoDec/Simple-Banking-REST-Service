package it.univaq.sose.simplebankingrestservice.webservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


@Path("/bank")
public interface BankRestApi {

    @Operation(operationId = "getbookAndReviews", description = "getBookAndReviews", responses = {
            @ApiResponse(description = "Book and Reviews", content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = Integer.class)),
                    @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = Integer.class)),})
    })
    @GET
    @Path("/test")
    Integer getBank();
}
