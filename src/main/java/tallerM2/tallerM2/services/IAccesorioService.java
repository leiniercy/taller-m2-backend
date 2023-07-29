package tallerM2.tallerM2.services;


import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Accesorio;

import java.io.IOException;
import java.util.List;

public interface IAccesorioService {

    public Accesorio findById(Long id) throws ValueNotFound, BadRequest;

    public List<Accesorio> findAll();

    public List<Accesorio> findAllByOrderByIdAsc();

    public Accesorio save(List<MultipartFile> files, String name, int price, int cant) throws Conflict, BadRequest,IOException;

    public Accesorio update(List<MultipartFile> files, String name, int price, int cant, Long id) throws ValueNotFound, BadRequest, IOException;

    public Accesorio delete(Accesorio p) throws ValueNotFound, BadRequest;

    public Accesorio deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Accesorio> deleteAll(List<Accesorio> products) throws ValueNotFound, BadRequest;

    public long count();
}
