package com.android.linglan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by changyizhang on 12/14/14.
 */
public class Md5Util {

  public static String md5(String paramString) {
    String returnStr;
    try {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes());
      returnStr = byteToHexString(localMessageDigest.digest());
      return returnStr;
    } catch (Exception e) {
      return paramString;
    }
  }

  public static String byteToHexString(byte[] b) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      String hex = Integer.toHexString(b[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      hexString.append(hex.toUpperCase());
    }
    return hexString.toString();
  }

  private static String MD5 = "MD5";

  private static final int STREAM_BUFFER_LENGTH = 1024 * 1024;

  static MessageDigest getDigest(String algorithm) {
    try {
      return MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Compute the input data's md5 value.
   *
   * @param inputStream
   * @return
   * @throws java.io.IOException
   */
  public static String md5Digest(InputStream inputStream) throws IOException {
    MessageDigest digest = getDigest(MD5);
    byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
    int read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);

    while (read > -1) {
      digest.update(buffer, 0, read);
      read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);
    }

    return getHexString(digest.digest());
  }

  private static String getHexString(byte[] digest) {
    BigInteger bi = new BigInteger(1, digest);
    return String.format("%032x", bi);
  }
}


