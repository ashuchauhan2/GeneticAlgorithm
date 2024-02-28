import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Ashu Chauhan
 * November 9, 2023
 * <p>
 * This class contains the actual Genetic Algorithm which uses evaluation methods from the Evaluation class
 * that was provided. Also, a Chromosone class which was made to store possible keys for the solution.
 */
public class Main {

    int gen = 0; //generation number
    int length; //key length
    ArrayList<Chromosone> elites = new ArrayList<>(); //elites
    ArrayList<Chromosone> pop = new ArrayList<>(); //population
    String text; //the string of text we are decrypting
    Random rndm; //seed
    Scanner sc; //reading file
    Chromosone e; //used to populate the population and as a useful chromosone throughout haha
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz-"; //ALL POSSIBLE LETTERS

    /**
     * Instantiates a new Main.
     *
     * @param seed          the seed
     * @param crossoverRate the crossover rate
     * @param mutationRate  the mutation rate
     * @throws FileNotFoundException the file not found exception
     */
    public Main(int seed, double crossoverRate, double mutationRate) throws FileNotFoundException {
        rndm = new Random(seed); // Initialize Random with the seed
        readFile("src/DataX.txt"); //CHANGE Data1.txt TO Data2.txt to change datasets.
        genInitPopulation();
        GeneticAlgorithm(crossoverRate, mutationRate);
    }


    /**
     * @param filePath, enter data1.txt or data2.txt
     * @throws FileNotFoundException
     */

    private void readFile(String filePath) throws FileNotFoundException {
        sc = new Scanner(new File(filePath));

        // Read the first line as an integer
        length = sc.nextInt();

        // Read the rest of the file as a string
        sc.nextLine(); // Move to the next line to skip the integer
        StringBuilder txt = new StringBuilder();
        while (sc.hasNext()) {
            txt.append(sc.nextLine());
        }

        text = txt.toString();

        sc.close();
    }

    private void GeneticAlgorithm(double crossoverRate, double mutationRate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select crossover method: (1) One Point Crossover, (2) Uniform Crossover");
        int crossoverMethod = scanner.nextInt();
        int maxGenerations = 127;
        while (gen < maxGenerations) {
            getElites();
            System.out.println("Generation: " + gen +
                    ", Best Key: " + elites.get(0).getKey() +
                    ", Fitness: " + elites.get(0).getFitness());

            ArrayList<Chromosone> newPopulation = new ArrayList<>();
            newPopulation.addAll(elites); // Keep the elites

            while (newPopulation.size() < pop.size()) {
                Chromosone parent1 = tournamentSelection();
                Chromosone parent2 = tournamentSelection();
                Chromosone child;

                // Decide whether to crossover based on the crossover rate
                if (rndm.nextDouble() < crossoverRate) {
                    // Use the selected crossover method
                    if (crossoverMethod == 1) {
                        child = onePointCrossover(parent1, parent2);
                    } else if (crossoverMethod == 2) {
                        child = uniformCrossover(parent1, parent2);
                    } else {
                        child = onePointCrossover(parent1, parent2); //default to onePointCrossover if input is not 1 or 2
                    }
                } else {
                    // If not crossing over, randomly choose one of the parents to clone
                    child = rndm.nextBoolean() ? new Chromosone(parent1) : new Chromosone(parent2);
                }
                mutate(child, mutationRate);
                child.setFitness(Evaluation.fitness(child.getKey(), text));
                newPopulation.add(child);
            }

            pop = newPopulation; // Replace the old population with the new one
            gen++;
        }
        scanner.close();
    }



    private Chromosone tournamentSelection() {
        Chromosone candidate1 = pop.get(rndm.nextInt(pop.size())); //random can1 selected
        Chromosone candidate2 = pop.get(rndm.nextInt(pop.size())); //random can2 selected

        while (candidate1 == candidate2) {
            candidate2 = pop.get(rndm.nextInt(pop.size())); //incase can1 and can2 are the same, select a diff can2
        }

        return (candidate1.getFitness() < candidate2.getFitness()) ? candidate1 : candidate2;
    }


    private ArrayList genInitPopulation() {
        for(int i = 0; i<5000; i++) { //5000 chromies are created for the init pop
            e = new Chromosone(length, rndm);
            e.setFitness(Evaluation.fitness(e.getKey(), text)); //setting each ones fitness
            pop.add(e); //adding to pop arraylist
        }

        return pop;
    }

    private void getElites() {

        elites.clear(); //to ensure we aren't adding the same ones into the new population with each iteration

        Chromosone e1 = pop.get(0);
        Chromosone e2 = pop.get(1);

        for(int i = 2; i < pop.size(); i++) {
            if(pop.get(i).getFitness() < e1.getFitness()) {
                e2 = e1; // e1 is no longer the best, so it becomes the second best
                e1 = pop.get(i); // pop.get(i) is now the best
            } else if (pop.get(i).getFitness() < e2.getFitness()) {
                e2 = pop.get(i); // pop.get(i) is better than the second best, so it replaces it
            }
        }
        elites.add(e1);
        elites.add(e2);
    }

    private Chromosone onePointCrossover(Chromosone mother, Chromosone father) {
        Chromosone child = new Chromosone(length, rndm);
        int crossoverPoint = rndm.nextInt(length); // Choose a crossover point
        for (int i = 0; i < crossoverPoint; i++) {
            child.key[i] = mother.key[i]; // Take the first part from the mother
        }
        for (int i = crossoverPoint; i < length; i++) {
            child.key[i] = father.key[i]; // Take the second part from the father
        }
        return child;
    }

    private Chromosone uniformCrossover(Chromosone parent1, Chromosone parent2) {
        Chromosone child = new Chromosone(parent1.key.length, rndm);
        for (int i = 0; i < child.key.length; i++) {
            child.key[i] = rndm.nextBoolean() ? parent1.key[i] : parent2.key[i]; //random gene chosen from each of the parents
        }
        return child;
    }



    private void mutate(Chromosone individual, double mutationRate) {
        for (int i = 0; i < individual.key.length; i++) {
            if (rndm.nextDouble() < mutationRate) { //percentage to mutate
                individual.key[i] = alphabet.charAt(rndm.nextInt(alphabet.length())); //mutation
            }
        }
    }


    /**
     *
     * @param args the input arguments
     * @throws FileNotFoundException the file not found exception
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a seed value for the random number generator: ");
        int seed = scanner.nextInt(); // Read the seed value from the console
        System.out.print("Enter crossover rate (0.1 for 10%): ");
        double crossoverRate = scanner.nextDouble(); //read crossover rate
        System.out.print("Enter mutation rate (0.01 for 1%): ");
        double mutationRate = scanner.nextDouble(); //read mutation rate

        Main e = new Main(seed, crossoverRate, mutationRate);

        System.out.println(Evaluation.decrypt(e.elites.get(0).getKey(), e.text));
    }

}
