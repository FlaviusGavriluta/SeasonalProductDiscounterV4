package com.codecool.seasonalproductdiscounter.service.transactions.simulator;

import com.codecool.seasonalproductdiscounter.model.offers.Offer;
import com.codecool.seasonalproductdiscounter.model.products.Product;
import com.codecool.seasonalproductdiscounter.model.transactions.Transaction;
import com.codecool.seasonalproductdiscounter.model.transactions.TransactionsSimulatorSettings;
import com.codecool.seasonalproductdiscounter.model.users.User;
import com.codecool.seasonalproductdiscounter.service.authentication.AuthenticationService;
import com.codecool.seasonalproductdiscounter.service.discounts.DiscountService;
import com.codecool.seasonalproductdiscounter.service.logger.Logger;
import com.codecool.seasonalproductdiscounter.service.products.repository.ProductRepository;
import com.codecool.seasonalproductdiscounter.service.transactions.repository.TransactionRepository;
import com.codecool.seasonalproductdiscounter.service.users.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class TransactionsSimulator {
    private static final Random RANDOM = new Random();

    private Logger logger;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private AuthenticationService authenticationService;
    private DiscountService discounterService;
    private TransactionRepository transactionRepository;

    public TransactionsSimulator(Logger logger, UserRepository userRepository,
                                 ProductRepository productRepository, AuthenticationService authenticationService,
                                 DiscountService discounterService, TransactionRepository transactionRepository) {
        this.logger = logger;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
        this.discounterService = discounterService;
        this.transactionRepository = transactionRepository;
    }

    public void run(TransactionsSimulatorSettings settings) {
        int successfulTransactions = 0;
        int rounds = 0;

        logger.logInfo("Starting simulation");
        while (successfulTransactions <= settings.transactionsCount()) {
            logger.logInfo(String.format("Simulation round #%d, successful transactions: %d/%d",
                    rounds++, successfulTransactions, settings.transactionsCount()));

            // Get a random user
            User user = getRandomUser(settings.usersCount());
            logger.logInfo(String.format("User [%s] looking to buy a product", user.userName()));

            // Auth user
            if (!authUser(user)) {
                // If auth is not successful, register the user
            }

            // Get user from the repo to have an ID (ID is auto-generated by the database)
            user = getUserFromRepo(user.userName());

            // User selects product
            Product product = selectProduct(user);

            // Out of products to sell - terminate cycle
            if (product == null) {
                break;
            }

            // Get offer
            Offer offer = getOffer(product, settings.date());

            // Create transaction
            Transaction transaction = createTransaction(settings.date(), user, product, offer.price());

            // Save transaction & set product_sold to TRUE
            if (saveTransaction(transaction)) {
                setProductAsSold(product);
                successfulTransactions++;
            }
        }
    }

    private User getRandomUser(int usersCount) {
        return new User(0, String.format("user%d", RANDOM.nextInt(usersCount)), "pw");
    }

    private User getUserFromRepo(String username) {
        return null;
    }

    private boolean authUser(User user) {
        return false;
    }

    private boolean registerUser(User user) {
        return false;
    }

    private Product selectProduct(User user) {
        return null;
    }

    private Product getRandomProduct() {
        List<Product> allProducts = productRepository.getAvailableProducts();

        if (allProducts.isEmpty()) {
            return null;
        }

        return allProducts.get(RANDOM.nextInt(allProducts.size()));
    }

    private Offer getOffer(Product product, LocalDate date) {
        return null;
    }

    private Transaction createTransaction(LocalDate date, User user, Product product, double price) {
        return null;
    }

    private boolean saveTransaction(Transaction transaction) {
        return false;
    }

    private boolean setProductAsSold(Product product) {
        return false;
    }
}
