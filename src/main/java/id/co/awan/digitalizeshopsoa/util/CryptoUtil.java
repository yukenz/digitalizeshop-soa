package id.co.awan.digitalizeshopsoa.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtil {

    public static String hmacSha256EncBase64(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException {

        final String HMACType = "HmacSHA256";

        Mac hmacSHA256 = Mac.getInstance(HMACType);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMACType);
        hmacSHA256.init(secretKey);

        byte[] macResult = hmacSHA256.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(macResult);
    }

}
