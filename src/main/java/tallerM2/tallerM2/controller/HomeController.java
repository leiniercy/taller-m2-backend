package tallerM2.tallerM2.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.model.Sell;
import tallerM2.tallerM2.services.servicesImpl.SellService;

@RestController
@RequestMapping(value = "/api/v1/home")
@CrossOrigin("*")
@Tag(name = "home", description = "The movile API")
public class HomeController {

    @Autowired
    private SellService sellService;


//    @GetMapping("/cantidad-vendida")
//    public int getCantidadVendida(
//            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
//            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
//    ){
//        List<Sell> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
//        int[] arr = new int[ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1];
//        for (Venta venta : ventas) {
//            int index = (int) ChronoUnit.DAYS.between(fechaInicio, venta.getFecha());
//            arr[index] += venta.getCantidad();
//        }
//        SegmentTree segmentTree = new SegmentTree(arr);
//        return segmentTree.query(0, arr.length - 1);
//    }
//}

}
