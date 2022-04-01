package anhtester.com.helpers;

import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Helpers {

    /**
     * @return lấy đường dẫn đến thư mục nguồn source mình có thêm dấu / ở cuối luôn
     */
    public static String getCurrentDir() {
        String current = System.getProperty("user.dir") + "/";
        return current;
    }

    /**
     * Tạo folder rỗng
     *
     * @param path đường dẫn cần tạo folder
     */
    public static void CreateFolder(String path) {
        // File is a class inside java.io package
        File file = new File(path);

        String result = null;

        int lengthSum = path.length();
        int lengthSub = path.substring(0, path.lastIndexOf('/')).length();

        result = path.substring(lengthSub, lengthSum);

        if (!file.exists()) {
            file.mkdir();  // mkdir is used to create folder
            System.out.println("Folder " + file.getName() + " created: " + path);
        } else {
            System.out.println("Folder already created");
        }
    }

    /**
     * @return lấy ra ngày tháng năm và giờ phút giây hiện tại của máy theo cấu trúc dd/MM/yyyy HH:mm:ss
     */
    public static String CurrentDateTime() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        System.out.println(formatter.format(now));
//        String Timestamp = now.toString().replace(":", "-");
        return formatter.format(now);
    }

    /**
     * @param str        chuỗi string cần tách ra theo điều kiện
     * @param valueSplit ký tự cần tách chuỗi thành mảng giá trị
     * @return mảng giá trị kiểu chuỗi sau khi tách
     */
    public static ArrayList<String> splitString(String str, String valueSplit) {
        ArrayList<String> arrayListString = new ArrayList<>();
        for (String s : str.split(valueSplit, 0)) {
            arrayListString.add(s);
        }
        return arrayListString;
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

    /**
     * In ra câu message trong Console log
     *
     * @param object truyền vào object bất kỳ
     */
    public static void logConsole(@Nullable Object object) {
        System.out.println(object);
    }

}