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
import tallerM2.tallerM2.model.User;
import tallerM2.tallerM2.services.servicesImpl.UserService;

import java.util.List;

import tallerM2.tallerM2.utils.dto.UserEditRequest;
import tallerM2.tallerM2.utils.dto.UserSaveRequest;

@RestController
@RequestMapping(value = "/api/v1/user")
//@PreAuthorize("authenticated")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "user", description = "The movile API")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Find all users", description = "Find all users", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all users sorted by id", description = "Find all users", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/sorted"}, produces = "application/json")
    ResponseEntity<?> allSorted() {
        return ResponseEntity.ok(service.findAllOrderByIdAsc());
    }

    @Operation(summary = "Find user by ID", description = "Search user by the id", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
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

    @Operation(summary = "Find user by username", description = "Search user by the username", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(value = "/get/name/{username}", produces = "application/json")
    public ResponseEntity<?> byUsername(@PathVariable(value = "username") String username) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.findByUsername(username));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Create new user", description = "Create a new user", tags = "user")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class)))
                    , @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
                    , @ApiResponse(responseCode = "409", description = "This User already exists", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
            }
    )
    @PostMapping(path = {"/save"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> save(
            @RequestBody UserSaveRequest userRequest
    ) throws Conflict, BadRequest, ValueNotFound {
        try {
            return ResponseEntity.ok(service.save(userRequest));
        } catch (Conflict c) {
            throw new Conflict(c.getMessage());
        }catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        }catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Update user", description = "Update user info", tags = "user")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class)))
                    , @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
                    , @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
            }
    )
    @PutMapping(path = {"/update/{id}"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@RequestBody UserEditRequest userRequest,
                                    @PathVariable(value = "id") Long id)
            throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.update(userRequest, id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Update user", description = "Update user info", tags = "user")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class)))
                    , @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
                    , @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
            }
    )
    @PutMapping(path = {"/change/password/{id}/{username}"}, produces = "application/json")
    public ResponseEntity<?> changePassword(@PathVariable(value = "id") Long id, @PathVariable(value = "username") String username)
            throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.changePassword(id,username));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Delete a user", description = "Delete a user", tags = "user")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
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

    @Operation(summary = "Delete users", description = "Delete list of users", tags = "user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
            }
    )
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<User> users) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(users));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("User not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }


}
