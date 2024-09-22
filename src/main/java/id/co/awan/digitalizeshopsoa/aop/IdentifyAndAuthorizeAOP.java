package id.co.awan.digitalizeshopsoa.aop;

import id.co.awan.digitalizeshopsoa.database.first.domain.BackendEntity;
import id.co.awan.digitalizeshopsoa.database.first.repo.BackendEntityRepo;
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
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;

import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B2P_CLAIM_PRINCIPAL;
import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B_ID;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class IdentifyAndAuthorizeAOP {

    private final JWTService jwtService;

    private final BackendEntityRepo backendEntityRepo;

    @Around("execution(* id.co.awan.digitalizeshopsoa.example.CountryEndpoint.*(..))")
    public Object handlerAOP(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();

        // MessageContext Aware
        for (Object arg : args) {
            if (arg instanceof MessageContext messageContext) {

                // Validate B2B
                String b2bId;
                try {
                    String accessToken_B2B = MessageContextUtil.getBearerToken("Authorization", messageContext);
                    Jws<Claims> claimsJws = jwtService.parseJWSWithPublicKey(accessToken_B2B);
                    b2bId = (String) claimsJws.getPayload().get(B2B_ID);
                    messageContext.setProperty(B2B_ID, b2bId);
                } catch (Exception e) {
                    throw new UnauthorizedSoapException("Invalid B2B Token");
                }


                // Validate B2B2P
                try {
                    String accessToken_B2B2P = MessageContextUtil.getBearerToken("Partner-Authorization", messageContext);

                    BackendEntity backendEntity = backendEntityRepo.findById(b2bId).orElseThrow(() -> new UnauthorizedSoapException("Unauthorized B2B Credential"));
                    String backendSecret = backendEntity.getSecret();

                    Jws<Claims> jwsB2B2P = jwtService.parseJWSWithSecrekKey(accessToken_B2B2P, backendSecret);
                    messageContext.setProperty(B2B2P_CLAIM_PRINCIPAL, jwsB2B2P.getPayload().get(B2B2P_CLAIM_PRINCIPAL));
                } catch (Exception e) {
                    throw new UnauthorizedSoapException("Invalid B2B2P Token");
                }

            }
        }

        return pjp.proceed(args);

    }

}
