package com.example.raiderdelivery_v4.ui.global;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AesCipher
 * <p>Encode/Decode text by password using AES-128-CBC algorithm</p>
 */
public class AesCipher {
    public static final int INIT_VECTOR_LENGTH = 16;
    public static final int INIT_VECTOR_LENGTH2 = 24;
    /**
     * @see <a href="https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">how-to-convert-a-byte-array-to-a-hex-string</a>
     */
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Encoded/Decoded data
     */
    protected String data;
    /**
     * Initialization vector value
     */
    protected String initVector;
    /**
     * Error message if operation failed
     */
    protected String errorMessage;

    private AesCipher() {
        super();
    }

    /**
     * AesCipher constructor.
     *
     * @param initVector   Initialization vector value
     * @param data         Encoded/Decoded data
     * @param errorMessage Error message if operation failed
     */
    private AesCipher(@Nullable String initVector, @Nullable String data, @Nullable String errorMessage) {
        super();

        this.initVector = initVector;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    /**
     * Encrypt input text by AES-128-CBC algorithm
     *
     * @param secretKey 16/24/32 -characters secret password
     * @param plainText Text for encryption
     * @return Encoded string or NULL if error
     */
    public static AesCipher encrypt(String secretKey, String plainText) {
        String initVector = "T1g994xo2UAqG81M";

        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get random initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
           // initVector = bytesToHex(initVectorBytes);
            initVector = "T1g994xo2UAqG81M";
            initVectorBytes = initVector.getBytes("UTF-8");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt input text
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

            //ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
            ByteBuffer byteBuffer = ByteBuffer.allocate(encrypted.length);
            //byteBuffer.put(initVectorBytes);
            byteBuffer.put(encrypted);

            // Result is base64-encoded string: initVector + encrypted result
            String result = Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);

            // Return successful encoded object
            return new AesCipher(initVector, result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new AesCipher(initVector, null, t.getMessage());
        }
    }

    /**
     * Decrypt encoded text by AES-128-CBC algorithm
     *
     * @param secretKey  16/24/32 -characters secret password
     * @param cipherText Encrypted text
     * @return Self object instance with data or error message
     */
//    public static AesCipher decrypt(String secretKey, String cipherText) {
//        try {
//            // Check secret length
//            if (!isKeyLengthValid(secretKey)) {
//                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
//            }
//
//            // Get raw encoded data
//            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
//
//            // Slice initialization vector
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
////            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted);
//            // Set secret password
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
////            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//            // Trying to get decrypted text
//            String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH , encrypted.length - INIT_VECTOR_LENGTH));
//           //String result1 = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH / 2, encrypted.length - (INIT_VECTOR_LENGTH / 2)));
//
////            String result = new String(cipher.doFinal(encrypted, 32, 32));
////            String result1 = new String(cipher.doFinal(encrypted, 32, 32));
////            String result2 = new String(cipher.doFinal(encrypted, 32, 32));
//
//            //return new AesCipher("", result,null);
//            // Return successful decoded object
//            //return new AesCipher(bytesToHex(ivParameterSpec.getIV()), result, null);
//
//            return new AesCipher("T1g994xo2UAqG81M", result, null);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            // Operation failed
//            return new AesCipher(null, null, t.getMessage());
//        }
//    }

    /**
     * Decrypt encoded text by AES-128-CBC algorithm
     *
     * @param secretKey  16/24/32 -characters secret password
     * @param cipherText Encrypted text
     * @return Self object instance with data or error message
     */

    public static AesCipher decrypt(String secretKey, String cipherText) {
        String initVector = "T1g994xo2UAqG81M";
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }
            // Get raw encoded data
            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
            // Slice initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
            // initVector = bytesToHex(initVectorBytes);
            initVector = "T1g994xo2UAqG81M";
            initVectorBytes = initVector.getBytes("UTF-8");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            // Set secret password
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            // Trying to get decrypted text
            String result = new String(cipher.doFinal(encrypted, 0, encrypted.length));
            // Return successful decoded object
            return new AesCipher(bytesToHex(ivParameterSpec.getIV()), result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new AesCipher(null, null, t.getMessage());
        }
    }

//    public static AesCipher decrypt(String secretKey, String cipherText) {
//        try {
//            // Check secret length
//            if (!isKeyLengthValid(secretKey)) {
//                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
//            }
//
//            // Get raw encoded data
//            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
//
//            // Slice initialization vector
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH2);
//            // Set secret password
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//            // Trying to get decrypted text
//        //    String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH2, encrypted.length));
//            String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH2, encrypted.length));
//
//            // Return successful decoded object
//            return new AesCipher(bytesToHex(ivParameterSpec.getIV()), result, null);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            // Operation failed
//            return new AesCipher(null, null, t.getMessage());
//        }
//    }


    /**
     * Check that secret password length is valid
     *
     * @param key 16/24/32 -characters secret password
     * @return TRUE if valid, FALSE otherwise
     */
    public static boolean isKeyLengthValid(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }

    /**
     * Convert Bytes to HEX
     *
     * @param bytes Bytes array
     * @return String with bytes values
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get encoded/decoded data
     */
    public String getData() {
        return data;
    }

    /**
     * Get initialization vector value
     */
    public String getInitVector() {
        return initVector;
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Check that operation failed
     *
     * @return TRUE if failed, FALSE otherwise
     */
    public boolean hasError() {
        return this.errorMessage != null;
    }

    /**
     * To string return resulting data
     *
     * @return Encoded/decoded data
     */
    public String toString() {
        return getData();
    }
}
//