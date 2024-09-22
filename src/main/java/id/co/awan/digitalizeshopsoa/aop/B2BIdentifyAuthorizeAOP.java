package id.co.awan.digitalizeshopsoa.aop;

import https.soa_digitalizeshop_id.ws.jwt.GetJWTB2BRequest;
import id.co.awan.digitalizeshopsoa.database.first.domain.BackendEntity;
import id.co.awan.digitalizeshopsoa.database.first.repo.BackendEntityRepo;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import id.co.awan.digitalizeshopsoa.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class B2BIdentifyAuthorizeAOP {

    private final BackendEntityRepo backendEntityRepo;

    @Around("execution(* id.co.awan.digitalizeshopsoa.endpoint.security.JWTEndpoint.getJWTB2BRequest(..)) && args(request)")
    public Object handlerAOP(ProceedingJoinPoint pjp, GetJWTB2BRequest request) throws Throwable {

        // Destruct Request
        String b2bid = request.getBackendId();
        String timestamp = request.getTimestamp();
        String signature = request.getSignature();

        // Is b2bId valid from DB?
        BackendEntity backendEntity = backendEntityRepo.findById(b2bid)
                .orElseThrow(() -> new UnauthorizedSoapException("Unauthorized Backend Credential"));

        // Is Signature Valid?
        String trueSignature = CryptoUtil.hmacSha256EncBase64(b2bid + "|" + timestamp, backendEntity.getSecret());
        if (!trueSignature.equals(signature)) {
            throw new UnauthorizedSoapException("Invalid Signature");
        }

        // Proceed Resoruce
        Object[] args = pjp.getArgs();
        return pjp.proceed(args);

    }

}
