package tallerM2.tallerM2.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Customer;
import tallerM2.tallerM2.services.servicesImpl.CustomerService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/customer")
@CrossOrigin("*")
@Tag(name = "customer", description = "The movile API")
public class CustomerController {

    @Autowired
    CustomerService service;

    @Operation(summary = "Find all customers", description = "Find all customers", tags = "customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all customers order by id", description = "Find all customers order by id", tags = "customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allSortedById() {
        return ResponseEntity.ok(service.findAllOrderByIdAsc());
    }

    @Operation(summary = "Find a customer by ID", description = "Search customer by the id", tags = "customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(value = "/get/{id}", produces = "application/json")
    public ResponseEntity<?> byId(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Create new customer",
            description = "Create a new customer",
            tags = "customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Customer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "This customer already exists",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @PostMapping(path = {"/save"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> save(@RequestBody Customer customer) throws Conflict, BadRequest {
        try {
            return ResponseEntity.ok(service.save(customer));
        } catch (Conflict conflict) {
            throw new Conflict(conflict.getMessage());
        }catch (BadRequest badRequest) {
           throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Update customer",
            description = "Update customer info",
            tags = "customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Customer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @PutMapping(path = {"/update"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update( @RequestBody Customer customer) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok( service.update(customer) );
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a customer by id",
            description = "Delete a customer",
            tags = "customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Customer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteById(id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete customers",
            description = "Delete list of customers",
            tags = "customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Customer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Customer> customers) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(customers));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Customer not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }


}
