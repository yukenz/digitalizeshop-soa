package id.co.awan.digitalizeshopsoa.util;

import id.co.awan.digitalizeshopsoa.aop.IdentifyAndAuthorizeAOP;
import id.co.awan.digitalizeshopsoa.config.VariableConfig;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPMessage;
import org.springframework.lang.NonNull;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

/**
 * Utility Class for handling MessageContext
 */
public class MessageContextUtil {

    /**
     * Get Token based on Header's Bearer schema
     *
     * @param headerName     Name of Header
     * @param messageContext MessageContext from DI Endpoint
     * @return accessToken, without Bearer prefix
     * @throws UnauthorizedSoapException if Authorization Header is not found, not Bearer, or Invalid Access Token
     */
    public static String getAccessTokenFromBearerHeader(
            @NonNull String headerName,
            @NonNull MessageContext messageContext
    ) throws UnauthorizedSoapException {
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


    /**
     * Get Principal, {@link MessageContext} must first processed by {@link IdentifyAndAuthorizeAOP}
     *
     * @param messageContext MessageContext from DI Endpoint
     * @return Principal, B2B2P claims from JWT with key {@link VariableConfig#B2B2P_CLAIM_PRINCIPAL}
     * @throws UnauthorizedSoapException if Principal isn't injected by {@link IdentifyAndAuthorizeAOP}
     * @throws ClassCastException        if Principal is not {@link String}
     */
    public static String getPrincipalFromMessageContext(MessageContext messageContext)
            throws UnauthorizedSoapException, ClassCastException {

        String principal = (String) messageContext.getProperty(VariableConfig.B2B2P_CLAIM_PRINCIPAL);

        if (principal == null) {
            throw new UnauthorizedSoapException("Principal is not found");
        }

        return principal;
    }

}
