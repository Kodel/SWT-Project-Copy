package pizzaShop.model.store;

import java.time.LocalDateTime;
import java.util.*;

import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.model.catalog_item.Pizza;

public class BakerTask extends TimerTask {

	// simuliert den Backvorgang

	private BakerTimer myTimer;
	private Pizza myPizza;
	private Oven myOven;
	private LocalDateTime endDate;
	private final BusinessTime businessTime;


	public BakerTask(BakerTimer timer, Pizza pizza, Oven oven, BusinessTime businessTime) {

		myTimer = timer;
		myPizza = pizza;
		myOven = oven;
		endDate = myTimer.getEndDate();
		this.businessTime = businessTime;

	}

	@Override
	public void run() {

		if (businessTime.getTime().isEqual(endDate) || businessTime.getTime().isAfter(endDate)) {
			myTimer.cancel();
			myPizza.setStatus(true);
			myOven.notifyObservers(myPizza);
			myOven.clear();
		}

		int i = myTimer.getCounter();
		System.out.println(i);
		i--;
		myTimer.setCounter(i);

		if (myTimer.getCounter() == 0) {

			myTimer.cancel();
			myPizza.setStatus(true);
			myOven.notifyObservers(myPizza);
			myOven.clear();
		}

	}

}
