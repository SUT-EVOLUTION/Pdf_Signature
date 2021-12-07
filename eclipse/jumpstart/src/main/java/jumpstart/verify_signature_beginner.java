package jumpstart;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.List;

public class verify_signature_beginner {

	public static final String DESTINATION = "F:\\Work\\eclipse\\pdf\\test\\";

    public static final String EX = "F:\\Work\\sskey\\signed_2021-10-04_00_04.pdf";
    
    public void verifySignatures(String path) throws IOException, GeneralSecurityException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(path));
        SignatureUtil signUtil = new SignatureUtil(pdfDoc);
        List<String> names = signUtil.getSignatureNames();

        System.out.println(path);
        for (String name : names) {
            System.out.println("===== " + name + " =====");
            verifySignature(signUtil, name);
        }

        pdfDoc.close();
    }
    
    public PdfPKCS7 verifySignature(SignatureUtil signUtil, String name) throws GeneralSecurityException {
        PdfPKCS7 pkcs7 = signUtil.readSignatureData(name);

        System.out.println("Signature covers whole document: " + signUtil.signatureCoversWholeDocument(name));
        System.out.println("Document revision: " + signUtil.getRevision(name) + " of " + signUtil.getTotalRevisions());
        System.out.println("Integrity check OK? " + pkcs7.verifySignatureIntegrityAndAuthenticity());

        return pkcs7;
    }

	public static void main(String[] args) throws IOException, GeneralSecurityException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        verify_signature_beginner app = new verify_signature_beginner() ;
        app.verifySignatures(EX);
    }

}
