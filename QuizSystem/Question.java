







public class Question {
    private final String questionText;
    private final String[] options;
    private final int correctAnswerIndex;

    public Question(String questionText, String[] options, int correctAnswerIndex) {
        // Validate questionText
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty.");
        }

        // Validate options
        if (options == null || options.length < 2) {
            throw new IllegalArgumentException("Options array must contain at least 2 choices.");
        }
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("Options cannot contain null or empty strings.");
            }
        }

        // Validate correctAnswerIndex
        if (correctAnswerIndex < 0 || correctAnswerIndex >= options.length) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and " + (options.length - 1));
        }

        this.questionText = questionText;
        this.options = options.clone(); // Defensive copy to prevent external modification
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options.clone(); 
    }

    public boolean isCorrect(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question: ").append(questionText).append("\n");
        for (int i = 0; i < options.length; i++) {
            sb.append(i + 1).append(". ").append(options[i]).append("\n");
        }
        sb.append("Correct Answer Index: ").append(correctAnswerIndex);
        return sb.toString();
    }
}







