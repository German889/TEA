package laba2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;


public class Encrypter {
    public static void encryptProcess1block(String originalFileName, String keyFileName, String encryptedFileName, byte[] keyAnotherWay){
        byte[] block = Utility.readBlockFromFile(originalFileName);
        byte[] key = new byte[16];
        if(keyAnotherWay == null){
            key = Utility.readKeyFromFile(keyFileName);
        } else key = keyAnotherWay;
        int[] numericKey = Utility.bytesToNumKey(key);
        int[] numericBlock = Utility.bytesToNum(block);
        int[] encryptedData = encrypt(numericBlock, numericKey);
        byte[] enc_bytes = Utility.numToBytes(encryptedData);
        Utility.writeBytesToFile(encryptedFileName, enc_bytes);
    }
    public static void encryptProcessFullBlocks(String keyFileName, String originalFileName, String encryptedFileName, int numBlocks, byte[] keyAnotherWay){
        try {
            // Чтение ключа из файла
            byte[] key = new byte[16];
            if(keyAnotherWay == null){
                key = Utility.readKeyFromFile(keyFileName);
            }
            else {
                key = keyAnotherWay;
            }
            int[] numericKey = Utility.bytesToNumKey(key);

            // Создание потоков для чтения и записи данных
            FileInputStream inputFile = new FileInputStream(originalFileName);
            FileOutputStream outputFile = new FileOutputStream(encryptedFileName);

            byte[] block = new byte[8]; // Буфер для блока данных

            for (int i = 0; i < numBlocks; i++) {
                int bytesRead = inputFile.read(block);
                if (bytesRead == -1) {
                    break; // Достигнут конец файла
                }

                int[] numericBlock = Utility.bytesToNum(block);
                int[] encryptedData = encrypt(numericBlock, numericKey);
                byte[] enc_bytes = Utility.numToBytes(encryptedData);
                outputFile.write(enc_bytes);
            }

            inputFile.close();
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void encryptProcessWithReminder(String originalFileName, String keyFileName, String encryptedFileName, int numBlocks, byte[] keyAnotherWay, boolean doWriteIVtoExternalFile){
        try {
            // Чтение ключа из файла
            byte[] key;
;            if(keyAnotherWay == null){
                key = Utility.readKeyFromFile(keyFileName);
            } else key = keyAnotherWay;
            int[] numericKey = Utility.bytesToNumKey(key);

            // Создание потоков для чтения и записи данных
            FileInputStream inputFile = new FileInputStream(originalFileName);
            FileOutputStream outputFile = new FileOutputStream(encryptedFileName);
            int[] intIV = generateRandomIV();
            byte[] inBeginFileIV = Utility.numToBytes(intIV);
            outputFile.write(inBeginFileIV);


            byte[] block = new byte[8]; // Буфер для блока данных
            List<Byte> reminder = new ArrayList<Byte>(); // хранение байтов остатка текста
            byte[] oneByte = new byte[1]; //буфер на 1 элемент для чтения остатка
            for (int i = 0; i < numBlocks; i++) {
                int bytesRead = inputFile.read(block);
                if (bytesRead == -1) {
                    break; // Достигнут конец файла
                }

                int[] numericBlock = Utility.bytesToNum(block);
                int[] encryptedData = encrypt(numericBlock, numericKey);
                byte[] enc_bytes = Utility.numToBytes(encryptedData);
                outputFile.write(enc_bytes);
            }
            while (true){
                int bytesRead = inputFile.read(oneByte);
                if (bytesRead == -1) break;
                reminder.add(oneByte[0]);
            }
            OFB enc2Data = encryptReminder(reminder, numericKey, intIV);
            byte[] checkReminder = enc2Data.reminder;
            outputFile.write(checkReminder);
            //Utility.appendBytesToFile("input.txt.enc", checkReminder);
            inputFile.close();
            outputFile.close();

            if (doWriteIVtoExternalFile == true) Utility.writeBytesToFile("IV.txt", inBeginFileIV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int[] generateKey(){
        int[] key = new int[4];
        Random random = new Random();
        for(int i=0; i<4; i++){
            key[i] = random.nextInt();
        }
        return key;
    }

    public static int[] generateRandomIV() {
        int[] iv = new int[2]; // IV длиной 2 слова (64 бита)
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < iv.length; i++) {
            iv[i] = random.nextInt(); // Генерируем случайное 32-битное слово
        }

        return iv;
    }
    public static OFB encryptReminder(List<Byte> reminder, int[] key, int[] IV){
//        int[] IV = generateRandomIV();
        int reminderLength = reminder.size();
        byte[] byteIV = Utility.numToBytes(IV);
        byte[] nextByteIV = new byte[8];
        int[] XORedInt = new int[2];
        for(int i = reminderLength; i<8; i++){
            nextByteIV[i] = byteIV[i-reminderLength];
        }
//        int[] encryptedIV = encrypt(IV, key);
//        System.out.println("encrypted IV ="+encryptedIV[0]+"|"+encryptedIV[1]);

        for( int i=0; i< (8-reminderLength); i++){
            reminder.add((byte)0); // ======================== добавляем нули
        }
        byte[] reminderByte = new byte[8];
        for (int i=0; i<8; i++){
            reminderByte[i] = reminder.get(i);
        }

        int[] reminderInt = Utility.bytesToNum(reminderByte);
        //--------------------------------------------------------- XOR
        XORedInt[0] = reminderInt[0] ^ IV[0];            //
        XORedInt[1] = reminderInt[1] ^ IV[1];            //
        //---------------------------------------------------------
        byte[] XORedByte = Utility.numToBytes(XORedInt);
        //---------------------------------------------------------обратный XOR
//        int[] checkXOR = new int[2];
//        checkXOR[0] = XORedInt[0] ^ encryptedIV[0];
//        checkXOR[1] = XORedInt[1] ^ encryptedIV[1];
//        byte[] checkXORbyte = Utility.numToBytes(checkXOR);
//        for(byte b: XORedByte){
//            System.out.println(String.valueOf(b)+"xored");
//        }
//        for(byte b: checkXORbyte){
//            System.out.println(String.valueOf(b)+"unxored");
//        }
        //----------------------------------------------------------
        OFB encData = new OFB();
        encData.IV = byteIV;
        encData.reminder = XORedByte;
        return encData;
    }
    public static int[] encrypt(int[] block, int[]key){
        int v0 = block[0];
        int v1 = block[1];
        int sum = 0;

        for(int i=0; i<Utility.ROUNDS; i++){
            // Вычисляем значение sum
            sum += Utility.DELTA;

            // Выполняем операции алгоритма
            v0 += ((v1 << 4) + key[0]) ^ (v1 + sum) ^ ((v1 >> 5) + key[1]);
            v1 += ((v0 << 4) + key[2]) ^ (v0 + sum) ^ ((v0 >> 5) + key[3]);
        }
        block[0] = v0; block[1] = v1;
        return block;
    }
    public static void fullEncryptionProcess(String originalFileName, String keyFileName, byte[] keyAnotherWay){
        Utility.cleanTheGarbage();
        if(keyAnotherWay == null) {//-----------------------------------------------------генерация ключа
            int[] key_ = Encrypter.generateKey();                                             //
            byte[] keyBYTES = Utility.numToBytesKey(key_);                                  //
            Utility.writeBytesToFile("key.txt", keyBYTES);                          //
            //------------------------------------------------------------------------
        }
        String encryptedFileName = originalFileName.concat(".enc");
        int countCharacters = Utility.countCharactersInFile(originalFileName);
        int numBlocks = countCharacters / 8;

        if(countCharacters == 8){
            Encrypter.encryptProcess1block(originalFileName, keyFileName, encryptedFileName, keyAnotherWay);
        } else if (countCharacters % 8 == 0) {
            Encrypter.encryptProcessFullBlocks(keyFileName, originalFileName, encryptedFileName, numBlocks, keyAnotherWay);
        } else{
            Encrypter.encryptProcessWithReminder(originalFileName, keyFileName, encryptedFileName, numBlocks, keyAnotherWay, false);
        }
        // TODO: write IV to begin of file
    }

    public static byte[] encryptAES(byte[] plaintext, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plaintext);
    }
}
