package id.co.awan.digitalizeshopsoa.aop;

import https.soa_digitalizeshop_id.ws.jwt.GetJWTB2B2PRequest;
import id.co.awan.digitalizeshopsoa.database.first.model.BackendModel;
import id.co.awan.digitalizeshopsoa.database.first.repo.BackendModelRepo;
import id.co.awan.digitalizeshopsoa.database.first.repo.SellerModelRepo;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import id.co.awan.digitalizeshopsoa.service.JWTService;
import id.co.awan.digitalizeshopsoa.util.MessageContextUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B_ID;
import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B_SECRET;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class B2B2PIdentifyAuthorizeAOP {

    private final JWTService jwtService;
    private final BackendModelRepo backendModelRepo;
    private final SellerModelRepo sellerModelRepo;


    @Qualifier("firstTransactionManager")
    private final PlatformTransactionManager transactionManager;


    @Around("execution(* id.co.awan.digitalizeshopsoa.endpoint.security.JWTEndpoint.getJWTB2B2PRequest(..)) && args(messageContext,request)")
    public Object handlerAOP(
            ProceedingJoinPoint pjp,
            MessageContext messageContext,
            @RequestPayload GetJWTB2B2PRequest request
    ) throws Throwable {

        // Validate B2B
        String b2bId;
        try {
            String b2bAccessToken = MessageContextUtil.getBearerToken("Authorization", messageContext);
            Jws<Claims> claimsJws = jwtService.parseJWSWithPublicKey(b2bAccessToken);
            Claims payload = claimsJws.getPayload();
            b2bId = (String) payload.get(B2B_ID);
        } catch (Exception e) {
            throw new UnauthorizedSoapException("Invalid B2B Token");
        }

        // Is b2bId valid from DB?
        BackendModel backendModel = backendModelRepo.findById(b2bId)
                .orElseThrow(() -> new UnauthorizedSoapException("Unauthorized B2B Credential"));
        messageContext.setProperty(B2B_SECRET, backendModel.getSecret());

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        Boolean isAuth = transactionTemplate.execute(status -> {
            String username = request.getUsername();
            String password = request.getPassword();
            return sellerModelRepo.authSeller(username, password);
        });

        // Validate Seller Credential
        if (Boolean.FALSE.equals(isAuth)) {
            throw new UnauthorizedSoapException("Invalid Seller Credential");
        }

        // Proceed Resoruce
        Object[] args = pjp.getArgs();
        return pjp.proceed(args);

    }

}
