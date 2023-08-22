package tallerM2.tallerM2.utils.dto;

import lombok.Data;

import java.util.List;

@Data
public class SellMonthRequest {
    List<Long> accesorios;
    List<Long> chargers;
    List<Long> moviles;
    List<Long> relojes;
}
