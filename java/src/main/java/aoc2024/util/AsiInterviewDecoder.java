package aoc2024.util;

import java.nio.charset.StandardCharsets;

/**
 * [35,44,47,36,33,13,44,36,63,62,61,44,46,40,96,36,35,57,40,33,33,36,42,40,35,46,40,99,46,34,32]
 */
public class AsiInterviewDecoder {

    final static byte [] encoded =  {35,44,47,36,33,13,44,36,63,62,61,44,46,40,96,36,35,57,40,33,33,36,42,40,35,46,40,99,46,34,32 };
    final static byte secretKey=77;
    public static void main(String[] args) {
        try {
            final byte[] decoded = encoded.clone();
            for (int i = 0; i < decoded.length; i++) {
                decoded[i] = (byte) (decoded[i] ^ secretKey);
            }
            System.out.println(new String(decoded, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
    }
}
