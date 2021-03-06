package farkle.game;

import java.util.Random;

public class Die 
{
	private int value = 0;
	
	private Random random = new Random();
	private boolean locked = false;
	private boolean selected = false; 
	
	public Die()
	{
		
	}
	
	public Die(int value, boolean locked, boolean selected)
	{
		this.value = value;
		this.locked = locked;
		this.selected = selected;
	}
	
	public void roll()
	{
		value = Math.abs((random.nextInt() % 6)) + 1;
	}
	
	public void reset()
	{
		value = 0;
		unlock();
		deselect();
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void lock()
	{
		locked = true;
	}
	
	public void unlock()
	{
		locked = false;
	}
	
	public boolean isLocked()
	{
		return locked;
	}
	
	public void select()
	{
		selected = true;
	}
	
	public void deselect()
	{
		selected = false;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public Die copy()
	{
		Die copy = new Die();
		copy.value = this.value;
		copy.selected = this.selected;
		copy.locked = this.locked;
		return copy;
				
	}
	
	public String toString()
	{
		return Integer.toString(getValue()) + " " + locked + " " + selected;
	}
	
	public static Die fromString(String str)
	{
		String[] params = str.split(" ");
		return new Die(Integer.parseInt(params[0]), Boolean.parseBoolean(params[1]), Boolean.parseBoolean(params[2]));
	}

}
