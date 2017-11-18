import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class homework1
{
	String algorithmName;
    String startState;
	String goalState;
	int noOfLiveTrafficLines;
	HashMap<String,Node> liveTrafficLines;
	int noOfSundayLiveTrafficLines;
	HashMap<String,Node> SundayTrafficLines;
	public static class Node
	{
		String stateName;
		int pathCost;
		ArrayList<Node> childNodes;
	}
	public static void main(String args[])
	{
		try 
		{
			FileReader readInputData = new FileReader("input.txt");
			BufferedReader bufferedReader = new BufferedReader(readInputData);
			List<String> inputData=new ArrayList<>();
			for (String lineData = bufferedReader.readLine(); lineData != null; lineData = bufferedReader.readLine())
			{
				inputData.add(lineData);
				System.out.println(lineData);
				System.out.println("inputData "+inputData);
			}
			homework homeData = new homework();
			/*homeData.algorithmName = inputData.get(0);
			System.out.println(homeData.algorithmName);
			homeData.startState = inputData.get(1);
			System.out.println(homeData.startState);
			homeData.goalState = inputData.get(2);
			System.out.println(homeData.goalState);
			homeData.noOfLiveTrafficLines = inputData.get(3);
			System.out.println(homeData.noOfLiveTrafficLines);*/
			for (int i=0; i<inputData.size(); i++)
			{
				if(!inputData.get(i).contains(" "))
				{
					switch(i)
					{
					     case 0 : homeData.algorithmName = inputData.get(i);
							      System.out.println(homeData.algorithmName);
							      break;
					     case 1: homeData.startState = inputData.get(i);
							     System.out.println(homeData.startState);
							     break;
					     case 2 : homeData.goalState = inputData.get(i);
							      System.out.println(homeData.goalState);
							      break;
					     case 3 : homeData.noOfLiveTrafficLines = Integer.parseInt(inputData.get(i));
							      System.out.println(homeData.noOfLiveTrafficLines);
							      break;
					     default : homeData.noOfSundayLiveTrafficLines = Integer.parseInt(inputData.get(i));
					     		   System.out.println(homeData.noOfLiveTrafficLines);
					     		   break;
					}
					/*if(i==0)
					{
						homeData.algorithmName = inputData.get(i);
						System.out.println(homeData.algorithmName);
					}
					else if(i==1)
					{
						homeData.startState = inputData.get(i);
						System.out.println(homeData.startState);
					}
					else if(i==2)
					{
						homeData.goalState = inputData.get(i);
						System.out.println(homeData.goalState);
					}	
					else if(i==3)
					{
						homeData.noOfLiveTrafficLines = Integer.parseInt(inputData.get(i));
						System.out.println(homeData.noOfLiveTrafficLines);
					}*/
				}
				else
				{
					String s[]=inputData.get(i).split(" ");
					if(s.length==3)
					{
						HashMap<String, HashMap<String, Integer>> graphData = new HashMap<String, HashMap<String,Integer>>();
						
					}
				}
			
			}
			//String stringData[][]=new String[inputData.size()][0];
			/*for (int i=0; i<inputData.size(); i++)
			{ 
				String s[]=inputData.get(i).split(" ");
				stringData[i]=s;
				//algorithmName = inputData.get(i);
				//System.out.println(Arrays.deepToString(stringData));
			}*/
			readInputData.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
	
