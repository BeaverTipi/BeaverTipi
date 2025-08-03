package kr.or.ddit.util.crypto;

public class UrlSafeBase64 {

    public static String encode(String base64) {
        return base64.replace("+", "-")
                     .replace("/", "_")
                     .replaceAll("=+$", "");
    }

    public static String decode(String urlSafeBase64) {
        String base64 = urlSafeBase64.replace("-", "+")
                                     .replace("_", "/");
        while (base64.length() % 4 != 0) {
            base64 += "=";
        }
        return base64;
    }
}