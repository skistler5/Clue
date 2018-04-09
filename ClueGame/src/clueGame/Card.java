package clueGame;

public class Card {

	private String cardName;
	private CardType cardType;
	
	public boolean equals(Card c){
		if(c.getCardName().equals(cardName) && c.getCardType().equals(cardType)){
			return true;
		}
		return false;
	}
	
	//Getters and Setters
	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType(){
		return cardType;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
}
