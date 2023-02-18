import java.math.BigDecimal;
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
        int lowerBound = Integer.parseInt(args[0]);
        int upperBound = Integer.parseInt(args[1]);
        main.start(lowerBound, upperBound);

    }


    private void start(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        // publicly known primitive root and prime number
        int[] publicPrimeAndPrimitiveRoot = generatePrimeAndPrimitiveRootPair();

        // generate public keys of both Alice and Bob
        BigInteger alicePublicKey = aliceCalculatePublicKey(publicPrimeAndPrimitiveRoot);
        BigInteger bobPublicKey = bobCalculatePublicKey(publicPrimeAndPrimitiveRoot);

        // generate private keys of both Alice and Bob
        BigInteger alicePrivateKey = aliceGeneratePrivateKey(bobPublicKey, publicPrimeAndPrimitiveRoot);
        BigInteger bobPrivateKey = bobGeneratePrivateKey(alicePublicKey, publicPrimeAndPrimitiveRoot);

        // show that both private keys are identical
        System.out.println("Alice private key: " + alicePrivateKey);
        System.out.println("Bob private key: " + bobPrivateKey);

    }

    private BigInteger aliceGeneratePrivateKey(BigInteger bobPublicKey, int[] publicVariables) {
        // the Alices secret key is Alice's public key raised to the power of Bob's secret number modulo the publicly known prime number
        BigInteger privateKey = bobPublicKey.modPow(BigInteger.valueOf(aliceSecretNumber), BigInteger.valueOf(publicVariables[0]));
        return privateKey;
    }

    private BigInteger bobGeneratePrivateKey(BigInteger alicePublicKey, int[] publicVariables) {
        // the secret key is Alice's public key raised to the power of Bob's secret number modulo the publicly known prime number
        BigInteger privateKey = alicePublicKey.modPow(BigInteger.valueOf(bobSecretNumber), BigInteger.valueOf(publicVariables[0]));
        return privateKey;
    }
    private BigInteger aliceCalculatePublicKey(int[] publicVariables) {
        // Alice's secret number can be any number in range (1 to prime - 1)
        int secretNumber = (int) ((Math.random() * (publicVariables[0] -1) - 1) + 1);
        aliceSecretNumber = secretNumber;
        // wrap around into BigInteger class to ensure that numeric overflow does not occur
        // generate Alice's public key by raising the primitive root to the power of the secret number modulo prime
        BigInteger alicePublicKey = BigInteger.valueOf(publicVariables[1]).modPow(BigInteger.valueOf(secretNumber), BigInteger.valueOf(publicVariables[0]));

        return alicePublicKey;
    }


    private BigInteger bobCalculatePublicKey(int[] publicVariables) {
        // Alice's secret number can be any number in range (1 to prime - 1)
        int secretNumber = (int) ((Math.random() * (publicVariables[0] -1) - 1) + 1);
        bobSecretNumber = secretNumber;
        // wrap around into BigInteger class to ensure that numeric overflow does not occur
        // generate Alice's public key by raising the primitive root to the power of the secret number modulo prime
        BigInteger alicePublicKey = BigInteger.valueOf(publicVariables[1]).modPow(BigInteger.valueOf(secretNumber), BigInteger.valueOf(publicVariables[0]));

        return alicePublicKey;
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





