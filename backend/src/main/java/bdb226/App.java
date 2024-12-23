package bdb226;

import java.net.InetSocketAddress;
// Import needed java utilities
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import io.javalin.http.Context;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 * 
 * When an HTTP client connects to this server on the default Javalin port
 * (8080),
 * and requests /hello, we return "Hello World". Otherwise, we produce an error.
 */
public class App {
    /**
     * The static database connection that allows us to do various database
     * operations on our tables
     */
    public static Database db;
    // Memcached session service and file cache service
    private static SessionService sessionService;
    private static FileCacheService fileCacheService;

    /** The default port our webserver uses. We set it to Javalin's default, 8080 */
    public static final int DEFAULT_PORT_WEBSERVER = 8080; // this should be a string
    // public static String secondary_Port = 5412;

    /**
     * The client ID for this project allowing for Google OAuth2.0 authentication
     */
    private static final String CLIENT_ID = "772038646799-d6fe7d3mkpm5unckcqdhk0eb5s2lkcr2.apps.googleusercontent.com";
    /** A JSON factory used to build a verifier for Google OAuth2.0 */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Safely gets integer value from named env var if it exists, otherwise returns
     * default
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        if (envar == null || envar.length() == 0 || System.getenv(envar.trim()) == null)
            return defaultVal;
        try (Scanner sc = new Scanner(System.getenv(envar.trim()))) {
            if (sc.hasNextInt())
                return sc.nextInt();
            else
                System.err.printf("ERROR: Could not read %s from environment, using default of %d%n", envar,
                        defaultVal);
        }
        return defaultVal;
    }

    /**
     * The main method for the program
     */
    public static void main(String[] args) throws Exception {
        // Configure MemCachier
        MemcachedClient memcachedClient = configureMemcachedClient();
        if (memcachedClient == null) {
            System.err.println("Failed to configure MemCachier. Exiting...");
            return;
        }

        // Initialize services
        sessionService = new SessionService(memcachedClient);
        fileCacheService = new FileCacheService(memcachedClient);
        main_uses_database(args); // Start the web server
    }

    /**
     * The backend webserver that connects to and uses a database to store
     * information.
     * 
     * Serves a static frontend from the build stored at /public/build (or whatever
     * is specified by STATIC_LOCATION).
     * To serve the test frontend (which currently just gives the google login
     * button and allows you to login with a user), specify
     * STATIC_LOCATION=./src/main/test_frontend
     * 
     * Either DATABASE_URI should be set, or the values of POSTGRES_{IP, PORT, USER,
     * PASS, DBNAME} to create the connection to the database.
     * 
     * Uses default port (8080), or whatever is specified by PORT.
     * 
     * Customized logger that displays the scheme (http, https, etc.), method name
     * (GET, POST, PUT, DELETE, etc.), the route/path being called, and the full URL
     * 
     * @param args The arguments provided by the user
     * 
     */
    public static void main_uses_database(String[] args) {
        /* holds connection to the database created from environment variables */
        Database db = Database.getDatabase(fileCacheService);

        // our javalin app on which most operations must be performed
        Javalin app = Javalin
                .create(
                        config -> {
                            config.staticFiles.add(staticFiles -> {
                                staticFiles.hostedPath = "/"; // change to host files on a subpath, like '/assets'
                                String static_location_override = System.getenv("STATIC_LOCATION");
                                if (static_location_override == null) { // serve from jar; files located in
                                                                        // src/main/resources/public/build
                                    staticFiles.directory = "/public/build"; // the directory where your files are
                                                                             // located
                                    staticFiles.location = Location.CLASSPATH; // Location.CLASSPATH (jar)
                                } else { // serve from filesystem
                                    System.out.println(
                                            "Overriding location of static file serving using STATIC_LOCATION env var: "
                                                    + static_location_override);
                                    staticFiles.directory = static_location_override; // the directory where your files
                                                                                      // are located
                                    staticFiles.location = Location.EXTERNAL; // Location.EXTERNAL (file system)
                                }
                                staticFiles.precompress = false; // if the files should be pre-compressed and cached in
                                                                 // memory (optimization)
                                // staticFiles.aliasCheck = null; // you can configure this to enable symlinks
                                // (= ContextHandler.ApproveAliases())
                                // staticFiles.headers = Map.of(...); // headers that will be set for the files
                                // staticFiles.skipFileFunction = req -> false; // you can use this to skip
                                // certain files in the dir, based on the HttpServletRequest
                                // staticFiles.mimeTypes.add(mimeType, ext); // you can add custom mimetypes for
                                // extensions
                            });
                            config.requestLogger.http(
                                    (ctx, ms) -> {
                                        System.out.printf("%s%n", "-".repeat(42));
                                        System.out.printf("%s\t%s\t%s%nfull url: %s%n", ctx.scheme(),
                                                ctx.method().name(), ctx.path(), ctx.fullUrl());
                                        System.out.printf("%s%n", "=".repeat(42));
                                    });
                        });

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // A basic GET route to test that the backend is running
        app.get("/helloworld", ctx -> {
            ctx.result("Hello world!");
        });
        app.post("/auth/login", ctx -> {
            System.out.printf("%s\t%s\t%s%n", ctx.scheme(), ctx.method().name(), ctx.path());
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            // (Receive idTokenString by HTTPS POST)
            SimpleIdTokenRequest req = gson.fromJson(ctx.body(), SimpleIdTokenRequest.class);
            StructuredResponse resp = null;
            String idTokenString = req.idToken();
            System.out.println(idTokenString);

            // Generate a unique session ID and store it in Memcached
            Random rand = new Random();
            String sessionId;
            int maxRetries = 5; // Prevent infinite loops in case of collisions
            boolean storedSuccessfully = false;

            for (int i = 0; i < maxRetries; i++) {
                sessionId = String.valueOf(rand.nextInt(1000000)); // Generate random session ID
                try {
                    // Attempt to store the session in Memcached
                    sessionService.storeSession(sessionId, String.valueOf(11), 3600); // 1-hour expiry
                    System.out.println("Session ID: " + sessionId);
                    storedSuccessfully = true;

                    // Create the JSON object to respond with
                    JsonObject data = new JsonObject();
                    data.addProperty("sessionId", sessionId);
                    data.addProperty("mUserId", 1);
                    resp = new StructuredResponse("ok", null, data);
                    break; // Exit the loop on success
                } catch (Exception e) {
                    System.err.println("Failed to store session in Memcached. Retrying...");
                }
            }

            if (!storedSuccessfully) {
                System.err.println("Could not generate a unique session ID after retries.");
                resp = new StructuredResponse("error", "Could not create a session", null);
            }
            /*
             * GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
             * GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
             * // Specify the CLIENT_ID of the app that accesses the backend:
             * .setAudience(Collections.singletonList(CLIENT_ID))
             * // Or, if multiple clients access the backend:
             * // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
             * .build();
             * 
             * // Verify (and decode) the ID token
             * GoogleIdToken idToken = verifier.verify(idTokenString);
             * 
             * if (idToken != null) {
             * Payload payload = idToken.getPayload();
             * 
             * // Print user identifier
             * String userIdString = payload.getSubject();
             * System.out.println("User ID: " + userIdString);
             * 
             * // Get profile information from payload
             * String email = payload.getEmail();
             * boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
             * String name = (String) payload.get("name");
             * String pictureUrl = (String) payload.get("picture");
             * String locale = (String) payload.get("locale");
             * String familyName = (String) payload.get("family_name");
             * String givenName = (String) payload.get("given_name");
             * 
             * // Use or store profile information
             * System.out.println("Email: " + email);
             * System.out.println("Email verified: " + emailVerified);
             * System.out.println("Name: " + name);
             * System.out.println("Locale: " + locale);
             * System.out.println("Family name: " + familyName);
             * System.out.println("Given name: " + givenName);
             * 
             * // Get the user data from the database based on the email, if it exists
             * UsersTable.UserData userData = db.users.selectOne(email);
             * 
             * int userId;
             * if (userData == null) { // If the user is not in the database, add them, and
             * get the new user ID back.
             * userId = db.users.insertRow(name, email, null, null, null);
             * } else { // If the user is in the database, just get their ID
             * userId = userData.mId();
             * }
             * System.out.println("User ID from database: " + userId);
             * 
             * // Generate a unique session ID and store it in Memcached
             * Random rand = new Random();
             * String sessionId;
             * int maxRetries = 5; // Prevent infinite loops in case of collisions
             * boolean storedSuccessfully = false;
             * 
             * for (int i = 0; i < maxRetries; i++) {
             * sessionId = String.valueOf(rand.nextInt(1000000)); // Generate random session
             * ID
             * try {
             * // Attempt to store the session in Memcached
             * sessionService.storeSession(sessionId, String.valueOf(userId), 3600); //
             * 1-hour expiry
             * System.out.println("Session ID: " + sessionId);
             * storedSuccessfully = true;
             * 
             * // Create the JSON object to respond with
             * JsonObject data = new JsonObject();
             * data.addProperty("sessionId", sessionId);
             * data.addProperty("mUserId", userId);
             * resp = new StructuredResponse("ok", null, data);
             * break; // Exit the loop on success
             * } catch (Exception e) {
             * System.err.println("Failed to store session in Memcached. Retrying...");
             * }
             * }
             * 
             * if (!storedSuccessfully) {
             * System.err.println("Could not generate a unique session ID after retries.");
             * resp = new StructuredResponse("error", "Could not create a session", null);
             * }
             * 
             * } else {
             * System.out.println("Invalid ID token.");
             * resp = new StructuredResponse("error",
             * "Something went wrong: Invalid ID token", null);
             * }
             * 
             */
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Start the server from either the environment variable PORT or from the
        // default port
        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER));
    }

    /**
     * Configure and return a MemcachedClient connected to MemCachier
     */
    private static MemcachedClient configureMemcachedClient() {
        try {
            String servers = System.getenv("MEMCACHIER_SERVERS");
            String username = System.getenv("MEMCACHIER_USERNAME");
            String password = System.getenv("MEMCACHIER_PASSWORD");

            if (servers == null || username == null || password == null) {
                System.err.println("MemCachier environment variables are not set properly.");
                return null;
            }

            XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers));
            builder.setCommandFactory(new BinaryCommandFactory());
            builder.addAuthInfo(AddrUtil.getOneAddress(servers), AuthInfo.plain(username, password));
            return builder.build();
        } catch (Exception e) {
            System.err.println("Error configuring Memcached client: " + e.getMessage());
            return null;
        }
    }
}
