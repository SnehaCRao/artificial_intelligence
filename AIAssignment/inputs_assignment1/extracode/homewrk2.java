import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class homewrk2
{
//	String algorithmName;
//    String startState;
//	String goalState;
//	int noOfLiveTrafficLines;
//	HashMap<String,Node> liveTrafficLines;
//	int noOfSundayLiveTrafficLines;
//	HashMap<String,Node> SundayTrafficLines;
//	public ArrayList<String> getChildren()
//	{
//        ArrayList<String> childNodes = new ArrayList<>();
//        if(this.leftChild != null)
//        {
//            childNodes.add(leftChild);
//        }
//        if(this.rightChild != null) {
//            childNodes.add(rightChild);
//        }
//        return childNodes;
//    }
//	private void breadthFirstSearch()
//	{
//		if(startState.equals(goalState))
//		{
//            System.out.println("Goal Node Found!");
//            System.out.println(startState);
//		}
//        Queue<String> queueBFS = new LinkedList<>();
//        ArrayList<String> exploredBFS = new ArrayList<>();
//        queueBFS.add(startState);
//        exploredBFS.add(startState);
//        while(!queueBFS.isEmpty())
//        {
//            String current = queueBFS.remove();
//            if(current.equals(goalState)) {
//                System.out.println(exploredBFS);
//                return true;
//            }
//            else{
//                if(current.getChildren().isEmpty())
//                    return false;
//                else
//                    queue.addAll(current.getChildren());
//            }
//            explored.add(current);
//        }
//
//        return false;
//        
//            
//        
//		//Queue<> queue = new LinkedList<>();
//	}
//	private void depthFirstSearch()
//	{
//		
//	}
//	private void uniformCostSearch()
//	{
//		
//	}
//	private void bestFirstSearch()
//	{
//		
//	}
//	/*public void addEdge(String from, String to, int distance) 
//	{
//	}	 
//	public HashMap<String,Integer> getNeighbors(String from) 
//	{
//		
//	}*/
//	public static class Node
//	{
//		String stateName;
//		int pathCost;
//		ArrayList<Node> childNodes;
//	}
//	public static void main(String args[])
//	{
//		try 
//		{
//			FileReader readInputData = new FileReader("input.txt");
//			BufferedReader bufferedReader = new BufferedReader(readInputData);
//			List<String> inputData=new ArrayList<>();
//			for (String lineData = bufferedReader.readLine(); lineData != null; lineData = bufferedReader.readLine())
//			{
//				inputData.add(lineData);
//				System.out.println(lineData);
//				System.out.println("inputData "+inputData);
//			}
//			homework homeData = new homework();
//			for (int i=0; i<inputData.size(); i++)
//			{
//				if(!inputData.get(i).contains(" "))
//				{
//					switch(i)
//					{
//					     case 0 : homeData.algorithmName = inputData.get(i);
//							      System.out.println(homeData.algorithmName);
//							      break;
//					     case 1: homeData.startState = inputData.get(i);
//							     System.out.println(homeData.startState);
//							     break;
//					     case 2 : homeData.goalState = inputData.get(i);
//							      System.out.println(homeData.goalState);
//							      break;
//					     case 3 : homeData.noOfLiveTrafficLines = Integer.parseInt(inputData.get(i));
//							      System.out.println(homeData.noOfLiveTrafficLines);
//							      break;
//					     default : homeData.noOfSundayLiveTrafficLines = Integer.parseInt(inputData.get(i));
//					     		   System.out.println(homeData.noOfLiveTrafficLines);
//					     		   break;
//					}
//				}
//				else
//				{
//					String s[]=inputData.get(i).split(" ");
//					if(s.length==3)
//					{
//						HashMap<String, HashMap<String, Integer>> graphData = new HashMap<String, HashMap<String,Integer>>();
//						HashMap<String, Integer> graphSubData = new HashMap<String, Integer>();
//						if(graphData!=null && graphData.containsKey(s[0]))
//						{
//							graphSubData.put(s[1], Integer.parseInt(s[2]));
//							graphData.put(s[0], graphSubData);
//							System.out.println(s[0]);
//						}
//						else
//						{
//							graphSubData.put(s[1], Integer.parseInt(s[2]));
//							graphData.put(s[0], graphSubData);
//						}
//						for(HashMap<String,Integer> edges : graphData.values())
//						{
//							for (int d : edges.values())
//							{
//								System.out.println("d "+d);
//							}
//						}
//						graphData.forEach((key, value) -> System.out.println(key + " : " + value));
//					}
//					if(s.length==2)
//					{
//						HashMap<String, HashMap<String, Integer>> graphSundayData = new HashMap<String, HashMap<String,Integer>>();
//						HashMap<String, Integer> graphSundaySubData = new HashMap<String, Integer>();
//						if(graphSundayData!=null && graphSundayData.containsKey(s[0]))
//						{
//							graphSundaySubData.put(homeData.goalState, Integer.parseInt(s[1]));
//							graphSundayData.put(s[0], graphSundaySubData);
//						}
//						else
//						{
//							graphSundaySubData.put(homeData.goalState, Integer.parseInt(s[1]));
//							graphSundayData.put(s[0], graphSundaySubData);
//						}
//						graphSundayData.forEach((key, value) -> System.out.println(key + " : " + value));
//					}
//				}
//			}
//			readInputData.close();
//			switch(homeData.algorithmName)
//			{
//			case "BFS":homeData.breadthFirstSearch();
//			           break;
//			case "DFS":homeData.depthFirstSearch();
//			           break;
//			case "UCS":homeData.uniformCostSearch();
//			           break;
//			case "A*":homeData.bestFirstSearch();
//			          break;
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	
}
	
