import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * 
 * main
 * 
 * @author SnehaChidambar Rao Waddankeri 
 * loginid: waddanke 
 * CSCI 561 - Fall 2016 - HW2
 *
 */
public class Assignment2
{
	private int boardWidthHeight;
	private String modePlay;
	private String youPlay;
	private String opponentPlay;
	private int depth;
	private int[][] cellValues;
	private String[][] boardState;
	private final static HashMap<Integer, String> mapColumnData = new HashMap<Integer, String>()
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put(0, "A");
			put(1, "B");
			put(2, "C");
			put(3, "D");
			put(4, "E");
			put(5, "F");
			put(6, "G");
			put(7, "H");
			put(8, "I");
			put(9, "J");
			put(10, "K");
			put(11, "L");
			put(12, "M");
			put(13, "N");
			put(14, "O");
			put(15, "P");
			put(16, "Q");
			put(17, "R");
			put(18, "S");
			put(19, "T");
			put(20, "U");
			put(21, "V");
			put(22, "W");
			put(23, "X");
			put(24, "Y");
			put(25, "Z");
		}
	};

	public static void main(String args[])
	{
		FileReader readInputData = null;
		BufferedReader bufferedReader = null;
		homework homeData = new homework();
		try
		{
			homeData.fileCopy();
			readInputData = new FileReader("input.txt");
			bufferedReader = new BufferedReader(readInputData);
			List<String> inputData = new ArrayList<String>();
			for (String lineData = bufferedReader.readLine(); lineData != null; lineData = bufferedReader.readLine())
			{
				inputData.add(lineData.trim());
			}
			homeData.boardWidthHeight = Integer.parseInt(inputData.get(0).trim());
			homeData.modePlay = inputData.get(1);
			homeData.youPlay = inputData.get(2);
			homeData.opponentPlay = homeData.youPlay.equals("O")?"X":"O";
			homeData.depth = Integer.parseInt(inputData.get(3).trim());
			int indexOfCellValues = 4;
			homeData.extractCellValueData(inputData, indexOfCellValues);
			int indexOfBoardValues = indexOfCellValues + homeData.boardWidthHeight;
			homeData.extractBoardStateData(inputData, indexOfBoardValues);
			if(!homeData.isBoardSpaceAvailable(homeData.boardState))
            {
				//TODO
            }
			Date startTime;
			Date endTime;
			switch (homeData.modePlay.toUpperCase())
			{
				case "MINIMAX" : startTime = new Date(); 
					             Move minMax = homeData.findBestPositionMinMax();
					             endTime = new Date();
					             System.out.println("Time taken "+(endTime.getTime()-startTime.getTime()));
                  				 homeData.writeOutput(minMax);
								 break;
				case "ALPHABETA" : startTime = new Date();
					               Move alphaBeta = homeData.findBestPositionAlphaBeta();
					               endTime = new Date();
					               System.out.println("Time taken "+(endTime.getTime()-startTime.getTime()));
							       homeData.writeOutput(alphaBeta);
					               break;
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				bufferedReader.close();
				readInputData.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param inputData
	 * @param indexOfCellValues
	 */
	private void extractCellValueData(List<String> inputData, int indexOfCellValues)
	{
		cellValues = new int[boardWidthHeight][boardWidthHeight];
		for (int i = 0; i < boardWidthHeight; i++)
		{
			String s[] = inputData.get(i + indexOfCellValues).split(" ");
			for (int j = 0; j < boardWidthHeight; j++)
			{
				cellValues[i][j] = Integer.parseInt(s[j]);
			}
		}
	}

	/**
	 * @param inputData
	 * @param indexOfBoardValues
	 */
	private void extractBoardStateData(List<String> inputData, int indexOfBoardValues)
	{
		boardState = new String[boardWidthHeight][boardWidthHeight];
		for (int i = 0; i < boardWidthHeight; i++)
		{
			String s[] = inputData.get(i + indexOfBoardValues).split("");
			for (int j = 0; j < boardWidthHeight; j++)
			{
				boardState[i][j] = s[j];
			}
		}
	}

	/**
	 * @param board
	 * @return
	 */
	private int evaluateScore(String[][] board)
	{
		int gameScoreX = 0;
		int gameScoreO = 0;
		int gameScoreTotal = 0;
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("X"))
				{
					gameScoreX += cellValues[i][j];
				}
				if (board[i][j].equals("O"))
				{
					gameScoreO += cellValues[i][j];
				}
			}
		}
		if (youPlay.equals("X"))
		{
			gameScoreTotal = gameScoreX - gameScoreO;
		}
		if (youPlay.equals("O"))
		{
			gameScoreTotal = gameScoreO - gameScoreX;
		}
		return gameScoreTotal;
	}
	
	/**
	 * @param homeData
	 * @return
	 */
	private boolean isBoardSpaceAvailable(String[][] board)
	{
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("."))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return
	 */
	private Move findBestPositionMinMax()
	{
		String[][] childBoardState = new String[boardWidthHeight][boardWidthHeight];
	    ArrayList<Move> scores = new ArrayList<Move>();
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (boardState[i][j].equals("."))
				{
					childBoardState = copyArray(boardState);
					childBoardState[i][j] = youPlay;
					boolean isRaid = updateAndCheckBoardForRaid(childBoardState, i, j, youPlay);
					int currentBoardScore;
					currentBoardScore = minMoveValue(childBoardState, depth-1);
					Move score = new Move(i, j, currentBoardScore, isRaid);
					scores.add(score);
				}
			}
		}
		return bestMoveValue(scores);
	}

	/**
	 * @param board
	 * @return
	 */
	private String[][] copyArray(String[][] board)
	{
		String[][] childBoard = new String[boardWidthHeight][boardWidthHeight];
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				childBoard[i][j] = board[i][j];
			}
		}
		return childBoard;
	}

	/**
	 * @param childBoardState
	 * @param i
	 * @param j
	 * @param player 
	 */
	private boolean updateAndCheckBoardForRaid(String[][] childBoardState, int i, int j, String player)
	{
		boolean isRaid = false;
		String opponentPlayer = (player.equals("O"))?"X":"O";
		if ((j != 0 && childBoardState[i][j - 1].equals(player))
				|| (j != (boardWidthHeight - 1) && childBoardState[i][j + 1].equals(player))
				|| (i != 0 && childBoardState[i - 1][j].equals(player))
				|| (i != (boardWidthHeight - 1) && childBoardState[i + 1][j].equals(player)))
		{
			if(i != 0 && childBoardState[i - 1][j].equals(opponentPlayer))
			{
				childBoardState[i - 1][j] = player;
				isRaid = true;
			}
			if(j != 0 && childBoardState[i][j - 1].equals(opponentPlayer))
			{
				childBoardState[i][j-1] = player;
				isRaid = true;
			}
			if(i != (boardWidthHeight - 1) && childBoardState[i + 1][j].equals(opponentPlayer))
			{
				childBoardState[i + 1][j] = player;
				isRaid = true;
			}
			if(j != (boardWidthHeight - 1) && childBoardState[i][j + 1].equals(opponentPlayer))
			{
				childBoardState[i][j+1] = player;
				isRaid = true;
			}
		}
		return isRaid;
	}

	/**
	 * @param moves
	 * @return
	 */
	private Move bestMoveValue(ArrayList<Move> moves)
	{
		int scoreIndex = 0;
		int bestBoardScore = Integer.MIN_VALUE;
		for (int i = 0; i < moves.size(); i++)
		{
			Move move = moves.get(i);
			if (move.getGameScore() > bestBoardScore)
			{
				bestBoardScore = move.getGameScore();
				scoreIndex = i;
			}
			else if(move.getGameScore() == bestBoardScore && !move.isRaid())
			{
				boolean isCurrentRaid = moves.get(scoreIndex).isRaid();
				scoreIndex = (isCurrentRaid)?i:scoreIndex;
			}
		}
		return moves.get(scoreIndex);
	}

	/**
	 * @param childBoard
	 * @param currentDepth
	 */
	private int maxMoveValue(String[][] board, int currentDepth)
	{
		if (currentDepth == 0 || !isBoardSpaceAvailable(board))
		{
			return evaluateScore(board);
		}
		int bestBoardScore = Integer.MIN_VALUE;
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("."))
				{
					String[][] childBoard = copyArray(board);
					childBoard[i][j] = youPlay;
					updateAndCheckBoardForRaid(childBoard, i, j, youPlay);
					int currentBoardScore = minMoveValue(childBoard, currentDepth - 1);
					if (currentBoardScore > bestBoardScore)
					{
						bestBoardScore = currentBoardScore;
					}
				}
			}
		}
		return bestBoardScore;
	}
	
	/**
	 * @param childBoard
	 * @param currentDepth
	 * @return
	 */
	private int minMoveValue(String[][] board, int currentDepth)
	{
		if (currentDepth == 0 || !isBoardSpaceAvailable(board))
		{
			return evaluateScore(board);
		}
		int bestBoardScore = Integer.MAX_VALUE;
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("."))
				{
				    String[][] childBoard = copyArray(board);
					childBoard[i][j] = opponentPlay;
					updateAndCheckBoardForRaid(childBoard, i, j, opponentPlay);
					int currentBoardScore = maxMoveValue(childBoard, currentDepth - 1);
					if (currentBoardScore < bestBoardScore)
					{
						bestBoardScore = currentBoardScore;
					}
				}
			}
		}
		return bestBoardScore;
	
	}
	/**
	 * @return
	 */
	private Move findBestPositionAlphaBeta()
	{
		String[][] childBoardState = new String[boardWidthHeight][boardWidthHeight];
	    ArrayList<Move> scores = new ArrayList<Move>();
	    int alpha = Integer.MIN_VALUE;
	    int beta = Integer.MAX_VALUE;
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (boardState[i][j].equals("."))
				{
					childBoardState = copyArray(boardState);
					childBoardState[i][j] = youPlay;
					boolean isRaid = updateAndCheckBoardForRaid(childBoardState, i, j, youPlay);
					alpha = betaMoveValue(childBoardState, depth-1, alpha, beta);
					Move score = new Move(i, j, alpha, isRaid);
					scores.add(score);
				}
			}
		}
		return bestMoveValue(scores);
	}
	
	/**
	 * @param childBoard
	 * @param currentDepth
	 * @param beta 
	 * @param alpha 
	 */
	private int alphaMoveValue(String[][] board, int currentDepth, int alpha, int beta)
	{
		if (currentDepth == 0 || !isBoardSpaceAvailable(board))
		{
			return evaluateScore(board);
		}
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("."))
				{
					String[][] childBoard = copyArray(board);
					childBoard[i][j] = youPlay;
					updateAndCheckBoardForRaid(childBoard, i, j, youPlay);
				    alpha = Math.max(alpha,betaMoveValue(childBoard, currentDepth - 1, alpha, beta));
					if(beta <= alpha)
					{
						break;
					}
				}
			}
		}
		return alpha;
	}
	
	/**
	 * @param childBoard
	 * @param currentDepth
	 * @param beta 
	 * @param alpha 
	 * @return
	 */
	private int betaMoveValue(String[][] board, int currentDepth, int alpha, int beta)
	{
		if (currentDepth == 0 || !isBoardSpaceAvailable(board))
		{
			return evaluateScore(board);
		}
		for (int i = 0; i < boardWidthHeight; i++)
		{
			for (int j = 0; j < boardWidthHeight; j++)
			{
				if (board[i][j].equals("."))
				{
				    String[][] childBoard = copyArray(board);
					childBoard[i][j] = opponentPlay;
					updateAndCheckBoardForRaid(childBoard, i, j, opponentPlay);
					beta = Math.min(beta, alphaMoveValue(childBoard, currentDepth - 1, alpha, beta));
					if(beta <= alpha)
					{
						break;
					}
				}
			}
		}
		return beta;
	}

	
	/**
	 * data : row, column, score, stake/raid(0/1)
	 * @param bestMove
	 * @throws UnsupportedEncodingException
	 */
	private void writeOutput(Move bestMove) throws UnsupportedEncodingException
	{
		try
		{
			PrintWriter writer = new PrintWriter("output.txt");
			int rowVal = bestMove.getI();
			int colVal = bestMove.getJ();
			String columnRow = mapColumnData.get(colVal)+(rowVal+1);
			String StakeRaid = (bestMove.isRaid())?"Raid":"Stake";
			writer.println(columnRow+" "+StakeRaid);
			boardState[rowVal][colVal] = youPlay;
			updateAndCheckBoardForRaid(boardState, rowVal, colVal, youPlay);
			for(int i = 0;i < boardWidthHeight; i++)
			{ 
				String currentLine = "";
				for(int j= 0; j < boardWidthHeight ;j++)
				{
					currentLine += boardState[i][j];
				}
				writer.println(currentLine);
			}
			writer.close();
			try
			{
				compareTwoTextFiles();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void fileCopy() throws IOException
	{
		File dir = new File(".");
		int VAL_FILE_NO = 100;
		String source = dir.getCanonicalPath() + File.separator + VAL_FILE_NO+".in";
		String dest = dir.getCanonicalPath() + File.separator + "input.txt";
		File fin = new File(source);
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		FileWriter fstream = new FileWriter(dest, true);
		BufferedWriter out = new BufferedWriter(fstream);
		String aLine = null;
		while ((aLine = in.readLine()) != null)
		{
			// Process each line and add output to Dest.txt file
			out.write(aLine);
			out.newLine();
		}
		// do not forget to close the buffer reader
		in.close();
		// close buffer writer
		out.close();
	}
	
	public void compareTwoTextFiles() throws IOException
	{
		BufferedReader br1 = null;
        BufferedReader br2 = null;
        String sCurrentLine;
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        int VAL_FILE_NO = 100;
        String dest = VAL_FILE_NO+".out";
        br1 = new BufferedReader(new FileReader(dest));
        br2 = new BufferedReader(new FileReader("output.txt"));
        while ((sCurrentLine = br1.readLine()) != null) 
        {
            list1.add(sCurrentLine);
        }
        while ((sCurrentLine = br2.readLine()) != null)
        {
            list2.add(sCurrentLine);
        }
        List<String> tmpList = new ArrayList<String>(list1);
        tmpList.removeAll(list2);
        System.out.println("content from out which is not there in output.txt");
        for(int i=0;i<tmpList.size();i++)
        {
            System.out.println(tmpList.get(i)); //content from out which is not there in output.txt
        }
        System.out.println("content from out which is not there in output.txt");
        tmpList = list2;
        tmpList.removeAll(list1);
        for(int i=0;i<tmpList.size();i++)
        {
            System.out.println(tmpList.get(i)); //content from out which is not there in output.txt
        }
		br1.close();
		br2.close();
		/*File dir = new File(".");
		String source1 = dir.getCanonicalPath() + File.separator + VAL_FILE_NO+".out";
		String dest1 = dir.getCanonicalPath() + File.separator + "output.txt";
		File f1 = new File(source1);
		File f2 = new File(dest1);
		boolean result = FileUtils.contentEquals(f1, f2);
		if (!result)
		{
			System.out.println("Files content are not equal.");
		} 
		else
		{
			System.out.println("Files content are equal.");
		}*/
		
		
		/*File dir = new File(".");
		int VAL_FILE_NO = 0;
		String source = dir.getCanonicalPath() + File.separator + VAL_FILE_NO+".out";
		String dest = dir.getCanonicalPath() + File.separator + "output.txt";
		File f1 = new File(source);
		File f2 = new File(dest);
		boolean result = FileUtils.contentEquals(f1, f2);
		if (!result)
		{
			System.out.println("Files content are not equal.");
		} else
		{
			System.out.println("Files content are equal.");
		}
		result = FileUtils.contentEquals(f1, f1);
		if (!result)
		{
			System.out.println("Files content are not equal.");
		} else
		{
			System.out.println("Files content are equal.");
		}*/
	}
	/**
	 * @author SnehaCW
	 *
	 */
	private class Move
	{
		private int i;
		private int j;
		private int gameScore;
		private boolean isRaid;

		/**
		 * @param i
		 * @param j
		 * @param gameScore
		 * @param isRaid
		 */
		public Move(int i, int j, int gameScore, boolean isRaid)
		{
			this.i = i;
			this.j = j;
			this.gameScore = gameScore;
			this.isRaid = isRaid;
		}

		public int getI()
		{
			return i;
		}

		public void setI(int i)
		{
			this.i = i;
		}

		public int getJ()
		{
			return j;
		}

		public void setJ(int j)
		{
			this.j = j;
		}

		public int getGameScore()
		{
			return gameScore;
		}

		public void setGameScore(int gameScore)
		{
			this.gameScore = gameScore;
		}

		public boolean isRaid()
		{
			return isRaid;
		}

		public void setRaid(boolean isRaid)
		{
			this.isRaid = isRaid;
		}

		public String toString()
		{
			return "i: " + i + " j: " + j + " gamescore: " + gameScore;
		}
	}

}
