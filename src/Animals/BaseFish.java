package Animals;

public class BaseFish
{
	private int x, y, age, oldX, oldY;
	
	public AnimalType type;
	
	public void addX(int d_x)
	{ 
		saveOldCoordinates();
		x += d_x;
	}
	
	public void addY(int d_y)
	{
		saveOldCoordinates();
		y += d_y; 
	}
	
	private void saveOldCoordinates()
	{
		oldY = y;
		oldX = x;
//		System.out.println("Y " + oldX + ", " + oldY + " : " + x + ", " + (y+d_y));
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public int getOldX()
	{
		return oldX;
	}
	
	public int getOldY()
	{
		return oldY;
	}

	public void setX(int x) 
	{
		this.oldX = x;
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.oldY = y;
		this.y = y;
	}
	
	public void setAge(int age) 
	{
		this.age = age;	
	}
}
