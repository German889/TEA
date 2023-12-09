package laba2;

import java.io.*;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
public class Decrypter {
    public static int[] decrypt(int[] block, int[] key){
        int v0 = block[0];
        int v1 = block[1];
        int sum = Utility.DELTA * Utility.ROUNDS;

        for(int i = 0; i < Utility.ROUNDS; i++){
            // Выполняем операции алгоритма в обратном порядке
            v1 -= ((v0 << 4) + key[2]) ^ (v0 + sum) ^ ((v0 >> 5) + key[3]);
            v0 -= ((v1 << 4) + key[0]) ^ (v1 + sum) ^ ((v1 >> 5) + key[1]);
            sum -= Utility.DELTA;
        }

        block[0] = v0;
        block[1] = v1;
        return block;
    }
    public static void decryptReminder(int numBlocks, int[] numericKey){
        Utility.createFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\output.txt");
        byte[] block = new byte[8];
        int[] XORed = new int[2];
        try(FileInputStream fis = new FileInputStream("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt.enc")){
            for(int i=0; i<numBlocks+1; i++){
                fis.read(block);
                int[] numBlock = Utility.bytesToNum(block);
                if (i != numBlocks){
                    int[] decryptedNumBlock = Decrypter.decrypt(numBlock, numericKey);
                    byte[] decryptedByteBlock = Utility.numToBytes(decryptedNumBlock);
                    Utility.appendBytesToFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\output.txt", decryptedByteBlock);
                } else{
                    byte[] IV = Utility.readBlockFromFile("IV.txt");
                    byte[] byteXORed = new byte[8];
                    for(int g =0; g<8; g++){
                        byteXORed[g] = (byte) (block[g] ^ IV[g]);
                    }
                    int countNULs = 0;
                    for (int b=0;b<8;b++){
                        if(byteXORed[b] == 0) countNULs++;
                    }
                    int countNotNULs = 8 - countNULs;
                    byte[] trimmedXORed = new byte[countNotNULs];
                    for(int h = 0;h<countNotNULs;h++){
                        if(byteXORed[h] != 0) trimmedXORed[h] = byteXORed[h];
                    }
                    Utility.appendBytesToFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\output.txt", trimmedXORed);
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void fullDecryptionProcess(String encryptedFileName, String keyFileName, byte[] keyAnotherWay){
//        String encryptedFileName = command[2];
//        String keyFileName = command[3];
        int countCharacters = Utility.countCharactersInFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt");
        int numBlocks = countCharacters / 8;
        byte[] key = null;
        int[] numericKey = null;
        if(keyAnotherWay == null){
            key = Utility.readKeyFromFile(keyFileName);
            numericKey = Utility.bytesToNumKey(key);
        } else{
            numericKey = Utility.bytesToNumKey(keyAnotherWay);
        }
        if(countCharacters == 8){
            byte[] encryptedBlock = Utility.readBlockFromFile(encryptedFileName);
            int[] numericBlock = Utility.bytesToNum(encryptedBlock);
            int[] decryptedBlock = Decrypter.decrypt(numericBlock, numericKey);
            byte[] decryptedBytesBlock = Utility.numToBytes(decryptedBlock);
            Utility.writeBytesToFile("output.txt", decryptedBytesBlock);
        } else if (countCharacters % 8 == 0) {
            Utility.createFile("output.txt");
            for(int i=0; i<numBlocks; i++){
                byte[] block = Utility.readBlockFromFile("input.txt.enc");
                int[] numBlock = Utility.bytesToNum(block);
                int[] decryptedNumBlock = Decrypter.decrypt(numBlock, numericKey);
                byte[] decryptedByteBlock = Utility.numToBytes(decryptedNumBlock);
                Utility.appendBytesToFile("output.txt", decryptedByteBlock);
            }
        } else{
            //TODO: read IV from begin of file
            Decrypter.decryptReminder(numBlocks, numericKey);
        }
    }
    public static byte[] decryptAES(byte[] ciphertext, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] cip = cipher.doFinal(ciphertext);
        byte[] see = cip;
        System.out.println(see);
        return cip;
    }
}
