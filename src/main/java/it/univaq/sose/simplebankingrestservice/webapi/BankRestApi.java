package it.univaq.sose.simplebankingrestservice.webapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import it.univaq.sose.simplebankingrestservice.dto.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SecurityScheme(
        name = "bearerAuth", // Utilizzato nelle annotazioni @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Path("/bank")
public interface BankRestApi {

    @Operation(operationId = "getAllServiceAccounts", description = "getAllServiceAccounts", responses = {
            @ApiResponse(description = "Admin and Banker Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))),
                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})

    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @GET
    @Path("/account/service")
    @RolesAllowed({"ADMIN"})
    void getAllServiceAccountsAsync(@Suspended final AsyncResponse asyncResponse);

    @Operation(operationId = "saveAdminAccount", description = "saveAdminAccount", responses = {
            @ApiResponse(description = "Save Admin Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountRequest.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @POST
    @Path("/account/service/save-admin")
    @RolesAllowed({"ADMIN"})
    AccountResponse saveAdminAccount(@RequestBody(description = "Account to be saved",
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
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountRequest.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @POST
    @Path("/account/service")
    @RolesAllowed({"ADMIN"})
    AccountResponse saveBankerAccount(@RequestBody(description = "Account to be saved",
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
                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = AccountAndBankAccount.class)))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @GET
    @Path("/account/bank-account")
    @RolesAllowed({"ADMIN", "BANKER"})
    void getAllAccountsAndBankAccountsAsync(@Suspended final AsyncResponse asyncResponse);

    @Operation(operationId = "getAccountAndBankAccount", description = "getAccountAndBankAccount", responses = {
            @ApiResponse(description = "Customer Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class)),}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @GET
    @Path("/account/{id}/bank-account")
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    void getAccountAndBankAccountAsync(@PathParam(value = "id") long id, @Suspended final AsyncResponse asyncResponse);

    @Operation(operationId = "saveAccountAndBankAccount", description = "saveAccountAndBankAccount", responses = {
            @ApiResponse(description = "Save Customer Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenBankAccountRequest.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @POST
    @Path("/account/bank-account")
    @RolesAllowed({"ADMIN", "BANKER"})
    AccountAndBankAccount saveAccountAndBankAccount(@RequestBody(description = "Account to be saved",
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
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = Boolean.class)),}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @DELETE
    @Path("/account/{id}")
    @RolesAllowed({"ADMIN"})
    boolean deleteAccount(@PathParam(value = "id") long id) throws NotFoundException;

    @Operation(operationId = "depositMoneyInBankAccount", description = "depositMoneyInBankAccount", responses = {
            @ApiResponse(description = "Deposit money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class)),}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @PUT
    @Path("/account/bank-account/{id}/deposit")
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    void depositMoneyInBankAccountAsync(@RequestBody(description = "Money Transfer",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MoneyTransfer.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = MoneyTransfer.class)),
            }
    ) MoneyTransfer moneyTransfer, @PathParam(value = "id") long id, @Suspended final AsyncResponse asyncResponse);

    @Operation(operationId = "withdrawMoneyInBankAccount", description = "withdrawMoneyInBankAccount", responses = {
            @ApiResponse(description = "Withdraw money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountAndBankAccount.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountAndBankAccount.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed, you are not authorised", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    },
            security = @SecurityRequirement(name = "bearerAuth"))
    @PUT
    @Path("/account/bank-account/{id}/withdraw")
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    AccountAndBankAccount withdrawMoneyInBankAccount(@RequestBody(description = "Money Transfer",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MoneyTransfer.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = MoneyTransfer.class)),
            }
    ) MoneyTransfer moneyTransfer, @PathParam(value = "id") long id) throws NotFoundException, InsufficientFundsException;

    @Operation(operationId = "login", description = "Authenticate user and return JWT", responses = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/login")
    Response login(@RequestBody(description = "Login",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserCredentials.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserCredentials.class)),
            }
    ) UserCredentials credentials) throws NotFoundException;
}
