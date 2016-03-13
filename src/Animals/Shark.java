package Animals;

public class Shark extends Fish
{
	private int starvation;

	public Shark(int x, int y) 
	{
		super(x, y);
		type = AnimalType.SHARK;
		starvation = 0;
	}

	public Shark() 
	{
		super();
		type = AnimalType.SHARK;
		starvation = 0;
	}
	
	public int getStarvation()
	{
		return starvation;
	}
	
	public void incrementStarvation()
	{
		starvation++;
	}
}
