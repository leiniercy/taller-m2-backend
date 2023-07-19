package tallerM2.tallerM2.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Movile;

public interface IMovileService {

    public Movile findById(Long id) throws ValueNotFound, BadRequest;

    public List<Movile> findAll();

    public List<Movile> findAllByOrderByIdAsc();

    public List<Movile> findAllByOrderByPriceAsc();

    public Movile save(Movile m) throws Conflict, BadRequest;

    public Movile update(Movile from, Long id) throws ValueNotFound, BadRequest;

    public Movile delete(Movile m) throws ValueNotFound, BadRequest;

    public Movile deleteById(Long id) throws ValueNotFound, BadRequest;

    public void deleteAll(List<Movile> moviles);

    public long count();

    public Long countByName(String name);

}
