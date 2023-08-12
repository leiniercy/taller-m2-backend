package tallerM2.tallerM2.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

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


    @GetMapping(path = "/pdf")
    public void generarPDf(HttpServletResponse response) throws IOException, DocumentException {
        //Configuramos la respuesta para que se descargue el archivo PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");

        // Creamos el documento PDF
        Document document = new Document(PageSize.A4);
        // Crear un escritor para el documento PDF
        PdfWriter.getInstance(document, response.getOutputStream());

        // Abrir el documento PDF
        document.open();
        Paragraph header = new Paragraph("Reporte de ventas\n\n", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(reportSaleTable());
        //Cerramos el documento PDF
        document.close();

    }
    private PdfPTable reportSaleTable() {
        PdfPTable table = new PdfPTable(new float[]{2, 1, 2, 1});
        table.setWidthPercentage(100);
        // Agregar encabezados de columna a la tabla
        // Agregar encabezados de columna a la tabla con estilo
        PdfPCell cell = new PdfPCell(new Paragraph("Nombre del cliente"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Telefóno"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Producto"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        PdfPCell cellSale = new PdfPCell(new Paragraph("Precio de venta"));
        cellSale.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellSale);

        int total=0;
//        for (Sell sell : sellService.findAll()) {
//            for (Product product : sell.getProducts()) {
//                //Nombre del cliente
//                cell = new PdfPCell(new Paragraph(sell.getCustomer().getCustomerName()));
//                cell.setPadding(5);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//                //Movil del cliente
//                cell = new PdfPCell(new Paragraph(sell.getCustomer().getCustomerMovile()));
//                cell.setPadding(5);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//                //Nombre del producto
//                cell = new PdfPCell(new Paragraph(product.getName()));
//                cell.setPadding(5);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//                //Precio de venta
//                cell = new PdfPCell(new Paragraph(Integer.toString(product.getPrice())));
//                cell.setPadding(5);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                total+=product.getPrice();
//            }
//        }

        // Agregar una fila con la suma total
        cell = new PdfPCell(new Paragraph("Total",new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD) ));
        cell.setColspan(3);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(Integer.toString(total)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        return table;
    }

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
