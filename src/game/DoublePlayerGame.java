package game;

import java.util.Scanner;

import users.Player;
import utilities.Display;

public class DoublePlayerGame extends Game {
	private InGameQuestionList questionList;
    private Player player1, player2;
    private int gamePointsPlayer1, gamePointsPlayer2;
    private Scanner in;
	

	protected DoublePlayerGame(QuestionCategory questionCategory, String playerName1, String playerName2) {
		super(questionCategory);
		
		gamePointsPlayer1 = gamePointsPlayer2 = 0;
		in = new Scanner(System.in);
		
		if (questionCategory.equals(QuestionCategory.RANDOM)){
            questionList = new RandomQuestionList(GameMode.DOUBLE, Game.questions);
        } else {
            questionList = new CategoryQuestionsList(GameMode.DOUBLE, questionCategory, Game.questions);
        }
		
		player1 = player2 = null;
        
        for (Player p : Game.players) {
        	if (p.getUserName().equals(playerName1)) {
        		player1 = p;
        	}
        	if (p.getUserName().equals(playerName2)) {
        		player2 = p;
        	}
        }   
        
        if (player1 == null) {
        	player1 = new Player(playerName1);
        	Game.players.add(player1);
        }
        if (player2 == null) {
        	player2 = new Player(playerName2);
        	Game.players.add(player2);
        }
        
        welcomePlayer(playerName1,player1);
        welcomePlayer(playerName2,player2);
	}
	
	@Override
	public void playGame() {
		for (Question currentQuestion : questionList.getQuestions()) {
			Display.drawPlayerHeader(player1.getUserName(), gamePointsPlayer1, player2.getUserName(),gamePointsPlayer2);
			
			Display.printFormatted(currentQuestion.toString());

			System.out.print("> " + player1.getUserName() + ": ");
			int player1answer = getPlayersAnswer();
			System.out.print("> " + player2.getUserName() + ": ");
			int player2answer = getPlayersAnswer();
			
			int player1score = currentQuestion.validateAnswer(player1answer);
			int player2score = currentQuestion.validateAnswer(player2answer);
			
			if (player1score == 0) {
				// answer is correct, increase answers
				player1.increaseAnsweredQuestions(currentQuestion.getCategory());
			}
			if (player2score == 0) {
				// answer is correct, increase answers
				player2.increaseAnsweredQuestions(currentQuestion.getCategory());
			}

			Display.printFormatted("Correct answer is: " + currentQuestion.getCorrectAnswer());
			
			if (player1score > player2score) {
				Display.printFormatted(player2.getUserName() + " was closer to the answer.");
				gamePointsPlayer2 += 10;
			} 
			else if (player1score < player2score) {
				Display.printFormatted(player1.getUserName() + " was closer to the answer.");
				gamePointsPlayer1 += 10;
			} 
			else {
				Display.printFormatted("Both players are correct.");
				gamePointsPlayer1 += 10;				
				gamePointsPlayer2 += 10;
			}
		}
		endGame();
	}

	@Override
	public void endGame() {
		addPointsToPlayer(player1, gamePointsPlayer1);
		addPointsToPlayer(player2, gamePointsPlayer2);
		
		if (gamePointsPlayer1 > gamePointsPlayer2) {
			String message = String.format("%s has won!\n%s has earned %d points, while %s has earned %d points.\n",
					player1.getUserName(), player1.getUserName(), gamePointsPlayer1,
					player2.getUserName(), gamePointsPlayer2);
			Display.printFormatted(message);
		} 
		else if (gamePointsPlayer1 < gamePointsPlayer2) {
			String message = String.format("%s has won!\n%s has earned %d points, while %s has earned %d points.\n",
					player2.getUserName(), player2.getUserName(), gamePointsPlayer2,
					player1.getUserName(), gamePointsPlayer1);
			Display.printFormatted(message);
		}
		else {
			Display.printFormatted("The game ends as a draw. Both players have earned " + gamePointsPlayer1 + " points.");
		}
		
		player1.checkForBadges(gamePointsPlayer1);
		player2.checkForBadges(gamePointsPlayer2);
	}

	@Override
	protected int getPlayersAnswer() {
		 String playerInput = in.nextLine();
		 
		 try {
			 int answer = Integer.parseInt(playerInput);
			 return answer;
		 } catch (NumberFormatException e) {
			 System.out.println("> Invalid input, try again");
			 return getPlayersAnswer();
		 }
	}

	
}
