import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.security.SecureRandom;
import java.io.IOException;

public class Utility {
    public final static int DELTA = 0x9e3779b9;
    public final static int ROUNDS = 32;
    public static void print(String str){
        System.out.println(str);
    }
    public static void print(int[] str){
        for(int i=0; i<str.length; i++){
            System.out.println(str[i] +"");
        }
    }
    public static byte[] SHA_3_hash(byte[] before){ // хеширование силами великой Джавы
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = messageDigest.digest(before);
        return digest;
    }
    public static void createFile(String fileName){
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                print("created file "+fileName);
            } else {
                print("not created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static byte[] readBlockFromFile(String fileName){
        byte[] reserve = {1,1};
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] bytes = new byte[8];
            int bytesRead = fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reserve;
    }
    private static String bytesToHex(byte[] hash) { // ето для sha-3 хеширования
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static byte[] readKeyFromFile(String fileName){
        byte[] reserve = {1,2};
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] bytes = new byte[16];
            int bytesRead = fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reserve;
    }
    public static void writeBytesToFile(String fileName, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendBytesToFile(String fileName, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(fileName, true)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void cleanTheGarbage(){
        File keyFileD = new File("key.txt");
        keyFileD.delete();
        File encFileD = new File("input.txt.enc");
        encFileD.delete();
        File outputFileD = new File("output.txt");
        outputFileD.delete();
        File outputFileIV = new File("IV.txt");
        outputFileIV.delete();
    }
    public static int countCharactersInFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            int character = 0;
            int count = 0;

            while (character != -1) {
                character = fileReader.read();
                count++;
            }

            fileReader.close();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // В случае ошибки ввода-вывода возвращаем -1
        }
    }
    public static int[] bytesToNum(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        // Преобразовать первые 4 байта в 32-битное число
        int num1 = buffer.getInt();
        // Преобразовать следующие 4 байта во второе 32-битное число
        int num2 = buffer.getInt();
        int[] num = new int[2];
        num[0] = num1;
        num[1] = num2;
        return num;
    }
    public static int[] bytesToNumKey(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        // Преобразовать первые 8 байт в 128-битное число
        int num1 = buffer.getInt();
        // Преобразовать следующие 8 байт во второе 128-битное число
        int num2 = buffer.getInt();
        int num3 = buffer.getInt();
        int num4 = buffer.getInt();
        int[] num = new int[4];
        num[0] = num1;
        num[1] = num2;
        num[2] = num3;
        num[3] = num4;
        return num;
    }
    public static byte[] numToBytes(int[] num) {
        int number1 = num[0];
        int number2 = num[1];
        ByteBuffer buffer = ByteBuffer.allocate(8);
        // Записать первое 32-битное число
        buffer.putInt(number1);
        // Записать второе 32-битное число
        buffer.putInt(number2);
        // Получить байты и преобразовать их в текст
        byte[] bytes = buffer.array();

        return bytes;
    }

    public static byte[] numToBytesKey(int[] num) {
        int number1 = num[0];
        int number2 = num[1];
        int number3 = num[2];
        int number4 = num[3];
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // Записать первое-четвёртое 32-битное число
        buffer.putInt(number1);
        buffer.putInt(number2);
        buffer.putInt(number3);
        buffer.putInt(number4);
        // Получить байты и преобразовать их в текст
        byte[] bytes = buffer.array();
        return bytes;
    }
}
