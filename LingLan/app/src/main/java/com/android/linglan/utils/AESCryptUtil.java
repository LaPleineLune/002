package com.android.linglan.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by LeeMy on 2016/3/15 0015.
 *
 * 用法：
 *
 * private AESCryptUtil aesCryptUtil;
 * String encrypt = "";
 * String decrypt = "";
 * aesCryptUtil = new AESCryptUtil();
 * encrypt = aesCryptUtil.encrypt("打死小金朋");
 * decrypt = aesCryptUtil.decrypt(encrypt);
 *
 * LogUtil.e("1打死小金朋=加密：：：" + encrypt + "1打死小金朋=解密：：：" + decrypt);
 */
public class AESCryptUtil {

    public Cipher cipher;
    public SecretKeySpec key;
    private AlgorithmParameterSpec spec;
    public static final String SEED_16_CHARACTER = "d.7r+[6D,4[]2cQ?|!>xE6d#{]3v%d{K@q#pVbU*Vm/SRDYsIl";

    public AESCryptUtil() {//  throws Exception
        // hash password with SHA-256 and crop the output to 128-bit for key
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(SEED_16_CHARACTER.getBytes("UTF-8"));
            byte[] keyBytes = new byte[32];
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            key = new SecretKeySpec(keyBytes, "AES");
            spec = getIV();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    /* 加密 */
    public String encrypt(String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            String encryptedText = new String(Base64.encode(encrypted,
                    Base64.DEFAULT), "UTF-8");
//            return encryptedText;
            return URLEncoder.encode(encryptedText, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /* 加密 */
    public String encryptPost(String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            String encryptedText = new String(Base64.encode(encrypted,
                    Base64.DEFAULT), "UTF-8");
            return encryptedText;
//            return URLEncoder.encode(encryptedText, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /* 解密 */
    public String decrypt(String cryptedText){
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(bytes);
            String decryptedText = new String(decrypted, "UTF-8");
            return decryptedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
