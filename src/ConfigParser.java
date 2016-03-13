import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ConfigParser 
{
	// To lazy to make getters
	public int fishNum, sharkNum, fishBreed, sharkBreed, sharkStarve, x, y;

	void checkConfig() 
	{
		if(x == 0 || y == 0)
		{
			System.out.println("Ocean cannot be 0 size");
			System.exit(-1);
		}
		else if(fishNum + sharkNum > x * y)
		{
			System.out.println("Too many fish in the sea! Overlap occured.");
			System.exit(-1);
		}
	}

	void readConfig() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("config"));
		
		String line = br.readLine();
		int i = 0;
		
	    while (line != null) 
	    {
	    	// Input file will always be in this order
	        switch(i)
	        {
	          	case 0:
	        		fishNum = parseInt(line);
	        		break;
	        	case 1:
	        		sharkNum = parseInt(line);
	        		break;
	        	case 2:
	        		fishBreed = parseInt(line);
	        		break;
	        	case 3:
	        		sharkBreed = parseInt(line);
	        		break;
	        	case 4:
	        		sharkStarve = parseInt(line);
	        		break;
	        	case 5:
	        		x = parseInt(line);
	        		break;
	        	case 6:
	        		y = parseInt(line);
	        		break;
	        }	
	        
	        i++;
	        line = br.readLine();
	    }
		    
		br.close();
	}

	int parseInt(String s)
	{
		s = s.split(" = ")[1].trim();
		return Integer.parseInt(s);
	}
}
