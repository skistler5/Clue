package clueGame;

public class Solution {
	public String person;
	public String room;
	public String weapon;
	public Solution(String person, String room, String weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	
	public boolean equals(Solution s){
		if(person != s.person){
			return false;
		}
		else if(room != s.room){
			return false;
		}
		else if(weapon != s.weapon){
			return false;
		}
		
		return true;
	}
}
