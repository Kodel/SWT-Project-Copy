package kickstart.model;

import javax.persistence.Entity;

@Entity
public class Ingredient extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -229325384666805584L;
	
	
	@SuppressWarnings("unused")
	private Ingredient(){}
	
	public Ingredient(String name, javax.money.MonetaryAmount price)
	{
		super(name,price);
	}
	
	public String toString()
	{
		return "Zutat";
	}
}
