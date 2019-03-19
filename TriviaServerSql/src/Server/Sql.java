package Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;

import general.Question;
import general.Score;

public class Sql {

	private static Connection connect;
	
	public static void connection() { // connect to mySql server

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Works");
		} catch (ClassNotFoundException e) {e.printStackTrace();}
	}

	public static void conectingToSQL() { // connect to mySql server
		connection();
		String host = "jdbc:mysql://localhost:3306/trivia_game";
		String username = "root";
		String password = "124434";
		try {
			connect = (Connection) DriverManager.getConnection(host, username, password);
			System.out.println("work");
		} catch (SQLException e) {e.printStackTrace();}
	}

	// get all questions from database and convert to Json string
	public static String getQuestions() {
		ArrayList<Question> questions = new ArrayList<>();
		Gson gsonQuetions = new Gson();
		PreparedStatement statement;
		
		try {
			statement = connect.prepareStatement("SELECT * FROM trivia_questions");
			ResultSet result = statement.executeQuery();

			while (result.next()) { // every row create new question and add to list.
				questions.add(new Question(result.getString(1), result.getString(2), result.getString(3),
						result.getString(4), result.getString(5), result.getInt(6), result.getString(7)));
			}
		} catch (SQLException e) {e.printStackTrace();}
		String stringQuestions = gsonQuetions.toJson(questions);
		
		return stringQuestions;
	}

	// return all scores from database and convert to Json string
	public static String getScores() {
		ArrayList<Score> scores = new ArrayList<>();
		Gson gsonScores = new Gson();
		PreparedStatement statement;
		
		try {
			statement = connect.prepareStatement("SELECT * FROM highscores");
			ResultSet result = statement.executeQuery();

			while (result.next()) { // every row create new score and add to list.
				scores.add(new Score(result.getString(1), result.getInt(2), result.getString(3)));
			}
		} catch (SQLException e) {e.printStackTrace();}
		String stringScores = gsonScores.toJson(scores);
		
		return stringScores;
	}
	
	// add new score to database
    public static void insertScoreToDB(String newScore) {
    	
    	Gson gson = new Gson();
        Score scoreObj = gson.fromJson(newScore, Score.class);//convert json string to Score object

        String sqlInsert = "insert into trivia_game.highscores (name, score, difficulty) values (?, ?, ?)";
        try {
            PreparedStatement pst = connect.prepareStatement(sqlInsert);
            pst.setString(1, scoreObj.getName());
            pst.setInt(2, scoreObj.getScore());
            pst.setString(3, scoreObj.getDifficulty());
            pst.execute();

        } catch (SQLException e) { e.printStackTrace();}
    }

}
