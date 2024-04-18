package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CreditCardController {

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private StatusService statusService;
	@Autowired
	private UserService userService;
	private static Logger logger = LogManager.getLogger(CreditCardController.class);

	@GetMapping("/userCards")
	public String viewCreditCards(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> userCreditCards = loggedUser.getCreditCards();
			model.addAttribute("cards", userCreditCards);
			model.addAttribute("user", loggedUser);
			return "userCards";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		}
	}

	@GetMapping("/applyCreditCard")
	public String applyCreditCard(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			return "applyCreditCard";
		} else {
			return "applyCreditCard";
		}
	}

	@PostMapping("/applyCreditCard")
	public String applyCreditCard(Model model, HttpSession session, @RequestParam String monthlySalary,
			@RequestParam String cardType) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		if (monthlySalary.isBlank() || cardType.isBlank()) {
			logger.warn("There are empty fields, please fill up");
			model.addAttribute("error", true);
			return "applyCreditCard";
		} else {
			if (Double.parseDouble(monthlySalary) < 1000) {
				logger.warn("Your salary is too low. You are required to have a monthly salary above $1000");
				model.addAttribute("error2", true);
				return "applyCreditCard";
			} else {
				String creditCardNumber = creditCardService.generateCreditCardNumber();
				String pin = creditCardService.generatePinNumber();
				double cardLimit = Double.parseDouble(monthlySalary) * 3;
				// Default approved
				Status statusName = statusService.findByStatusName("Approved");
				CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, cardLimit, cardType, statusName, 0,
						loggedUser);
				creditCardService.persist(createCreditCard);
				logger.info("Credit card of number " + creditCardNumber + " created");
				loggedUser.setCreditCards(createCreditCard);
				userService.update(loggedUser);

				return "redirect:/userCards";
			}
		}
	}

	@GetMapping("/creditCard/paybills")
	public String goToPaybillsPage(Model model, HttpSession session) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");
		List<CreditCard> ccList = currentUser.getCreditCards();

		// add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("CcList", ccList);

		return "paybills";
	}

	@GetMapping("/admin/creditcards")
	public String creditcardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		List<CreditCard> ccList = creditCardService.findAllCreditCards();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		for (CreditCard cc : ccList) {
			List<Transaction> transaction = cc.getTransactions();
			transactionList.addAll(transaction);
		}
		model.addAttribute("transactions", transactionList);
		model.addAttribute("user", returnedUser);
		model.addAttribute("creditCards", ccList);
		return "admincreditcard";
	}
}
