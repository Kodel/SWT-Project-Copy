package kickstart.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Pizza extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4746830994439574139L;
	@OneToMany private List<Ingredient> ingredients; 
	
	@SuppressWarnings("unused")
	private Pizza(){}
	
	public Pizza(String name, javax.money.MonetaryAmount price, Ingredient startitem)
	{
		super(name,price.add(startitem.getPrice()));
		this.ingredients = new LinkedList<Ingredient>();
		ingredients.add(startitem);
	}
	
	public boolean addIngredient(Ingredient i)
	{
		if(ingredients.contains(i)) return false;
		
		ingredients.add(i);
		this.setPrice(getPrice().add(i.getPrice()));
		return true;
	}
	
	public Ingredient removeIngredient(Ingredient i)
	{
		if(ingredients.contains(i))
		{
			ingredients.remove(i);
			this.setPrice(getPrice().subtract(i.getPrice()));
			return i;
		}
		
		return null;
	}
	
	public List<Ingredient> getIngredients()
	{
		return ingredients;
	}
	
	public String toString()  //TODO: nicerer String
	{
		String result = "Pizza";
		
		if(ingredients.size() != 0) result += " besteht aus "; 
		for(Ingredient i : ingredients)
		{
			
			result += ", " + i.getName();
		}
		
		
		return result + ".";
	}
}
