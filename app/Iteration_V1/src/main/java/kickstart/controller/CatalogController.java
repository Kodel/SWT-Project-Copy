package kickstart.controller;


import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.model.catalog_item.Ingredient;
import kickstart.model.catalog_item.Item;
import kickstart.model.catalog_item.ItemType;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.store.ItemCatalog;




@Controller
public class CatalogController {
	
	private final ItemCatalog itemCatalog;
	
	@Autowired
	public CatalogController(ItemCatalog itemCatalog)
	{
		this.itemCatalog = itemCatalog;
	}
	
	@ModelAttribute("cart")			//nötig um auf catalog.html anzuzeigen?
	public Cart initializeCart() {
		return new Cart();
	}
	
	@RequestMapping("/catalog")
	public String showCatalog(Model model)
	{
		model.addAttribute("items",itemCatalog.findAll());
		return "catalog";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("pid") ProductIdentifier id) {
		itemCatalog.delete(id);
		// was wenn noch im cart?
		return "redirect:catalog";

	}
	
	@RequestMapping("/editItem")
	public String editItem(@RequestParam("pid") ProductIdentifier id) {
		
		return "redirect:catalog";

	}
	
	@RequestMapping("addItem")
	public String addItem()
	{
		return "addItem";
	}
	
	@RequestMapping("/createItem")
	public String createItem(@RequestParam("itemname") String name, 
							 @RequestParam("itemprice") Number price,
							 @RequestParam("itemtype") String type)
	{
		Item neu;
	
		if(type.equals("PIZZA"))
		{
			neu = new Pizza(name,Money.of(price, EURO));
			
		}
		else if(type.equals("INGREDIENT"))
		{
			neu = new Ingredient(name,Money.of(price, EURO));
		}
		else
		{
			ItemType t = ItemType.SALAD;
			if(type.equals("DRINK")) t = ItemType.DRINK;
			if(type.equals("FREEDRINK")) t = ItemType.FREEDRINK;
			if(type.equals("SALAD")) t = ItemType.SALAD;
			
			neu = new Item(name,Money.of(price, EURO),t);
		}
		
		itemCatalog.save(neu);
		
		return "redirect:catalog";
	}

}
