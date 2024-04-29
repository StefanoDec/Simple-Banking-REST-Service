package it.univaq.sose.simplebankingrestservice.webapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.simplebankingrestservice.dto.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/bank")
public interface BankRestApi {

    @Operation(operationId = "getAllServiceAccounts", description = "getAllServiceAccounts", responses = {
            @ApiResponse(description = "Admin and Banker Account", content = {
                    @Content(mediaType = MediaType.TEXT_XML, array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))),
                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))),
                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))})
    })
    @GET
    @Path("/account/service")
    public List<AccountResponse> getAllServiceAccounts();

    @Operation(operationId = "saveAdminAccount", description = "saveAdminAccount", responses = {
            @ApiResponse(description = "Save Admin Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountRequest.class)),})
    })
    @POST
    @Path("/account/service/save-admin")
    public AccountResponse saveAdminAccount(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = AccountRequest.class)),
            }
    ) AccountRequest request);

    @Operation(operationId = "saveAdminAccount", description = "saveAdminAccount", responses = {
            @ApiResponse(description = "Save Banker Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountRequest.class)),})
    })
    @POST
    @Path("/account/service")
    public AccountResponse saveBankerAccount(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = AccountRequest.class)),
            }
    ) AccountRequest request);

    @Operation(operationId = "getAllAccountsAndBankAccounts", description = "getAllAccountsAndBankAccounts", responses = {
            @ApiResponse(description = "Customer Bank Accounts", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = AccountAndBankAccount.class))),
                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = AccountAndBankAccount.class)))})
    })
    @GET
    @Path("/account/bank-account")
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts();

    @Operation(operationId = "getAccountAndBankAccount", description = "getAccountAndBankAccount", responses = {
            @ApiResponse(description = "Customer Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class)),})
    })
    @GET
    @Path("/account/{id}/bank-account")
    public AccountAndBankAccount getAccountAndBankAccount(@PathParam(value = "id") long id) throws NotFoundException;

    @Operation(operationId = "saveAccountAndBankAccount", description = "saveAccountAndBankAccount", responses = {
            @ApiResponse(description = "Save Customer Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenBankAccountRequest.class)),})
    })
    @POST
    @Path("/account/bank-account")
    public AccountAndBankAccount saveAccountAndBankAccount(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = OpenBankAccountRequest.class)),
            }
    ) OpenBankAccountRequest request);

    @Operation(operationId = "deleteAccount", description = "deleteAccount", responses = {
            @ApiResponse(description = "Delete Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = Boolean.class)),})
    })
    @DELETE
    @Path("/account/{id}")
    boolean deleteAccount(@PathParam(value = "id") long id) throws NotFoundException;

    @Operation(operationId = "depositMoneyInBankAccount", description = "depositMoneyInBankAccount", responses = {
            @ApiResponse(description = "Deposit money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class)),})
    })
    @PUT
    @Path("/account/bank-account/{id}/deposit")
    public AccountAndBankAccount depositMoneyInBankAccount(@RequestBody(description = "Money Transfer",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MoneyTransfer.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = MoneyTransfer.class)),
            }
    ) MoneyTransfer moneyTransfer, @PathParam(value = "id") long id) throws NotFoundException;

    @Operation(operationId = "withdrawMoneyInBankAccount", description = "withdrawMoneyInBankAccount", responses = {
            @ApiResponse(description = "Withdraw money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class)),})
    })
    @PUT
    @Path("/account/bank-account/{id}/withdraw")
    public AccountAndBankAccount withdrawMoneyInBankAccount(@RequestBody(description = "Money Transfer",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MoneyTransfer.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = MoneyTransfer.class)),
            }
    ) MoneyTransfer moneyTransfer, @PathParam(value = "id") long id) throws NotFoundException, InsufficientFundsException;


}
