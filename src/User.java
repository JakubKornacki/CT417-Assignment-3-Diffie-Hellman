import java.math.BigInteger;
import java.util.HashMap;

public class User {

    private int[] publicVariables;
    private int secretNumber;
    private BigInteger publicKey;
    private BigInteger privateKey;
    private HashMap<String, BigInteger> listOfPrivateKeysToUser;
    private HashMap<String, BigInteger> listOfPublicKeysToUser;

    public User (int[] publicVariables) {
        this.publicVariables = publicVariables;
        generateSecretNumber();
        generatePublicKey();
        listOfPrivateKeysToUser = new HashMap<>();
        listOfPublicKeysToUser = new HashMap<>();
    }

    private void generateSecretNumber() {
        // any number in range 1 - prime -1
        secretNumber = (int) ((Math.random() * (publicVariables[0] -1) - 1) + 1);
    }

    // should be private but can stay public for test purposes
    public int getSecretNumber() {
        return secretNumber;
    }

    private void generatePublicKey() {
        // public key of this user is the primitive root raised to the power of this users secret number modulo prime
        publicKey = BigInteger.valueOf(publicVariables[1]).modPow(BigInteger.valueOf(secretNumber), BigInteger.valueOf(publicVariables[0]));
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void addToListOfPublicKeys(BigInteger peersPublicKey, String peerName) {
        if(!listOfPublicKeysToUser.containsKey(peerName)) {
            listOfPublicKeysToUser.put(peerName, peersPublicKey);
        }
    }


    public void generatePrivateKeyToPeer(BigInteger peersPublicKey, String peerName) {
        // the secret key is peer's public key raised to the power of user's secret number modulo the publicly known prime number
        privateKey = peersPublicKey.modPow(BigInteger.valueOf(secretNumber), BigInteger.valueOf(publicVariables[0]));
        if(!listOfPrivateKeysToUser.containsKey(peerName)) {
            listOfPrivateKeysToUser.put(peerName, privateKey);
        }
    }

    // should be private but can stay public for test purposes
    public BigInteger getPrivateKey(String peerName) {
        if (listOfPrivateKeysToUser.containsKey(peerName)) {
            return listOfPrivateKeysToUser.get(peerName);
        } else  {
            return null;
        }
    }

}