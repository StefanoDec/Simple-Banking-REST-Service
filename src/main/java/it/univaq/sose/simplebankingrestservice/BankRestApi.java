package it.univaq.sose.simplebankingrestservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/bank")
public interface BankRestApi {

    @GET
    @Path("/test")
    @Produces({MediaType.TEXT_PLAIN})
    int getBank();
}
