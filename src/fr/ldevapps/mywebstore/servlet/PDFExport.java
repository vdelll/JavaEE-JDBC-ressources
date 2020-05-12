package fr.ldevapps.mywebstore.servlet;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import fr.ldevapps.mywebstore.model.Article;
import fr.ldevapps.mywebstore.model.ShoppingCartLine;

@WebServlet("/pdfexport")
public class PDFExport extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Sinulation de données stockées en base
		int idCommand = 0;
		try {
			idCommand = Integer.parseInt(request.getParameter("idCommand"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		ShoppingCartLine [] lines = {
				new ShoppingCartLine(new Article(1, "Stylo bille", "Bic", 0.2), 5),
				new ShoppingCartLine(new Article(18, "Bonbon", "Haribo", 0.1), 50),
		};
		
		// Accès au fichier
		String masterPath = request.getServletContext().getRealPath("/WEB-INF/FacMaster.pdf");
		
		// Type MIME du document PDF
		response.setContentType("application/pdf");

		try (PdfReader reader = new PdfReader(masterPath);
				PdfWriter writer = new PdfWriter(response.getOutputStream());
				PdfDocument document = new PdfDocument(reader, writer)) {
			
			PdfPage page = document.getPage(1);
			
			// Va permettre d'écrire sur la page
			PdfCanvas canvas = new PdfCanvas(page);
			
			// Création de la police de caractère
			FontProgram fontProgram = FontProgramFactory.createFont();
			PdfFont font = PdfFontFactory.createFont(fontProgram, "UTF-8", true);
			canvas.setFontAndSize(font, 12);
			
			canvas.beginText();
			
			canvas.setTextMatrix(0, 0);
			canvas.showText("Origine");
			
			// Numéro de commande
			canvas.setTextMatrix(470, 775);
			canvas.showText("" + idCommand);
			
			// Affichage des commandes
			int top = 520;
			double totalPrice = 0;
			NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("fr", "FR"));
			for (ShoppingCartLine line : lines) {
				
				Article article = line.getArticle();
				
				canvas.setTextMatrix(60, top);
				canvas.showText("" + article.getIdArticle());
				
				canvas.setTextMatrix(130, top);
				canvas.showText(article.getDescription());
				
				canvas.setTextMatrix(270, top);
				canvas.showText(article.getBrand());
				
				canvas.setTextMatrix(350, top);
				canvas.showText(formatter.format(article.getUnitaryPrice()));
				
				canvas.setTextMatrix(435, top);
				canvas.showText("" + line.getQuantity());
				
				double totalLine = article.getUnitaryPrice() * line.getQuantity();
				totalPrice += totalLine;
				canvas.setTextMatrix(493, top);
				canvas.showText(formatter.format(totalLine));
				
				top -= 20;
				
			}
			
			// Affichage du montant total
			canvas.setTextMatrix(482, 180);
			canvas.showText(formatter.format(totalPrice));
			
			canvas.setTextMatrix(482, 152);
			canvas.showText(formatter.format(totalPrice * 0.2));
			
			canvas.setTextMatrix(482, 124);
			canvas.showText(formatter.format(totalPrice * 1.2));
			
			canvas.endText();

		}

	}

}
