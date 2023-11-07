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
        String b;
        return block;
    }
}
