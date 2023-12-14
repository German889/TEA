package laba5;

import java.util.*;

public class Integer24bits {
    private static List<Boolean> bits = new ArrayList<>();

    public static String decimalToBinary(int decimalNumber) {
        String result = Integer.toBinaryString(decimalNumber);
        while (result.length() < 8){
            result = "0"+result;
        }
        return result;
    }

    public static boolean[] binaryStringToBooleanArray(String binaryString) {
        boolean[] booleanArray = new boolean[8];

        // Проверяем, чтобы строка представляла двоичное число длиной 8
        if (binaryString.length() != 8) {
            throw new IllegalArgumentException("Строка должна содержать 8 символов");
        }

        // Преобразуем строку в массив boolean
        for (int i = 0; i < 8; i++) {
            char ch = binaryString.charAt(i);
            if (ch == '0') {
                booleanArray[i] = false;
            } else if (ch == '1') {
                booleanArray[i] = true;
            } else {
                throw new IllegalArgumentException("Неверный символ в строке");
            }
        }
        return booleanArray;
    }


    public static void add_3_ASCIIBytes(byte[] byteBlock){
        for(int i=0; i<byteBlock.length; i++){
            String binary = decimalToBinary((int)byteBlock[i]);
            boolean[] bitsFromString = binaryStringToBooleanArray(binary);
            for(int h = 0; h<bitsFromString.length; h++){
                bits.add(bitsFromString[h]);
            }
        }
    }
    public static byte[] get_4_Base64Bytes(){
        byte[] base64Bytes = new byte[4];
        boolean[] byteInBool1 = new boolean[6];
        boolean[] byteInBool2 = new boolean[6];
        boolean[] byteInBool3 = new boolean[6];
        boolean[] byteInBool4 = new boolean[6];
        int i=0;
        for(int h=0; h<6; h++){byteInBool1[h] = bits.get(i); i++;}
        for(int h=0; h<6; h++){byteInBool2[h] = bits.get(i); i++;}
        for(int h=0; h<6; h++){byteInBool3[h] = bits.get(i); i++;}
        for(int h=0; h<6; h++){byteInBool4[h] = bits.get(i); i++;}
        //-------------------------------------------------------
        String bitString1 = "";
        for(boolean bit : byteInBool1) {
            bitString1 += bit ? "1" : "0";
        }
        char ch1 = BASE64TABLE.getCharByByte(bitString1);
        base64Bytes[0] = (byte) ch1;
        //-------------------------------------------------------
        String bitString2 = "";
        for(boolean bit : byteInBool2) {
            bitString2 += bit ? "1" : "0";
        }
        char ch2 = BASE64TABLE.getCharByByte(bitString2);
        base64Bytes[1] = (byte) ch2;
        //-------------------------------------------------------
        String bitString3 = "";
        for(boolean bit : byteInBool3) {
            bitString3 += bit ? "1" : "0";
        }
        char ch3 = BASE64TABLE.getCharByByte(bitString3);
        base64Bytes[2] = (byte) ch3;
        //-------------------------------------------------------
        String bitString4 = "";
        for(boolean bit : byteInBool4) {
            bitString4 += bit ? "1" : "0";
        }
        char ch4 = BASE64TABLE.getCharByByte(bitString4);
        base64Bytes[3] = (byte) ch4;
        //-------------------------------------------------------
        bits.clear();
        return base64Bytes;
    }
    public static byte[] convert_base64_4_bytes_to_3_ASCII(byte[] bytes){
        String all_24_bits = "";
        for(int b=0; b<4; b++) if(bytes[b]=='=') bytes[b] = 'A';
        List<String> binary_8_Codes = new ArrayList<>();
        for(int i=0; i<4; i++) {
            byte n = bytes[i];
            char m = (char) n;
            all_24_bits += (BASE64TABLE.getBinaryByChar((char) bytes[i]));
        }
        binary_8_Codes.add(all_24_bits.substring(0,8));
        binary_8_Codes.add(all_24_bits.substring(8,16));
        binary_8_Codes.add(all_24_bits.substring(16,24));
        byte[] codes = new byte[3];
        for(int i=0; i<3; i++){
            codes[i] = (byte)Integer.parseInt(binary_8_Codes.get(i),2);
        }
        return codes;
    }

}
