package id.co.awan.digitalizeshopsoa.endpoint.security;

import https.soa_digitalizeshop_id.ws.jwt.GetJWTB2B2PRequest;
import https.soa_digitalizeshop_id.ws.jwt.GetJWTB2BRequest;
import https.soa_digitalizeshop_id.ws.jwt.GetJWTResponse;
import id.co.awan.digitalizeshopsoa.database.first.repo.SellerModelRepo;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import id.co.awan.digitalizeshopsoa.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B_SECRET;

@Endpoint
@RequiredArgsConstructor
public class JWTEndpoint {

    // Never Edit Global
    public static final String NAMESPACE_URI = "https://soa.digitalizeshop.id/ws/jwt";
    public static final String PORT = "JWTPort";

    // Never Edit Per Service
    public static final String LOCAL_PART_B2B = "getJWTB2BRequest";
    public static final String LOCAL_PART_B2B2P = "getJWTB2B2PRequest";

    // Editable
    public static final int EXPIRED_IN_SECOND = 900;

    // CDI
    private final JWTService jwtService;
    private final SellerModelRepo sellerModelRepo;


    // JWT B2B
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_B2B)
    @ResponsePayload
    public GetJWTResponse getJWTB2BRequest(
            @RequestPayload GetJWTB2BRequest request
    )
            throws UnauthorizedSoapException {

        // Generate B2B Token
        String accessToken = jwtService.generateB2BToken(
                request.getBackendId(),
                EXPIRED_IN_SECOND);

        // Finalize Response
        GetJWTResponse response = new GetJWTResponse();
        response.setAccessToken(accessToken);
        response.setExpiresIn(EXPIRED_IN_SECOND);
        return response;
    }

    // JWT B2B2P
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_B2B2P)
    @ResponsePayload
    public GetJWTResponse getJWTB2B2PRequest(
            MessageContext messageContext,
            @RequestPayload GetJWTB2B2PRequest request
    ) throws UnauthorizedSoapException {

        // Inject in AOP
        String backendSecret = (String) messageContext.getProperty(B2B_SECRET);

        // Generate B2B2P Token
        String accessToken = jwtService.generateB2B2PToken(
                request.getUsername(),
                backendSecret,
                EXPIRED_IN_SECOND);

        // Finalize Response
        GetJWTResponse response = new GetJWTResponse();
        response.setAccessToken(accessToken);
        response.setExpiresIn(EXPIRED_IN_SECOND);
        return response;
    }

}