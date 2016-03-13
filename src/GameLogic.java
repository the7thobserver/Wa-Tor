import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import Animals.AnimalType;
import Animals.BaseFish;
import Animals.Fish;
import Animals.Shark;


public class GameLogic 
{
	private ConfigParser parser;
	private Gui gui;
	private boolean running;
	private Random random;
	
	private int tick;
	private ArrayList<Fish> fish;
	private ArrayList<Shark> shark;
	
	public void init() 
	{
		parser = new ConfigParser();
		
		try {
			parser.readConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parser.checkConfig();
		
		gui = new Gui();
		gui.createGrid(parser.x, parser.y);
		
		fish = new ArrayList<Fish>();
		shark = new ArrayList<Shark>();
		
		createNewFish(parser.fishNum, AnimalType.FISH, null);
		createNewFish(parser.sharkNum, AnimalType.SHARK, null);
		
		running = true;
		run();
	}

	public void createNewFish(int fishNum, AnimalType type, Fish oldFish) 
	{
		random = new Random();
		BaseFish baseFish = null;
		
		for(int i = 0; i < fishNum; i++)
		{
			if(type.equals(AnimalType.FISH))
			{
				Fish newFish = new Fish();
				fish.add(newFish);
				baseFish = (BaseFish) newFish;
			}
			else if(type.equals(AnimalType.SHARK))
			{
				Shark newFish = new Shark();
				shark.add(newFish);
				baseFish = (BaseFish) newFish;
			}
			else
			{
				System.out.println("Invalid type of fish!");
			}
				
			if(oldFish == null)
			{
				System.out.println("Randomizing " + type + " " + i);
				randomizeFish(type, baseFish);
			}
			else
			{
				System.out.println("Spawning " + type + " " + i + " at " + oldFish.getOldX() + ", " + oldFish.getOldY());
				oldFish.setAge(0);
				generateFish(type, oldFish, baseFish);
			}
		}
	}
	
	private void generateFish(AnimalType type, Fish oldFish, BaseFish baseFish) 
	{
		baseFish.setX(oldFish.getOldX());
		baseFish.setY(oldFish.getOldY());
		
		gui.placeFish(type, baseFish.getX(), baseFish.getY());
	}

	private void randomizeFish(AnimalType type, BaseFish fish)
	{
		int n_x = random.nextInt(parser.x);
		int n_y = random.nextInt(parser.y);
			
		fish.setX(n_x);
		fish.setY(n_y);
		
		if(!gui.placeFish(type, n_x, n_y))
		{
			System.out.println(n_x + " " + n_y + " is taken, randomizing again...");
			randomizeFish(type, fish);
		}
		
		System.out.println("Spawning at " + n_x + " " + n_y);
	}
	
	private void run() 
	{
		tick = 0;
		
		while(running)
		{
			wait(1000);

			System.out.println("\nIteration " + tick);
			
			updateFish();
			updateSharks();
			
			tick++;
			
			if(tick == 9)
			{
				fishLocations();
				break;
			}			
		}
	}
	
	private void updateSharks() 
	{
		for(int i = 0; i < shark.size(); i++)
		{
			if(shark.get(i).getStarvation() == parser.sharkStarve)
			{
				gui.remove(shark.get(i));
				shark.remove(i);
				continue;
			}
				
			shark.get(i).incrementStarvation();
			
			updateSharkDirection(shark.get(i));
			
			if(!gui.isSquareEmpty(shark.get(i).getX(), shark.get(i).getY()))
			{
				System.out.println("SHARK HAS EATTEN " + shark.get(i).getX() + " " +  shark.get(i).getY());
				// Linear search ... blah
				removeFish(shark.get(i).getX(), shark.get(i).getY());
			}
						
			gui.update(shark.get(i));
		}
	}

	private void removeFish(int x, int y) 
	{
		for(int i = 0; i < fish.size(); i++)
		{
			if(fish.get(i).getX() == x && fish.get(i).getY() == y)
			{
				fish.remove(i);
			}
		}
	}

	private void updateSharkDirection(Fish fish) 
	{
		// Get all possible moves
		int[] validMoves = new int[4];
		validMoves[0] = 1;		// Right
		validMoves[1] = -1;		// Left
		validMoves[2] = 1;		// Up
		validMoves[3] = -1;		// Down
		
		// If invalid, move goes to 0
		if(!isValidX(fish.getX() + 1))
			validMoves[0] = 0;
		if(!isValidX(fish.getX() - 1))
			validMoves[1] = 0;
		if(!isValidY(fish.getY() + 1))
			validMoves[2] = 0;
		if(!isValidY(fish.getY() - 1))
			validMoves[3] = 0;
		
		int nextMove = random.nextInt(validMoves.length);
		
		while(validMoves[nextMove] == 0)
			nextMove = random.nextInt(validMoves.length);
		
		if(nextMove == 0)
			fish.addX(validMoves[0]);
		if(nextMove == 1)
			fish.addX(validMoves[1]);
		if(nextMove == 2)
			fish.addY(validMoves[2]);
		if(nextMove == 3)
			fish.addY(validMoves[3]);
	}

	private void updateFish() 
	{
		for(int i = 0; i < fish.size(); i++)
		{
			fish.get(i).setAge(fish.get(i).getAge() + 1);
			
			System.out.println("Fish Age " + (fish.get(i).getAge() - 1) + " -> " + fish.get(i).getAge());
			moveToValidCoordinate(fish.get(i));
			
			gui.update(fish.get(i));
			
			// If breeding age then multiply
			if(fish.get(i).getAge() == parser.fishBreed)
			{
				createNewFish(1, AnimalType.FISH, fish.get(i));
			}
		}
	}

	private void moveToValidCoordinate(BaseFish fish) 
	{
		// Get all possible moves
		int[] validMoves = new int[4];
		validMoves[0] = 1;		// Right
		validMoves[1] = -1;		// Left
		validMoves[2] = 1;		// Up
		validMoves[3] = -1;		// Down
		
		int invalidMoves = 0;
		
		// If invalid, move goes to 0
		if(!isValidX(fish.getX() + 1) || !gui.isSquareEmpty(fish.getX() + 1, fish.getY()))
		{
			validMoves[0] = 0;
			invalidMoves++;
		}
		if(!isValidX(fish.getX() - 1) || !gui.isSquareEmpty(fish.getX() - 1, fish.getY()))
		{
			validMoves[1] = 0;
			invalidMoves++;
		}
		if(!isValidY(fish.getY() + 1) || !gui.isSquareEmpty(fish.getX(), fish.getY() + 1))
		{
			validMoves[2] = 0;
			invalidMoves++;
		}
		if(!isValidY(fish.getY() - 1) || !gui.isSquareEmpty(fish.getX(), fish.getY() - 1))
		{
			validMoves[3] = 0;
			invalidMoves++;
		}		
		
		if(invalidMoves == validMoves.length)
			return;
		
		int nextMove = random.nextInt(validMoves.length);
				
		while(validMoves[nextMove] == 0)
			nextMove = random.nextInt(validMoves.length);
		
		if(nextMove == 0)
			fish.addX(validMoves[0]);
		else if(nextMove == 1)
			fish.addX(validMoves[1]);
		else if(nextMove == 2)
			fish.addY(validMoves[2]);
		else if(nextMove == 3)
			fish.addY(validMoves[3]);
	}

	private boolean isValidX(int i) 
	{
		return i >= 0 && i < parser.x;
	}

	private boolean isValidY(int i) 
	{
		return i >= 0 && i < parser.y;
	}
	
	private void wait(int i)
	{
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void fishLocations()
	{
		for(int i = 0; i < fish.size(); i++)
			System.out.println("FISH #" + i + " AGE:" + fish.get(i).getAge() + " | " + fish.get(i).getX() + " " + fish.get(i).getY());
		
		for(int i = 0; i < shark.size(); i++)
			System.out.println("SHARK #" + i + " AGE:" + shark.get(i).getAge() + " | " + shark.get(i).getX() + " " + shark.get(i).getY());
	}
}
