package jumpstart;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.CertificateUtil;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.ITSAClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collection;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Ds_beginner {
	
    public static final String DESTINATION = "F:\\Work\\eclipse\\pdf\\test\\";

    public static final String SRC = "F:\\Work\\eclipse\\pdf\\test\\test01.pdf";

    public static final String[] RESULT_FILES = new String[] {
            "test03.pdf"
    };

    public static void sign(
    		String src, 
    		String dest, 
    		Certificate[] chain, 
    		PrivateKey pk,
            String digestAlgorithm, 
            String provider, 
            PdfSigner.CryptoStandard subfilter,
            String reason, 
            String location, 
            Collection<ICrlClient> crlList,
            IOcspClient ocspClient, 
            ITSAClient tsaClient, 
            int estimatedSize) throws GeneralSecurityException, IOException {
        
    	PdfReader reader = new PdfReader(src);
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

        // Create the signature appearance
        Rectangle rect = new Rectangle(36, 648, 200, 100);
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance
                .setReason(reason)
                .setLocation(location)
                // Specify if the appearance before field is signed will be used
                // as a background for the signed field. The "false" value is the default value.
                .setReuseAppearance(false)
                .setPageRect(rect)
                .setPageNumber(1);
        signer.setFieldName("not prayuth");
        
        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        signer.signDetached(digest, pks, chain, crlList, ocspClient, tsaClient, estimatedSize, subfilter);
    }

    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, GeneralSecurityException {
        File file = new File(DESTINATION);
        file.mkdirs();

        //# Properties properties = new Properties();

        /* This properties file should contain a CAcert certificate that belongs to the user,
         * according to the original sample purpose. However right now it contains a simple
         * self-signed certificate in p12 format, which serves as a stub.
         */
       //# properties.load(new FileInputStream("F:\\Work\\sskey\\cert.pfx"));

        // Get path to the p12 file
       //# String path = properties.getProperty("PRIVATE");

        // Get a password & certificate
        String path = "F:\\Work\\sskey\\Hun.p12"  ;
        char[] pass = "P@ssw0rd".toCharArray();
        
        // -- Main Code --
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        // The first argument defines that the keys and certificates are stored using PKCS#12
        KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
        ks.load(new FileInputStream(path), pass);
        String aliase = ks.aliases().nextElement();
        // -- End Main Code --
        PrivateKey pk = (PrivateKey) ks.getKey(aliase, pass);
        Certificate[] chain = ks.getCertificateChain(aliase);
        for (int i = 0; i < chain.length; i++) {
        	 X509Certificate cert = (X509Certificate)chain[i];
        	 System.out.println(String.format("[%s] %s", i, cert.getSubjectDN()));
        	 System.out.println(CertificateUtil.getCRLURL(cert));
        	}


        sign(SRC, DESTINATION + RESULT_FILES[0], chain, pk,
                DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
                "Test", "Ghent", null, null, null, 0);
    }
}

