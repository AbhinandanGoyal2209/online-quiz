// public class User implements Comparable<User> {
//     private String username;
//     private int score;

//     public User(String username) {
//         this.username = username;
//         this.score = 0;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public int getScore() {
//         return score;
//     }

//     public void addScore(int points) {
//         this.score += points;
//     }

//     @Override
//     public int compareTo(User other) {
//         return Integer.compare(other.score, this.score); // Max heap: higher score first
//     }
// }



public class User implements Comparable<User> {
    private String username;
    private int score;

    public User(String username) {
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
        this.score += points;
    }

    @Override
    public int compareTo(User other) {
        return Integer.compare(other.score, this.score); // Max heap: higher score first
    }
}