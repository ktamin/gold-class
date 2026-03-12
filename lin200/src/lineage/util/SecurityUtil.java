package lineage.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class SecurityUtil {

    // 솔트 생성
    public static byte[] makeSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // SHA-256 해시
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input);
        } catch (Exception e) {
            return null;
        }
    }

    // 비밀번호 해시 생성 (salt + pw)
    public static byte[] hashPassword(String pw, byte[] salt) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(salt);
            bos.write(pw.getBytes(StandardCharsets.UTF_8));
            return sha256(bos.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    // 비밀번호 검증
    public static boolean checkPassword(String pw, byte[] salt, byte[] hash) {
        byte[] h = hashPassword(pw, salt);
        return Arrays.equals(h, hash);
    }
}
