/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import tallerM2.tallerM2.model.*;
import tallerM2.tallerM2.services.servicesImpl.ProductService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import tallerM2.tallerM2.utils.MinioAdapter;

@RestController
@RequestMapping(value = "/api/v1/product")
@CrossOrigin("*")
@Tag(name = "product", description = "The movile API")
public class ProductController {

    @Autowired
    ProductService service;
    @Autowired
    MinioAdapter minioAdapter;

    @PersistenceContext
    public EntityManager em;

    @Operation(summary = "Find all products", description = "Find all products", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    @GetMapping(path = {"/all/{taller}"}, produces = "application/json")
    public ResponseEntity<?> all(@PathVariable(value = "taller")String taller) {
        return ResponseEntity.ok(service.findAll(taller));
    }

    @Operation(summary = "Find all than 0", description = "Find all product where cant > 0", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/product/cant/{taller}"}, produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> allProductsThanCero(@PathVariable(value = "taller") String taller) {
        return ResponseEntity.ok(service.findAllCantThanCero(taller));
    }

    @Operation(summary = "Find all accesories", description = "Find all accesories", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    @GetMapping(path = {"/all/accesorio/{taller}"}, produces = "application/json")
    public ResponseEntity<?> allAccesorios(@PathVariable(value = "taller") String taller) {
        return ResponseEntity.ok(service.findAllAccesorios(taller));
    }

    @Operation(summary = "Cuenta la cant de accesorios", description = "count accesorios", tags = "product")
    @GetMapping(value = "/getCant/{taller}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> getCant(@PathVariable(value = "taller")  String taller) {
        return ResponseEntity.ok(service.count(taller));
    }

    @Operation(summary = "Find a accesorie by ID", description = "Search accesorie by the id", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
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

    @Operation(summary = "Create new product", description = "Create a new product", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "409", description = "This product already exists", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping(path = {"/save"}, /*consumes = "application/json",*/ produces = "application/json")
    public ResponseEntity<?> save(
            @Valid
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("taller") String taller,
            @RequestParam("files") List<MultipartFile> files
    )
            throws Conflict, BadRequest, IOException {

        try {
            return ResponseEntity.ok(service.save(files, name, price, cant, taller));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest(br.getMessage());
        }catch ( ConstraintViolationException valid){
            throw new ConstraintViolationException("Validation errors: ",valid.getConstraintViolations());
        }
    }

    @Operation(summary = "Update product", description = "Update product info", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping(path = {"/update/{id}"}, /*consumes = "application/json",*/ produces = "application/json")
    public ResponseEntity<?> update(
            @Valid
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("cant") int cant,
            @RequestParam("taller") String taller,
            @RequestParam("id") Long id
    ) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok(service.update(files, name, price, cant, taller, id));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }catch ( ConstraintViolationException valid){
            throw new ConstraintViolationException("Validation errors: ",valid.getConstraintViolations());
        }
    }

    @Operation(summary = "Delete a product by id", description = "Delete a product", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR')")
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

    @Operation(summary = "Delete products", description = "Delete list of products", tags = "product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))
        )}
    )
    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Product> products) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(products));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Product not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Get image by name",description = "Get image by name from the server",tags = "product")
    @GetMapping(path = "/image/{name}")
    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(minioAdapter.getFile(name));
    }

}
