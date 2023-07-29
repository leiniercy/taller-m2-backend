package tallerM2.tallerM2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.repository.FileRepository;
import tallerM2.tallerM2.services.servicesImpl.ImageService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/file")
@CrossOrigin("*")
@Tag(name = "file", description = "The movile API")
public class FileController {
    @Autowired
    FileRepository repository;
    @Autowired
    private ImageService imageService;

    @GetMapping(path = {"/all"}, produces = "application/json")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping(path = {"/save"}, /*consumes = "application/json",*/ produces = "application/json")
    public ResponseEntity<?> save(
            @RequestParam("files") List<MultipartFile> files
    )
            throws  IOException {

        try {

            for( MultipartFile file : files){
                File f = new File();
                f.setName(imageService.guardarArchivo(file));
                f.setUrl(imageService.getURLFolderPath() + f.getName());
                repository.save(f);
            }

            return ResponseEntity.ok("Guardados correctamente");
        } catch (IOException ex) {
            throw new IOException("Error loading file");
        }
    }


}
