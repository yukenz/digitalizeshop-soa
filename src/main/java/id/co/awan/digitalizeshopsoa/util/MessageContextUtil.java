package id.co.awan.digitalizeshopsoa.util;

import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class MessageContextUtil {

    public static String getBearerToken(String headerName, MessageContext messageContext) throws UnauthorizedSoapException {
        SaajSoapMessage saaj = (SaajSoapMessage) messageContext.getRequest();
        SOAPMessage saajMessage = saaj.getSaajMessage();
        MimeHeaders mimeHeaders = saajMessage.getMimeHeaders();

        String bearerText = mimeHeaders.getHeader(headerName)[0];

        // Authorization header must be present
        if (bearerText == null) {
            throw new UnauthorizedSoapException("Authorization Header is not found");
        }

        // Schema must bearer
        String schemaBearer = null;
        try {
            schemaBearer = bearerText.substring(0, 6);
            if (!schemaBearer.equals("Bearer")) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedSoapException("Authorization Header is not Bearer");
        }

        // Access Token must Not Null
        String accessToken;
        try {

            accessToken = bearerText.substring(7);

            // Validate Access Token
            return accessToken;
        } catch (Exception e) {
            throw new UnauthorizedSoapException("Invalid Access Token");
        }

    }

}
