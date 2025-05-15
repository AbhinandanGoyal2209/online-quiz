import java.util.ArrayList;
import java.util.List;

public class QuestionManager {
    private final List<Question> questions;

    public QuestionManager() {
        this.questions = new ArrayList<>();
    }

    // CREATE
    public void addQuestion(Question q) {
        if (q == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        questions.add(q);
        System.out.println("Question added successfully!");
    }

    // READ
    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions); // Return a copy to prevent external modification
    }

    public Question getQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid question index");
        }
        return questions.get(index);
    }

    // UPDATE
    public boolean updateQuestion(int index, Question newQuestion) {
        if (index < 0 || index >= questions.size()) {
            return false;
        }
        questions.set(index, newQuestion);
        return true;
    }

    // DELETE
    public boolean deleteQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            return false;
        }
        questions.remove(index);
        return true;
    }

    // DISPLAY all questions (optional helper)
    public void displayAllQuestions() {
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("[" + i + "] " + questions.get(i).getQuestionText());
        }
    }

    // Get total count
    public int getTotalQuestions() {
        return questions.size();
    }
}
