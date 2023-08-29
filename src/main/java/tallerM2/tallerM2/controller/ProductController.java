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
import java.io.IOException;
import java.util.List;
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
    @GetMapping(path = {"/all"}, produces = "application/json")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Find all products, Taller 2M", description = "Find all products, Taller 2M", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/products/2M"}, produces = "application/json")
    public ResponseEntity<?> allProducts2M() {
        return ResponseEntity.ok(service.findAllProductsTaller2M());
    }
    @Operation(summary = "Find all products, Taller MJ", description = "Find all products Taller MJ", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/products/MJ"}, produces = "application/json")
    public ResponseEntity<?> allProductsMJ() {
        return ResponseEntity.ok(service.findAllProductsTallerMJ());
    }

    @Operation(summary = "Find all accesories, Taller 2M", description = "Find all accesories, Taller 2M", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/accesorios/2M"}, produces = "application/json")
    public ResponseEntity<?> allAccesorios2M() {
        return ResponseEntity.ok(service.findAllAccesoriosTaller2M());
    }
    @Operation(summary = "Find all accesories, Taller MJ", description = "Find all accesories Taller MJ", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/accesorios/MJ"}, produces = "application/json")
    public ResponseEntity<?> allAccesoriosMJ() {
        return ResponseEntity.ok(service.findAllAccesoriosTallerMJ());
    }

    @Operation(summary = "Find all than 0", description = "Find all product where cant > 0", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/product/cant/{taller}"}, produces = "application/json")
    public ResponseEntity<?> allProductsThanCero(@PathVariable(value = "taller") String taller ){
        return ResponseEntity.ok(service.findAllCantThanCero(taller));
    }

    @Operation(summary = "Find all accesories", description = "Find all accesories", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/accesorio"}, produces = "application/json")
    public ResponseEntity<?> allAccesorios() {
        return ResponseEntity.ok(service.findAllAccesorios());
    }

    @Operation(summary = "Cuenta la cant de accesorios", description = "count accesorios", tags = "product")
    @GetMapping(value = "/getCant")
    public ResponseEntity<?> getCant()  {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Find a accesorie by ID", description = "Search accesorie by the id", tags = "product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
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
            summary = "Create new product",
            description = "Create a new product",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
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
            @RequestParam("taller") String taller,
            @RequestParam("files") List<MultipartFile> files
    )
            throws Conflict, BadRequest, IOException {

        try {
            return ResponseEntity.ok(service.save(files,name, price, cant,taller));
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest(br.getMessage());
        }
    }

    @Operation(
            summary = "Update product",
            description = "Update product info",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
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
            @RequestParam("taller") String taller,
            @RequestParam("id") Long id
    ) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok( service.update(files,name, price, cant,taller, id) );
        } catch (IOException ex) {
            throw new BadRequest("Error loading file");
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a product by id",
            description = "Delete a product",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
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
                            description = "Product not found",
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
            summary = "Delete products",
            description = "Delete list of products",
            tags = "product"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
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
                            description = "Product not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorObject.class)
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Get image by name",
            description = "Get image by name from the server",
            tags = "product"
    )
    @GetMapping(path = "/image/{name}")
    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(minioAdapter.getFile(name));
    }
    
}