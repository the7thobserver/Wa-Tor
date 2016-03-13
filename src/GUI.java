import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import Animals.AnimalType;
import Animals.Fish;
import Animals.Shark;

public class Gui 
{
	private JFrame f;
	private JPanel panel;
	private int x, y;
	private JLabel[][] ocean;
	
	public Gui()
	{
		
	}
	
	public void createGrid(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		ocean = new JLabel[x][y];
		
		f = new JFrame("Wa-Tor");
		panel = new JPanel(new GridLayout(x, y, 0, 0));
		JLabel l;
		
		for (int i = 0; i < x; i++)
			for(int j = 0; j < y; j++)
			{
				l = new JLabel("", JLabel.CENTER);
				// JLabel l = new JLabel(new ImageIcon("image_file.png"),
				// JLabel.CENTER);
				l.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				l.setFont(l.getFont().deriveFont(20f));
				panel.add(l);
				ocean[i][j] = l;
			}

		f.setContentPane(panel);
		f.setSize(1080, 720);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public boolean placeFish(AnimalType type, int n_x, int n_y) 
	{
		if(ocean[n_x][n_y].getText().equals(""))
		{
			ocean[n_x][n_y].setText(type.toString());
			return true;
		}
		
		return false;
	}

	public void update(Fish fish) 
	{	
		System.out.println("Redrawing:  " + fish.getOldX() + ", " + fish.getOldY() + " -> " +  fish.getX() + " ," + fish.getY());
		
		ocean[fish.getOldX()][fish.getOldY()].setText("");
		ocean[fish.getX()][fish.getY()].setText(fish.type.toString());
	}
	
	public boolean isValidLocation(int x, int y)
	{
		return ocean[x][y].getText().equals("");
	}

	public boolean isSquareEmpty(int x, int y) 
	{
		return !ocean[x][y].getText().equals("");
	}

	public void remove(Fish fish) 
	{
		ocean[fish.getX()][fish.getY()].setText("");
	}

}
