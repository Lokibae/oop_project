package model;

public class Question {
    private int questionId;
    private int surveyId;
    private String questionText;
    private String questionType;
    private String options;
    private int minRating;
    private int maxRating;

    // Constructor for Question (Multiple Choice, Rating Scale, or Text Input)
    public Question(int questionId, int surveyId, String questionText,
                    String questionType, String options, int minRating, int maxRating) {
        this.questionId = questionId;
        this.surveyId = surveyId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    // Constructor for Question without ID (for new questions)
    public Question(int surveyId, String questionText, String questionType,
                    String options, int minRating, int maxRating) {
        this.surveyId = surveyId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    // --- Getters & Setters ---
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public int getSurveyId() { return surveyId; }
    public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public int getMinRating() { return minRating; }
    public void setMinRating(int minRating) { this.minRating = minRating; }

    public int getMaxRating() { return maxRating; }
    public void setMaxRating(int maxRating) { this.maxRating = maxRating; }
}
