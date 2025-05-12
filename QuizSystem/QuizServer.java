// // import java.io.*;
// // import java.net.*;
// // import java.util.*;

// // public class QuizServer {
// //     private static final int PORT = 12345;
// //     private static ArrayList<Question> questions = new ArrayList<>();
// //     private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
// //     private static HashMap<String, User> users = new HashMap<>();
// //     private static ArrayList<ClientHandler> clients = new ArrayList<>();
// //     private static int currentQuestionIndex = 0;

// //     public static void main(String[] args) throws IOException {
// //         // Initialize questions
// //         questions.add(new Question("What is 2+2?", new String[]{"1", "2", "3", "4"}, 3));
// //         questions.add(new Question("Capital of France?", new String[]{"Paris", "London", "Berlin", "Madrid"}, 0));

// //         ServerSocket serverSocket = new ServerSocket(PORT);
// //         System.out.println("Server started on port " + PORT);

// //         while (true) {
// //             Socket clientSocket = serverSocket.accept();
// //             ClientHandler clientHandler = new ClientHandler(clientSocket);
// //             clients.add(clientHandler);
// //             new Thread(clientHandler).start();
// //         }
// //     }

// //     // Broadcast leaderboard to all clients
// //     private static synchronized void broadcastLeaderboard() {
// //         StringBuilder lb = new StringBuilder("Leaderboard:\n");
// //         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
// //         int rank = 1;
// //         while (!temp.isEmpty()) {
// //             User user = temp.poll();
// //             lb.append(rank++).append(". ").append(user.getUsername())
// //               .append(": ").append(user.getScore()).append("\n");
// //         }
// //         for (ClientHandler client : clients) {
// //             client.sendMessage(lb.toString());
// //         }
// //     }

// //     // Update user score and leaderboard
// //     public static synchronized void updateScore(String username, int points) {
// //         User user = users.computeIfAbsent(username, User::new);
// //         user.addScore(points);
// //         leaderboard.remove(user); // O(log n)
// //         leaderboard.offer(user);  // O(log n)
// //         broadcastLeaderboard();
// //     }

// //     // Get current question
// //     public static synchronized Question getCurrentQuestion() {
// //         if (currentQuestionIndex < questions.size()) {
// //             return questions.get(currentQuestionIndex);
// //         }
// //         return null;
// //     }

// //     // Move to next question
// //     public static synchronized void nextQuestion() {
// //         if (currentQuestionIndex < questions.size() - 1) {
// //             currentQuestionIndex++;
// //             broadcastQuestion();
// //         }
// //     }

// //     // Broadcast current question
// //     private static synchronized void broadcastQuestion() {
// //         Question q = getCurrentQuestion();
// //         if (q != null) {
// //             StringBuilder msg = new StringBuilder("Question: " + q.getQuestionText() + "\nOptions:\n");
// //             String[] options = q.getOptions();
// //             for (int i = 0; i < options.length; i++) {
// //                 msg.append(i + 1).append(". ").append(options[i]).append("\n");
// //             }
// //             for (ClientHandler client : clients) {
// //                 client.sendMessage(msg.toString());
// //             }
// //         } else {
// //             for (ClientHandler client : clients) {
// //                 client.sendMessage("Quiz ended!");
// //             }
// //         }
// //     }

// //     static class ClientHandler implements Runnable {
// //         private Socket socket;
// //         private PrintWriter out;
// //         private BufferedReader in;
// //         private String username;

// //         public ClientHandler(Socket socket) throws IOException {
// //             this.socket = socket;
// //             this.out = new PrintWriter(socket.getOutputStream(), true);
// //             this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// //             System.out.println("New client connected: " + socket.getInetAddress());
// //         }

// //         public void sendMessage(String message) {
// //             out.println(message);
// //         }

// //         @Override
// //         public void run() {
// //             try {
// //                 // Get username
// //                 username = in.readLine();
// //                 if (username == null || username.trim().isEmpty()) {
// //                     username = "Unknown-" + socket.getInetAddress();
// //                 }
// //                 System.out.println("Client identified as: " + username);
// //                 sendMessage("Welcome, " + username + "!");
// //                 broadcastQuestion();

// //                 // Handle client answers
// //                 String line;
// //                 while ((line = in.readLine()) != null) {
// //                     try {
// //                         int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
// //                         Question currentQuestion = getCurrentQuestion();
// //                         if (currentQuestion != null && currentQuestion.isCorrect(answer)) {
// //                             updateScore(username, 10); // Award 10 points
// //                             sendMessage("Correct!");
// //                         } else {
// //                             sendMessage("Incorrect!");
// //                         }
// //                         nextQuestion();
// //                     } catch (NumberFormatException e) {
// //                         sendMessage("Please enter a number.");
// //                     }
// //                 }
// //             } catch (IOException e) {
// //                 System.out.println("Client error (" + (username != null ? username : "Unknown") + "): " + e.getMessage());
// //             } finally {
// //                 System.out.println("Client disconnected: " + (username != null ? username : "Unknown-" + socket.getInetAddress()));
// //                 try {
// //                     socket.close();
// //                 } catch (IOException e) {
// //                     e.printStackTrace();
// //                 }
// //                 clients.remove(this); // Remove client from list
// //             }
// //         }
// //     }
// // }


// // import java.io.*;
// // import java.net.*;
// // import java.util.*;

// // public class QuizServer {
// //     private static final int PORT = 12345;
// //     private static final int QUESTIONS_PER_USER = 5;
// //     private static ArrayList<Question> questionPool = new ArrayList<>();
// //     private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
// //     private static HashMap<String, User> users = new HashMap<>();
// //     private static ArrayList<ClientHandler> clients = new ArrayList<>();

// //     public static void main(String[] args) throws IOException {
// //         // Initialize advanced Java questions
// //         questionPool.add(new Question(
// //             "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
// //             new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
// //             2
// //         ));
// //         questionPool.add(new Question(
// //             "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
// //             new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
// //             1
// //         ));
// //         questionPool.add(new Question(
// //             "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
// //             new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
// //             2
// //         ));
// //         questionPool.add(new Question(
// //             "What happens when an uncaught checked exception is thrown in a thread?",
// //             new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
// //             0
// //         ));
// //         questionPool.add(new Question(
// //             "Which Java 8 feature allows functional interfaces to be implemented inline?",
// //             new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
// //             1
// //         ));
// //         questionPool.add(new Question(
// //             "In generics, what is type erasure?",
// //             new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
// //             0
// //         ));
// //         questionPool.add(new Question(
// //             "Which design pattern ensures a class has only one instance and provides a global access point?",
// //             new String[]{"Factory", "Observer", "Singleton", "Adapter"},
// //             2
// //         ));
// //         questionPool.add(new Question(
// //             "What is the purpose of the `volatile` keyword in Java?",
// //             new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
// //             1
// //         ));
// //         questionPool.add(new Question(
// //             "Which `ExecutorService` method submits a task and returns a `Future` object?",
// //             new String[]{"execute()", "submit()", "run()", "invokeAll()"},
// //             1
// //         ));
// //         questionPool.add(new Question(
// //             "What is the difference between `throws` and `throw` in exception handling?",
// //             new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
// //             0
// //         ));

// //         ServerSocket serverSocket = new ServerSocket(PORT);
// //         System.out.println("Server started on port " + PORT);

// //         while (true) {
// //             Socket clientSocket = serverSocket.accept();
// //             ClientHandler clientHandler = new ClientHandler(clientSocket);
// //             clients.add(clientHandler);
// //             new Thread(clientHandler).start();
// //         }
// //     }

// //     // Broadcast leaderboard to all clients
// //     private static synchronized void broadcastLeaderboard() {
// //         StringBuilder lb = new StringBuilder("Leaderboard:\n");
// //         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
// //         int rank = 1;
// //         while (!temp.isEmpty()) {
// //             User user = temp.poll();
// //             lb.append(rank++).append(". ").append(user.getUsername())
// //               .append(": ").append(user.getScore()).append("\n");
// //         }
// //         for (ClientHandler client : clients) {
// //             client.sendMessage(lb.toString());
// //         }
// //     }

// //     // Update user score and leaderboard
// //     public static synchronized void updateScore(String username, int points) {
// //         User user = users.computeIfAbsent(username, User::new);
// //         user.addScore(points);
// //         leaderboard.remove(user); // O(log n)
// //         leaderboard.offer(user);  // O(log n)
// //         broadcastLeaderboard();
// //     }

// //     static class ClientHandler implements Runnable {
// //         private Socket socket;
// //         private PrintWriter out;
// //         private BufferedReader in;
// //         private String username;
// //         private List<Question> userQuestions;
// //         private int currentQuestionIndex;
// //         private int questionsAnswered;

// //         public ClientHandler(Socket socket) throws IOException {
// //             this.socket = socket;
// //             this.out = new PrintWriter(socket.getOutputStream(), true);
// //             this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// //             this.currentQuestionIndex = 0;
// //             this.questionsAnswered = 0;
// //             // Randomly select 5 questions for this user
// //             this.userQuestions = new ArrayList<>(questionPool);
// //             Collections.shuffle(userQuestions);
// //             this.userQuestions = userQuestions.subList(0, Math.min(QUESTIONS_PER_USER, userQuestions.size()));
// //             System.out.println("New client connected: " + socket.getInetAddress());
// //         }

// //         public void sendMessage(String message) {
// //             out.println(message);
// //         }

// //         private void sendCurrentQuestion() {
// //             if (currentQuestionIndex < userQuestions.size()) {
// //                 Question q = userQuestions.get(currentQuestionIndex);
// //                 StringBuilder msg = new StringBuilder("Question: " + q.getQuestionText() + "\nOptions:\n");
// //                 String[] options = q.getOptions();
// //                 for (int i = 0; i < options.length; i++) {
// //                     msg.append(i + 1).append(". ").append(options[i]).append("\n");
// //                 }
// //                 sendMessage(msg.toString());
// //             } else {
// //                 sendMessage("Quiz ended! Your final score: " + users.get(username).getScore());
// //                 broadcastLeaderboard();
// //             }
// //         }

// //         @Override
// //         public void run() {
// //             try {
// //                 // Get username
// //                 username = in.readLine();
// //                 if (username == null || username.trim().isEmpty()) {
// //                     username = "Unknown-" + socket.getInetAddress();
// //                 }
// //                 System.out.println("Client identified as: " + username);
// //                 sendMessage("Welcome, " + username + "!");
// //                 sendCurrentQuestion();

// //                 // Handle client answers
// //                 String line;
// //                 while ((line = in.readLine()) != null && questionsAnswered < QUESTIONS_PER_USER) {
// //                     // Validate input
// //                     if (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
// //                         sendMessage("Invalid input! Please choose 1, 2, 3, or 4.");
// //                         continue; // Stay on the same question
// //                     }
// //                     try {
// //                         int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
// //                         Question currentQuestion = userQuestions.get(currentQuestionIndex);
// //                         if (currentQuestion.isCorrect(answer)) {
// //                             QuizServer.updateScore(username, 10); // Award 10 points
// //                             sendMessage("Correct!");
// //                         } else {
// //                             sendMessage("Incorrect!");
// //                         }
// //                         questionsAnswered++;
// //                         currentQuestionIndex++;
// //                         sendCurrentQuestion();
// //                         if (questionsAnswered >= QUESTIONS_PER_USER) {
// //                             in.close(); // Stop accepting input
// //                             break;
// //                         }
// //                     } catch (NumberFormatException e) {
// //                         sendMessage("Invalid input! Please choose 1, 2, 3, or 4.");
// //                     }
// //                 }
// //             } catch (IOException e) {
// //                 System.out.println("Client error (" + (username != null ? username : "Unknown") + "): " + e.getMessage());
// //             } finally {
// //                 System.out.println("Client disconnected: " + (username != null ? username : "Unknown-" + socket.getInetAddress()));
// //                 try {
// //                     socket.close();
// //                 } catch (IOException e) {
// //                     e.printStackTrace();
// //                 }
// //                 clients.remove(this); // Remove client from list
// //             }
// //         }
// //     }
// // }



// // // cd E:\JAVA ONLINE QUIZ SYSTEM\QuizSystem
// // // javac *.java
// // // java QuizServer
// // // Server started on port 12345
// // // cd E:\JAVA ONLINE QUIZ SYSTEM\QuizSystem
// // // java QuizClient






// import java.io.*;
// import java.net.*;
// import java.util.*;

// public class QuizServer {
//     private static final int PORT = 12345;
//     private static final int QUESTIONS_PER_USER = 5;
//     private static final int NUM_USERS = 4;
//     private static ArrayList<Question> questionPool = new ArrayList<>();
//     private static List<User> users = new ArrayList<>();
//     private static List<List<Question>> userQuestions = new ArrayList<>();
//     private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
//     private static PrintWriter clientOut;
//     private static BufferedReader clientIn;

//     public static void main(String[] args) throws IOException {
//         // Initialize 20 advanced Java questions
//         questionPool.add(new Question(
//             "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
//             new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
//             new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
//             new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What happens when an uncaught checked exception is thrown in a thread?",
//             new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which Java 8 feature allows functional interfaces to be implemented inline?",
//             new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
//             1
//         ));
//         questionPool.add(new Question(
//             "In generics, what is type erasure?",
//             new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which design pattern ensures a class has only one instance and provides a global access point?",
//             new String[]{"Factory", "Observer", "Singleton", "Adapter"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of the `volatile` keyword in Java?",
//             new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which `ExecutorService` method submits a task and returns a `Future` object?",
//             new String[]{"execute()", "submit()", "run()", "invokeAll()"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the difference between `throws` and `throw` in exception handling?",
//             new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which class is used to create a fixed-size thread pool?",
//             new String[]{"ThreadPoolExecutor", "ScheduledExecutorService", "Executors", "ForkJoinPool"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the default capacity of a `HashMap` in Java?",
//             new String[]{"8", "16", "32", "64"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which Stream operation is used to reduce elements to a single value?",
//             new String[]{"map()", "filter()", "reduce()", "collect()"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What does the `synchronized` keyword do when applied to a method?",
//             new String[]{"Locks the object", "Pauses the thread", "Prevents instantiation", "Optimizes execution"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which annotation is used to mark a method for dependency injection in Spring?",
//             new String[]{"@Inject", "@Autowired", "@Resource", "@Bean"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of `Optional` in Java 8?",
//             new String[]{"Handle null values", "Optimize loops", "Manage threads", "Parse JSON"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which pattern separates object construction from its representation?",
//             new String[]{"Builder", "Prototype", "Facade", "Decorator"},
//             0
//         ));
//         questionPool.add(new Question(
//             "What is the output of `Integer.valueOf(127) == Integer.valueOf(127)`?",
//             new String[]{"true", "false", "Compilation error", "Runtime exception"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which interface is used for custom sorting in Java?",
//             new String[]{"Comparable", "Comparator", "Sortable", "Orderable"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What does `CompletableFuture` provide in Java?",
//             new String[]{"Thread pooling", "Asynchronous programming", "Garbage collection", "Serialization"},
//             1
//         ));

//         // Prompt for 4 unique usernames
//         Scanner scanner = new Scanner(System.in);
//         Set<String> usernames = new HashSet<>();
//         System.out.println("Enter 4 unique usernames:");
//         while (usernames.size() < NUM_USERS) {
//             System.out.print("Username " + (usernames.size() + 1) + ": ");
//             String username = scanner.nextLine().trim();
//             if (username.isEmpty() || usernames.contains(username)) {
//                 System.out.println("Error: Username must be non-empty and unique.");
//             } else {
//                 usernames.add(username);
//                 User user = new User(username);
//                 users.add(user);
//                 leaderboard.add(user);
//                 // Assign 5 random questions per user
//                 List<Question> questions = new ArrayList<>(questionPool);
//                 Collections.shuffle(questions);
//                 userQuestions.add(questions.subList(0, QUESTIONS_PER_USER));
//             }
//         }
//         scanner.close();

//         ServerSocket serverSocket = new ServerSocket(PORT);
//         System.out.println("Server started on port " + PORT);

//         // Accept single client connection
//         Socket clientSocket = null;
//         try {
//             clientSocket = serverSocket.accept();
//             clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
//             clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             System.out.println("Client connected: " + clientSocket.getInetAddress());

//             // Run the quiz
//             runQuiz();
//         } catch (IOException e) {
//             System.out.println("Error accepting client: " + e.getMessage());
//         } finally {
//             if (clientSocket != null && !clientSocket.isClosed()) {
//                 try {
//                     clientSocket.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//             serverSocket.close();
//         }
//     }

//     private static void runQuiz() throws IOException {
//         int round = 0;
//         while (round < QUESTIONS_PER_USER && clientIn != null) {
//             for (int userIndex = 0; userIndex < NUM_USERS; userIndex++) {
//                 if (clientIn == null) {
//                     System.out.println("Client disconnected prematurely.");
//                     return;
//                 }
//                 String username = users.get(userIndex).getUsername();
//                 Question q = userQuestions.get(userIndex).get(round);
//                 // Send question
//                 StringBuilder msg = new StringBuilder("TURN:" + username + "\nQuestion: " + q.getQuestionText() + "\nOptions:\n");
//                 String[] options = q.getOptions();
//                 for (int i = 0; i < options.length; i++) {
//                     msg.append(i + 1).append(". ").append(options[i]).append("\n");
//                 }
//                 System.out.println("Question " + (round + 1) + " sent to " + username);
//                 clientOut.println(msg.toString());

//                 // Get answer
//                 String line;
//                 while ((line = clientIn.readLine()) != null) {
//                     if (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                         continue;
//                     }
//                     try {
//                         int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
//                         if (q.isCorrect(answer)) {
//                             users.get(userIndex).addScore(10); // 10 points for correct
//                             clientOut.println("Correct!");
//                         } else {
//                             clientOut.println("Incorrect!");
//                         }
//                         updateLeaderboard();
//                         break;
//                     } catch (NumberFormatException e) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                     }
//                 }
//                 // Send pop-up after user's 5th question
//                 if (round == QUESTIONS_PER_USER - 1) {
//                     clientOut.println("Quiz ended for " + username + "! Your final score: " + users.get(userIndex).getScore());
//                     clientOut.println("POPUP:Thanks, your test has now completed!");
//                 }
//             }
//             round++;
//         }
//         // Broadcast final leaderboard and end message
//         broadcastFinalLeaderboard();
//         if (clientOut != null) {
//             clientOut.println("Test has ended now.");
//         }
//     }

//     // Update leaderboard
//     private static void updateLeaderboard() {
//         leaderboard.clear();
//         leaderboard.addAll(users);
//         StringBuilder lb = new StringBuilder("Leaderboard:\n");
//         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
//         int rank = 1;
//         while (!temp.isEmpty()) {
//             User user = temp.poll();
//             lb.append(rank++).append(". ").append(user.getUsername())
//               .append(": ").append(user.getScore()).append("\n");
//         }
//         clientOut.println(lb.toString());
//     }

//     // Broadcast final leaderboard
//     private static void broadcastFinalLeaderboard() {
//         StringBuilder lb = new StringBuilder("Quiz ended! Final Leaderboard:\n");
//         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
//         int rank = 1;
//         while (!temp.isEmpty()) {
//             User user = temp.poll();
//             lb.append(rank++).append(". ").append(user.getUsername())
//               .append(": ").append(user.getScore()).append("\n");
//         }
//         clientOut.println(lb.toString());
//     }
// }









// import java.io.*;
// import java.net.*;
// import java.util.*;

// public class QuizServer {
//     private static final int PORT = 12345;
//     private static final int QUESTIONS_PER_USER = 5;
//     private static final int NUM_USERS = 4;
//     private static ArrayList<Question> questionPool = new ArrayList<>();
//     private static List<User> users = new ArrayList<>();
//     private static List<List<Question>> userQuestions = new ArrayList<>();
//     private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
//     private static PrintWriter clientOut;
//     private static BufferedReader clientIn;

//     public static void main(String[] args) throws IOException {
//         // Initialize 20 advanced Java questions
//         questionPool.add(new Question(
//             "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
//             new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
//             new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
//             new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What happens when an uncaught checked exception is thrown in a thread?",
//             new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which Java 8 feature allows functional interfaces to be implemented inline?",
//             new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
//             1
//         ));
//         questionPool.add(new Question(
//             "In generics, what is type erasure?",
//             new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which design pattern ensures a class has only one instance and provides a global access point?",
//             new String[]{"Factory", "Observer", "Singleton", "Adapter"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of the `volatile` keyword in Java?",
//             new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which `ExecutorService` method submits a task and returns a `Future` object?",
//             new String[]{"execute()", "submit()", "run()", "invokeAll()"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the difference between `throws` and `throw` in exception handling?",
//             new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which class is used to create a fixed-size thread pool?",
//             new String[]{"ThreadPoolExecutor", "ScheduledExecutorService", "Executors", "ForkJoinPool"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the default capacity of a `HashMap` in Java?",
//             new String[]{"8", "16", "32", "64"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which Stream operation is used to reduce elements to a single value?",
//             new String[]{"map()", "filter()", "reduce()", "collect()"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What does the `synchronized` keyword do when applied to a method?",
//             new String[]{"Locks the object", "Pauses the thread", "Prevents instantiation", "Optimizes execution"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which annotation is used to mark a method for dependency injection in Spring?",
//             new String[]{"@Inject", "@Autowired", "@Resource", "@Bean"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of `Optional` in Java 8?",
//             new String[]{"Handle null values", "Optimize loops", "Manage threads", "Parse JSON"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which pattern separates object construction from its representation?",
//             new String[]{"Builder", "Prototype", "Facade", "Decorator"},
//             0
//         ));
//         questionPool.add(new Question(
//             "What is the output of `Integer.valueOf(127) == Integer.valueOf(127)`?",
//             new String[]{"true", "false", "Compilation error", "Runtime exception"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which interface is used for custom sorting in Java?",
//             new String[]{"Comparable", "Comparator", "Sortable", "Orderable"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What does `CompletableFuture` provide in Java?",
//             new String[]{"Thread pooling", "Asynchronous programming", "Garbage collection", "Serialization"},
//             1
//         ));

//         // Prompt for 4 unique usernames
//         Scanner scanner = new Scanner(System.in);
//         Set<String> usernames = new HashSet<>();
//         System.out.println("Enter 4 unique usernames:");
//         while (usernames.size() < NUM_USERS) {
//             System.out.print("Username " + (usernames.size() + 1) + ": ");
//             String username = scanner.nextLine().trim();
//             if (username.isEmpty() || usernames.contains(username)) {
//                 System.out.println("Error: Username must be non-empty and unique.");
//             } else {
//                 usernames.add(username);
//                 User user = new User(username);
//                 users.add(user);
//                 leaderboard.add(user);
//                 // Assign 5 random questions per user
//                 List<Question> questions = new ArrayList<>(questionPool);
//                 Collections.shuffle(questions);
//                 userQuestions.add(questions.subList(0, QUESTIONS_PER_USER));
//             }
//         }
//         scanner.close();

//         ServerSocket serverSocket = new ServerSocket(PORT);
//         System.out.println("Server started on port " + PORT);

//         // Accept single client connection
//         Socket clientSocket = null;
//         try {
//             clientSocket = serverSocket.accept();
//             clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
//             clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             System.out.println("Client connected: " + clientSocket.getInetAddress());

//             // Run the quiz
//             runQuiz();
//         } catch (IOException e) {
//             System.out.println("Error accepting client: " + e.getMessage());
//         } finally {
//             if (clientSocket != null && !clientSocket.isClosed()) {
//                 try {
//                     clientSocket.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//             serverSocket.close();
//         }
//     }

//     private static void runQuiz() throws IOException {
//         int round = 0;
//         int totalQuestionsAnswered = 0;
//         int maxQuestions = NUM_USERS * QUESTIONS_PER_USER; // 4 users * 5 questions = 20

//         while (round < QUESTIONS_PER_USER && clientIn != null) {
//             for (int userIndex = 0; userIndex < NUM_USERS; userIndex++) {
//                 if (totalQuestionsAnswered >= maxQuestions) {
//                     break; // Stop if 20 questions have been answered
//                 }

//                 if (clientIn == null) {
//                     System.out.println("Client disconnected prematurely.");
//                     return;
//                 }

//                 String username = users.get(userIndex).getUsername();
//                 Question q = userQuestions.get(userIndex).get(round);

//                 // Send turn prompt
//                 clientOut.println("TURN:" + username);
//                 // Send question
//                 StringBuilder msg = new StringBuilder("Question: " + q.getQuestionText() + "\nOptions:\n");
//                 String[] options = q.getOptions();
//                 for (int i = 0; i < options.length; i++) {
//                     msg.append(i + 1).append(". ").append(options[i]).append("\n");
//                 }
//                 System.out.println("Question " + (round + 1) + " sent to " + username);
//                 clientOut.println(msg.toString());

//                 // Get answer
//                 String line;
//                 while ((line = clientIn.readLine()) != null) {
//                     if (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                         continue;
//                     }
//                     try {
//                         int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
//                         if (q.isCorrect(answer)) {
//                             users.get(userIndex).addScore(10); // 10 points for correct
//                             clientOut.println("Correct!");
//                         } else {
//                             clientOut.println("Incorrect!");
//                         }
//                         totalQuestionsAnswered++;
//                         break; // Move to next question without updating leaderboard
//                     } catch (NumberFormatException e) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                     }
//                 }
//             }
//             round++;
//         }

//         // After all questions, show final leaderboard and thanks message
//         if (clientOut != null) {
//             broadcastFinalLeaderboard();
//             clientOut.println("Thanks, your test has now completed!");
//         }
//     }

//     // Broadcast final leaderboard
//     private static void broadcastFinalLeaderboard() {
//         StringBuilder lb = new StringBuilder("Quiz ended! Final Leaderboard:\n");
//         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
//         leaderboard.clear();
//         leaderboard.addAll(users); // Update leaderboard with final scores
//         int rank = 1;
//         while (!temp.isEmpty()) {
//             User user = temp.poll();
//             lb.append(rank++).append(". ").append(user.getUsername())
//               .append(": ").append(user.getScore()).append("\n");
//         }
//         clientOut.println(lb.toString());
//     }
// }






// import java.io.*;
// import java.net.*;
// import java.sql.*;
// import java.util.*;

// public class QuizServer {
//     private static final int PORT = 12345;
//     private static final int QUESTIONS_PER_USER = 5;
//     private static final int NUM_USERS = 4;
//     private static ArrayList<Question> questionPool = new ArrayList<>();
//     private static List<User> users = new ArrayList<>();
//     private static List<List<Question>> userQuestions = new ArrayList<>();
//     private static PriorityQueue<User> leaderboard = new PriorityQueue<>();
//     private static PrintWriter clientOut;
//     private static BufferedReader clientIn;
//     private static Connection dbConnection;

//     public static void main(String[] args) throws IOException {
//         // Initialize database connection
//         initializeDatabase();

//         // Test the database connection by querying the table
//         testDatabaseConnection();

//         // Initialize 20 advanced Java questions
//         questionPool.add(new Question(
//             "Which method ensures thread-safe singleton initialization in a multi-threaded environment?",
//             new String[]{"Synchronized method", "Double-checked locking", "Static initializer", "Lazy initialization"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the output of `List.of(1, 2, 3).stream().map(x -> x * 2).collect(Collectors.toList())`?",
//             new String[]{"[1, 2, 3]", "[2, 4, 6]", "[1, 4, 9]", "Compilation error"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which collection class is thread-safe and suitable for high-concurrency read/write operations?",
//             new String[]{"HashMap", "Hashtable", "ConcurrentHashMap", "TreeMap"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What happens when an uncaught checked exception is thrown in a thread?",
//             new String[]{"Thread terminates", "Program exits", "Exception is ignored", "Thread is suspended"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which Java 8 feature allows functional interfaces to be implemented inline?",
//             new String[]{"Annotations", "Lambda expressions", "Generics", "Optional"},
//             1
//         ));
//         questionPool.add(new Question(
//             "In generics, what is type erasure?",
//             new String[]{"Removing type parameters at runtime", "Converting types at compile time", "Casting objects", "Type checking at runtime"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which design pattern ensures a class has only one instance and provides a global access point?",
//             new String[]{"Factory", "Observer", "Singleton", "Adapter"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of the `volatile` keyword in Java?",
//             new String[]{"Synchronize methods", "Ensure visibility of variables across threads", "Prevent object creation", "Optimize loops"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which `ExecutorService` method submits a task and returns a `Future` object?",
//             new String[]{"execute()", "submit()", "run()", "invokeAll()"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the difference between `throws` and `throw` in exception handling?",
//             new String[]{"`throws` declares, `throw` creates", "`throw` declares, `throws` creates", "Both are same", "`throws` is for unchecked exceptions"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which class is used to create a fixed-size thread pool?",
//             new String[]{"ThreadPoolExecutor", "ScheduledExecutorService", "Executors", "ForkJoinPool"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What is the default capacity of a `HashMap` in Java?",
//             new String[]{"8", "16", "32", "64"},
//             1
//         ));
//         questionPool.add(new Question(
//             "Which Stream operation is used to reduce elements to a single value?",
//             new String[]{"map()", "filter()", "reduce()", "collect()"},
//             2
//         ));
//         questionPool.add(new Question(
//             "What does the `synchronized` keyword do when applied to a method?",
//             new String[]{"Locks the object", "Pauses the thread", "Prevents instantiation", "Optimizes execution"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which annotation is used to mark a method for dependency injection in Spring?",
//             new String[]{"@Inject", "@Autowired", "@Resource", "@Bean"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What is the purpose of `Optional` in Java 8?",
//             new String[]{"Handle null values", "Optimize loops", "Manage threads", "Parse JSON"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which pattern separates object construction from its representation?",
//             new String[]{"Builder", "Prototype", "Facade", "Decorator"},
//             0
//         ));
//         questionPool.add(new Question(
//             "What is the output of `Integer.valueOf(127) == Integer.valueOf(127)`?",
//             new String[]{"true", "false", "Compilation error", "Runtime exception"},
//             0
//         ));
//         questionPool.add(new Question(
//             "Which interface is used for custom sorting in Java?",
//             new String[]{"Comparable", "Comparator", "Sortable", "Orderable"},
//             1
//         ));
//         questionPool.add(new Question(
//             "What does `CompletableFuture` provide in Java?",
//             new String[]{"Thread pooling", "Asynchronous programming", "Garbage collection", "Serialization"},
//             1
//         ));

//         // Prompt for 4 unique usernames
//         Scanner scanner = new Scanner(System.in);
//         Set<String> usernames = new HashSet<>();
//         System.out.println("Enter 4 unique usernames:");
//         while (usernames.size() < NUM_USERS) {
//             System.out.print("Username " + (usernames.size() + 1) + ": ");
//             String username = scanner.nextLine().trim();
//             if (username.isEmpty() || usernames.contains(username)) {
//                 System.out.println("Error: Username must be non-empty and unique.");
//             } else {
//                 usernames.add(username);
//                 User user = new User(username);
//                 users.add(user);
//                 leaderboard.add(user);
//                 // Assign 5 random questions per user
//                 List<Question> questions = new ArrayList<>(questionPool);
//                 Collections.shuffle(questions);
//                 userQuestions.add(questions.subList(0, QUESTIONS_PER_USER));
//             }
//         }
//         scanner.close();

//         ServerSocket serverSocket = new ServerSocket(PORT);
//         System.out.println("Server started on port " + PORT);

//         // Accept single client connection
//         Socket clientSocket = null;
//         try {
//             clientSocket = serverSocket.accept();
//             clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
//             clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             System.out.println("Client connected: " + clientSocket.getInetAddress());

//             // Run the quiz
//             runQuiz();
//         } catch (IOException e) {
//             System.out.println("Error accepting client: " + e.getMessage());
//         } finally {
//             if (clientSocket != null && !clientSocket.isClosed()) {
//                 try {
//                     clientSocket.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//             serverSocket.close();
//             // Close database connection
//             closeDatabase();
//         }
//     }

//     private static void initializeDatabase() {
//         String url = "jdbc:mysql://localhost:3306/quiz_system?useSSL=false&serverTimezone=UTC";
//         String user = "root"; // Replace with your MySQL username
//         String password = "abhigoyal@220905"; // Replace with your MySQL password

//         try {
//             // Load the MySQL JDBC driver
//             Class.forName("com.mysql.cj.jdbc.Driver");
//             dbConnection = DriverManager.getConnection(url, user, password);
//             // Ensure auto-commit is enabled
//             dbConnection.setAutoCommit(true);
//             System.out.println("Connected to database successfully.");
//         } catch (ClassNotFoundException e) {
//             System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         } catch (SQLException e) {
//             System.err.println("Failed to connect to database: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
//     }

//     private static void testDatabaseConnection() {
//         try (Statement stmt = dbConnection.createStatement()) {
//             ResultSet rs = stmt.executeQuery("SELECT 1 FROM quiz_results LIMIT 1");
//             System.out.println("Database connection test successful.");
//         } catch (SQLException e) {
//             System.err.println("Database connection test failed: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
//     }

//     private static void closeDatabase() {
//         if (dbConnection != null) {
//             try {
//                 dbConnection.close();
//                 System.out.println("Database connection closed.");
//             } catch (SQLException e) {
//                 System.err.println("Error closing database connection: " + e.getMessage());
//                 e.printStackTrace();
//             }
//         }
//     }

//     private static void saveResultsToDatabase() {
//         String insertSQL = "INSERT INTO quiz_results (username, score) VALUES (?, ?)";
//         try (PreparedStatement pstmt = dbConnection.prepareStatement(insertSQL)) {
//             for (User user : users) {
//                 pstmt.setString(1, user.getUsername());
//                 pstmt.setInt(2, user.getScore());
//                 int rowsAffected = pstmt.executeUpdate();
//                 System.out.println("Inserted " + rowsAffected + " row(s) for user: " + user.getUsername() + " with score: " + user.getScore());
//             }
//         } catch (SQLException e) {
//             System.err.println("Error saving results to database: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     private static void runQuiz() throws IOException {
//         int round = 0;
//         int totalQuestionsAnswered = 0;
//         int maxQuestions = NUM_USERS * QUESTIONS_PER_USER; // 4 users * 5 questions = 20

//         while (round < QUESTIONS_PER_USER && clientIn != null) {
//             for (int userIndex = 0; userIndex < NUM_USERS; userIndex++) {
//                 if (totalQuestionsAnswered >= maxQuestions) {
//                     break; // Stop if 20 questions have been answered
//                 }

//                 if (clientIn == null) {
//                     System.out.println("Client disconnected prematurely.");
//                     return;
//                 }

//                 String username = users.get(userIndex).getUsername();
//                 Question q = userQuestions.get(userIndex).get(round);

//                 // Send turn prompt
//                 clientOut.println("TURN:" + username);
//                 // Send question
//                 StringBuilder msg = new StringBuilder("Question: " + q.getQuestionText() + "\nOptions:\n");
//                 String[] options = q.getOptions();
//                 for (int i = 0; i < options.length; i++) {
//                     msg.append(i + 1).append(". ").append(options[i]).append("\n");
//                 }
//                 System.out.println("Question " + (round + 1) + " sent to " + username);
//                 clientOut.println(msg.toString());

//                 // Get answer
//                 String line;
//                 while ((line = clientIn.readLine()) != null) {
//                     if (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                         continue;
//                     }
//                     try {
//                         int answer = Integer.parseInt(line) - 1; // Convert to 0-based index
//                         if (q.isCorrect(answer)) {
//                             users.get(userIndex).addScore(10); // 10 points for correct
//                             clientOut.println("Correct!");
//                         } else {
//                             clientOut.println("Incorrect!");
//                         }
//                         totalQuestionsAnswered++;
//                         break; // Move to next question without updating leaderboard
//                     } catch (NumberFormatException e) {
//                         clientOut.println("Invalid input! Please choose 1, 2, 3, or 4.");
//                     }
//                 }
//             }
//             round++;
//         }

//         // After all questions, show final leaderboard, save results, and send thanks message
//         if (clientOut != null) {
//             broadcastFinalLeaderboard();
//             saveResultsToDatabase(); // Save results to database
//             clientOut.println("Thanks, your test has now completed!");
//         }
//     }

//     // Broadcast final leaderboard
//     private static void broadcastFinalLeaderboard() {
//         StringBuilder lb = new StringBuilder("Quiz ended! Final Leaderboard:\n");
//         PriorityQueue<User> temp = new PriorityQueue<>(leaderboard);
//         leaderboard.clear();
//         leaderboard.addAll(users); // Update leaderboard with final scores
//         int rank = 1;
//         while (!temp.isEmpty()) {
//             User user = temp.poll();
//             lb.append(rank++).append(". ").append(user.getUsername())
//               .append(": ").append(user.getScore()).append("\n");
//         }
//         clientOut.println(lb.toString());
//     }
// }















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