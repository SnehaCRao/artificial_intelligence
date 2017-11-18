import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class homeworkTest
{
	private static int VAL_FILE_NO;

	public static void main(String[] args)
	{
		for (int i = 1; i <= 23; i++)
		{
			if(i == 21){
				continue;
			}
			VAL_FILE_NO = i;
			System.out.println("val_file_no: " + VAL_FILE_NO);
			try
			{
				fileCopy();
				homework.clear();
				homework.main(null);
				compareTwoTextFiles();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void fileCopy() throws IOException
	{
		File dir = new File(".");
		String source = dir.getCanonicalPath() + File.separator + "input"+VAL_FILE_NO + ".txt";
		String dest = dir.getCanonicalPath() + File.separator + "input.txt";
		File fin = new File(source);
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		FileWriter fstream = new FileWriter(dest, false);
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

	private static void compareTwoTextFiles() throws IOException
	{
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		String sCurrentLine;
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		String dest = "output"+VAL_FILE_NO + ".txt";
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
		System.out.println(list1);
		System.out.println(list2);
		List<String> tmpList = new ArrayList<String>(list1);
		tmpList.removeAll(list2);
		System.out.println("content from out which is not there in .out file");
		for (int i = 0; i < tmpList.size(); i++)
		{
			System.out.println(tmpList.get(i)); // content from out which is not
												// there in output.txt
		}
		System.out.println("content from out which is not there in output.txt");
		tmpList = list2;
		tmpList.removeAll(list1);
		for (int i = 0; i < tmpList.size(); i++)
		{
			System.out.println(tmpList.get(i)); // content from out which is not
												// there in output.txt
		}
		br1.close();
		br2.close();
	}

}
