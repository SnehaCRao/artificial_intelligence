import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Comparator;

/**
 * 
 * main
 * 
 * @author SnehaChidambar Rao Waddankeri
 * loginid: waddanke
 * CSCI 561 - Fall 2016 - HW1
 *
 */
public class Assignment1
{
	String algorithmName;
	String startState;
	String goalState;
	int noOfLiveTrafficLines;
	Map<String, LinkedHashMap<String, Integer>> liveTrafficLines;
	int noOfSundayLiveTrafficLines;
	Map<String, Integer> sundayTrafficLines;

	/**
	 * @param currentData
	 * @return
	 */
	public Set<String> getChildren(String currentData)
	{
		HashMap<String, Integer> neighborMap = liveTrafficLines.get(currentData);
		if (neighborMap != null)
		{
			return neighborMap.keySet();
		}
		return null;
	}

	/**
	 * @param currentData
	 * @return
	 */
	public HashMap<String, Integer> getChildrenMap(ArrayList<String> currentData)
	{
		HashMap<String, Integer> neighborMap = liveTrafficLines.get(currentData.get(0));
		return neighborMap;
	}

	public Integer getSundayTrafficData(String currentData)
	{
		Integer neighborAStarValue = sundayTrafficLines.get(currentData);
		return neighborAStarValue;
	}

	/**
	 * 
	 * @param data
	 * @throws UnsupportedEncodingException
	 */
	public void displayData(ArrayList<String> data) throws UnsupportedEncodingException
	{
		try
		{
			PrintWriter writer = new PrintWriter("output.txt");
			for (int i = 0; i < data.size(); i++)
			{
				writer.println(data.get(i) + " " + i);
			}
			writer.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private ArrayList<String> breadthFirstSearch()
	{
		ArrayList<String> path = new ArrayList<String>();
		if (startState.equals(goalState))
		{
			path.add(startState);
			return path;
		}
		Queue<String> frontierQueueBFS = new LinkedList<String>();
		HashSet<String> exploredBFS = new HashSet<String>();
		ArrayList<String> shortestPath = new ArrayList<String>();
		frontierQueueBFS.add(startState);
		while (!frontierQueueBFS.isEmpty())
		{
			String current = frontierQueueBFS.poll();
			if (current.equals(goalState))
			{
				return processNavigationPath(startState, goalState, path, shortestPath);
			}
			exploredBFS.add(current);
			Set<String> childNode = getChildren(current);
			if (childNode != null && !childNode.isEmpty())
			{
				for (String child : childNode)
				{
					if (!exploredBFS.contains(child) && !frontierQueueBFS.contains(child))
					{
						path.add(child);
						path.add(current);
						frontierQueueBFS.add(child);
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param startStart
	 * @param goalState
	 * @param path
	 * @param shortestPath
	 * @return
	 */
	public static ArrayList<String> processNavigationPath(String startStart, String goalState, ArrayList<String> path,
			ArrayList<String> shortestPath)
	{
		boolean flag = true;
		while (flag)
		{
			int index = path.indexOf(goalState);
			shortestPath.add(0, goalState);
			goalState = path.get(index + 1);
			if (startStart.equals(goalState))
			{
				shortestPath.add(0, startStart);
				flag = false;
			}
		}
		return shortestPath;
	}
	
	/**
	 * @param startStart
	 * @param goalState
	 * @param path
	 * @param shortestPath
	 * @return
	 */
	public static ArrayList<String> processNavigationPathForAStarSearch(String startStart, String goalState, ArrayList<String> path,
			ArrayList<String> shortestPath)
	{
			boolean flag = true;
			while (flag)
			{
				int index = getIndexOfChildKey(path, goalState);//path.indexOf(goalState);
				shortestPath.add(0, goalState);
				goalState = path.get(index + 1);
				if (startStart.equals(goalState))
				{
					shortestPath.add(0, startStart);
					flag = false;
				}
			}
			return shortestPath;
	}


	private ArrayList<String> depthFirstSearch()
	{
		ArrayList<String> path = new ArrayList<String>();
		if (startState.equals(goalState))
		{
			path.add(startState);
			return path;
		}
		Stack<String> frontierStackDFS = new Stack<String>();
		HashSet<String> exploredDFS = new HashSet<String>();
		ArrayList<String> shortestPath = new ArrayList<String>();
		frontierStackDFS.add(startState);
		while (!frontierStackDFS.isEmpty())
		{
			String current = frontierStackDFS.pop();
			exploredDFS.add(current);
			Set<String> childNode = getChildren(current);
			if (childNode != null && !childNode.isEmpty())
			{
				String[] childNodeArr = childNode.toArray(new String[childNode.size()]);
				for (int j = childNodeArr.length - 1; j >= 0; j--)
				{
					String child = childNodeArr[j];
					if (!exploredDFS.contains(child) && !frontierStackDFS.contains(child))
					{
						path.add(child);
						path.add(current);
						if (child.equals(goalState))
						{
							return processNavigationPath(startState, goalState, path, shortestPath);
						}
						frontierStackDFS.add(child);
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private ArrayList<String> uniformCostSearch()
	{
		ArrayList<String> path = new ArrayList<String>();
		if (startState.equals(goalState))
		{
			path.add(startState);
			return path;
		}
		PriorityQueue<ArrayList<String>> frontierQueueUCS = new PriorityQueue<ArrayList<String>>(20,
				new Comparator<ArrayList<String>>()
				{
					@Override
					public int compare(ArrayList<String> data1, ArrayList<String> data2)
					{
						if (Integer.parseInt(data1.get(1)) >= Integer.parseInt(data2.get(1)))
						{
							return 1;
						} 
						else if (Integer.parseInt(data1.get(1)) < Integer.parseInt(data2.get(1)))
						{
							return -1;
						}
						else
						{
							return 0;
						}
					}
				});
		
		ArrayList<String> startStateData = new ArrayList<String>();
		startStateData.add(startState);
		startStateData.add("0");
		HashSet<ArrayList<String>> exploredUCS = new HashSet<ArrayList<String>>();
		ArrayList<String> shortestPath = new ArrayList<String>();
		frontierQueueUCS.add(startStateData);
		while (!frontierQueueUCS.isEmpty())
		{
			ArrayList<String> current = frontierQueueUCS.poll();
			if (current.get(0).equals(goalState))
			{
				return processNavigationPath(startState, goalState, path, shortestPath);
			}
			exploredUCS.add(current);
			HashMap<String, Integer> childNode = getChildrenMap(current);
			if (childNode != null && !childNode.isEmpty())
			{
				for (Map.Entry<String, Integer> child : childNode.entrySet())
				{
					String childKey = child.getKey();
					boolean childPresentCheck = isChildPresentFrontierQueue(frontierQueueUCS, childKey);
					boolean closedQueueCheck = isChildPresentInClosed(exploredUCS,childKey);
					Integer updateValue = childNode.get(childKey) + Integer.parseInt(current.get(1));
					if (!closedQueueCheck && !childPresentCheck)
					{
						ArrayList<String> updateChild = new ArrayList<String>();
						path.add(childKey);
						path.add(current.get(0));
						updateChild.add(childKey);
						updateChild.add(updateValue.toString());
						frontierQueueUCS.add(updateChild);
					} 
					else if (childPresentCheck)
					{
						for (ArrayList<String> data : frontierQueueUCS)
						{
							if (data.get(0).equals(childKey) && updateValue < Integer.parseInt(data.get(1)))
							{
								frontierQueueUCS.remove(data);
								ArrayList<String> updateChild = new ArrayList<String>();
								updateChild.add(childKey);
								updateChild.add(updateValue.toString());
								frontierQueueUCS.add(updateChild);
								int val = path.indexOf(childKey);
								path.remove(val + 1);
								path.remove(val);
								path.add(childKey);
								path.add(current.get(0));
								break;
							}
						}
					} 
					else if (closedQueueCheck)
					{
						for (ArrayList<String> data : exploredUCS)
						{
							if (data.get(0).equals(childKey) && updateValue < Integer.parseInt(data.get(1)))
							{
								exploredUCS.remove(current);
								ArrayList<String> updateChild = new ArrayList<String>();
								updateChild.add(childKey);
								updateChild.add(updateValue.toString());
								frontierQueueUCS.add(updateChild);
								int val = path.indexOf(childKey);
								path.remove(val + 1);
								path.remove(val);
								path.add(childKey);
								path.add(current.get(0));
								break;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param frontierQueueData
	 * @param child
	 * @return
	 */
	private boolean isChildPresentFrontierQueue(PriorityQueue<ArrayList<String>> frontierQueueData, String child)
	{
		for (ArrayList<String> data : frontierQueueData)
		{
			if (child.equals(data.get(0)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	private ArrayList<String> AStarSearch()
	{
		ArrayList<String> path = new ArrayList<String>();
		if (startState.equals(goalState))
		{
			path.add(startState);
			return path;
		}
		PriorityQueue<ArrayList<String>> frontierQueueAStar = new PriorityQueue<ArrayList<String>>(1000,
				new Comparator<ArrayList<String>>()
				{
					@Override
					public int compare(ArrayList<String> data1, ArrayList<String> data2)
					{
						if (Integer.parseInt(data1.get(1)) >= Integer.parseInt(data2.get(1)))
						{
							return 1;
						} 
						else if (Integer.parseInt(data1.get(1)) < Integer.parseInt(data2.get(1)))
						{
							return -1;
						} 
						else
						{
							return 0;
						}
					}
				});
		HashSet<ArrayList<String>> exploredAStar = new HashSet<ArrayList<String>>();
		ArrayList<String> shortestPath = new ArrayList<String>();
		ArrayList<String> startStateData = new ArrayList<String>();
		
		Integer sundayTrafficValue = getSundayTrafficData(startState);
		Integer startStateVal = 0 + sundayTrafficValue;
		startStateData.add(startState);
		startStateData.add(startStateVal.toString());
		frontierQueueAStar.add(startStateData);
		while (!frontierQueueAStar.isEmpty())
		{
			ArrayList<String> current = frontierQueueAStar.poll();
			if (current.get(0).equals(goalState))
			{
				return processNavigationPathForAStarSearch(startState, goalState, path, shortestPath);
			}
			exploredAStar.add(current);
			HashMap<String, Integer> childNode = getChildrenMap(current);
			if (childNode != null && !childNode.isEmpty())
			{
				for (Map.Entry<String, Integer> child : childNode.entrySet())
				{
					String childKey = child.getKey();
					boolean childPresentCheck = isChildPresentFrontierQueue(frontierQueueAStar, childKey);
					boolean closedQueueCheck = isChildPresentInClosed(exploredAStar,childKey);
					sundayTrafficValue = getSundayTrafficData(childKey);
					Integer currentPathCost = Integer.parseInt(current.get(1)) - sundayTrafficLines.get(current.get(0));// g[current]
					Integer updateValue = childNode.get(childKey) + sundayTrafficValue + currentPathCost;
					if (!closedQueueCheck && !childPresentCheck)
					{
						ArrayList<String> updateChild = new ArrayList<String>();
						path.add(childKey);
						path.add(current.get(0));
						updateChild.add(childKey);
						updateChild.add(updateValue.toString());
						frontierQueueAStar.add(updateChild);
					} 
					else if (childPresentCheck)
					{
						for (ArrayList<String> data : frontierQueueAStar)
						{
							if (data.get(0).equals(childKey) && updateValue < (Integer.parseInt(data.get(1))))
							{
								frontierQueueAStar.remove(data);
								ArrayList<String> updateChild = new ArrayList<String>();
								updateChild.add(childKey);
								updateChild.add(updateValue.toString());
								frontierQueueAStar.add(updateChild);
								int val = getIndexOfChildKey(path, childKey);
								path.remove(val + 1);
								path.remove(val);
								path.add(childKey);
								path.add(current.get(0));
								break;
							}
						}
					}
					else if(closedQueueCheck)
					{
						for (ArrayList<String> data : exploredAStar)
						{
							if (childKey.equals(data.get(0)) && updateValue < (Integer.parseInt(data.get(1))))
							{
								exploredAStar.remove(data);
								ArrayList<String> updateChild = new ArrayList<String>();
								updateChild.add(childKey);
								updateChild.add(updateValue.toString());
								frontierQueueAStar.add(updateChild);
								int val = getIndexOfChildKey(path, childKey);
								path.remove(val + 1);
								path.remove(val);
								path.add(childKey);
								path.add(current.get(0));
								break;
							}
						}
					}
				}
			}
		}
		return null;

	}

	/**
	 * @param path
	 * @param childKey
	 * @return
	 */
	private static int getIndexOfChildKey(ArrayList<String> path, String childKey) 
	{
		for(int i =0; i < path.size(); i++)
		{
			if(childKey.equals(path.get(i)) && i % 2 == 0)
			{
				return i;
			}
		}
		return -1;
	}

	private boolean isChildPresentInClosed(HashSet<ArrayList<String>> exploredAStar, String child)
	{
		for (ArrayList<String> data : exploredAStar)
		{
			if (data.get(0).equals(child))
			{
				return true;
			}
		}
		return false;
	}

	public static void main(String args[])
	{
		try
		{
			FileReader readInputData = new FileReader("input.txt");
			BufferedReader bufferedReader = new BufferedReader(readInputData);
			List<String> inputData = new ArrayList<>();
			for (String lineData = bufferedReader.readLine(); lineData != null; lineData = bufferedReader.readLine())
			{
				inputData.add(lineData.trim());
			}
			homework homeData = new homework();
			homeData.algorithmName = inputData.get(0);
			homeData.startState = inputData.get(1);
			homeData.goalState = inputData.get(2);
			homeData.noOfLiveTrafficLines = Integer.parseInt(inputData.get(3).trim());
			int indexOfNoOfLiveTrafficLines = 3;
			int indexOfNoOfSundayTrafficLines = -1;
			Integer indexOfSundayLines = 3 + homeData.noOfLiveTrafficLines;
			if ((inputData.get(indexOfSundayLines) != null) && (inputData.size() > (indexOfSundayLines + 1)))
			{
				homeData.noOfSundayLiveTrafficLines = Integer
						.parseInt(inputData.get(indexOfNoOfLiveTrafficLines + homeData.noOfLiveTrafficLines + 1).trim());
				indexOfNoOfSundayTrafficLines = indexOfNoOfLiveTrafficLines + homeData.noOfLiveTrafficLines + 1;
			}
			extractLiveTrafficData(inputData, homeData, indexOfNoOfLiveTrafficLines);
			homeData.liveTrafficLines
					.forEach((key, value) -> System.out.println("final live traffic lines " + key + " : " + value));
			if (indexOfNoOfSundayTrafficLines > 0)
			{
				extractSundayTrafficLine(inputData, homeData, indexOfNoOfSundayTrafficLines);
				System.out.println("final sunday traffic lines " + homeData.sundayTrafficLines);
			}
			readInputData.close();
			switch (homeData.algorithmName.toUpperCase())
			{
			case "BFS":
				ArrayList<String> bfsData = homeData.breadthFirstSearch();
				homeData.displayData(bfsData);
				System.out.println(bfsData);
				break;
			case "DFS":
				ArrayList<String> dfsData = homeData.depthFirstSearch();
				homeData.displayData(dfsData);
				System.out.println(dfsData);
				break;
			case "UCS":
				ArrayList<String> ucsData = homeData.uniformCostSearch();
				homeData.displayWeightedData(ucsData);
				System.out.println(ucsData);
				break;
			case "A*":
				ArrayList<String> aStarData = homeData.AStarSearch();
				homeData.displayWeightedData(aStarData);
				System.out.println(aStarData);
				break;
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param data
	 * @throws UnsupportedEncodingException
	 */
	private void displayWeightedData(ArrayList<String> data) throws UnsupportedEncodingException
	{
		try
		{
			PrintWriter writer = new PrintWriter("output.txt");
			Integer startPathCost = 0;
			Integer totalPathCost = 0;
			for (int i = 0; i < data.size(); i++)
			{
				if (data.get(i).equals(startState))
				{
					totalPathCost = startPathCost;
				} else
				{
					HashMap<String, Integer> nodeVal = liveTrafficLines.get(data.get(i - 1));
					for (Map.Entry<String, Integer> entryVal : nodeVal.entrySet())
					{
						if (entryVal.getKey().equals(data.get(i)))
						{
							totalPathCost += entryVal.getValue();
						}
					}
				}
				writer.println(data.get(i) + " " + totalPathCost);
			}
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param inputData
	 * @param homeData
	 * @param indexOfNoOfSundayTrafficLines
	 */
	private static void extractSundayTrafficLine(List<String> inputData, homework homeData,
			int indexOfNoOfSundayTrafficLines)
	{
		homeData.sundayTrafficLines = new HashMap<String, Integer>();
		for (int i = indexOfNoOfSundayTrafficLines; i < inputData.size(); i++)
		{
			String s[] = inputData.get(i).split(" ");
			if (s.length == 2)
			{
				homeData.sundayTrafficLines.put(s[0], Integer.parseInt(s[1]));
			}
		}
	}

	/**
	 * @param inputData
	 * @param homeData
	 * @param indexOfNoOfLiveTrafficLines
	 */
	private static void extractLiveTrafficData(List<String> inputData, homework homeData,
			int indexOfNoOfLiveTrafficLines)
	{

		homeData.liveTrafficLines = new HashMap<String, LinkedHashMap<String, Integer>>();
		for (int i = indexOfNoOfLiveTrafficLines + 1; i <= indexOfNoOfLiveTrafficLines
				+ homeData.noOfLiveTrafficLines; i++)
		{
			String s[] = inputData.get(i).split(" ");
			if (s.length == 3)
			{
				LinkedHashMap<String, Integer> graphSubData = null;
				if (homeData.liveTrafficLines != null && homeData.liveTrafficLines.containsKey(s[0]))
				{
					graphSubData = homeData.liveTrafficLines.get(s[0]);
					graphSubData.put(s[1], Integer.parseInt(s[2].trim()));
				} 
				else
				{
					graphSubData = new LinkedHashMap<String, Integer>();
					graphSubData.put(s[1], Integer.parseInt(s[2].trim()));
					homeData.liveTrafficLines.put(s[0], graphSubData);
				}
			}
		}
	}
}