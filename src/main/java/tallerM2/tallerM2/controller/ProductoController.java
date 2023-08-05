/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping(value = "/api/v1/product")
@CrossOrigin("*")
@Tag(name = "product", description = "The movile API")
public class ProductoController {

//    @Autowired
//    private ProductService service;
//    @Autowired
//    private ImageService imageService;
//
//    @Operation(summary = "Find all products", description = "Find all products", tags = "product")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
//        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
//    })
//    @GetMapping(path = {"/all"}, produces = "application/json")
//    public ResponseEntity<?> all() {
//        return ResponseEntity.ok(service.findAll());
//    }
//
//    @Operation(summary = "Find all products sorted by id", description = "Find all products", tags = "product")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
//        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
//    })
//    @GetMapping(path = {"/all-sorted-id"}, produces = "application/json")
//    ResponseEntity<?> allSorted() {
//        return ResponseEntity.ok(service.findAllByOrderByIdAsc());
//    }
//
//    @Operation(summary = "Find a product by ID", description = "Search product by the id", tags = "product")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
//        @ApiResponse(responseCode = "404", description = "product not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
//        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
//    })
//    @GetMapping(value = "/get/{id}", produces = "application/json")
//    public ResponseEntity<?> byId(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
//        try {
//            return ResponseEntity.ok(service.findById(id));
//        } catch (ValueNotFound vn) {
//            throw new ValueNotFound(vn.getMessage());
//        } catch (BadRequest br) {
//            throw new BadRequest("Bad request");
//        }
//    }
//
//    @Operation(
//            summary = "Create new product",
//            description = "Create a new product",
//            tags = "product"
//    )
//    @ApiResponses(
//            value = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "successful operation",
//                        content = @Content(
//                                schema = @Schema(implementation = Product.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "400",
//                        description = "Bad request",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "409",
//                        description = "This product already exists",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                )
//            }
//    )
//
//    @PostMapping(path = {"/save"}, /*consumes = "application/json",*/ produces = "application/json")
//    public ResponseEntity<?> save(
//            @RequestParam("name") String name,
//            @RequestParam("price") int price,
//            @RequestParam("cant") int cant,
//            @RequestParam("image") MultipartFile file
//    )
//            throws Conflict, BadRequest, IOException {
//
//        try {
//            Product pro = new Product();
//            pro.setName(name);
//            pro.setPrice(price);
//            pro.setCant(cant);
//            pro.setImage(imageService.guardarArchivo(file));
//            return ResponseEntity.ok(service.save(pro));
//        } catch (IOException ex) {
//            throw new BadRequest("Error loading file");
//        } catch (Conflict c) {
//            throw new Conflict(c.getMessage());
//        } catch (BadRequest br) {
//            throw new BadRequest(br.getMessage());
//        }
//    }
//
//    @Operation(
//            summary = "Update product",
//            description = "Update product info",
//            tags = "product"
//    )
//    @ApiResponses(
//            value = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "successful operation",
//                        content = @Content(
//                                schema = @Schema(implementation = Product.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "404",
//                        description = "Product not found",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "400",
//                        description = "Bad request",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                )
//            }
//    )
//    @PutMapping(path = {"/update/{id}"}, /*consumes = "application/json",*/ produces = "application/json")
//    public ResponseEntity<?> update(
//            @RequestParam("image") MultipartFile file,
//            @RequestParam("name") String name,
//            @RequestParam("price") int price,
//            @RequestParam("cant") int cant,
//            @RequestParam("id") Long id
//    ) throws ValueNotFound, BadRequest, IOException {
//
//        try {
//            Product pro = new Product();
//            pro.setName(name);
//            pro.setPrice(price);
//            pro.setCant(cant);
//            if (!file.getOriginalFilename().equals("emptyFile.png")) {
//                pro.setImage(imageService.guardarArchivo(file));
//            }else{
//                 pro.setImage(file.getOriginalFilename());
//            }
//            return ResponseEntity.ok(service.update(pro, id));
//        } catch (IOException ex) {
//            throw new BadRequest("Error loading file");
//        } catch (ValueNotFound vn) {
//            throw new ValueNotFound(vn.getMessage());
//        } catch (BadRequest br) {
//            throw new BadRequest("Bad request");
//        }
//    }
//
//    @Operation(
//            summary = "Delete a product by id",
//            description = "Delete a product",
//            tags = "product"
//    )
//    @ApiResponses(
//            value = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "successful operation",
//                        content = @Content(
//                                schema = @Schema(implementation = Product.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "400",
//                        description = "Bad request",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "404",
//                        description = "Product not found",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                )
//            }
//    )
//    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
//    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
//        try {
//            return ResponseEntity.ok(service.deleteById(id));
//        } catch (ValueNotFound vn) {
//            throw new ValueNotFound(vn.getMessage());
//        } catch (BadRequest br) {
//            throw new BadRequest("Bad request");
//        }
//    }
//
//    @Operation(
//            summary = "Delete products",
//            description = "Delete list of product",
//            tags = "product"
//    )
//    @ApiResponses(
//            value = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "successful operation",
//                        content = @Content(
//                                schema = @Schema(implementation = Product.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "400",
//                        description = "Bad request",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                ),
//                @ApiResponse(
//                        responseCode = "404",
//                        description = "Product not found",
//                        content = @Content(
//                                schema = @Schema(implementation = ErrorObject.class)
//                        )
//                )
//            }
//    )
//    @DeleteMapping(value = "/deleteAll", produces = "application/json")
//    public ResponseEntity<?> deleteAll(@RequestBody List<Product> products) throws ValueNotFound, BadRequest {
//        try {
//            return ResponseEntity.ok(service.deleteAll(products));
//        } catch (ValueNotFound vn) {
//            throw new ValueNotFound("Product not found");
//        } catch (BadRequest br) {
//            throw new BadRequest("Bad request");
//        }
//    }
//
//    @Operation(
//            summary = "Get image by name",
//            description = "Get image by name from the server",
//            tags = "product"
//    )
//    @GetMapping(path = "/image/{name}")
//    public ResponseEntity<?> obtenerImagen(@PathVariable(value = "name") String name) throws IOException {
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageService.obtenerImagen(name));
//    }

}
