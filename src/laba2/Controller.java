package laba2;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Controller {
    public static byte[] session_key = new byte[16]; //16 неповторимых байт
    public static byte[] hash = new byte[32]; //32 байта
    public static void commandRecognizerLaba2(){
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
                    Encrypter.encryptProcess1block(originalFileName, keyFileName, encryptedFileName, null);
                } else if (countCharacters % 8 == 0) {
                    Encrypter.encryptProcessFullBlocks(keyFileName, originalFileName, encryptedFileName, numBlocks, null);
                } else{
                    Encrypter.encryptProcessWithReminder(originalFileName, keyFileName, encryptedFileName, numBlocks, null, false);
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
                    Decrypter.decryptReminder(numBlocks, numericKey);
                }
            }
        }
    }
    //------------------------method edge
    public static void commandRecognizerLaba3() {
        Utility.print("enter encrypt to encrypt or decrypt to decrypt");
        Scanner reader = new Scanner(System.in);
        String decision = reader.next();
        switch (decision){
            case "encrypt":
                full_session_encryption();
                break;
            case "decrypt":
                full_session_decryption();
                break;
            default:
                Utility.print("aboba");
        }
    }
    public static void full_session_encryption(){
        session_key = Utility.numToBytesKey(Encrypter.generateKey());
        Encrypter.fullEncryptionProcess("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt", "", session_key);
        try{
            Utility.print("enter password on latinica");
            Utility.enterPassword(); // ввод пароля
//            Utility.simulatePasswordEntering();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            byte[] crypted_session_key = Encrypter.encryptAES(session_key, hash); // шифрование ключа сессии
            Utility.appendBytesToFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt.enc", crypted_session_key);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void full_session_decryption(){
        session_key = Utility.readLastBytesFromFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt.enc", 16);
        try{
            Utility.print("enter password using latinica");
            Utility.enterPassword();
//            Utility.simulatePasswordEntering();
            session_key = Decrypter.decryptAES(session_key, hash);
            Utility.deleteLastBytesFromFile("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt.enc", 16);
            Decrypter.fullDecryptionProcess("C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba2\\files\\input.txt.enc", null, session_key);
        }catch(UnsupportedEncodingException uee){
            uee.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
