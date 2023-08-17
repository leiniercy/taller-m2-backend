package tallerM2.tallerM2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Movile;
import tallerM2.tallerM2.services.servicesImpl.ImageService;
import tallerM2.tallerM2.services.servicesImpl.MovileService;

@RestController
@RequestMapping(value = "/api/v1/movile")
@CrossOrigin("*")
@Tag(name = "movile", description = "The movile API")
public class MovileController {

    @Autowired
    private MovileService service;
    @Autowired
    private ImageService imageService;

    @Operation(summary = "Find all moviles", description = "Find all moviles", tags = "movile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Movile.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all moviles sorted by id", description = "Find all moviles", tags = "movile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Movile.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allSorted() {
        return ResponseEntity.ok(service.findAllByOrderByIdAsc());
    }

    @Operation(summary = "Find movile by ID", description = "Search movile by the id", tags = "movile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Movile.class))),
            @ApiResponse(responseCode = "404", description = "movile not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
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

    @Operation(summary = "Count cant of movile", description = "count moviles", tags = "movile")
    @GetMapping(value = "/getCant")
    public ResponseEntity<?> getCant() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
            summary = "Create new movile",
            description = "Create a new movile",
            tags = "movile"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Movile.class)
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
                            description = "This Movile already exists",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @PostMapping(path = {"/save"}, produces = "application/json")
    public ResponseEntity<?> save(@RequestParam("name") String name,
                                  @RequestParam("price") int price,
                                  @RequestParam("cant") int cant,
                                  @RequestParam("taller") String taller,
                                  @RequestParam("sizeStorage") int sizeStorage,
                                  @RequestParam("ram") int ram,
                                  @RequestParam("camaraTrasera") int camaraTrasera,
                                  @RequestParam("camaraFrontal") int camaraFrontal,
                                  @RequestParam("banda2G") boolean banda2G,
                                  @RequestParam("banda3G") boolean banda3G,
                                  @RequestParam("banda4G") boolean banda4G,
                                  @RequestParam("banda5G") boolean banda5G,
                                  @RequestParam("bateria") long bateria,
                                  @RequestParam("files") List<MultipartFile> files
    ) throws Conflict, BadRequest, IOException {
        try {
            return ResponseEntity.ok(service.save(files, name, price, cant, taller,
                    sizeStorage, ram, camaraTrasera, camaraFrontal,
                    banda2G, banda3G, banda4G, banda5G, bateria));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Update movile",
            description = "Update movile info",
            tags = "movile"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Movile.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movile not found",
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
    @PutMapping(path = {"/update/{id}"}, produces = "application/json")
    public ResponseEntity<?> update(@RequestParam("files") List<MultipartFile> files,
                                    @RequestParam("name") String name,
                                    @RequestParam("price") int price,
                                    @RequestParam("cant") int cant,
                                    @RequestParam("taller") String taller,
                                    @RequestParam("sizeStorage") int sizeStorage,
                                    @RequestParam("ram") int ram,
                                    @RequestParam("camaraTrasera") int camaraTrasera,
                                    @RequestParam("camaraFrontal") int camaraFrontal,
                                    @RequestParam("banda2G") boolean banda2G,
                                    @RequestParam("banda3G") boolean banda3G,
                                    @RequestParam("banda4G") boolean banda4G,
                                    @RequestParam("banda5G") boolean banda5G,
                                    @RequestParam("bateria") long bateria,
                                    @PathVariable(value = "id") Long id)
            throws ValueNotFound, BadRequest, IOException {
        try {
            return ResponseEntity.ok(service.update(files, name, price, cant, taller,
                    sizeStorage, ram, camaraTrasera, camaraFrontal,
                    banda2G, banda3G, banda4G, banda5G, bateria, id));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a movile",
            description = "Delete a movile",
            tags = "movile"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Movile.class)
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
                            description = "Movile not found",
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
            summary = "Delete moviles",
            description = "Delete list of moviles",
            tags = "movile"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Movile.class)
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
                            description = "Movile not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Movile> moviles) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(moviles));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Movile not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Get image by name",
            description = "Get image by name from the server",
            tags = "movile"
    )
    @GetMapping(path = "/image/{name}")
    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageService.obtenerImagen(name));
    }


}
