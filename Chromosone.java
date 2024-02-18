import java.util.Random;

/**
 * Ashu Chauhan
 * November 9, 2023
 * <p>
 * This class contains the actual Genetic Algorithm which uses evaluation methods from the Evaluation class
 * that was provided. Also, a Chromosone class which was made to store possible keys for the solution.
 */
public class Chromosone {
    private Random random; //seed
    private double fitness; //fitness which is evaluated from fitness method in evaluation class
    public char[] key; //THE KEY
    private String str = "abcdefghijklmnopqrstuvwxyz-"; //list of all possible chars for the key


    /**
     * Instantiates a new Chromosone.
     *
     * @param len    the length of the key
     * @param random the seed
     */
    public Chromosone(int len, Random random) {
        this.random = random; //seed
        key = new char[len];

        for(int i = 0; i < len; i++) {
            key[i] = str.charAt(random.nextInt(27));
        }
    }

    /**
     * Instantiates a new Chromosone.
     *
     * @param other the chromosone to copy
     */
    public Chromosone(Chromosone other) { //cloning a chromosone
        this.key = other.key.clone();
        this.fitness = other.fitness;
    }


    /**
     * Gets fitness.
     *
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Sets fitness.
     *
     * @param in the in
     */
    public void setFitness(double in) {
        fitness = in;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        String out = "";
        for(char i: key) {
            out += i;
        }
        return out;
    }


}
