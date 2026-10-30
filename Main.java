
import java.util.*;
import java.nio.file.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            System.out.println("1. Encrypt File");
            System.out.println("2. Decrypt File");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter input file path: ");
            String input = sc.nextLine();

            System.out.print("Enter output file path: ");
            String output = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            if (password.length() < 8) {
                System.out.println("Password must have 8 characters.");
                return;
            }

            if (choice == 1) {
                encrypt(input, output, password);
            } 
            else if (choice == 2) {
                decrypt(input, output, password);
            } 
            else {
                System.out.println("Invalid choice!");
            }

        } catch (Exception e) {
            System.out.println("Error: Wrong password or file problem.");
        }
    }

    static SecretKey getKey(String password, byte[] salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), salt, 150000, 128);

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] key = factory.generateSecret(spec).getEncoded();
        spec.clearPassword();

        return new SecretKeySpec(key, "AES");
    }

    static void encrypt(String input, String output, String password)
            throws Exception {

        byte[] data = Files.readAllBytes(Paths.get(input));

        byte[] salt = new byte[16];
        byte[] iv = new byte[12];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        random.nextBytes(iv);

        SecretKey key = getKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key,
                new GCMParameterSpec(128, iv));

        byte[] encrypted = cipher.doFinal(data);

        byte[] result = new byte[28 + encrypted.length];

        System.arraycopy(salt, 0, result, 0, 16);
        System.arraycopy(iv, 0, result, 16, 12);
        System.arraycopy(encrypted, 0, result, 28,
                encrypted.length);

        Files.write(Paths.get(output), result);

        System.out.println("File encrypted successfully!");
    }

    static void decrypt(String input, String output, String password)
            throws Exception {

        byte[] data = Files.readAllBytes(Paths.get(input));

        if (data.length < 44) {
            throw new Exception("Invalid encrypted file");
        }

        byte[] salt = Arrays.copyOfRange(data, 0, 16);
        byte[] iv = Arrays.copyOfRange(data, 16, 28);
        byte[] encrypted = Arrays.copyOfRange(data, 28, data.length);

        SecretKey key = getKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key,
                new GCMParameterSpec(128, iv));

        byte[] original = cipher.doFinal(encrypted);

        Files.write(Paths.get(output), original);

        System.out.println("File decrypted successfully!");
    }
}
