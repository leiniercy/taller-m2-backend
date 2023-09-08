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

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Charger;
import tallerM2.tallerM2.services.servicesImpl.ChargerService;

import java.io.IOException;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(value = "/api/v1/charger")
@CrossOrigin("*")
@Tag(name = "charger", description = "The movile API")
public class ChargerController {

    @Autowired
    private ChargerService service;

    @Operation(summary = "Find all charger", description = "Find all charger", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Charger.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all charger, Taller 2M", description = "Find all charge, Taller 2M", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Charger.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all/2M"}, produces = "application/json")
    ResponseEntity<?> all2M() {
        return ResponseEntity.ok(service.findAllTaller2M());
    }

    @Operation(summary = "Find all charger, Taller MJ", description = "Find all charger, Taller MJ", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Charger.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all/MJ"}, produces = "application/json")
    ResponseEntity<?> allMJ() {
        return ResponseEntity.ok(service.findAllTallerMJ());
    }

    @Operation(summary = "Find all charger sorted by id", description = "Find all charger", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Charger.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allSorted() {
        return ResponseEntity.ok(service.findAllByOrderByIdAsc());
    }

    @Operation(summary = "Find charger by ID", description = "Search charger by the id", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Charger.class))),
        @ApiResponse(responseCode = "404", description = "Charger not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
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

    @Operation(summary = "Count cant of charger", description = "count chargers", tags = "charger")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(value = "/getCant/{taller}")
    public ResponseEntity<?> getCant(@PathVariable(value = "taller") String taller) {
        return ResponseEntity.ok(service.count(taller));
    }

    @Operation(summary = "Create new charger", description = "Create a new charger", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Charger.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "409", description = "This Charger already exists", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = {"/save"}, produces = "application/json")
    public ResponseEntity<?> save(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("taller") String taller,
            @RequestParam("connectorType") String connectorType,
            @RequestParam("compatibleDevice") String compatibleDevice,
            @RequestParam("files") List<MultipartFile> files
    ) throws Conflict, BadRequest, IOException {
        try {
            return ResponseEntity.ok(service.save(files, name, price, cant, taller,
                    connectorType, compatibleDevice));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Update charger", description = "Update charger info", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Charger.class))),
        @ApiResponse(responseCode = "404", description = "Charger not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = {"/update/{id}"}, produces = "application/json")
    public ResponseEntity<?> update(@RequestParam("files") List<MultipartFile> files,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("taller") String taller,
            @RequestParam("connectorType") String connectorType,
            @RequestParam("compatibleDevice") String compatibleDevice,
            @PathVariable(value = "id") Long id)
            throws ValueNotFound, BadRequest, IOException {
        try {
            return ResponseEntity.ok(service.update(files, name, price, cant, taller, connectorType, compatibleDevice, id));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Delete a charger by id", description = "Delete a charger", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Charger.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "Charger not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(summary = "Delete chargers", description = "Delete list of chargers", tags = "charger")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Charger.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "Charger not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))
        )}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Charger> chargers) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(chargers));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Charger not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

}
