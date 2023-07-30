package tallerM2.tallerM2.services;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.model.Movile;

import java.util.List;

public interface IFileService {
    public File findById(Long id) throws ValueNotFound, BadRequest;
    public List<File> findAll();
    public File save(File file);
    public File update(File from);
    public void deleteById(Long id) throws ValueNotFound, BadRequest;
    public void deleteAll(List<File> files) throws ValueNotFound, BadRequest;
}
