package pizzaShop.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.CartHelper;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.PizzaOrder;

/**
 * Controller that contains the {@link Cart} abd creates {@link PizzaOrder}'s
 * 
 * @author joris
 *
 */
@Controller
@SessionAttributes("cart")
public class CartController {

	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final CartHelper cartHelper;
	private Optional<Customer> customer = Optional.empty();

	private ErrorClass error;

	@Autowired
	public CartController(ItemCatalog itemCatalog, TanManagement tanManagement, CustomerRepository customerRepository,
			CartHelper cartHelper) {
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.cartHelper = cartHelper;

		error = new ErrorClass(false);
	}

	/**
	 * initializes ModelAttribute cart as a {@link Cart}
	 * @return new instance of {@link Cart}
	 */
	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}

	/**
	 * sets up attributes for the cart.html
	 * @param model {@link Model} for the html view
	 * @param {@link Cart} as a ModelAttribute 
	 * @return cart view
	 */
	@RequestMapping("/cart")
	public String pizzaCart(Model model, @ModelAttribute Cart cart) {
		model.addAttribute("items", itemCatalog.findAll());

		ArrayList<Item> freeDrinks = (ArrayList<Item>) itemCatalog.findByType(ItemType.FREEDRINK);
		boolean freeDrink = false;
		Iterator<CartItem> ci = cart.iterator();
		while (ci.hasNext()) {
			if (((Item) ci.next().getProduct()).getType().equals(ItemType.FREEDRINK)) {
				freeDrink = true;
				break;
			}
		}
		customer = (customer.isPresent()) ? Optional.of(customerRepository.findOne(customer.get().getId()))
				: Optional.empty();

		
		model.addAttribute("error", error);
		model.addAttribute("customer", customer);
		model.addAttribute("freeDrinks", freeDrinks);
		model.addAttribute("freeDrink", freeDrink);
		return "cart";

	}

	/**
	 * adds a {@link Item} from the {@link ItemCatalog} to the {@link Cart}
	 * @param id {@link ProductIdentifier} of the {@link Item} for the {@link CartItem} 
	 * @param number amount of the {@link Item} to be added
	 * @param cart {@link Cart} as a ModelAttribute
	 * @return redirects to the itemCatalog page
	 */
	@RequestMapping(value = "/addCartItem", method = RequestMethod.POST)
	public String addItem(@RequestParam("pid") ProductIdentifier id, @RequestParam("number") int number,
			@ModelAttribute Cart cart) {

		if (itemCatalog.findOne(id).isPresent()) {
			cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(number));
		}
		return "redirect:catalog";

	}

	/**
	 * removes a {@link CartItem} from the {@link Cart}
	 * @param cartId id of the {@link CartItem} to be removed
	 * @param cart {@link Cart} as a ModelAttribute
	 * @return redirects to the cart page
	 */
	@RequestMapping(value = "/removeCartItem", method = RequestMethod.POST)
	public String removeItem(@RequestParam("ciid") String cartId, @ModelAttribute Cart cart) {
		cart.removeItem(cartId);
		cartHelper.updateFreeDrink(cart);
		return "redirect:cart";

	}

	/**
	 * setup for {@link CartHelper} changeQuantity()
	 * @param cartId id of the {@link CartItem} to be changed
	 * @param amount change in quantity
	 * @param quantity current quantity of the {@link CartItem}
	 * @param id of the {@link Item} contained in the {@link CartItem}
	 * @param cart {@link Cart} as a ModelAttribute 
	 * @return redirects to the cart page
	 */
	@RequestMapping(value = "/changeQuantity", method = RequestMethod.POST)
	public String changeQuantity(@RequestParam("ciid") String cartId, @RequestParam("amount") int amount,
			@RequestParam("quantity") int quantity, @RequestParam("pid") ProductIdentifier id,
			@ModelAttribute Cart cart) {
		error.setError(false);
		Item item = itemCatalog.findOne(id).orElse(null);
		if (quantity + amount == 0) {
			cart.removeItem(cartId);

		} else {

			try {
				cartHelper.changeQuantity(item, amount, cart);
			} catch (Exception e) {
				error.setError(true);
				error.setMessage(e.getMessage());
				cart.removeItem(cartId);

			}
		}
		return "redirect:cart";

	}

	/**
	 * adds an {@link Item} of the type FREEDRINK to the {@link Cart}
	 * @param id of the {@link Item}
	 * @param cart {@link Cart} as a ModelAttribute 
	 * @return redirects to the cart page
	 */
	@RequestMapping(value = "/addFreeDrink", method = RequestMethod.POST)
	public String addFreeDrink(@RequestParam("iid") ProductIdentifier id, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(1));
		return "redirect:cart";
	}

	/**
	 * setup for {@link CartHelper} createPizzaOrder()
	 * 
	 * @param model
	 *            for the html view
	 * @param cart
	 *            {@link Cart} for the order
	 * @param pickUpStr
	 *            "0,1" if pickUp radio button is checked, else "0"
	 * @param cutleryStr
	 *            "0,1" if cutlery radio button is checked, else "0"
	 * @param userAccount
	 *            currently logged in {@link UserAccount}
	 * @return redirects to the cart page
	 * @throws Exception 
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(Model model, @ModelAttribute Cart cart, @RequestParam("pickUp") String pickUpStr,
			@RequestParam("cutlery") String cutleryStr, @RequestParam("remark") String remark, @LoggedIn Optional<UserAccount> userAccount) {

		error.setError(false);

		boolean pickUp = pickUpStr.equals("0,1") ? true : false;
		boolean cutlery = cutleryStr.equals("0,1") ? true : false;
		
		try {
			cartHelper.createPizzaOrder(cutlery, pickUp, userAccount.orElse(null), cart, customer.orElse(null), remark);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}

		return "redirect:orders";
	}

	/**
	 * setup for {@link CartHelper} checkTan()
	 * 
	 * @param telephoneNumber
	 *            telephoneNumber to be checked
	 * @param tanValue
	 *            {@link String} containing value of {@link Tan} to be checked
	 * @return
	 */
	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {

		Tan tan = tanManagement.getTan(telephoneNumber);

		error.setError(false);

		if (tan.getTanNumber().equals(tanValue)) {
			customer = cartHelper.checkTan(tan);
		} else {
			error.setError(true);
			error.setMessage("Fehler bei der TAN-Überprüfung! Erneut eingeben!");
		}

		return "redirect:cart";

	}

	/**
	 * logs out currently checked-in customer
	 * 
	 * @return redirects to cart page
	 */
	@RequestMapping(value = "/logoutCustomer", method = RequestMethod.POST)
	public String logoutCustomer() {
		customer = Optional.empty();
		return "redirect:cart";
	}
}
