package com.hei.course.service;

import com.hei.course.entity.JReservation;
import com.hei.course.repository.ReservationRepository;
import com.hei.course.security.UserPrincipal;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class TicketService {

  private static final DateTimeFormatter DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("Indian/Antananarivo"));

  private final ReservationRepository reservationRepository;

  public byte[] generateTicket(UUID reservationId, UserPrincipal currentUser) {
    JReservation reservation =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

    boolean isClient = "CLIENT".equals(currentUser.getUser().getUserRole().name());
    if (isClient && !reservation.getUser().getId().equals(currentUser.getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this ticket");
    }

    return buildPdf(reservation);
  }

  private byte[] buildPdf(JReservation reservation) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Document document = new Document(PageSize.A5);
      PdfWriter.getInstance(document, out);
      document.open();

      Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
      Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
      Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

      Paragraph title = new Paragraph("Ticket de cinéma", titleFont);
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);
      document.add(new Paragraph(" "));

      addLine(
          document,
          "Film :",
          reservation.getProjection().getMovie().getTitle(),
          labelFont,
          valueFont);
      addLine(
          document,
          "Séance :",
          DATETIME_FORMATTER.format(reservation.getProjection().getDatetime()),
          labelFont,
          valueFont);
      addLine(
          document,
          "Salle :",
          reservation.getProjection().getRoom().getNumber(),
          labelFont,
          valueFont);
      addLine(
          document,
          "Prix :",
          reservation.getProjection().getSeatPrice() + " Ar",
          labelFont,
          valueFont);
      addLine(
          document,
          "Client :",
          reservation.getUser().getFirstname() + " " + reservation.getUser().getLastname(),
          labelFont,
          valueFont);
      addLine(document, "Code réservation :", reservation.getId().toString(), labelFont, valueFont);

      document.close();
      return out.toByteArray();
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate ticket", e);
    }
  }

  private void addLine(
      Document document, String label, String value, Font labelFont, Font valueFont)
      throws com.lowagie.text.DocumentException {
    Paragraph p = new Paragraph();
    p.add(new com.lowagie.text.Chunk(label + " ", labelFont));
    p.add(new com.lowagie.text.Chunk(value, valueFont));
    document.add(p);
  }
}
