import java.util.*;

public class Main {


    ArrayList<Integer> primeNumbers;
    LinkedHashMap<Integer, ArrayList<Integer>> primesAndPrimitiveRoots;
    private int prime;
    private int primitiveRootOfPrime;


    public static void main(String args[]) {
        if(args.length < 2 || args[0].isEmpty() || args[1].isEmpty()) {
            System.out.println("Incorrect command line arguments.");
            System.exit(1);
        }
        Main main = new Main();
        int lowerBound = Integer.parseInt(args[0]);
        int upperBound = Integer.parseInt(args[1]);

        main.start(lowerBound, upperBound);

    }


    private void start(int lowerBound, int upperBound) {
        //primeNumbers = new ArrayList<>(Arrays.asList(2,3,5,7,11,13,17,19,23));
        //primeNumbers = getPrimeNumbers(lowerBound, upperBound);
        //primesAndPrimitiveRoots = getPrimitiveRoots(primeNumbers);
 /*
        do {
            prime = (int) ((Math.random() * (upperBound - lowerBound)) + lowerBound);
        } while (!isPrime(prime)); */
    }


     private ArrayList<Integer> getPrimeNumbers(int lowerBound, int upperBound) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        if((lowerBound > upperBound) || (upperBound < lowerBound) || (lowerBound < 2) || (upperBound < 0) ) {
            System.out.println("Incorrectly specified range.");
            System.exit(1);
        }


        if(lowerBound == 2) {
            temp.add(2);
        }

        boolean prime;

        for(int i = lowerBound; i <= upperBound; i++) {
            prime = true;
            // check all numbers that are odd (all prime numbers are odd apart from number 2)
            if(i % 2 != 0){
               for(int j = 2; j < i / 2 ; j++) {
                   if(i % j == 0) {
                       prime = false;
                       break;
                   }
               }
               if(prime) {
                   temp.add(i);
                }
            }
        }

        return temp;
    }


    private void printPrimesAndPrimitiveRoots() {
        Integer[] arr;
        for(Map.Entry<Integer, ArrayList<Integer>> entry : primesAndPrimitiveRoots.entrySet())  {
            System.out.println("Prime number: " + entry.getKey() + " has the following primitive roots:");
            for(int i = 0; i < entry.getValue().size(); i++) {
                System.out.println(entry.getValue().get(i));
            }
        }
    }

    private LinkedHashMap<Integer, ArrayList<Integer>> getPrimitiveRoots(ArrayList<Integer> primeNumbers) {
        HashSet<Integer> set = new HashSet<Integer>();
        LinkedHashMap<Integer, ArrayList<Integer>> primesAndPrimitiveRoots = new LinkedHashMap<Integer, ArrayList<Integer>>();

        int primeNumber;
        int setEntry;
        boolean flag;
        for(int i = 0; i < primeNumbers.size(); i++) {
            primeNumber = primeNumbers.get(i);
            for (int primitiveRootCandidate = 1; primitiveRootCandidate <= primeNumber; primitiveRootCandidate++) {
                flag = true;
                set.clear();
                for (int squared = 1; squared < primeNumber; squared++) {
                    setEntry = ((int) (Math.pow(primitiveRootCandidate, squared) % primeNumber));
                    if (!set.add(setEntry)) {
                        flag = false;
                        set.clear();
                        break;
                    }
                }
                if (flag) {
                    if (primesAndPrimitiveRoots.containsKey(primeNumber)) {
                        primesAndPrimitiveRoots.get(primeNumber).add(primitiveRootCandidate);
                    } else {
                        primesAndPrimitiveRoots.put(primeNumber, new ArrayList<>(Arrays.asList(primitiveRootCandidate)));
                    }
                }
            }
        }
        return primesAndPrimitiveRoots;
    }
}
