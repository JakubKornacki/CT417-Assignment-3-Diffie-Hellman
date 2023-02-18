import java.math.BigInteger;
import java.util.*;

public class Main {
    private int primeNumber;
    private int primitiveRoot;
    private int aliceSecretNumber;
    private int bobSecretNumber;
    private int lowerBound;
    private int upperBound;
    public static void main(String args[]) {
        if(args.length < 2 || args[0].isEmpty() || args[1].isEmpty()) {
            System.out.println("Incorrect command line arguments.");
            System.exit(1);
        }
        Main main = new Main();
        // upper and lower bounds for the prime number
        int lowerBound = Integer.parseInt(args[0]);
        int upperBound = Integer.parseInt(args[1]);
        main.start(lowerBound, upperBound);

    }


    private void start(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        // publicly known primitive root and prime number
        int[] publicPrimeAndPrimitiveRoot = generatePrimeAndPrimitiveRootPair();

        //  Problem 2 Part A:
        System.out.println("Problem 2 Part A:");
        // users
        User alice = new User(publicPrimeAndPrimitiveRoot);
        User bob = new User(publicPrimeAndPrimitiveRoot);
        User mallory = new User(publicPrimeAndPrimitiveRoot);

        // let's pretend Alice and Bob are exchanging their public keys and Mallory intercepts them
        mallory.addToListOfPublicKeys(alice.getPublicKey(), "Alice");
        mallory.addToListOfPublicKeys(bob.getPublicKey(), "Bob");

        // mallory sends her public key claiming that she is either Alice or Bob (They both receive the same key which is Mallory's public key)
        // Alice and Bob generate their private keys based on the public key passed by Mallory thinking that they're agreeing with each other only
        alice.generatePrivateKeyToPeer(mallory.getPublicKey(), "Bob");
        bob.generatePrivateKeyToPeer(mallory.getPublicKey(), "Alice");

        // mallory calculates her private keys to Alice and Bob based on the original public keys
        mallory.generatePrivateKeyToPeer(alice.getPublicKey(), "Alice");
        mallory.generatePrivateKeyToPeer(bob.getPublicKey(), "Bob");
        // Alice and Mallory should have the same private keys now but Alice thinks she is talking to Bob
        System.out.println("Alice's and Mallory's private keys:");
        System.out.println("Alice's private key but Alice thinks she is talking to Bob: " + alice.getPrivateKey("Bob"));
        System.out.println("Mallory's private key: " + mallory.getPrivateKey("Alice"));
        // Bob and Mallory should have the same private keys now but Bob thinks he is talking to Alice
        System.out.println("\n");
        System.out.println("Bob's and Mallory's private keys:");
        System.out.println("Bob's private key but Bob thinks he is talking to Alice: " + bob.getPrivateKey("Alice"));
        System.out.println("Mallory's private key: " + mallory.getPrivateKey("Bob"));

        // Problem 2 Part B:
        System.out.println("\nProblem 2 Part B:");
        // generate new users alice1 and bob1 to start from scratch
        User alice1 = new User(publicPrimeAndPrimitiveRoot);
        User bob1 = new User(publicPrimeAndPrimitiveRoot);

        // generate the secret number by brute force attack
        int aliceSecretNumber = retrieveSecretNumber(alice1.getPublicKey(), publicPrimeAndPrimitiveRoot);
        System.out.println("Alice's secret number: " + alice1.getSecretNumber());
        System.out.println("Alice's secret number calculated from Alice's public key, prime and primitive root: " + aliceSecretNumber + "\n");

        // generate the secret number by brute force attack
        int bobSecretNumber = retrieveSecretNumber(bob1.getPublicKey(), publicPrimeAndPrimitiveRoot);
        System.out.println("Bob's secret number: " + bob1.getSecretNumber());
        System.out.println("Bob's secret number calculated from Bob's public key, prime and primitive root: " + bobSecretNumber);


    }
    private int retrieveSecretNumber(BigInteger userPublicKey, int[] publicVariables) {
        // try out all possible secret numbers in range 1 to prime -1
        for(int secretNumberCandidate = 1; secretNumberCandidate < publicVariables[0] -1; secretNumberCandidate++) {
            // calculate the public key candidate using the same formula as the public key would be calculated with the actual secret key
            BigInteger publicKeyCandidate = BigInteger.valueOf(publicVariables[1]).modPow(BigInteger.valueOf(secretNumberCandidate), BigInteger.valueOf(publicVariables[0]));
            // if the public key candidate equal the intercepted public key we know that the secret number used to generate this public key is equal to our secret number candidate
            // since this is the only variable that changes, prime and primitive roots remain the same both for the actual public key generation and for this brute force attack
            if(publicKeyCandidate.equals(userPublicKey)) {
                return secretNumberCandidate;
            }
        }
        // else return -1 if the secret number has not been found
        return -1;
    }

    private int[] generatePrimeAndPrimitiveRootPair() {
        // generate new candidates until a prime number is found
        do {
            primeNumber = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
        } while (!isPrime(primeNumber));
        // generate new candidates until a primitive root of the above prime  number is found
        do {
            // primitive root needs to be in range (1 to prime-1)
            primitiveRoot = (int) ((Math.random() * (primeNumber-1) - 1) + 1);
        } while (!isPrimitiveRoot(primitiveRoot, primeNumber));

        return new int[] {primeNumber,primitiveRoot};
    }




    public boolean isPrimitiveRoot(int primitive, int prime) {
        // hash set to store unique values
        HashSet<BigInteger> set = new HashSet<>();
        // wrap the variables in this method to BigIntegers as int or long values would not suffice
        // since this approach generates very large numbers, for example, 8996 to the power of 15642
        BigInteger localPrime = BigInteger.valueOf(prime);
        BigInteger localPrimitive = BigInteger.valueOf(primitive);
        // loop from (1 to prime-1), this is the exponent part used in the below function
        for(int exponent = 1; exponent < prime; exponent++) {
            // raise the primitive root candidate by the power of the exponent (loop iterator) and apply the modulus operator with the value of the wrapped prime number on the result
            BigInteger temp = localPrimitive.modPow(BigInteger.valueOf(exponent), localPrime);
            // add the result to the set, if the add method returns a false then this element already exists in the set and the current temp is a duplicate
            // the set should contain all relative primes to the prime numbers which are in range (1 to prime - 1) and these should be unique
            // if this occurred then this primitive root candidate is not a primitive root of the prime
            if(!set.add(temp)) {
                return false;
            }
        }
        // if this statement is reached then it is certain that this primitive root candidate is a primitive root
        return true;
    }

    private boolean isPrime(int number) {

        // 2 is the only prime number that is even and therefore the test is false for all other even numbers
        if(number == 2) {
            return true;
        }  else if(number % 2 == 0)  {
            return false;
        } else if (number <= 1) {
            return false;
        }

        // loop from 2 until the half of the number
        for(int j = 2; j <= number / 2 ; j++) {
            // if any number along the loop divides the without leaving any remainder then the number is not prime
            if(number % j == 0) {
                return false;
            }
        }
        // if all numbers along the loop divided the number with leaving remainder then the number was is only divisible by 1 and itself which means it is prime
        return true;
    }
}