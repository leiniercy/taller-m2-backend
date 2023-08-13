package tallerM2.tallerM2.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
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
import tallerM2.tallerM2.utils.dto.SellRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

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
    @GetMapping(path = {"/all"}, produces = "application/json")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find all sales order by id", description = "Find all sales order by id", tags = "sell")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all-sorted"}, produces = "application/json")
    ResponseEntity<?> allOrderById() {
        return ResponseEntity.ok(service.findAllOrderByIdAsc());
    }

    @Operation(summary = "Find all sales by date", description = "Find all sales by date order by id", tags = "sell")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sell.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    @GetMapping(path = {"/all/date/{sellDate}"}, produces = "application/json")
    ResponseEntity<?> allByDate(
            @PathVariable(value = "sellDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sellDate
    ) throws BadRequest {
        try {
            List<Sell> ventas = service.findAllByDate(sellDate);
            return ResponseEntity.ok(ventas);
        } catch (Exception ex) {
            throw new BadRequest("Bad request");
        }
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generarPDf(
            HttpServletResponse response,
            @RequestBody List<Sell> sales
    ) throws IOException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Creamos el documento PDF
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        // Abrir el documento PDF
        document.open();
        Paragraph header = new Paragraph("Reporte de diario\n\n", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
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

    private PdfPTable reportSaleTable(List<Sell> sales) {
        PdfPTable table = new PdfPTable(new float[]{2, 1, 2, 1});
        table.setWidthPercentage(100);
        // Agregar encabezados de columna a la tabla
        // Agregar encabezados de columna a la tabla con estilo
        PdfPCell cell = new PdfPCell(new Paragraph("Nombre del cliente"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Producto"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Descripción"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        PdfPCell cellSale = new PdfPCell(new Paragraph("Precio de venta"));
        cellSale.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellSale);

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

    @Operation(summary = "Find a sell by ID", description = "Search sell by the id", tags = "sell")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Sell.class))),
        @ApiResponse(responseCode = "404", description = "Sell not found", content = @Content(schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
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

    @Operation(
            summary = "Create new sell",
            description = "Create a new sell",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = @Content(
                                schema = @Schema(implementation = Sell.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "This sell already exists",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                )
            }
    )
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

    @Operation(
            summary = "Update sell",
            description = "Update sell info",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = @Content(
                                schema = @Schema(implementation = Sell.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sell not found",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                )
            }
    )
    @PutMapping(path = {"/update/{id}"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@RequestBody Sell sell, @RequestParam("id") Long id) throws ValueNotFound, BadRequest, IOException {

        try {
            return ResponseEntity.ok(service.update(sell, id));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        } catch (BadRequest br) {
            throw new BadRequest("Bad request");
        }
    }

    @Operation(
            summary = "Delete a sell by id",
            description = "Delete a sell",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = @Content(
                                schema = @Schema(implementation = Sell.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sell not found",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                )
            }
    )
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

    @Operation(
            summary = "Delete sales",
            description = "Delete list of sales",
            tags = "sell"
    )
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "successful operation",
                        content = @Content(
                                schema = @Schema(implementation = Sell.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sell not found",
                        content = @Content(
                                schema = @Schema(implementation = ErrorObject.class)
                        )
                )
            }
    )
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
