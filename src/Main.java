import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String DIR_PATH = "D:\\test";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        File dir = new File(DIR_PATH);
        if (!dir.isDirectory()) {
            System.out.println("Please specify a valid directory path");
            return;
        }

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        Map<String, File> images = new HashMap<>();
        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".jpg") && !file.getName().endsWith(".png")) {
                continue;
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] fileBytes = new byte[(int) file.length()];
                fis.read(fileBytes);
                byte[] digest = messageDigest.digest(fileBytes);

                StringBuilder hashText = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    hashText.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
                }
                String fileHash = hashText.toString();

                if (images.containsKey(fileHash)) {
                    Files.delete(file.toPath());
                    System.out.println("Deleted identical image: " + file.getName());
                } else {
                    images.put(fileHash, file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total unique images: " + images.size());
    }
}