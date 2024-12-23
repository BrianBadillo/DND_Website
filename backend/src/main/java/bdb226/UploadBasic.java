package bdb226;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Arrays;

/* Class to demonstrate use of Drive insert file API */
public class UploadBasic {
    private static final String CREDENTIALS_ENV_VAR = "GOOGLE_CREDENTIALS_JSON";
    private static final String FOLDER_ID = "1wdPnFsRv7Q0N9iN6BXVjDoscjYHawEdU";

    /**
     * Upload new file.
     *
     * @param base64String The Base64 encoded file content.
     * @return Inserted file metadata if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String uploadBasic(String base64String) throws IOException {
        // Decode the Base64 string into bytes
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // Create a temporary file
        java.io.File tempFile = java.io.File.createTempFile("upload", ".tmp");
        tempFile.deleteOnExit(); // Ensure temp file is deleted when the JVM exits

        // Write the decoded bytes to the temporary file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            System.err.println("Failed to write to temporary file: " + e.getMessage());
            throw e;
        }

        String credentialsJson = System.getenv(CREDENTIALS_ENV_VAR);
        if (credentialsJson == null) {
            throw new IOException("Missing environment variable: " + CREDENTIALS_ENV_VAR);
        }
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));

        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        // Build a new authorized API client service
        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(), requestInitializer)
                .setApplicationName("Drive samples")
                .build();

        // Set file metadata
        File fileMetadata = new File();
        fileMetadata.setName("uploaded_file.jpg");
        fileMetadata.setParents(Arrays.asList(FOLDER_ID));

        // Specify media type and file-path for file
        FileContent mediaContent = new FileContent("image/jpeg", tempFile);

        try {
            // Upload the file to Google Drive
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // Handle error
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        String base64String = "<your_base64_string_here>"; // Replace with your Base64 string
        uploadBasic(base64String);
    }
}