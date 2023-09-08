package tallerM2.tallerM2.services;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Reloj;

import java.io.IOException;
import java.util.List;

public interface IRelojService {

    public Reloj findById(Long id) throws ValueNotFound, BadRequest;
    public List<Reloj> findAll(String taller);
    public List<Reloj> findAllByOrderByIdAsc();

    public Reloj save(List<MultipartFile> files, String name, int price, int cant, String taller, String specialFeature, String compatibleDevice, int bateryLife) throws Conflict, BadRequest, IOException;

    public Reloj update(List<MultipartFile> files, String name, int price, int cant, String taller, String specialFeature, String compatibleDevice,int bateryLife, Long id) throws ValueNotFound, BadRequest, IOException;

    public Reloj delete(Reloj reloj) throws ValueNotFound, BadRequest;

    public Reloj deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Reloj> deleteAll(List<Reloj> relojs) throws ValueNotFound, BadRequest;

    public long count(String taller);
}
