package tallerM2.tallerM2.services;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Sell;

import java.time.LocalDate;
import java.util.List;

public interface ISellService {
    public Sell findById(Long id) throws ValueNotFound, BadRequest;

    public List<Sell> findAll();

    public List<Sell> findAllOrderByIdAsc();

    public Sell save(Sell sell) throws Conflict, BadRequest;

    public Sell update(Sell sell, Long id) throws ValueNotFound, BadRequest;

    public Sell delete(Sell sell) throws ValueNotFound, BadRequest;

    public Sell deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Sell> deleteAll(List<Sell> sales)throws ValueNotFound, BadRequest;

    public long count();
}
