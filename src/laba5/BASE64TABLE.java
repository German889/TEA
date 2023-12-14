package laba5;

import java.util.HashMap;
import java.util.Map;

public class BASE64TABLE {

    private static Map<String, String> base64ToAscii = new HashMap<>();
    private static Map<String, String> invertedMap = new HashMap<>();

    static{
        base64ToAscii.put("000000", "A");
        base64ToAscii.put("000001", "B");
        base64ToAscii.put("000010", "C");
        base64ToAscii.put("000011", "D");
        base64ToAscii.put("000100", "E");
        base64ToAscii.put("000101", "F");
        base64ToAscii.put("000110", "G");
        base64ToAscii.put("000111", "H");
        base64ToAscii.put("001000", "I");
        base64ToAscii.put("001001", "J");
        base64ToAscii.put("001010", "K");
        base64ToAscii.put("001011", "L");
        base64ToAscii.put("001100", "M");
        base64ToAscii.put("001101", "N");
        base64ToAscii.put("001110", "O");
        base64ToAscii.put("001111", "P");
        base64ToAscii.put("010000", "Q");
        base64ToAscii.put("010001", "R");
        base64ToAscii.put("010010", "S");
        base64ToAscii.put("010011", "T");
        base64ToAscii.put("010100", "U");
        base64ToAscii.put("010101", "V");
        base64ToAscii.put("010110", "W");
        base64ToAscii.put("010111", "X");
        base64ToAscii.put("011000", "Y");
        base64ToAscii.put("011001", "Z");
        base64ToAscii.put("011010", "a");
        base64ToAscii.put("011011", "b");
        base64ToAscii.put("011100", "c");
        base64ToAscii.put("011101", "d");
        base64ToAscii.put("011110", "e");
        base64ToAscii.put("011111", "f");
        base64ToAscii.put("100000", "g");
        base64ToAscii.put("100001", "h");
        base64ToAscii.put("100010", "i");
        base64ToAscii.put("100011", "j");
        base64ToAscii.put("100100", "k");
        base64ToAscii.put("100101", "l");
        base64ToAscii.put("100110", "m");
        base64ToAscii.put("100111", "n");
        base64ToAscii.put("101000", "o");
        base64ToAscii.put("101001", "p");
        base64ToAscii.put("101010", "q");
        base64ToAscii.put("101011", "r");
        base64ToAscii.put("101100", "s");
        base64ToAscii.put("101101", "t");
        base64ToAscii.put("101110", "u");
        base64ToAscii.put("101111", "v");
        base64ToAscii.put("110000", "w");
        base64ToAscii.put("110001", "x");
        base64ToAscii.put("110010", "y");
        base64ToAscii.put("110011", "z");
        base64ToAscii.put("110100", "0");
        base64ToAscii.put("110101", "1");
        base64ToAscii.put("110110", "2");
        base64ToAscii.put("110111", "3");
        base64ToAscii.put("111000", "4");
        base64ToAscii.put("111001", "5");
        base64ToAscii.put("111010", "6");
        base64ToAscii.put("111011", "7");
        base64ToAscii.put("111100", "8");
        base64ToAscii.put("111101", "9");
        base64ToAscii.put("111110", "+");
        base64ToAscii.put("111111", "/");
        for (Map.Entry<String, String> entry : base64ToAscii.entrySet()) {
            invertedMap.put(entry.getValue(), entry.getKey());
        }
    }
    public static char getCharByByte(String byte_6_bits){
        char answer = base64ToAscii.get(byte_6_bits).charAt(0);
        return answer;
    }
    public static String getBinaryByChar(char c){
        String answer = invertedMap.get(""+c);
        return answer;
    }
}
