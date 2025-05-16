




import java.io.*;
import java.net.*;
import javax.swing.*;

public class QuizClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Prompt for username via GUI
            String username = JOptionPane.showInputDialog(null, "Enter your username:", "Quiz Login", JOptionPane.PLAIN_MESSAGE);
            if (username == null) {
                // User canceled the dialog
                return;
            }
            username = username.trim();
            if (username.isEmpty()) {
                username = "Guest";
            }
            out.println(username);

            // Handle server messages
            Thread serverListener = new Thread(() -> {
                try {
                    String line;
                    String currentUser = null;
                    StringBuilder questionBlock = new StringBuilder();
                    StringBuilder leaderboardBlock = new StringBuilder();
                    boolean expectInput = false;
                    boolean quizEnded = false;
                    boolean isQuestionBlock = false;
                    boolean isLeaderboardBlock = false;

                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("TURN:")) {
                            currentUser = line.substring("TURN:".length());
                            continue;
                        } else if (line.startsWith("Question:")) {
                            isQuestionBlock = true;
                            questionBlock = new StringBuilder(line).append("\n");
                            continue;
                        } else if (line.startsWith("POPUP:")) {
                            String message = line.substring("POPUP:".length());
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, message, "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
                            });
                        } else if (line.equals("Thanks, your test has now completed!")) {
                            quizEnded = true;
                            expectInput = false;
                            // Display leaderboard if collected
                            if (leaderboardBlock.length() > 0) {
                                final String leaderboardMessage = leaderboardBlock.toString();
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, leaderboardMessage, "Final Leaderboard", JOptionPane.INFORMATION_MESSAGE);
                                });
                            }
                            break;
                        } else if (line.startsWith("Quiz ended! Final Leaderboard:")) {
                            isLeaderboardBlock = true;
                            leaderboardBlock = new StringBuilder(line).append("\n");
                            continue;
                        } else {
                            if (isQuestionBlock) {
                                questionBlock.append(line).append("\n");
                            } else if (isLeaderboardBlock) {
                                leaderboardBlock.append(line).append("\n");
                            }
                        }

                        // Detect end of question block (last option line)
                        if (isQuestionBlock && line.startsWith("4. ")) {
                            expectInput = true;
                            isQuestionBlock = false;
                        }

                        // Prompt for answer via GUI if expecting input
                        if (expectInput && !quizEnded && !line.startsWith("Invalid input")) {
                            String finalQuestionBlock = questionBlock.toString();
                            String finalCurrentUser = currentUser;
                            SwingUtilities.invokeLater(() -> {
                                String[] options = {"1", "2", "3", "4"};
                                String answer = (String) JOptionPane.showInputDialog(
                                    null,
                                    finalCurrentUser + "'s turn:\n" + finalQuestionBlock,
                                    "Quiz Question",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]
                                );
                                if (answer != null) {
                                    out.println(answer);
                                } else {
                                    // User canceled; send an invalid response to keep server expecting input
                                    out.println("invalid");
                                }
                            });
                            expectInput = false; // Prevent multiple prompts
                        }

                        // Handle invalid input
                        if (line.startsWith("Invalid input")) {
                            final String errorMessage = line;
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
                            });
                            expectInput = true; // Re-prompt for input
                        }
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Disconnected from server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });
            serverListener.start();

           
            serverListener.join();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Failed to connect or communicate with server. Please ensure the server is running at " +
                SERVER_ADDRESS + ":" + SERVER_PORT + ". Error: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Client interrupted: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}