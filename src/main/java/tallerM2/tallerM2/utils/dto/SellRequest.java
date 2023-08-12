package tallerM2.tallerM2.utils.dto;

import lombok.Data;
import tallerM2.tallerM2.model.Customer;
import tallerM2.tallerM2.model.Product;

import java.time.LocalDate;
import java.util.List;

@Data
public class SellRequest {

    List<String> descriptions;
    List<Integer> prices;
    Customer customer;
    String tallerName;
    LocalDate date;
    List<Product> products;
    List<Integer> quantities;

}
