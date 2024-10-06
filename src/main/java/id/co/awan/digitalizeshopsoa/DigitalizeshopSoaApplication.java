package id.co.awan.digitalizeshopsoa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.security.Security;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DigitalizeshopSoaApplication {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(DigitalizeshopSoaApplication.class, args);
    }

}
