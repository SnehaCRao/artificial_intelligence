import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * main
 * 
 * @author Sneha Chidambar Rao Waddankeri 
 * loginid: waddanke 
 * CSCI 561 - Fall 2016 - HW3
 *
 */
/**
 * @author SnehaCW
 *
 */
public class Assignment3
{
	private static final String OPERATOR_NEGATE = "~";
	private static final String CLOSE_PARENTHESIS = ")";
	private static final String OPEN_PARENTHESIS = "(";
	private static final String OPERATOR_OR = "|";
	private static final String OPERATOR_AND = "&";
	private static final String NEGATED_PARENTHESIS = "~(";
	private static final String IMPLICATION_SYMBOL = "=>";

	private int indexOfNoOfSentences;
	private int noOfQueries;
	private int noOfSentences;
	private static ArrayList<String> queryList = new ArrayList<String>();
	private static ArrayList<String> kbList = new ArrayList<String>();
	private static ArrayList<String> cnfList = new ArrayList<String>();
	private static ArrayList<String> clauses;
	private static HashMap<String, String> infiniteLoopDetection = new HashMap<String, String>();
	static boolean debugEnabled = true;

	public static void clear()
	{
		queryList = new ArrayList<String>();
		kbList = new ArrayList<String>();
		cnfList = new ArrayList<String>();
		infiniteLoopDetection = new HashMap<String, String>();
	}

	public static void main(String args[])
	{
		FileReader readInputData = null;
		BufferedReader bufferedReader = null;
		try
		{
			readInputData = new FileReader("input.txt");
			bufferedReader = new BufferedReader(readInputData);
			List<String> inputData = new ArrayList<String>();
			for (String lineData = bufferedReader.readLine(); lineData != null; lineData = bufferedReader.readLine())
			{
				inputData.add(lineData.trim());
			}
			homework homeData = new homework();
			homeData.noOfQueries = Integer.parseInt(inputData.get(0).trim());
			extractQuery(inputData, homeData);
			homeData.indexOfNoOfSentences = homeData.noOfQueries + 1;
			homeData.noOfSentences = Integer.parseInt(inputData.get(homeData.indexOfNoOfSentences).trim());
			extractKnowledgeBase(inputData, homeData);
			convertToCNF();
			ArrayList<String> results = new ArrayList<String>();
			for (String query : queryList)
			{
				clauses = new ArrayList<String>(cnfList);
				query = query.replaceAll(" ", "");
				if (query.startsWith(OPERATOR_NEGATE))
				{
					query = query.substring(1, query.length());
				} else
				{
					query = OPEN_PARENTHESIS + OPERATOR_NEGATE + query + CLOSE_PARENTHESIS;
				}
				boolean result = resolution(query);
				if (debugEnabled)
				{
					System.out.println("resoultion result for query : " + query + "is :" + result);
				}
				results.add(result + "");
			}
			writeFile(results);
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				bufferedReader.close();
				readInputData.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private static void convertToCNF()
	{
		for (String kbStatement : kbList)
		{
			String eliminatedImplication = eliminateImplication(kbStatement.replace(" ", ""));
			String inwardNegatedString = inwardNegation(eliminatedImplication);
			String distributedString = getAndOverOrDistributedString(inwardNegatedString);
			List<String> seperateClause = seggregateClause(distributedString);
			cnfList.addAll(seperateClause);
		}
		if (debugEnabled)
		{
			System.out.println("cnflist " + cnfList);
		}
	}

	/**
	 * @param inputData
	 * @param homeData
	 */
	private static void extractKnowledgeBase(List<String> inputData, homework homeData)
	{
		for (int j = (homeData.indexOfNoOfSentences + 1); j <= (homeData.noOfQueries + homeData.noOfSentences + 1); j++)
		{
			kbList.add(inputData.get(j));
		}
	}

	/**
	 * @param inputData
	 * @param homeData
	 */
	private static void extractQuery(List<String> inputData, homework homeData)
	{
		for (int i = 1; i <= homeData.noOfQueries; i++)
		{
			queryList.add(inputData.get(i));
		}
	}

	/**
	 * @param statement
	 * @return
	 */
	public static HashSet<String> identifyPredicates(String statement)
	{
		HashSet<String> predicateSet = new HashSet<String>();
		String expr = statement.replaceAll(" ", "");
		String delims = "[~&=>|]";
		String query[] = expr.split(delims);
		String regex = "[A-Za-z]+";
		Pattern pattern = Pattern.compile(regex);
		for (int i = 0; i < query.length; i++)
		{
			Matcher match = pattern.matcher(query[i]);
			if (match.find())
			{
				int startIndex = match.start();
				int closedBracketIndex = query[i].indexOf(CLOSE_PARENTHESIS);
				if (closedBracketIndex == -1)
				{
					continue;
				}
				String predicate = query[i].substring(startIndex, closedBracketIndex + 1);
				predicateSet.add(predicate);
			}
		}
		return predicateSet;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static ArrayList<Predicate> getPredicates(String statement)
	{
		ArrayList<Predicate> predicateSet = new ArrayList<Predicate>();
		String expr = statement.replaceAll(" ", "");
		String delims = "[&=>|]";
		String query[] = expr.split(delims);
		String regex = "[A-Za-z]+";
		Pattern pattern = Pattern.compile(regex);
		for (int i = 0; i < query.length; i++)
		{
			Matcher match = pattern.matcher(query[i]);
			if (match.find())
			{
				int startIndex = match.start();
				boolean isNegated = false;
				if (startIndex != 0 && OPERATOR_NEGATE.equals(query[i].substring(startIndex - 1, startIndex)))
				{
					isNegated = true;
				}
				int closedBracketIndex = query[i].indexOf(CLOSE_PARENTHESIS);
				if (closedBracketIndex == -1)
				{
					continue;
				}
				String predicate = query[i].substring(startIndex, closedBracketIndex + 1);
				predicateSet.add(new Predicate(predicate, isNegated));
			}
		}
		return predicateSet;
	}

	/**
	 * @param predicateSet
	 */
	public static void identifyConstants(HashSet<String> predicateSet)
	{
		Set<String> constantSet = new HashSet<String>();
		for (String s : predicateSet)
		{
			constantSet.addAll(identifyConstants(s));
		}
	}

	/**
	 * @param statement
	 * @return
	 */
	private static Set<String> identifyConstants(String statement)
	{
		Set<String> constantSet = new HashSet<String>();
		String regex = "^[A-Z]+";
		Pattern pattern = Pattern.compile(regex);
		int openBrackIndex = statement.indexOf(OPEN_PARENTHESIS);
		int closeBrackIndex = statement.indexOf(CLOSE_PARENTHESIS);
		String str = statement.substring(openBrackIndex + 1, closeBrackIndex);
		String strSplit[] = str.split(",");
		for (int i = 0; i < strSplit.length; i++)
		{
			Matcher match = pattern.matcher(strSplit[i]);
			if (match.find())
			{
				constantSet.add(strSplit[i]);
			}
		}
		return constantSet;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static String eliminateImplication(String statement)
	{
		String removedImplicationString = statement;
		while (statement.contains(IMPLICATION_SYMBOL))
		{
			int implicationIndex = statement.indexOf(IMPLICATION_SYMBOL);
			int openParenthesisIndex = getOpenParenthesisIndex(statement.substring(0, implicationIndex));
			String leftPart = statement.substring(openParenthesisIndex + 1, implicationIndex);
			leftPart = "(~" + leftPart + CLOSE_PARENTHESIS;
			removedImplicationString = statement.substring(0, openParenthesisIndex + 1) + leftPart + OPERATOR_OR
					+ statement.substring(implicationIndex + IMPLICATION_SYMBOL.length(), statement.length());
			statement = removedImplicationString;
		}
		return removedImplicationString;
	}

	/**
	 * @param rightPart
	 * @return
	 */
	private static int getCloseParenthesisIndex(String rightPart)
	{
		int strLength = rightPart.length();
		Stack<String> parenthesisStack = new Stack<String>();
		int closeParenthesisIndex = -1;
		for (int i = 0; i < strLength; i++)
		{
			if (rightPart.charAt(i) == ')')
			{
				// opening frst paren
				if (parenthesisStack.isEmpty())
				{
					closeParenthesisIndex = i;
					break;
				} else
				{
					parenthesisStack.pop();
				}
			} else if (rightPart.charAt(i) == '(')
			{
				parenthesisStack.push(OPEN_PARENTHESIS);
			}
		}
		return closeParenthesisIndex;
	}

	/**
	 * @param leftPart
	 * @return
	 */
	private static int getOpenParenthesisIndex(String leftPart)
	{
		int strLength = leftPart.length();
		Stack<String> parenthesisStack = new Stack<String>();
		int openParenthesisIndex = -1;
		for (int i = strLength - 1; i >= 0; i--)
		{
			if (leftPart.charAt(i) == '(')
			{
				// opening frst paren
				if (parenthesisStack.isEmpty())
				{
					openParenthesisIndex = i;
					break;
				} else
				{
					parenthesisStack.pop();
				}

			} else if (leftPart.charAt(i) == ')')
			{
				parenthesisStack.push(CLOSE_PARENTHESIS);
			}
		}
		return openParenthesisIndex;
	}

	/**
	 * @param statement
	 * @return
	 */
	public static String inwardNegation(String statement)
	{
		String negatedStatement = statement;
		while (statement.contains(NEGATED_PARENTHESIS))
		{
			int indexOfNegatedParenthesis = statement.indexOf(NEGATED_PARENTHESIS);
			int closeParenthesisIndex = getCloseParenthesisIndex(
					statement.substring(indexOfNegatedParenthesis + NEGATED_PARENTHESIS.length(), statement.length()))
					+ indexOfNegatedParenthesis + NEGATED_PARENTHESIS.length();
			String temp = statement.substring(indexOfNegatedParenthesis + NEGATED_PARENTHESIS.length(),
					closeParenthesisIndex);
			ArrayList<String> operatorOperandStatements = getOperatorOperandStatement(temp);
			String negateOperatorOperand = "";
			for (String operatorOperand : operatorOperandStatements)
			{
				negateOperatorOperand += negateOperatorOperand(operatorOperand);
			}
			negatedStatement = statement.substring(0, indexOfNegatedParenthesis) + negateOperatorOperand
					+ statement.substring(closeParenthesisIndex + 1, statement.length());
			statement = negatedStatement;
		}
		negatedStatement = negatedStatement.replaceAll(OPERATOR_NEGATE + OPERATOR_NEGATE, "");
		return negatedStatement;
	}

	/**
	 * @param operatorOperand
	 * @return
	 */
	private static String negateOperatorOperand(String operatorOperand)
	{
		String negateStatement = null;
		if (operatorOperand.equals(OPERATOR_AND))
		{
			negateStatement = OPERATOR_OR;
		} else if (operatorOperand.equals(OPERATOR_OR))
		{
			negateStatement = OPERATOR_AND;
		} else
		{
			if (operatorOperand.startsWith("(~"))
			{
				negateStatement = operatorOperand.replace("(~", "");
				negateStatement = negateStatement.substring(0, negateStatement.length() - 1);
			} else
			{
				negateStatement = "(~" + operatorOperand + CLOSE_PARENTHESIS;
			}

		}
		return negateStatement;
	}

	/**
	 * @param temp
	 * @return
	 */
	private static ArrayList<String> getOperatorOperandStatement(String temp)
	{
		ArrayList<String> operatorOperandStatements = new ArrayList<String>();
		if (temp.charAt(0) == '(')
		{
			int closeParenthesisIndex = getCloseParenthesisIndex(temp.substring(1, temp.length()));
			operatorOperandStatements.add(temp.substring(0, closeParenthesisIndex + 2));
			if (!(closeParenthesisIndex == temp.length() - 1))
			{
				char operatorCheckChar = temp.charAt(closeParenthesisIndex + 2);
				if (operatorCheckChar == '&' || operatorCheckChar == '|')
				{
					operatorOperandStatements.add(operatorCheckChar + "");
				}
				operatorOperandStatements.add(temp.substring(closeParenthesisIndex + 3, temp.length()));
			}
		} else
		{
			int indexOfAnd = temp.indexOf(OPERATOR_AND);
			int indexOfOr = temp.indexOf(OPERATOR_OR);
			int indexOfOperator = -1;
			String operator = "";
			if (indexOfAnd != -1 && (indexOfAnd < indexOfOr || indexOfOr == -1))
			{
				indexOfOperator = indexOfAnd;
				operator = OPERATOR_AND;
			} else if (indexOfOr != -1)
			{
				indexOfOperator = indexOfOr;
				operator = OPERATOR_OR;
			}
			if (indexOfOperator != -1)
			{
				operatorOperandStatements.add(temp.substring(0, indexOfOperator));
				operatorOperandStatements.add(operator);
				operatorOperandStatements.add(temp.substring(indexOfOperator + 1, temp.length()));
			} else
			{
				operatorOperandStatements.add(temp);
			}
		}
		return operatorOperandStatements;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static String getAndOverOrDistributedString(String statement)
	{
		String andOverOrString = statement;
		int indexOfOr = getAndOverOrIndex(statement);
		while (indexOfOr != -1)
		{
			andOverOrString = distributeAndOverOr(statement, indexOfOr);
			statement = andOverOrString;
			indexOfOr = getAndOverOrIndex(statement);
		}
		return andOverOrString;
	}

	/**
	 * @param statement
	 * @param indexOfOr
	 * @return
	 */
	private static String distributeAndOverOr(String statement, int indexOfOr)
	{
		String distributeAndOverOrString = null;
		int openParenthesisIndex = getOpenParenthesisIndex(statement.substring(0, indexOfOr));
		int closeParenthesisIndex = getCloseParenthesisIndex(statement.substring(indexOfOr + 1, statement.length()))
				+ (indexOfOr + 1);
		String leftOperand = statement.substring(openParenthesisIndex + 1, indexOfOr);
		String rightOperand = statement.substring(indexOfOr + 1, closeParenthesisIndex);
		if (leftOperand.contains(OPERATOR_AND))
		{
			int indexOfAnd = leftOperand.indexOf(OPERATOR_AND);
			String leftPart = leftOperand.substring(1, indexOfAnd) + "|" + rightOperand;
			String rightPart = leftOperand.substring(indexOfAnd + 1, leftOperand.length() - 1) + "|" + rightOperand;
			distributeAndOverOrString = OPEN_PARENTHESIS + leftPart + CLOSE_PARENTHESIS + OPERATOR_AND
					+ OPEN_PARENTHESIS + rightPart + CLOSE_PARENTHESIS;
			distributeAndOverOrString = statement.substring(0, openParenthesisIndex + 1) + distributeAndOverOrString
					+ statement.substring(closeParenthesisIndex, statement.length());
		} else if (rightOperand.contains(OPERATOR_AND))
		{
			int indexOfAnd = rightOperand.indexOf(OPERATOR_AND);
			String leftPart = leftOperand + "|" + rightOperand.substring(1, indexOfAnd);
			String rightPart = leftOperand + "|" + rightOperand.substring(indexOfAnd + 1, rightOperand.length() - 1);
			distributeAndOverOrString = OPEN_PARENTHESIS + leftPart + CLOSE_PARENTHESIS + OPERATOR_AND
					+ OPEN_PARENTHESIS + rightPart + CLOSE_PARENTHESIS;
			distributeAndOverOrString = statement.substring(0, openParenthesisIndex + 1) + distributeAndOverOrString
					+ statement.substring(closeParenthesisIndex, statement.length());
		}
		return distributeAndOverOrString;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static int getAndOverOrIndex(String statement)
	{
		String temp = statement;
		int indexVal = -1;
		int tempIndex = -1;
		while (temp.contains(OPERATOR_OR))
		{
			int indexOfOr = temp.indexOf(OPERATOR_OR);
			tempIndex += indexOfOr + 1;
			int openParenthesisIndex = getOpenParenthesisIndex(temp.substring(0, indexOfOr));
			int closeParenthesisIndex = getCloseParenthesisIndex(temp.substring(indexOfOr + 1, temp.length()))
					+ (indexOfOr + 1);
			String leftOperand = temp.substring(openParenthesisIndex + 1, indexOfOr);
			String rightOperand = temp.substring(indexOfOr + 1, closeParenthesisIndex);
			if (leftOperand.contains(OPERATOR_AND) || rightOperand.contains(OPERATOR_AND))
			{
				indexVal = tempIndex;
				break;
			}
			temp = temp.substring(indexOfOr + 1, temp.length());
		}
		return indexVal;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static List<String> seggregateClause(String statement)
	{
		ArrayList<String> seperatedClauses = new ArrayList<String>();
		if (statement.contains(OPERATOR_AND))
		{
			String temp[] = statement.split(OPERATOR_AND);
			for (String str : temp)
			{
				int openParenthesisIndex = getOpenParenthesisIndex(str);
				if (openParenthesisIndex != -1)
				{
					str = str.substring(openParenthesisIndex + 1, str.length());
				}
				int closeParenthesisIndex = getCloseParenthesisIndex(str);
				if (closeParenthesisIndex != -1)
				{
					str = str.substring(0, closeParenthesisIndex);
				}
				str = factoring(str);
				seperatedClauses.add(str);
			}
		} else
		{
			statement = factoring(statement);
			seperatedClauses.add(statement);
		}
		return seperatedClauses;
	}

	/**
	 * @param s1
	 * @param s2
	 * @param substitution
	 * @return
	 */
	private static Map<String, String> unify(Object s1, Object s2, Map<String, String> substitution)
	{
		if (substitution == null)
		{
			return null;
		}
		if (s1 instanceof String && s2 instanceof String)
		{
			String x = (String) s1;
			String y = (String) s2;
			if (x.equals(y))
			{
				return substitution;
			}
			if (isVariable(x))
			{
				return unifyVar(x, y, substitution);
			}
			if (isVariable(y))
			{
				return unifyVar(y, x, substitution);
			}
			ArrayList<Predicate> predicateX = getPredicates(x);
			ArrayList<Predicate> predicateY = getPredicates(y);
			if (!predicateX.isEmpty() && !predicateY.isEmpty())
			{
				for (Predicate xPredicate : predicateX)
				{
					for (Predicate yPredicate : predicateY)
					{
						String predicateXName = getPredicateName(xPredicate.getPredicate());
						String predicateYName = getPredicateName(yPredicate.getPredicate());
						if (predicateXName.equals(predicateYName) && (xPredicate.isNegated ^ yPredicate.isNegated))
						{
							List<String> predicateXArg = getPredicateArg(xPredicate.getPredicate());
							List<String> predicateYArg = getPredicateArg(yPredicate.getPredicate());
							if (predicateXArg.size() == predicateYArg.size())
							{
								substitution = unify(predicateXArg, predicateYArg, substitution);
							}
						}
					}
				}
				return substitution;
			}
		}
		if (s1 instanceof List && s2 instanceof List)
		{
			@SuppressWarnings("unchecked")
			List<String> xArgs = (List<String>) s1;
			@SuppressWarnings("unchecked")
			List<String> yArgs = (List<String>) s2;
			String firstXArg = xArgs.get(0);
			String firstYArg = yArgs.get(0);
			xArgs.remove(0);
			yArgs.remove(0);
			if (!xArgs.isEmpty() && !yArgs.isEmpty())
			{
				return unify(xArgs, yArgs, unify(firstXArg, firstYArg, substitution));
			} else
			{
				return unify(firstXArg, firstYArg, substitution);
			}
		}
		return null;
	}

	private static List<String> getPredicateArg(String predicateYName)
	{
		predicateYName = predicateYName.substring(predicateYName.indexOf(OPEN_PARENTHESIS) + 1);
		String[] strSplit = predicateYName.split("[(),]+");
		return new ArrayList<String>(Arrays.asList(strSplit));
	}

	private static String getPredicateName(String predicate)
	{
		int index = predicate.indexOf(OPEN_PARENTHESIS);
		if (index != -1)
		{
			return predicate.substring(0, index);
		}
		return null;
	}

	/**
	 * @param x
	 * @param y
	 * @param substitution
	 * @return
	 */
	private static Map<String, String> unifyVar(String x, String y, Map<String, String> substitution)
	{
		if (substitution.get(x) != null)
		{
			return unify(substitution.get(x), y, substitution);
		}
		if (substitution.get(y) != null)
		{
			return unify(substitution.get(y), x, substitution);
		}
		substitution.put(x, y);
		return substitution;
	}

	/**
	 * @param x
	 * @return
	 */
	private static boolean isVariable(String x)
	{
		boolean isVariable = false;
		String regex = "^[a-z]{1}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(x);
		if (match.find())
		{
			isVariable = true;
		}
		return isVariable;
	}

	/**
	 * @param goal
	 * @param clauses
	 * @return
	 */
	private static boolean resolution(String goal)
	{
		ArrayList<String> clausesCopy = new ArrayList<String>(clauses);
		for (String clause : clausesCopy)
		{
			if (debugEnabled)
			{
				System.out.println("goal : " + goal + "clause : " + clause);
			}
			String subsumeResult = subsume(goal, clause);
			if (goal.equals(subsumeResult))
			{
				continue;
			} else if (subsumeResult == null)
			{
				return true;
			} else
			{
				if (clauses.contains(subsumeResult))
				{
					continue;
				}
				clauses.add(subsumeResult);
				boolean result = resolution(subsumeResult);
				if (result)
				{
					return true;
				} else
				{
					continue;
				}
			}
		}
		return false;
	}

	/**
	 * @param goal
	 * @param clause
	 * @return
	 */
	private static String subsume(String goal, String clause)
	{
		String subsumeString = goal;
		if (infiniteLoopDetection.get(goal) != null && clause.equals(infiniteLoopDetection.get(goal)))
		{
			return subsumeString;
		}
		boolean isSubsumePossible = canSubsume(goal, clause);
		String tempGoal = goal;
		String tempClause = clause;
		if (isSubsumePossible)
		{
			Map<String, String> substitution = unify(goal, clause, new HashMap<String, String>());
			if (substitution != null && !substitution.isEmpty())
			{
				if (debugEnabled)
				{
					System.out.println(substitution);
				}
				Set<Entry<String, String>> entrySet = substitution.entrySet();
				for (Entry<String, String> entry : entrySet)
				{
					if (!isVariable(entry.getValue()))
					{
						goal = replaceWithSubstitution(goal, entry.getKey(), entry.getValue());
						clause = replaceWithSubstitution(clause, entry.getKey(), entry.getValue());
					} else
					{
						String clausePredicate = getSubsumePredicate(goal, clause);
						String unifyClausePredicate = replaceWithSubstitution(clausePredicate, entry.getKey(),
								entry.getValue());
						clause = clause.replace(clausePredicate, unifyClausePredicate);

						String goalPredicate = getSubsumePredicate(clause, goal);
						String unifyGoalPredicate = replaceWithSubstitution(clausePredicate, entry.getKey(),
								entry.getValue());
						goal = goal.replace(goalPredicate, unifyGoalPredicate);
					}
				}
			}
			HashSet<String> goalPredicates = identifyPredicates(goal);
			HashSet<String> clausePredicates = identifyPredicates(clause);
			boolean isSubsume = false;
			for (String goalPredicate : goalPredicates)
			{
				for (String clausePredicate : clausePredicates)
				{
					if (clausePredicate.equals(goalPredicate))
					{
						int clauseIndex;
						int goalIndex;
						if (goal.contains(OPERATOR_NEGATE + goalPredicate)
								&& !clause.contains(OPERATOR_NEGATE + clausePredicate))
						{
							clauseIndex = clause.indexOf(clausePredicate);
							clause = clause.substring(0, clauseIndex)
									+ clause.substring(clauseIndex + clausePredicate.length(), clause.length());
							goalIndex = goal.indexOf(OPERATOR_NEGATE + goalPredicate) - 1;
							goal = goal.substring(0, goalIndex) + goal.substring(
									goalIndex + goalPredicate.length() + OPERATOR_NEGATE.length() + 1, goal.length());
							isSubsume = true;
						} else if (!goal.contains(OPERATOR_NEGATE + goalPredicate)
								&& clause.contains(OPERATOR_NEGATE + clausePredicate))
						{
							clauseIndex = clause.indexOf(OPERATOR_NEGATE + clausePredicate) - 1;
							clause = clause.substring(0, clauseIndex) + clause.substring(
									clauseIndex + clausePredicate.length() + OPERATOR_NEGATE.length() + 1,
									clause.length());
							goalIndex = goal.indexOf(goalPredicate);
							goal = goal.substring(0, goalIndex)
									+ goal.substring(goalIndex + goalPredicate.length(), goal.length());
							isSubsume = true;
						}
						goal = formatStatement(goal);
						clause = formatStatement(clause);
					}
				}
			}
			if (isSubsume)
			{
				if (goal == null || goal.isEmpty())
				{
					if (clause == null || clause.isEmpty())
					{
						subsumeString = null;
					} else
					{
						subsumeString = clause;
					}
				}

				else if (clause == null || clause.isEmpty())
				{
					if (goal == null || goal.isEmpty())
					{
						subsumeString = null;
					} else
					{
						subsumeString = goal;
					}
				} else
				{
					if (!subsumeString.equals(goal))
					{
						if (clause.contains(goal))
						{
							subsumeString = clause;
						} else
						{
							subsumeString = OPEN_PARENTHESIS + goal + OPERATOR_OR + clause + CLOSE_PARENTHESIS;
							subsumeString = factoring(subsumeString);
						}
					}
				}
			}
		}
		boolean hasInfiniteLoop = hasInfiniteLoop(tempGoal, tempClause);
		if (hasInfiniteLoop)
		{
			infiniteLoopDetection.put(subsumeString, tempClause);
		}
		if (debugEnabled)
		{
			System.out.println("subsumeString : " + subsumeString);
		}
		return subsumeString;
	}

	/**
	 * @param clause
	 * @param entry
	 * @return
	 */
	private static String replaceWithSubstitution(String clause, String key, String value)
	{
		int notVarIndex = clause.indexOf(OPEN_PARENTHESIS);
		do
		{
			int index = clause.indexOf(key, notVarIndex);
			if (index == -1)
			{
				break;
			}
			boolean isVariable = indexHasVariable(clause, index);
			if (isVariable)
			{
				clause = clause.substring(0, index) + value + clause.substring(index + 1, clause.length());
			} else
			{
				notVarIndex = index + 1;
			}
		} while (true);
		return clause;
	}

	/**
	 * @param clause1
	 * @param clause2
	 * @return
	 */
	private static String factoring(String clause)
	{
		ArrayList<Predicate> clause1Predicates = getPredicates(clause);
		ArrayList<Predicate> clause2Predicates = getPredicates(clause);
		ArrayList<String> factoredPredicates = new ArrayList<String>();
		for (int i = 0; i < clause1Predicates.size(); i++)
		{
			for (int j = 0; j < clause1Predicates.size(); j++)
			{
				if (i == j)
				{
					continue;
				}
				Predicate clause1Predicate = clause1Predicates.get(i);
				Predicate clause2Predicate = clause2Predicates.get(j);
				;
				String predicate1 = clause1Predicate.getPredicate();
				String predicate2 = clause2Predicate.getPredicate();
				String predicate1Name = getPredicateName(predicate1);
				String predicate2Name = getPredicateName(predicate2);
				if (factoredPredicates.contains(predicate1Name))
				{
					continue;
				}
				if (predicate1.equals(predicate2) && (clause1Predicate.isNegated() == clause2Predicate.isNegated))
				{
					if (clause2Predicate.isNegated())
					{
						predicate2 = OPERATOR_NEGATE + predicate2;
					}
					predicate2 = predicate2.replace("(", "\\(");
					predicate2 = predicate2.replace(")", "\\)");
					clause = clause.replaceFirst(predicate2, "");
					clause = formatStatement(clause);
					factoredPredicates.add(predicate2Name);
				} else if (predicate1Name.equals(predicate2Name))
				{
					List<String> predicate1Arg = getPredicateArg(predicate1);
					List<String> predicate2Arg = getPredicateArg(predicate2);
					boolean canFactor = false;
					int clause1var = 0;
					int clause2var = 0;
					if (predicate1Arg.size() == predicate2Arg.size())
					{
						if (clause1Predicate.isNegated() == clause2Predicate.isNegated)
						{
							for (int k = 0; k < predicate1Arg.size(); k++)
							{
								if (predicate1Arg.get(k).equals(predicate2Arg.get(k)))
								{
									canFactor = true;
								} else if (isVariable(predicate1Arg.get(k)) || isVariable(predicate2Arg.get(k)))
								{
									canFactor = true;
								} else
								{
									canFactor = false;
									break;
								}
								if (isVariable(predicate1Arg.get(k)))
								{
									clause1var++;
								}
								if (isVariable(predicate2Arg.get(k)))
								{
									clause2var++;
								}
							}
						}
					}
					if (canFactor)
					{
						if (clause1var >= clause2var)
						{
							if (clause2Predicate.isNegated())
							{
								predicate1 = OPERATOR_NEGATE + predicate1;
							}
							factoredPredicates.add(predicate2Name);
							clause = clause.replace(predicate1, "");
							clause = formatStatement(clause);
						} else
						{
							if (clause2Predicate.isNegated())
							{
								predicate2 = OPERATOR_NEGATE + predicate2;
							}
							factoredPredicates.add(predicate2Name);
							clause = clause.replace(predicate2, "");
							clause = formatStatement(clause);
						}
					}
				}
			}
		}
		return clause;
	}

	/**
	 * @param goal
	 * @param clause
	 * @return
	 */
	private static boolean canSubsume(String goal, String clause)
	{
		int count = 0;
		ArrayList<Predicate> predicateGoal = getPredicates(goal);
		ArrayList<Predicate> predicateClause = getPredicates(clause);
		if (!predicateGoal.isEmpty() && !predicateClause.isEmpty())
		{
			for (Predicate goalPredicate : predicateGoal)
			{
				for (Predicate clausePredicate : predicateClause)
				{
					String predicateGoalName = getPredicateName(goalPredicate.getPredicate());
					String predicateClauseName = getPredicateName(clausePredicate.getPredicate());
					if (predicateGoalName.equals(predicateClauseName)
							&& (goalPredicate.isNegated ^ clausePredicate.isNegated))
					{
						count++;
						break;
					}
				}
			}
		}
		if (count == 1)
		{
			return true;
		}
		return false;
	}

	/**
	 * @param goal
	 * @param clause
	 * @return
	 */
	private static String getSubsumePredicate(String goal, String clause)
	{
		String predicate = null;
		ArrayList<Predicate> predicateGoal = getPredicates(goal);
		ArrayList<Predicate> predicateClause = getPredicates(clause);
		if (!predicateGoal.isEmpty() && !predicateClause.isEmpty())
		{
			for (Predicate goalPredicate : predicateGoal)
			{
				for (Predicate clausePredicate : predicateClause)
				{
					String predicateGoalName = getPredicateName(goalPredicate.getPredicate());
					String predicateClauseName = getPredicateName(clausePredicate.getPredicate());
					if (predicateGoalName.equals(predicateClauseName)
							&& (goalPredicate.isNegated ^ clausePredicate.isNegated))
					{
						predicate = clausePredicate.getPredicate();
						break;
					}
				}
			}
		}
		return predicate;
	}

	/**
	 * @param goal
	 * @param clause
	 * @return
	 */
	private static boolean hasInfiniteLoop(String goal, String clause)
	{
		boolean hasContractingPredicate = false;
		boolean hasSamePredicate = false;
		ArrayList<Predicate> predicateGoal = getPredicates(goal);
		ArrayList<Predicate> predicateClause = getPredicates(clause);
		if (!predicateGoal.isEmpty() && !predicateClause.isEmpty())
		{
			for (Predicate goalPredicate : predicateGoal)
			{
				for (Predicate clausePredicate : predicateClause)
				{
					String predicateGoalName = getPredicateName(goalPredicate.getPredicate());
					String predicateClauseName = getPredicateName(clausePredicate.getPredicate());
					if (predicateGoalName.equals(predicateClauseName)
							&& (goalPredicate.isNegated ^ clausePredicate.isNegated))
					{
						hasContractingPredicate = true;
					}
					if (predicateGoalName.equals(predicateClauseName)
							&& (goalPredicate.isNegated && clausePredicate.isNegated))
					{
						hasSamePredicate = true;
					}
				}
			}
		}
		if (hasContractingPredicate && hasSamePredicate)
		{
			return true;
		}
		return false;
	}

	/**
	 * @param statement
	 * @return
	 */
	private static String formatStatement(String statement)
	{
		String clause = "";
		String split[] = statement.split("\\" + OPERATOR_OR);
		for (int i = 0; i < split.length; i++)
		{
			String regex = "^[()]*$";
			Pattern pattern = Pattern.compile(regex);
			Matcher match = pattern.matcher(split[i]);
			if (!match.find())
			{
				if (!clause.isEmpty())
				{
					clause += OPERATOR_OR;
				}
				int openIndex = getOpenParenthesisIndex(split[i]);
				int closeIndex = getCloseParenthesisIndex(split[i]);
				if (openIndex != -1)
				{
					clause += split[i].substring(0, openIndex) + split[i].substring(openIndex + 1, split[i].length());
				} else if (closeIndex != -1)
				{
					if (openIndex != -1)
					{
						clause += split[i].substring(0, closeIndex)
								+ split[i].substring(closeIndex + 1, split[i].length());
					} else
					{
						clause += split[i].substring(0, closeIndex);
					}
				} else
				{
					clause += split[i];
				}
			}
		}
		return clause;
	}

	/**
	 * @param goal
	 * @param index
	 * @return
	 */
	private static boolean indexHasVariable(String goal, int index)
	{
		String regex = "^[(),]$";
		Pattern pattern = Pattern.compile(regex);
		String previousChar = goal.substring(index - 1, index);
		String nextChar = goal.substring(index + 1, index + 2);
		Matcher match = pattern.matcher(previousChar);
		if (match.find())
		{
			match = pattern.matcher(nextChar);
			if (match.find())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param results
	 * @throws UnsupportedEncodingException
	 */
	public static void writeFile(ArrayList<String> results) throws UnsupportedEncodingException
	{
		try
		{
			PrintWriter writer = new PrintWriter("output.txt");
			for (int i = 0; i < results.size(); i++)
			{
				writer.println(results.get(i).toUpperCase());
			}
			writer.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static class Predicate
	{
		private boolean isNegated;
		private String predicate;

		public Predicate(String predicate, boolean isNegated)
		{
			this.isNegated = isNegated;
			this.predicate = predicate;
		}

		public String getPredicate()
		{
			return predicate;
		}

		public void setPredicate(String predicate)
		{
			this.predicate = predicate;
		}

		public boolean isNegated()
		{
			return isNegated;
		}

		public void setNegated(boolean isNegated)
		{
			this.isNegated = isNegated;
		}
	}
}