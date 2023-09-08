package tallerM2.tallerM2.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Sell;
import tallerM2.tallerM2.services.servicesImpl.SellService;
import tallerM2.tallerM2.utils.dto.SellMonthRequest;
import tallerM2.tallerM2.utils.dto.SellRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(value = "/api/v1/sell")
@CrossOrigin("*")
@Tag(name = "sell", description = "The movile API")
public class SellController {

    @Autowired
    SellService service;

    @Operation(summary = "Find all sales", description = "Find all sales", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all sales order by id", description = "Find all sales order by id", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allOrderById() {
        return ResponseEntity.ok(service.findAllOrderByIdAsc());
    }

    @Operation(summary = "Find all sales by date", description = "Find all sales by date order by id", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = {"/all/date/{sellDate}/{taller}"}, produces = "application/json")
    ResponseEntity<?> allByDate(
            @PathVariable(value = "sellDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sellDate,
            @PathVariable(value = "taller") String taller
    ) throws BadRequest {
        try {
            List<Sell> ventas = service.findAllByDate(sellDate, taller);
            return ResponseEntity.ok(ventas);
        } catch (Exception ex) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Find all sales by week", description = "Find all sales by week", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = {"/all/date/week/{taller}"}, produces = "application/json")
    ResponseEntity<?> allByWeek(@PathVariable(value = "taller") String taller) throws BadRequest {
        try {
            return ResponseEntity.ok(service.getSalesByCurrentWeek(taller));
        } catch (Exception ex) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Find all sales by month", description = "Find all sales by month", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = {"/all/date/month/{taller}"}, produces = "application/json")
    ResponseEntity<?> allByMonth(@PathVariable(value = "taller") String taller) throws BadRequest {
        try {
            List<Long> list = new LinkedList<>();
            for (int i = 0; i < 12; i++) {
                list.add(service.countSellByMonth(i + 1, taller));
            }
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Find all sales by month and product", description = "Find all sales by month and product", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = {"/all/date/month/product/{taller}"}, produces = "application/json")
    ResponseEntity<?> allByMonthAndProduct(@PathVariable(value = "taller") String taller) throws BadRequest {
        try {
            SellMonthRequest sellMonthRequest = new SellMonthRequest();
            List<Long> accesorios = new LinkedList<>();
            for (int i = 0; i < 12; i++) {
                accesorios.add(service.countSellByMonthAndAccesorio(i + 1, taller));
            }
            sellMonthRequest.setAccesorios(accesorios);
            List<Long> charges = new LinkedList<>();
            for (int i = 0; i < 12; i++) {
                charges.add(service.countSellByMonthAndCharger(i + 1, taller));
            }
            sellMonthRequest.setChargers(charges);
            List<Long> moviles = new LinkedList<>();
            for (int i = 0; i < 12; i++) {
                moviles.add(service.countSellByMonthAndMovile(i + 1, taller));
            }
            sellMonthRequest.setMoviles(moviles);
            List<Long> relojes = new LinkedList<>();
            for (int i = 0; i < 12; i++) {
                relojes.add(service.countSellByMonthAndReloj(i + 1, taller));
            }
            sellMonthRequest.setRelojes(relojes);
            return ResponseEntity.ok(sellMonthRequest);
        } catch (Exception ex) {
            throw new BadRequest("Bad request: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get daily report", description = "Get daily report", tags = "sell")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping(value = "/pdf/diario/{taller}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generarPDFReporteDiario(
            HttpServletResponse response,
            @PathVariable(value = "taller") String taller,
            @RequestBody List<Sell> sales
    ) throws IOException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Creamos el documento PDF
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        // Abrir el documento PDF
        document.open();

        Paragraph header = new Paragraph(taller + "\nReporte de diario\n\n", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(reportSaleTable(sales));
        //Cerramos el documento PDF
        document.close();
        response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        return ResponseEntity.ok(baos.toByteArray());
    }

    //PDF Reporte Diario
    private PdfPTable reportSaleTable(List<Sell> sales) {
        PdfPTable table = new PdfPTable(new float[]{2, 2, 1, 1, 2, 1});
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Paragraph("Nombre del cliente"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Producto"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Cantidad"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Usuario"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Descripción"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Precio de venta"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        Collections.sort(sales, new Comparator<>() {
            @Override
            public int compare(Sell o1, Sell o2) {
                return new CompareToBuilder()
                        .append(o1.getUsername(), o2.getUsername())
                        .append(o1.getCustomer().getCustomerName(), o2.getCustomer().getCustomerName())
                        .append(o1.getProduct().getName(), o2.getProduct().getName())
                        .toComparison();
            }
        });

        int total = 0;
        for (Sell sell : sales) {
            //Nombre del cliente
            cell = new PdfPCell(new Paragraph(sell.getCustomer().getCustomerName()));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            //Nombre del producto
            cell = new PdfPCell(new Paragraph(sell.getProduct().getName()));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            //Cantidad de producto
            cell = new PdfPCell(new Paragraph(Integer.toString(sell.getCantProduct())));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            //Nombre de usuario
            cell = new PdfPCell(new Paragraph(sell.getUsername()));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            //Descripcion
            cell = new PdfPCell(new Paragraph(sell.getDescription()));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            //Precio de venta
            cell = new PdfPCell(new Paragraph(Integer.toString(sell.getSalePrice())));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            total += sell.getSalePrice();
        }

        // Agregar una fila con la suma total
        cell = new PdfPCell(new Paragraph("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.setColspan(5);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(Integer.toString(total)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        return table;
    }

    @Operation(summary = "Return info of sale", description = "Return info of sale", tags = "sell")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping(value = "/pdf/venta/{taller}/{name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generarPDfVenta(
            HttpServletResponse response,
            @PathVariable(value = "taller") String taller,
            @PathVariable(value = "name") String name,
            @RequestBody List<Sell> sales
    ) throws IOException, DocumentException, ParseException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Creamos el documento PDF
        Rectangle carta = new Rectangle(612, 792); // Tamaño carta en puntos (72 puntos por pulgada)
        Document document = new Document(carta, 72, 72, 72, 72); // Márgenes de 1 pulgada en todos los lados (72 puntos por pulgada)
        PdfWriter.getInstance(document, baos);
        // Abrir el documento PDF
        document.open();
        String tallerName = "";
        if (taller.equals("Taller 2M")) {
            tallerName = "Taller T2";
        } else {
            tallerName = "Taller TM";
        }
        // Encabezado del recibo
        Paragraph header = new Paragraph(tallerName + "\n", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        //Fecha y hora        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = dateFormat.format(new Date());

        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateOnlyFormat.format(dateFormat.parse(dateStr));

        SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm");
        String time = timeOnlyFormat.format(dateFormat.parse(dateStr));

        Paragraph customerInfo = new Paragraph("Usuario: " + name + "\nCliente: " + sales.get(0).getCustomer().getCustomerName() + "\nFecha: " + date + "  Hora: " + time + "\n\n", new Font(Font.FontFamily.HELVETICA, 12));
        customerInfo.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(customerInfo);

        // Tabla de detalles de venta
        document.add(reporteVenta(sales));
        //Importe de compra
        double total = sales.stream().mapToDouble(Sell::getSalePrice).sum();
        Paragraph totalInfo = new Paragraph("\n\nSALDO TOTAL: $" + String.format("%.2f", total), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        totalInfo.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalInfo);
        //Garantia
        Paragraph garantia = new Paragraph("Garantía: 7 días \nNota: La garantía no incluye daños causados como: golpes, caídas, que se moje, etc.", new Font(Font.FontFamily.HELVETICA, 12));
        garantia.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(garantia);
        //Saludo 
        Paragraph saludo = new Paragraph("\n\nCOPIA DEL CLIENTE\nGracias por su visita", new Font(Font.FontFamily.HELVETICA, 10));
        saludo.setAlignment(Element.ALIGN_CENTER);
        document.add(saludo);
        //Cerramos el documento PDF
        document.close();
        response.setHeader("Content-Disposition", "attachment; filename=venta.pdf");
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        return ResponseEntity.ok(baos.toByteArray());
    }

    private PdfPTable reporteVenta(List<Sell> sales) throws DocumentException {
        // Tabla de detalles de venta
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 1, 1});
        PdfPCell cell = new PdfPCell(new Phrase("Servicio"));
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);
        //Cantidad
        cell = new PdfPCell(new Phrase("Cantidad"));
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        //Precio
        cell = new PdfPCell(new Phrase("Precio"));
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        for (Sell sell : sales) {
            //Nombre del producto
            cell = new PdfPCell(new Phrase(String.valueOf(sell.getProduct().getName())));
            cell.setPadding(5);
            cell.setBorderColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(cell);

            //Nombre del producto
            cell = new PdfPCell(new Phrase(String.valueOf(sell.getCantProduct())));
            cell.setBorderColor(BaseColor.WHITE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            //Nombre del producto
            cell = new PdfPCell(new Phrase(String.valueOf(sell.getSalePrice())));
            cell.setBorderColor(BaseColor.WHITE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

        }

        return table;
    }

    @Operation(summary = "Find a sell by ID", description = "Search sell by the id", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
            @ApiResponse(responseCode = "404", description = "Sell not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping(value = "/get/{id}", produces = "application/json")
    public ResponseEntity<?> byId(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Create new sell", description = "Create a new sell", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "409", description = "This sell already exists", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping(path = {"/save"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> save(@RequestBody SellRequest sellRequest) throws Conflict, BadRequest {
        try {
            return ResponseEntity.ok(service.save(sellRequest));
        } catch (Conflict conflict) {
            throw new Conflict(conflict.getMessage());
        } catch (BadRequest badRequest) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Delete a sale by id", description = "Delete a sale", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "404", description = "Sell not found", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteById(id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(summary = "Delete sales", description = "Delete list of sales", tags = "sell")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
            @ApiResponse(responseCode = "404", description = "Sell not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))
            )}
    )
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteAll", produces = "application/json")
    public ResponseEntity<?> deleteAll(@RequestBody List<Sell> sales) throws ValueNotFound, BadRequest {
        try {
            return ResponseEntity.ok(service.deleteAll(sales));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound("Sell not found");
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

}
