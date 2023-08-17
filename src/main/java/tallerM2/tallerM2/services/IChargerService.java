package tallerM2.tallerM2.services;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Charger;

import java.io.IOException;
import java.util.List;

public interface IChargerService {

    public Charger findById(Long id) throws ValueNotFound, BadRequest;

    public List<Charger> findAll();

    public List<Charger> findAllByOrderByIdAsc();

    public Charger save(List<MultipartFile> files, String name, int price, int cant, String taller, String connectorType, String compatibleDevice) throws Conflict, BadRequest, IOException;

    public Charger update(List<MultipartFile> files, String name, int price, int cant, String taller, String connectorType, String compatibleDevice, Long id) throws ValueNotFound, BadRequest, IOException;

    public Charger delete(Charger charger) throws ValueNotFound, BadRequest;

    public Charger deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Charger> deleteAll(List<Charger> chargers) throws ValueNotFound, BadRequest;

    public long count();

}
