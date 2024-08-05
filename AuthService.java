public class AuthService {
    private static final String USERNAME = "Harsh Sharma";
    private static final String PASSWORD = "harsh@0000";

    public boolean authenticate(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}
