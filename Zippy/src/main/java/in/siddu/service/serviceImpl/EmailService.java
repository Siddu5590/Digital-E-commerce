package in.siddu.service.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import in.siddu.controller.CategoryController;
import in.siddu.entity.Order;
import in.siddu.entity.User;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final CategoryController categoryController;

    @Autowired
    private JavaMailSender mailSender;

    EmailService(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    public void sendEmail(User user, List<Order> orders) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create PDF
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Title
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Order Confirmation", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // User info
        document.add(new Paragraph("User Name: " + user.getName()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Email: " + user.getPhone()));
        document.add(new Paragraph(" "));

        // Order table
        PdfPTable table = new PdfPTable(5); // 5 columns
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 2, 2, 3});

        addTableHeader(table);
        double grandTotal = 0;

        for (Order order : orders) {
            table.addCell(String.valueOf(order.getId()));
            table.addCell(order.getProduct().getPname());
            table.addCell(String.valueOf(order.getQuantity()));
            table.addCell("₹" + order.getTotalPrice());
            table.addCell(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            grandTotal += order.getTotalPrice();
        }

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Price: ₹" + grandTotal, boldFont));

        document.close();

        // Send Email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Your Order Confirmation");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Dear ").append(user.getName()).append(",<br><br>");
        emailContent.append("Your order has been placed successfully!<br>");
        emailContent.append("Please find the attached PDF for full details.<br><br>");
        emailContent.append("<strong>Total Orders:</strong> ").append(orders.size()).append("<br>");
        emailContent.append("<strong>Total Amount:</strong> ₹").append(grandTotal).append("<br><br>");
        emailContent.append("Thank you for shopping with us!<br>");
        emailContent.append("ZippyGo Team");

        helper.setText(emailContent.toString(), true);
        helper.addAttachment("OrderConfirmation.pdf", new ByteArrayResource(baos.toByteArray()));

        mailSender.send(message);
        
        System.out.println("mail send successfully....");
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Order ID", "Product", "Quantity", "Price", "Date")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setPhrase(new Phrase(header));
                    table.addCell(cell);
                });
    }
}
