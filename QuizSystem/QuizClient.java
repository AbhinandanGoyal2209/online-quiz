











import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class QuizClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 5000);
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            // Prompt and send username
            System.out.print("Enter your username: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                username = "Guest";
            }
            out.println(username);
            System.out.println("Username sent: " + username);

            // Handle server messages
            Thread serverListener = new Thread(() -> {
                try {
                    String line;
                    String currentUser = null;
                    boolean expectInput = false;
                    boolean quizEnded = false;
                    boolean isQuestionBlock = false;

                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("TURN:")) {
                            currentUser = line.substring("TURN:".length());
                            System.out.println("\n" + currentUser + "'s turn:");
                            continue;
                        } else if (line.startsWith("Question:")) {
                            isQuestionBlock = true; // Start of question block
                            System.out.println(line);
                            continue;
                        } else if (line.startsWith("POPUP:")) {
                            String message = line.substring("POPUP:".length());
                            try {
                                JOptionPane.showMessageDialog(null, message, "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                System.out.println(message); // Fallback for non-GUI environments
                            }
                        } else if (line.equals("Thanks, your test has now completed!")) {
                            quizEnded = true; // Stop prompting for input
                            expectInput = false; // Ensure no further prompts
                            System.out.println(line);
                            break; // Exit the server listener thread
                        } else if (line.startsWith("Quiz ended! Final Leaderboard:")) {
                            quizEnded = true; // Stop prompting for input
                            expectInput = false; // Ensure no further prompts
                            System.out.println(line);
                            continue;
                        } else {
                            System.out.println(line);
                        }

                        // Detect end of question block (last option line)
                        if (isQuestionBlock && line.startsWith("4. ")) {
                            expectInput = true;
                            isQuestionBlock = false; // End of question block
                        }

                        // Prompt for answer only if expecting input and quiz hasn't ended
                        if (expectInput && !quizEnded && !line.startsWith("Invalid input") && 
                            !line.startsWith("Correct") && !line.startsWith("Incorrect")) {
                            System.out.print(currentUser + ", enter your answer (1-4): ");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server: " + e.getMessage());
                }
            });
            serverListener.start();

            // Send user input only until quiz ends
            while (scanner.hasNextLine()) {
                // Check if quiz has ended before prompting for input
                if (!serverListener.isAlive()) {
                    break; // Exit the loop if the server listener has stopped
                }
                String answer = scanner.nextLine();
                out.println(answer);
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("Failed to connect or communicate with server. Please ensure the server is running at " + 
                               SERVER_ADDRESS + ":" + SERVER_PORT + ". Error: " + e.getMessage());
        }
    }
}






