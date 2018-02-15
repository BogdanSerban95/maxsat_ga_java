import java.util.Random;

public class GeneticAlgorithm {

    public static String randomBitString(int length) {
        StringBuilder bitString = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int bit = rnd.nextInt(2);
            bitString.append(bit);
        }
        return bitString.toString();
    }
}
