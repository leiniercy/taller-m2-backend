package tallerM2.tallerM2.services;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.model.Sell;
import tallerM2.tallerM2.utils.dto.SellRequest;

import java.time.LocalDate;
import java.util.List;

public interface ISellService {
    public Sell findById(Long id) throws ValueNotFound, BadRequest;

    public List<Sell> findAll();

    public List<Sell> findAllOrderByIdAsc();

    public List<Sell> findAllByDate(LocalDate sellDate, String taller);

    public List<Sell> save(SellRequest sellRequest) throws Conflict, BadRequest;

    public Sell delete(Sell sell) throws ValueNotFound, BadRequest;

    public Sell deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Sell> deleteAll(List<Sell> sales)throws ValueNotFound, BadRequest;

    public long count();
}
