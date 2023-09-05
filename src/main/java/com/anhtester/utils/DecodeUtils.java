/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class DecodeUtils {

    private DecodeUtils() {
        super();
    }

    //Encrypt and Decrypt data

    private static final String key1 = "AES";
    private static final String key2 = "AES/ECB/PKCS5Padding";

    private static String encryptionKeyString = "autotestselenium";
    private static final byte[] encryptionKeyBytes = encryptionKeyString.getBytes();

    private static SecretKey generateKey() {
        SecretKey key = new SecretKeySpec(encryptionKeyBytes, key1);
        return key;
    }

    public static String encrypt(String Data) {
        String encryptedValue = null;
        try {
            SecretKey key = generateKey();
            Cipher c = Cipher.getInstance(key2);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            encryptedValue = Base64.encodeBase64String(encVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) {
        String decryptedValue = null;
        try {
            SecretKey key = generateKey();
            Cipher c = Cipher.getInstance(key2);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decodeBase64(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedValue;
    }
}
