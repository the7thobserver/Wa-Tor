package Animals;

public class Fish extends BaseFish
{
	public Fish(int x, int y)
	{
		setX(x);
		setY(y);
		setAge(0);
		type = AnimalType.FISH;
	}
	
	public Fish() 
	{
		setAge(0);
		type = AnimalType.FISH;
	}


}
