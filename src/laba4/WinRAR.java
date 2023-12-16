package laba4;

import javax.imageio.IIOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WinRAR {
    private static String inputFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba4\\files\\input.txt";
    private static String outputFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba4\\files\\output.txt";
    private static String dearchivedFile = "C:\\Users\\Givermaen\\IdeaProjects\\TEA\\TEA\\src\\laba4\\files\\dearchived.txt";
    public static void archiveate(){
        try(FileInputStream fis = new FileInputStream(inputFile)){
                long bytesInFile = countBytesInFile(inputFile);
                byte repeatCount = 0;
                byte previous = 0;
                byte[] buffer = new byte[1];
                for(int i=0; i<bytesInFile; i++){
                    fis.read(buffer);

                    if(previous == buffer[0]){
                        repeatCount++;
                        if (repeatCount == Byte.MAX_VALUE){
                            processSequence(previous, repeatCount);
                            repeatCount = 0; // Сбрасываем счетчик
                        }
                    }else {
                        processSequence(previous, repeatCount);
                        repeatCount = 1;
                    }
                    if(i == bytesInFile-1) {
                        processSequence(buffer[0], repeatCount);
                        repeatCount = 0;
                    }
                    previous = buffer[0];
                }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void dearchivate(){
        try(FileInputStream fis = new FileInputStream(outputFile)){
            long bytesInFile = countBytesInFile(outputFile);
            byte[] buffer = new byte[1];
            byte previous = 0;
            for(int i=0; i<bytesInFile; i++){
                fis.read(buffer);
                if(previous <0){
                    previous *= -1;
                    writeRepeated(buffer[0], previous);
                    previous = 0;
                } else writeRepeated(buffer[0], (byte)1);
                if(buffer[0] < 0) previous = buffer[0];

            }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch(IOException e){
            e.printStackTrace();
        }
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
    public static void processSequence(byte previous, byte repeatCount){

        try(FileOutputStream fos = new FileOutputStream(outputFile, true)){
            if((repeatCount > 2) && (previous != 0)){
                System.out.println("char= "+(char)previous+" repeated= "+repeatCount);
                if(repeatCount !=0)fos.write(-1*repeatCount);
                fos.write(previous);
            }
            else{
                if(previous !=0)fos.write(previous);
            }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void writeRepeated(byte symbol, byte repetitions){
        try(FileOutputStream fos = new FileOutputStream(dearchivedFile, true)){
            if (repetitions > 1) {
                for(int i=0; i<repetitions; i++){
                    fos.write(symbol);
                }
            } else {
                if(symbol >0)fos.write(symbol);
            }
        }catch(FileNotFoundException fne){
            fne.getMessage();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
