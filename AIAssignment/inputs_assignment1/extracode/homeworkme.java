import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class homeworkme 
{
//	String algorithmName;
//    String startState;
//	String goalState;
//	int noOfLiveTrafficLines;
//	HashMap<String, HashMap<String, Integer>> liveTrafficLines; 
//	int noOfSundayLiveTrafficLines;
//	HashMap<String, HashMap<String, Integer>> sundayTrafficLines;
//	public Set getChildren(String currentData)
//	{
//		homeData.
//    }
//	private void breadthFirstSearch()
//	{
//		if(startState.equals(goalState))
//		{
//            System.out.println("Goal Node Found!");
//            System.out.println(startState);
//		}
//        Queue<String> frontierQueueBFS = new LinkedList<>();
//        HashSet<String> exploredBFS = new HashSet<>();
//        frontierQueueBFS.add(startState);
//        while(!frontierQueueBFS.isEmpty())
//        {
//            String current = frontierQueueBFS.poll();
//            exploredBFS.add(current);
//            if(current.equals(goalState)) 
//            {
//                System.out.println(exploredBFS);
//                //return true;
//            }
//            else
//            {
//                if(getChildren(current).isEmpty())
//                    return false;
//                else
//                    queue.addAll(current.getChildren());
//            }
//            explored.add(current);
//        }
//
//        return false;*/
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
//				homeData.algorithmName = inputData.get(0);
//				homeData.startState = inputData.get(1);
//				homeData.goalState = inputData.get(2);
//				homeData.noOfLiveTrafficLines = Integer.parseInt(inputData.get(3));
//				homeData.noOfSundayLiveTrafficLines = Integer.parseInt((inputData.get(3))+3);
//				String s[]=inputData.get(i).split(" ");
//				if(s.length==3)
//				{
//					homeData.liveTrafficLines = new HashMap<String, HashMap<String,Integer>>(); 
//					HashMap<String, Integer> graphSubData = null;
//					if(homeData.liveTrafficLines!=null && homeData.liveTrafficLines.containsKey(s[0]))
//					{
//						graphSubData = homeData.liveTrafficLines.get(s[0]);
//						if(graphSubData == null)
//						{
//							graphSubData = new HashMap<String, Integer>();
//						}
//						graphSubData.add(s[1], Integer.parseInt(s[2]));
//						//homeData.liveTrafficLines.add(s[0], graphSubData);
//						System.out.println(s[0]);
//					}
//					else
//					{
//						graphSubData = new HashMap<String, Integer>();
//						graphSubData.put(s[1], Integer.parseInt(s[2]));
//						homeData.liveTrafficLines.put(s[0], graphSubData);
//					}
//					for(HashMap<String,Integer> edges : homeData.liveTrafficLines.values())
//					{
//						for (int d : edges.values())
//						{
//							System.out.println("d "+d);
//						}
//					}
//					homeData.liveTrafficLines.forEach((key, value) -> System.out.println(key + " : " + value));
//				}
//				if(s.length==2)
//				{
//					homeData.sundayTrafficLines = new HashMap<String, HashMap<String,Integer>>();
//					HashMap<String, Integer> graphSundaySubData = null;
//					if(homeData.sundayTrafficLines!=null && homeData.sundayTrafficLines.containsKey(s[0]))
//					{
//						graphSundaySubData.put(homeData.goalState, Integer.parseInt(s[1]));
//						homeData.sundayTrafficLines.put(s[0], graphSundaySubData);
//					}
//					else
//					{
//						graphSundaySubData = new HashMap<String, Integer>();
//						graphSundaySubData.put(homeData.goalState, Integer.parseInt(s[1]));
//						homeData.sundayTrafficLines.put(s[0], graphSundaySubData);
//					}
//					homeData.sundayTrafficLines.forEach((key, value) -> System.out.println(key + " : " + value));
//				}
//				
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
//	
	
}
	
