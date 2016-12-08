package pizzaShop.model.catalog_item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;

/**
 * PiizaClass for represesenting a Pizza of the PizzaShop
 * @author Florentin Dörre
 */
@Entity
public class Pizza extends Item {
	
	
	private static final long serialVersionUID = 4746830994439574139L;
	@ElementCollection private List<String> ingredients; 
	@ElementCollection private List<String> orderQueue = new ArrayList<String>();
	
	
	private boolean isFinished;
	
	@SuppressWarnings("unused")
	
	private Pizza(){}
	
	/**
	 * @see Item
	 * @param name equally to the name at the item class 
	 * @param price equally to the name at the item class
	 * sets the ItemType to "PIZZA"
	 * creates an empty array list for ingredients
	 * at the beginning a pizza is 
	 */
	public Pizza(String name, javax.money.MonetaryAmount price)
	{
		super(name,price,ItemType.PIZZA);
		this.ingredients = new ArrayList<String>();
		//this.orderQueue = 
		this.setStatus(false);
	}
	
	/**
	 * 
	 * @param status <CODE>true</CODE> after pizza was baked
	 * 
	 */
	public void setStatus(boolean status){
		
		isFinished = status;
		
	}
	
	/**
	 * saves the name of the ingredient in the ingredients list
	 * @param i ingredient which will be put on the pizza 
	 * @return <CODE>false</CODE> if ingredient already on the pizza
	 * <CODE>true</CODE> if successfully put on the pizza
	 */
	public boolean addIngredient(Ingredient i) //change name ?
	{
		if(ingredients.contains(i.getName())) return false;
		
		ingredients.add(i.getName());
		Collections.sort(ingredients);
		this.setPrice(getPrice().add(i.getPrice()));
		return true;
	}
	
	/**
	 * 
	 * @param i ingredient which will be removed from the pizza
	 * @return <CODE>null</CODE> if the ingredient wasnt on the pizza
	 * otherwise returns the name of the removed ingredient
	 */
	public String removeIngredient(Ingredient i)
	{
		if(ingredients.contains(i.getName()))
		{
			ingredients.remove(i.getName());
			this.setPrice(getPrice().subtract(i.getPrice()));
			return i.getName();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return a List of Strings containing the names of the ingredients on the pizza
	 */
	public List<String> getIngredients()
	{
		return ingredients;
	}

	public boolean getStatus() {
		return isFinished;
	}
	
	/**
	 * to save the order(as a string) containing the pizza 
	 * @param o OrderIdentifier of a order which should contain the pizza
	 */
	public void addOrder(OrderIdentifier o){
		orderQueue.add(o.toString());
		
	}
	
	/**
	 * 
	 * @return returns the first order as a String
	 */
	public String getFirstOrder(){
		return orderQueue.get(0);
	}


	/**
	 * removes order from the list 
	 * @return returns the name of the first order in the list
	 * @throws Exception when there is no order in the list
	 */
	public String removeFirstOrder() throws Exception{
		if(orderQueue.isEmpty()) throw new NullPointerException("Die Pizza hat keine Orders zugewiesen");
		return orderQueue.remove(0);
	}
	
	/**
	 * @return returns the Pizza(ingriedientnames)
	 * @see pizzaShop.model.catalog_item.Item#toString()
	 */
	public String toString()  //TODO: nicerer String
	{
		String result = super.toString();
		java.util.Iterator<String> i =ingredients.iterator();
		
		if(i.hasNext()) 
		{	
			result += "(" + i.next();
			
			for(;i.hasNext();)
			{
				result += "," + i.next();
			}
		
			result += ")";
		}
		
		return result;
	}
}
