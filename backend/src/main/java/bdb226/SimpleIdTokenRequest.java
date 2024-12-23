package bdb226;

/**
 * Information from a request providing an ID token for authentication
 * 
 * @param idToken The encoded ID token received from the frontend client
 */
public record SimpleIdTokenRequest(String idToken) {

}