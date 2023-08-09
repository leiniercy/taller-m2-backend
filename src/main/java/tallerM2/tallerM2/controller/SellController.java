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
import tallerM2.tallerM2.model.Sell;
import tallerM2.tallerM2.services.servicesImpl.SellService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sell")
@CrossOrigin("*")
@Tag(name = "sell", description = "The movile API")
public class SellController {

    @Autowired
    SellService service;


    @Operation(summary = "Find all sales", description = "Find all sales", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all sales order by id", description = "Find all sales order by id", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allOrderById() {
        return ResponseEntity.ok(service.findAllOrderByIdAsc());
    }

    @Operation(summary = "Find a sell by ID", description = "Search sell by the id", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
            @ApiResponse(responseCode = "404", description = "Sell not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
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
            summary = "Create new sell",
            description = "Create a new sell",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Sell.class)
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
                            description = "This sell already exists",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @PostMapping(path = {"/save"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> save( @RequestBody Sell sell) throws Conflict, BadRequest {
        try {
            return ResponseEntity.ok(service.save(sell));
        } catch (Conflict conflict) {
            throw new Conflict(conflict.getMessage());
        } catch (BadRequest badRequest) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Update sell",
            description = "Update sell info",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Sell.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sell not found",
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
    @PutMapping(path = {"/update/{id}"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update( @RequestBody Sell sell, @RequestParam("id") Long id) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok( service.update(sell, id) );
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a sell by id",
            description = "Delete a sell",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Sell.class)
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
                            description = "Sell not found",
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
            summary = "Delete sales",
            description = "Delete list of sales",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Sell.class)
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
                            description = "Sell not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Sell> sales) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(sales));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Sell not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }
}
