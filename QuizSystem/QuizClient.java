// import java.io.*;
// import java.net.*;
// import java.util.Scanner;

// public class QuizClient {
//     private static final String SERVER_ADDRESS = "localhost";
//     private static final int SERVER_PORT = 12345;

//     public static void main(String[] args) {
//         try (Socket socket = new Socket()) {
//             // Attempt to connect with a timeout
//             socket.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 5000);
//             System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             Scanner scanner = new Scanner(System.in);

//             // Send username
//             System.out.print("Enter your username: ");
//             String username = scanner.nextLine();
//             out.println(username);

//             // Start a thread to read server messages
//             new Thread(() -> {
//                 try {
//                     String line;
//                     while ((line = in.readLine()) != null) {
//                         System.out.println(line);
//                     }
//                 } catch (IOException e) {
//                     System.out.println("Disconnected from server: " + e.getMessage());
//                 }
//             }).start();

//             // Send answers
//             while (scanner.hasNextLine()) {
//                 String answer = scanner.nextLine();
//                 out.println(answer);
//             }

//         } catch (IOException e) {
//             System.err.println("Failed to connect or communicate with server: " + e.getMessage());
//         }
//     }
// }



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

            // Handle server messages
            new Thread(() -> {
                try {
                    String line;
                    String currentUser = null;
                    boolean expectInput = false;
                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("TURN:")) {
                            currentUser = line.substring("TURN:".length());
                            expectInput = true;
                            System.out.println("\n" + currentUser + "'s turn:");
                            continue;
                        } else if (line.startsWith("POPUP:")) {
                            String message = line.substring("POPUP:".length());
                            try {
                                JOptionPane.showMessageDialog(null, message, "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                System.out.println(message); // Fallback for non-GUI environments
                            }
                        } else {
                            System.out.println(line);
                            if (expectInput && !line.startsWith("Invalid input") && !line.startsWith("Correct") && !line.startsWith("Incorrect")) {
                                System.out.print(currentUser + ", enter your answer (1-4): ");
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server: " + e.getMessage());
                }
            }).start();

            // Send user input
            while (scanner.hasNextLine()) {
                String answer = scanner.nextLine();
                out.println(answer);
            }
        } catch (IOException e) {
            System.err.println("Failed to connect or communicate with server: " + e.getMessage());
        }
    }
}