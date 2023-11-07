import java.io.*;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        commandRecognizer();
    }
    public static void commandRecognizer(){
        Scanner reader = new Scanner(System.in);
        String userInput = reader.nextLine();
        String[] command = userInput.split(" ");
        if(command[0].equals("encrypt")){
            if (command[1].equals("-e")) {
                Utility.cleanTheGarbage();
                // ------------------------------------------------------------генерация ключа
                int[] key_ = Encrypter.generateKey();                                             //
                byte[] keyBYTES = Utility.numToBytesKey(key_);                                  //
                Utility.writeBytesToFile("key.txt", keyBYTES);                          //
                //------------------------------------------------------------------------
                String originalFileName = command[2];
                String keyFileName = command[3];
                String encryptedFileName = originalFileName.concat(".enc");
                int countCharacters = Utility.countCharactersInFile(originalFileName);
                int numBlocks = countCharacters / 8;

                if(countCharacters == 8){
                    Encrypter.encryptProcess1block(originalFileName, keyFileName, encryptedFileName);
                } else if (countCharacters % 8 == 0) {
                    Encrypter.encryptProcessFullBlocks(keyFileName, originalFileName, encryptedFileName, numBlocks);
                } else{
                    Encrypter.encryptProcessWithReminder(originalFileName, keyFileName, encryptedFileName, numBlocks);
                }
            }
            if (command[1].equals("-d")) {
                String encryptedFileName = command[2];
                String keyFileName = command[3];
                int countCharacters = Utility.countCharactersInFile("input.txt");
                int numBlocks = countCharacters / 8;
                byte[] key = Utility.readKeyFromFile(keyFileName);
                int[] numericKey = Utility.bytesToNumKey(key);
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
                    Utility.createFile("output.txt");
                    byte[] block = new byte[8];
                    int[] XORed = new int[2];
                    try(FileInputStream fis = new FileInputStream("input.txt.enc")){
                        for(int i=0; i<numBlocks+1; i++){
                            fis.read(block);
                            int[] numBlock = Utility.bytesToNum(block);
                            if (i != numBlocks){
                                int[] decryptedNumBlock = Decrypter.decrypt(numBlock, numericKey);
                                byte[] decryptedByteBlock = Utility.numToBytes(decryptedNumBlock);
                                Utility.appendBytesToFile("output.txt", decryptedByteBlock);
                            } else{
                                byte[] IV = Utility.readBlockFromFile("IV.txt");
                                byte[] byteXORed = new byte[8];
                                for(int g =0; g<countCharacters%8; g++){
                                    byteXORed[g] = (byte) (block[g] ^ IV[g]);
                                }
                                byte[] trimmedXORed = new byte[countCharacters % 8];
                                for(int h = 0;h<8;h++){
                                    if(byteXORed[h] != 0) trimmedXORed[h] = byteXORed[h];
                                }
                                Utility.appendBytesToFile("output.txt", trimmedXORed);
                            }
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}