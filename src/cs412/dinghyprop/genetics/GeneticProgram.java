package cs412.dinghyprop.genetics;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * GP overseer.
 */
public final class GeneticProgram {
    public enum INIT_POP_METHOD { GROW, FILL, RHALF_AND_HALF }

    private static final Set<String> arithmetics = new HashSet<String>(Arrays.asList("+", "-", "*", "/", "^"));
    private static final Set<String> comparitors = new HashSet<String>(Arrays.asList("<", "<=", ">", ">=", "==", "!="));

    private static final Set<String> navigators = new HashSet<String>(Arrays.asList("(move)", "(turn-left)", "(turn-right)"));

    private static final Set<String> hazards = new HashSet<String>(Arrays.asList("front", "short-left", "short-right", "left", "right", "rear"));
    private static final Set<String> positions = new HashSet<String>(Arrays.asList("position-x", "position-y", "goal-position-x", "goal-position-y", "heading"));

    private static final Object[] functions = {arithmetics};
    private static final Object[] terminals = {navigators, hazards, positions};

    private String[] population;
    private int populationSize;
    private Random rand = new SecureRandom();

    public GeneticProgram(int populationSize, INIT_POP_METHOD method, int maxDepth) {
        this.populationSize = populationSize;
        population = new String[populationSize];
        switch (method) {
            case GROW:
                for (int i = 0; i < population.length; i++) {
                    population[i] = grow(maxDepth);
                }
                break;
            case FILL:
                for (int i = 0; i < population.length; i++) {
                    population[i] = fill(maxDepth);
                }
                break;
            case RHALF_AND_HALF:
                rampedHalfAndHalf(maxDepth);
                break;
        }
    }

    private String randomTerminal() {
        Set<String> termSet = (Set<String>) terminals[rand.nextInt(terminals.length)];
        return (String) termSet.toArray()[rand.nextInt(termSet.size())];
    }

    private String randomFunction() {
        Set<String> termSet = (Set<String>) functions[rand.nextInt(functions.length)];
        return (String) termSet.toArray()[rand.nextInt(termSet.size())];
    }

    private String randomComparison() {
        return (String) comparitors.toArray()[rand.nextInt(comparitors.size())];
    }

    private String grow(int maxHeight) {
        String res = grow_help(maxHeight);
        if (!res.startsWith("(") && !res.endsWith(")")) {
            res = "(+ 0 " + res + ')';
        }
        return res;
    }

    private String grow_help(int maxHeight) {
        if (maxHeight == 1 || rand.nextBoolean()) {
            return randomTerminal();
        }
        int nextMax = maxHeight - 1;
        return '(' + randomFunction() + ' ' + grow(nextMax) + ' ' + grow(nextMax) + ')';
    }

    private String fill(int maxHeight) {
        if (maxHeight == 1) {
            return randomTerminal();
        }
        int nextMax = maxHeight - 1;
        return '(' + randomFunction() + ' ' + fill(nextMax) + ' ' + fill(nextMax) + ')';
    }

    private void rampedHalfAndHalf(int maxDepth) {
        int half = population.length / 2;
        int i = 0;
        while (i < half) {
            population[i] = grow(maxDepth);
            i++;
        }
        while (i < population.length) {
            population[i] = fill(maxDepth);
            i++;
        }
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public static void main(String[] args) {
        GeneticProgram gp = new GeneticProgram(10, INIT_POP_METHOD.FILL, 10);
        for (String ind : gp.population) {
            System.out.println(ind);
        }
    }
}
