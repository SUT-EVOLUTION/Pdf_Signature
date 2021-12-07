package jumpstart;

import java.io.FileNotFoundException;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class pharse {
	public static void main (String[] args) throws FileNotFoundException {
		CreatePDF();
	}
	
	public static void CreatePDF() throws FileNotFoundException {
		
		String pdfpath = "F:\\Work\\eclipse\\pdf\\test\\test01.pdf";
		String pdftext1 = "Peter Piper Pick A Peck Of Pickle Pepper";
		
		// Create Paragraph
		Paragraph paragraph_1 = new Paragraph(pdftext1);
		// Create PDF
		PdfWriter writer = new PdfWriter(pdfpath);
		
		PdfDocument pdfdoc = new PdfDocument(writer);
		pdfdoc.addNewPage();
		
		Document doc = new Document(pdfdoc);
		doc.add(paragraph_1);
		
		// End Create
		doc.close();
	}
}
