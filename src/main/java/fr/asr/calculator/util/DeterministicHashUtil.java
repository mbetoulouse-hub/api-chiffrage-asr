package fr.asr.calculator.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeterministicHashUtil {

    public static String hash(String... inputs) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (String input : inputs) {
                if (input != null) {
                    digest.update(input.getBytes(StandardCharsets.UTF_8));
                }
            }
            byte[] hashBytes = digest.digest();
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
