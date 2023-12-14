package laba5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Base64Process {
    private static String originalFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba5\\files\\default.txt";
    private static String encodedFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba5\\files\\transport_ready.txt";
    private static String decodedFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba5\\files\\decoded.txt";

    public static void start(){
        encode();
    }

    public static long countBytesInFile(String filepath) {
        long bytesCount = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            int data;
            while ((data = fis.read()) != -1) {
                bytesCount++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to open file " + filepath);
        } catch (IOException e) {
            System.out.println("Error reading data from file " + filepath);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                System.out.println("Error closing file " + filepath);
            }
        }
        return bytesCount;
    }
    public static void encode(){
        long bytesInFile = countBytesInFile(originalFile);
        long blocks = bytesInFile / 3;
        int reminderBytes = (int)(bytesInFile - blocks*3);
        byte[] block = new byte[3];
        byte[] base64Block = new byte[4];
        try(FileInputStream fis = new FileInputStream(originalFile)){
            try(FileOutputStream fos = new FileOutputStream(encodedFile)){
                for(int i=0; i<blocks; i++){
                    fis.read(block);
                    Integer24bits.add_3_ASCIIBytes(block);
                    base64Block = Integer24bits.get_4_Base64Bytes();
                    fos.write(base64Block);
                }
                if(reminderBytes == 1){
                    byte[] b = new byte[1];
                    fis.read(b);
                    block[0] = b[0];
                    block[1] = 0;
                    block[2] = 0;
                    Integer24bits.add_3_ASCIIBytes(block);
                    base64Block = Integer24bits.get_4_Base64Bytes();
                    base64Block[2] = '=';
                    base64Block[3] = '=';
                }
                else if(reminderBytes == 2){
                    byte[] b = new byte[2];
                    fis.read(b);
                    block[0] = b[0];
                    block[1] = b[1];
                    block[2] = 0;
                    Integer24bits.add_3_ASCIIBytes(block);
                    base64Block = Integer24bits.get_4_Base64Bytes();
                    base64Block[3] = '=';
                } else System.out.println("в конце файла не 1 и не 2 байта и не блок из 3, их ="+reminderBytes+" всего байтов="+bytesInFile);
                fos.write(base64Block);
            }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch (IOException e){
            e.getMessage();
        }
    }
    public static void decode(){
        long bytesInFile = countBytesInFile(encodedFile);
        long blocks = bytesInFile / 4;
        byte[] base64block = new byte[4];
        byte[] asciiBlock = new byte[3];
        try(FileInputStream fis = new FileInputStream(encodedFile)) {
            try (FileOutputStream fos = new FileOutputStream(decodedFile)) {
                for(int i=0; i<blocks; i++){
                    fis.read(base64block);
                    asciiBlock = Integer24bits.convert_base64_4_bytes_to_3_ASCII(base64block);
                    fos.write(asciiBlock[0]);
                    if(asciiBlock[1] != 0) fos.write(asciiBlock[1]);
                    if(asciiBlock[2] != 0) fos.write(asciiBlock[2]);
                }
            }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
