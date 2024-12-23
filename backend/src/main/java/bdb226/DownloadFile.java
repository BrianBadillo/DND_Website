package bdb226;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64; // Import Base64 class for encoding

/* Class to demonstrate use-case of drive's download file. */
public class DownloadFile {
    private static final String CREDENTIALS_ENV_VAR = "GOOGLE_CREDENTIALS_JSON";

    /**
     * Download a Document file in PDF format and return it as a Base64 encoded
     * string.
     *
     * @param realFileId file ID of any workspace document format file.
     * @return Base64 encoded string if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String download(String realFileId) throws IOException {
        String credentialsJson = System.getenv(CREDENTIALS_ENV_VAR);
        if (credentialsJson == null) {
            throw new IOException("Missing environment variable: " + CREDENTIALS_ENV_VAR);
        }
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credentials);

        // Build a new authorized API client service.
        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Drive samples")
                .build();

        try {
            OutputStream outputStream = new ByteArrayOutputStream();

            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);

            // Convert the downloaded byte array to Base64 and return the string
            byte[] fileBytes = ((ByteArrayOutputStream) outputStream).toByteArray();
            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to move file: " + e.getDetails());
            throw e;
        }
    }
}
