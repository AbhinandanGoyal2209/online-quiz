
import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 12345;
    private static final int QUESTIONS_PER_USER = 5;
    private static final int NUM_USERS = 4;
    private static final String RESULTS_FILE = "quiz_results.txt"; // File to store results
    private static ArrayList<Question> questionPool = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<List<Question>> userQuestions = new ArrayList<>();
    private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
    private static PrintWriter clientOut;
    private static BufferedReader clientIn;

    public static void main(String[] args) throws IOException {
        // Initialize 20 advanced Java questions
        questionPool.add(new Question(
            "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
            new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
            2
        ));
        questionPool.add(new Question(
            "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
            new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
            1
        ));
        questionPool.add(new Question(
            "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
            new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
            2
        ));
        questionPool.add(new Question(
            "What happens when an uncaught checked exception is thrown in a thread?",
            new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
            0
        ));
        questionPool.add(new Question(
            "Which Java 8 feature allows functional interfaces to be implemented inline?",
            new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
            1
        ));
        questionPool.add(new Question(
            "In generics, what is type erasure?",
            new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
            0
        ));
        questionPool.add(new Question(
            "Which design pattern ensures a class has only one instance and provides a global access point?",
            new String[]{"Factory", "Observer", "Singleton", "Adapter"},
            2
        ));
        questionPool.add(new Question(
            "What is the purpose of the `volatile` keyword in Java?",
            new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
            1
        ));
        questionPool.add(new Question(
            "Which `ExecutorService` method submits a task and returns a `Future` object?",
            new String[]{"execute()", "submit()", "run()", "invokeAll()"},
            1
        ));
        questionPool.add(new Question(
            "What is the difference between `throws` and `throw` in exception handling?",
            new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
            0
        ));
        questionPool.add(new Question(
            "Which class is used to create a fixed-size thread pool?",
            new String[]{"ThreadPoolExecutor", "ScheduledExecutorService", "Executors", "ForkJoinPool"},
            2
        ));
        questionPool.add(new Question(
            "What is the default capacity of a `HashMap` in Java?",
            new String[]{"8", "16", "32", "64"},
            1
        ));
        questionPool.add(new Question(
            "Which Stream operation is used to reduce elements to a single value?",
            new String[]{"map()", "filter()", "reduce()", "collect()"},
            2
        ));
        questionPool.add(new Question(
            "What does the `synchronized` keyword do when applied to a method?",
            new String[]{"Locks the object", "Pauses the thread", "Prevents instantiation", "Optimizes execution"},
            0
        ));
        questionPool.add(new Question(
            "Which annotation is used to mark a method for dependency injection in Spring?",
            new String[]{"@Inject", "@Autowired", "@Resource", "@Bean"},
            1
        ));
        questionPool.add(new Question(
            "What is the purpose of `Optional` in Java 8?",
            new String[]{"Handle null values", "Optimize loops", "Manage threads", "Parse JSON"},
            0
        ));
        questionPool.add(new Question(
            "Which pattern separates object construction from its representation?",
            new String[]{"Builder", "Prototype", "Facade", "Decorator"},
            0
        ));
        questionPool.add(new Question(
            "What is the output of `Integer.valueOf(127) == Integer.valueOf(127)`?",
            new String[]{"true", "false", "Compilation error", "Runtime exception"},
            0
        ));
        questionPool.add(new Question(
            "Which interface is used for custom sorting in Java?",
            new String[]{"Comparable", "Comparator", "Sortable", "Orderable"},
            1
        ));
        questionPool.add(new Question(
            "What does `CompletableFuture` provide in Java?",
            new String[]{"Thread pooling", "Asynchronous programming", "Garbage collection", "Serialization"},
            1
        ));

        // Prompt for 4 unique usernames
        Scanner scanner = new Scanner(System.in);
        Set<String> usernames = new HashSet<>();
        System.out.println("Enter 4 unique usernames:");
        while (usernames.size() < NUM_USERS) {
            System.out.print("Username " + (usernames.size() + 1) + ": ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty() || usernames.contains(username)) {
                System.out.println("Error: Username must be non-empty and unique.");
            } else {
                usernames.add(username);
                User user = new User(username);
                users.add(user);
                leaderboard.add(user);
                // Assign 5 random questions per user
                List<Question> questions = new ArrayList<>(questionPool);
                Collections.shuffle(questions);
                userQuestions.add(questions.subList(0, QUESTIONS_PER_USER));
            }
        }
        scanner.close();

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        // Accept single client connection
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Run the quiz
            runQuiz();
        } catch (IOException e) {
            System.out.println("Error accepting client: " + e.getMessage());
        } finally {
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
        }
    }

    private static void runQuiz() throws IOException {
        int round = 0;
        int totalQuestionsAnswered = 0;
        int maxQuestions = NUM_USERS * QUESTIONS_PER_USER; // 4 users * 5 questions = 20

        while (round < QUESTIONS_PER_USER && clientIn != null) {
            for (int userIndex = 0; userIndex < NUM_USERS; userIndex++) {
                if (totalQuestionsAnswered >= maxQuestions) {
                    break; // Stop if 20 questions have been answered
                }

                if (clientIn == null) {
                    System.out.println("Client disconnected prematurely.");
                    return;
                }

                String username = users.get(userIndex).getUsername();
                Question q = userQuestions.get(userIndex).get(round);

                // Send turn prompt
                clientOut.println("TURN:" + username);
                // Send question
                StringBuilder msg = new StringBuilder("Question: " + q.getQuestionText() + "\nOptions:\n");
                String[] options = q.getOptions();
                for (int i = 0; i < options.length; i++) {
                    msg.append(i + 1).append(". ").append(options[i]).append("\n");
                }
                System.out.println("Question " + (round + 1) + " sent to " + username);
                clientOut.println(msg.toString());

                // Get answer
                String line;
                while ((line = clientIn.readLine()) != null) {
                    if (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
                        clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
                        continue;
                    }
                    try {
                        int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
                        if (q.isCorrect(answer)) {
                            users.get(userIndex).addScore(10); // 10 points for correct
                            clientOut.println("Correct!");
                        } else {
                            clientOut.println("Incorrect!");
                        }
                        totalQuestionsAnswered++;
                        break; // Move to next question without updating leaderboard
                    } catch (NumberFormatException e) {
                        clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
                    }
                }
            }
            round++;
        }

        // After all questions, show final leaderboard, save results to text file, and send thanks message
        if (clientOut != null) {
            broadcastFinalLeaderboard();
            saveResultsToTextFile(); // Save results to text file
            clientOut.println("Thanks, your test has now completed!");
        }
    }

    // Save results to a text file
    private static void saveResultsToTextFile() {
        try (PrintWriter fileOut = new PrintWriter(new FileWriter(RESULTS_FILE, true))) { // Append mode
            fileOut.println("Quiz Results - " + new Date());
            for (User user : users) {
                fileOut.println("Username: " + user.getUsername() + ", Score: " + user.getScore());
            }
            fileOut.println("------------------------");
            System.out.println("Results saved to " + RESULTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving results to text file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Broadcast final leaderboard
    private static void broadcastFinalLeaderboard() {
        StringBuilder lb = new StringBuilder("Quiz ended! Final Leaderboard:\n");
        PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
        leaderboard.clear();
        leaderboard.addAll(users); // Update leaderboard with final scores
        int rank = 1;
        while (!temp.isEmpty()) {
            User user = temp.poll();
            lb.append(rank++).append(". ").append(user.getUsername())
              .append(": ").append(user.getScore()).append("\n");
        }
        clientOut.println(lb.toString());
    }
}
















