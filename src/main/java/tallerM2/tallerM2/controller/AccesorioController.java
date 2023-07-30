package tallerM2.tallerM2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.services.servicesImpl.AccesorioService;
import tallerM2.tallerM2.services.servicesImpl.ImageService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/accesorio")
@CrossOrigin("*")
@Tag(name = "accesorio", description = "The movile API")
public class AccesorioController {

    @Autowired
    AccesorioService service;
    @Autowired
    private ImageService imageService;

    @Operation(summary = "Find all accesories", description = "Find all accesories", tags = "accesorio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Accesorio.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all"}, produces = "application/json")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }


    @Operation(summary = "Find a accesorie by ID", description = "Search accesorie by the id", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Accesorio.class))),
            @ApiResponse(responseCode = "404", description = "product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
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
            summary = "Create new accesorio",
            description = "Create a new accesorio",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Accesorio.class)
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
                            description = "This product already exists",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @PostMapping(path = {"/save"}, /*consumes = "application/json",*/ produces = "application/json")
    public ResponseEntity<?> save(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("files") List<MultipartFile> files
    )
            throws Conflict, BadRequest, IOException {

        try {
            return ResponseEntity.ok(service.save(files,name, price, cant));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest(br.getMessage());
        }
    }

    @Operation(
            summary = "Update accesorio",
            description = "Update accesorio info",
            tags = "accesorio"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Accesorio.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Accesorio not found",
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
    @PutMapping(path = {"/update/{id}"}, /*consumes = "application/json",*/ produces = "application/json")
    public ResponseEntity<?> update(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("id") Long id
    ) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok( service.update(files,name, price, cant, id) );
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a accesorio by id",
            description = "Delete a accesorio",
            tags = "accesorio"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Accesorio.class)
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
                            description = "Accesorio not found",
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
            summary = "Delete accesorios",
            description = "Delete list of accesorios",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Accesorio.class)
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
                            description = "Accesorio not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Accesorio> accesorios) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(accesorios));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Accesorio not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Get image by name",
            description = "Get image by name from the server",
            tags = "product"
    )
    @GetMapping(path = "/image/{name}")
    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageService.obtenerImagen(name));
    }

    //    @GetMapping(path = "/image/{name}")
//    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
//        return ResponseEntity.ok(imageService.obtenerImagen(name));
//    }

}
