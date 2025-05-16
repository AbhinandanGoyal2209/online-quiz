
























import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 12345;
    private static final int QUESTIONS_PER_USER = 5;
    private static final String RESULTS_FILE = "quiz_results.txt"; // File to store results
    private static ArrayList<Question> javaQuestionPool = new ArrayList<>();
    private static ArrayList<Question> dsaQuestionPool = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<List<Question>> userQuestions = new ArrayList<>();
    private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
    private static PrintWriter clientOut;
    private static BufferedReader clientIn;

    public static void main(String[] args) throws IOException {
        // Initialize Java question pool
        javaQuestionPool.add(new Question(
            "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
            new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
            2
        ));
        javaQuestionPool.add(new Question(
            "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
            new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
            1
        ));
        javaQuestionPool.add(new Question(
            "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
            new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
            2
        ));
        javaQuestionPool.add(new Question(
            "What happens when an uncaught checked exception is thrown in a thread?",
            new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which Java 8 feature allows functional interfaces to be implemented inline?",
            new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
            1
        ));
        javaQuestionPool.add(new Question(
            "In generics, what is type erasure?",
            new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which design pattern ensures a class has only one instance and provides a global access point?",
            new String[]{"Factory", "Observer", "Singleton", "Adapter"},
            2
        ));
        javaQuestionPool.add(new Question(
            "What is the purpose of the `volatile` keyword in Java?",
            new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
            1
        ));
        javaQuestionPool.add(new Question(
            "Which `ExecutorService` method submits a task and returns a `Future` object?",
            new String[]{"execute()", "submit()", "run()", "invokeAll()"},
            1
        ));
        javaQuestionPool.add(new Question(
            "What is the difference between `throws` and `throw` in exception handling?",
            new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which class is used to create a fixed-size thread pool?",
            new String[]{"ThreadPoolExecutor", "ScheduledExecutorService", "Executors", "ForkJoinPool"},
            2
        ));
        javaQuestionPool.add(new Question(
            "What is the default capacity of a `HashMap` in Java?",
            new String[]{"8", "16", "32", "64"},
            1
        ));
        javaQuestionPool.add(new Question(
            "Which Stream operation is used to reduce elements to a single value?",
            new String[]{"map()", "filter()", "reduce()", "collect()"},
            2
        ));
        javaQuestionPool.add(new Question(
            "What does the `synchronized` keyword do when applied to a method?",
            new String[]{"Locks the object", "Pauses the thread", "Prevents instantiation", "Optimizes execution"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which annotation is used to mark a method for dependency injection in Spring?",
            new String[]{"@Inject", "@Autowired", "@Resource", "@Bean"},
            1
        ));
        javaQuestionPool.add(new Question(
            "What is the purpose of `Optional` in Java 8?",
            new String[]{"Handle null values", "Optimize loops", "Manage threads", "Parse JSON"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which pattern separates object construction from its representation?",
            new String[]{"Builder", "Prototype", "Facade", "Decorator"},
            0
        ));
        javaQuestionPool.add(new Question(
            "What is the output of `Integer.valueOf(127) == Integer.valueOf(127)`?",
            new String[]{"true", "false", "Compilation error", "Runtime exception"},
            0
        ));
        javaQuestionPool.add(new Question(
            "Which interface is used for custom sorting in Java?",
            new String[]{"Comparable", "Comparator", "Sortable", "Orderable"},
            1
        ));
        javaQuestionPool.add(new Question(
            "What does `CompletableFuture` provide in Java?",
            new String[]{"Thread pooling", "Asynchronous programming", "Garbage collection", "Serialization"},
            1
        ));

        // Initialize DSA question pool
        dsaQuestionPool.add(new Question(
            "What is the time complexity of binary search on a sorted array?",
            new String[]{"O(n)", "O(log n)", "O(n log n)", "O(1)"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which data structure is used to implement a LIFO (Last In, First Out) mechanism?",
            new String[]{"Queue", "Stack", "Heap", "Tree"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the worst-case time complexity of QuickSort?",
            new String[]{"O(n log n)", "O(n)", "O(n^2)", "O(log n)"},
            2
        ));
        dsaQuestionPool.add(new Question(
            "Which algorithm is used to find the shortest path in a weighted graph?",
            new String[]{"Breadth-First Search", "Depth-First Search", "Dijkstra’s Algorithm", "Kruskal’s Algorithm"},
            2
        ));
        dsaQuestionPool.add(new Question(
            "What is the space complexity of a recursive factorial function?",
            new String[]{"O(1)", "O(n)", "O(log n)", "O(n^2)"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which data structure is best for implementing a priority queue?",
            new String[]{"Array", "Linked List", "Heap", "Stack"},
            2
        ));
        dsaQuestionPool.add(new Question(
            "What is the main advantage of using a hash table?",
            new String[]{"Sorted data", "Constant-time average case lookup", "Dynamic resizing", "No collisions"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which sorting algorithm is stable and has O(n^2) complexity?",
            new String[]{"QuickSort", "MergeSort", "Bubble Sort", "HeapSort"},
            2
        ));
        dsaQuestionPool.add(new Question(
            "What is the purpose of dynamic programming?",
            new String[]{"Sorting data", "Optimizing recursive solutions", "Graph traversal", "Data compression"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which traversal visits the root node first in a binary tree?",
            new String[]{"Inorder", "Preorder", "Postorder", "Level-order"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the time complexity of inserting an element into a balanced BST?",
            new String[]{"O(1)", "O(log n)", "O(n)", "O(n log n)"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which algorithm finds the minimum spanning tree of a graph?",
            new String[]{"Dijkstra’s Algorithm", "Kruskal’s Algorithm", "Bellman-Ford", "Floyd-Warshall"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the time complexity of accessing an element in an array?",
            new String[]{"O(1)", "O(log n)", "O(n)", "O(n^2)"},
            0
        ));
        dsaQuestionPool.add(new Question(
            "Which data structure is used for breadth-first search?",
            new String[]{"Stack", "Queue", "Heap", "Array"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the worst-case time complexity of MergeSort?",
            new String[]{"O(n)", "O(n log n)", "O(n^2)", "O(log n)"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which algorithm is used to detect a cycle in a directed graph?",
            new String[]{"BFS", "DFS", "Dijkstra’s Algorithm", "Prim’s Algorithm"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the purpose of a trie data structure?",
            new String[]{"Sorting", "Graph traversal", "String prefix matching", "Dynamic programming"},
            2
        ));
        dsaQuestionPool.add(new Question(
            "Which searching algorithm is best for an unsorted array?",
            new String[]{"Binary Search", "Linear Search", "Jump Search", "Interpolation Search"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "What is the time complexity of deleting a node from a linked list?",
            new String[]{"O(1)", "O(n)", "O(log n)", "O(n^2)"},
            1
        ));
        dsaQuestionPool.add(new Question(
            "Which algorithm solves the all-pairs shortest path problem?",
            new String[]{"Dijkstra’s Algorithm", "Bellman-Ford", "Floyd-Warshall", "Prim’s Algorithm"},
            2
        ));

        // Prompt for subject selection
        Scanner scanner = new Scanner(System.in);
        ArrayList<Question> selectedQuestionPool;
        while (true) {
            System.out.print("Choose a subject (Java or DSA): ");
            String subject = scanner.nextLine().trim().toLowerCase();
            if (subject.equals("java")) {
                selectedQuestionPool = javaQuestionPool;
                break;
            } else if (subject.equals("dsa")) {
                selectedQuestionPool = dsaQuestionPool;
                break;
            } else {
                System.out.println("Error: Please enter 'Java' or 'DSA'.");
            }
        }

        // Prompt for number of users (1 to 5)
        int numUsers = 0;
        while (true) {
            System.out.print("Enter the number of students (1-5): ");
            try {
                numUsers = Integer.parseInt(scanner.nextLine().trim());
                if (numUsers >= 1 && numUsers <= 5) {
                    break;
                } else {
                    System.out.println("Error: Number of students must be between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }

        // Prompt for unique usernames
        Set<String> usernames = new HashSet<>();
        System.out.println("Enter " + numUsers + " unique usernames:");
        while (usernames.size() < numUsers) {
            System.out.print("Username " + (usernames.size() + 1) + ": ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty() || usernames.contains(username)) {
                System.out.println("Error: Username must be non-empty and unique.");
            } else {
                usernames.add(username);
                User user = new User(username);
                users.add(user);
                leaderboard.add(user);
                // Assign 5 random questions per user from selected pool
                List<Question> questions = new ArrayList<>(selectedQuestionPool);
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
            runQuiz(numUsers);
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

    private static void runQuiz(int numUsers) throws IOException {
        int round = 0;
        int totalQuestionsAnswered = 0;
        int maxQuestions = numUsers * QUESTIONS_PER_USER;

        while (round < QUESTIONS_PER_USER && clientIn != null) {
            for (int userIndex = 0; userIndex < numUsers; userIndex++) {
                if (totalQuestionsAnswered >= maxQuestions) {
                    break; // Stop if all questions have been answered
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
                        }
                        totalQuestionsAnswered++;
                        break; // Move to next question
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
            saveResultsToTextFile();
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

    private static void broadcastFinalLeaderboard() {
        StringBuilder lb = new StringBuilder("Quiz ended! Final Leaderboard:\n");
        PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
        leaderboard.clear();
        leaderboard.addAll(users); 
        int rank = 1;
        while (!temp.isEmpty()) {
            User user = temp.poll();
            lb.append(rank++).append(". ").append(user.getUsername())
              .append(": ").append(user.getScore()).append("\n");
        }
        clientOut.println(lb.toString());
    }
}