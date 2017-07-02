package controller;

public class Room {
	public boolean isHunter = false;
	public boolean isWumpus = false;
	public boolean isPit = false;
	public boolean isBlood = false;
	public boolean isSlime = false;
	public boolean isEmpty = false;
	public boolean isGoop = false;
	private boolean visited = false;
	public int x;
	public int y;
	
	public Room(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void visit(){
		visited = true;
	}
	
	public String toString(){
		if(isHunter)
			return "O";
		if(isBlood && visited)
			return "B";
		if(isSlime && visited)
			return "S";
		if(isGoop && visited)
			return "G";
		if(visited)
			return " ";
		return "X";
	}

}
