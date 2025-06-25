package dao;

import model.Survey;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyDAO {
    private Connection connection;

    public SurveyDAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/survey_db";  // Your database URL
            String username = "root";  // Your database username
            String password = "Daniel_10";  // Your database password
            connection = DriverManager.getConnection(url, username, password);  // Attempt to connect
            System.out.println("Connection to database established successfully!");  // Log successful connection
        } catch (SQLException e) {
            e.printStackTrace();  // Print out the exception stack trace if the connection fails
            System.err.println("Failed to connect to the database. Please check the database credentials and connection.");
        }
    }

    // Method to get all surveys from the database
    public List<Survey> getAllSurveys() {
        List<Survey> surveys = new ArrayList<>();
        String query = "SELECT * FROM surveys";  // Modify to get all columns if needed
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Survey survey = new Survey(
                        rs.getInt("id"),  // Fetch the ID from the database
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status")
                );
                surveys.add(survey);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching surveys from the database.");
        }
        return surveys;
    }

    // Method to save survey into the database
    public boolean saveSurvey(Survey survey) {
        String query = "INSERT INTO surveys (username, title, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, survey.getUsername());
            stmt.setString(2, survey.getTitle());
            stmt.setString(3, survey.getDescription());
            stmt.setString(4, survey.getStartDate());
            stmt.setString(5, survey.getEndDate());
            stmt.setString(6, survey.getStatus());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving survey to the database.");
            return false;
        }
    }

    // Method to get questions for a specific survey by its ID
    public List<Question> getQuestionsBySurveyId(int surveyId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE survey_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, surveyId);  // Using surveyId to fetch questions
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question question = new Question(
                        rs.getInt("question_id"),
                        rs.getInt("survey_id"),
                        rs.getString("question_text"),
                        rs.getString("question_type"),
                        rs.getString("options"),
                        rs.getInt("min_rating"),
                        rs.getInt("max_rating")
                );
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching questions for survey ID: " + surveyId);
        }
        return questions;
    }

    // Method to add a new question to the database
    public boolean addQuestion(Question question) {
        String query = "INSERT INTO questions (survey_id, question_text, question_type, options, min_rating, max_rating) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, question.getSurveyId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getQuestionType());
            stmt.setString(4, question.getOptions());
            stmt.setInt(5, question.getMinRating());
            stmt.setInt(6, question.getMaxRating());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding question to the database.");
            return false;
        }
    }

    // Method to update an existing question
    public boolean updateQuestion(Question question) {
        String query = "UPDATE questions SET question_text = ?, question_type = ?, options = ?, min_rating = ?, max_rating = ? WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getQuestionType());
            stmt.setString(3, question.getOptions());  // Save the options as a string
            stmt.setInt(4, question.getMinRating());
            stmt.setInt(5, question.getMaxRating());
            stmt.setInt(6, question.getQuestionId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating question in the database.");
            return false;
        }
    }
    // Method to get options for a specific question
    public String getQuestionOptions(int questionId) {
        String query = "SELECT options FROM questions WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);  // Use questionId to fetch the options
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("options");  // Return the options field for the given questionId
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching question options for question ID: " + questionId);
        }
        return "";  // Return empty string if no options found
    }
    // Method to delete a question from the database
    public boolean deleteQuestion(int questionId) {
        String query = "DELETE FROM questions WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting question from the database.");
            return false;
        }
    }
    // Method to delete the survey and its associated questions from the database
    public boolean deleteSurveyAndQuestions(int surveyId) {
        String deleteQuestionsQuery = "DELETE FROM questions WHERE survey_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuestionsQuery)) {
            stmt.setInt(1, surveyId);
            stmt.executeUpdate();  // Delete the associated questions

            String deleteSurveyQuery = "DELETE FROM surveys WHERE id = ?";
            try (PreparedStatement stmtSurvey = connection.prepareStatement(deleteSurveyQuery)) {
                stmtSurvey.setInt(1, surveyId);
                int rowsAffected = stmtSurvey.executeUpdate();  // Delete the survey
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting survey and its questions.");
            return false;
        }
    }
    // Method to get a specific survey title by its ID
    public String getSurveyTitle(int surveyId) {
        String query = "SELECT title FROM surveys WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, surveyId);  // Using surveyId to fetch the title
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching survey title from the database.");
        }
        return null;
    }

    // Method to get a specific survey status by its ID
    public String getSurveyStatus(int surveyId) {
        String query = "SELECT status FROM surveys WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, surveyId);  // Using surveyId to fetch the status
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching survey status from the database.");
        }
        return null;
    }

    // Method to update the survey title in the database
    public boolean updateSurveyTitle(int surveyId, String newTitle) {
        String query = "UPDATE surveys SET title = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setInt(2, surveyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating survey title in the database.");
            return false;
        }
    }

    // Method to update the survey status in the database
    public boolean updateSurveyStatus(int surveyId, String newStatus) {
        String query = "UPDATE surveys SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, surveyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating survey status in the database.");
            return false;
        }
    }

    // Method to delete the survey from the database
    public boolean deleteSurvey(int surveyId) {
        String query = "DELETE FROM surveys WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, surveyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting survey from the database.");
            return false;
        }
    }
}
