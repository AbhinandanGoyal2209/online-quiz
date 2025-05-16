



public class User implements Comparable<User> {
    private final String username;
    private int score;

    public User(String username) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
        this.score = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to add cannot be negative: " + points);
        }
        this.score += points;
        // Ensure score doesn't go below 0 (in case of future deductions)
        if (this.score < 0) {
            this.score = 0;
        }
    }

    @Override
    public int compareTo(User other) {
        return Integer.compare(other.score, this.score); 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", score=" + score +
               '}';
    }
}
















