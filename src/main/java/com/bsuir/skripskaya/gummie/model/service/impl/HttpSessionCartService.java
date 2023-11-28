package com.bsuir.skripskaya.gummie.model.service.impl;

import com.bsuir.skripskaya.gummie.model.dao.GummieDao;
import com.bsuir.skripskaya.gummie.model.dao.StockDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcGummieDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcStockDao;
import com.bsuir.skripskaya.gummie.model.entities.cart.Cart;
import com.bsuir.skripskaya.gummie.model.entities.cart.CartItem;
import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.model.exceptions.OutOfStockException;
import com.bsuir.skripskaya.gummie.model.exceptions.ServiceException;
import com.bsuir.skripskaya.gummie.model.service.CartService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;
/**
 * @author skripskaya
 * @version 1.0
 */
public class HttpSessionCartService implements CartService {
    /**
     * Attribute of cart in session
     */
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    /**
     * Attribute of cart in request attribute
     */
    private static final String CART_ATTRIBUTE = "cart";
    /**
     * Instance of HttpSessionCartService
     */
    private static volatile HttpSessionCartService instance;
    /**
     * Instance of GummieDao
     */
    private GummieDao gummieDao;
    /**
     * Instance of StockDao
     */
    private StockDao stockDao;

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of HttpSessionCartServiece
     */

    public static HttpSessionCartService getInstance() {
        if (instance == null) {
            synchronized (HttpSessionCartService.class) {
                if (instance == null) {
                    instance = new HttpSessionCartService();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor of HttpSessionCartService
     */
    private HttpSessionCartService() {
        gummieDao = JdbcGummieDao.getInstance();
        stockDao = JdbcStockDao.getInstance();
    }

    /**
     * Get cart from session
     *
     * @param currentSession session with cart
     * @return cart from session
     */
    @Override
    public Cart getCart(HttpSession currentSession) {
        synchronized (currentSession) {
            Cart cart = (Cart) currentSession.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                currentSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            if (cart.getTotalCost() == null) {
                cart.setTotalCost(BigDecimal.ZERO);
            }
            return cart;
        }
    }

    /**
     * Add Gummie to cart
     *
     * @param cart           cart to adding
     * @param productId      productId of gummie to add
     * @param quantity       quantity of gummie to add
     * @param currentSession session with cart
     * @throws OutOfStockException throws when gummie outOfStock
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void add(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        Optional<CartItem> productMatch;
        synchronized (currentSession) {
            Gummie gummie;
            try {
                gummie = gummieDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (gummie != null) {
//                if (countingQuantityIncludingCart(cart, gummie) < quantity) {
//                    throw new OutOfStockException(gummie, quantity, countingQuantityIncludingCart(cart, gummie));
//                }
                if ((productMatch = getCartItemMatch(cart, gummie)).isPresent()) {
                    cart.getItems().
                            get(cart.getItems().indexOf(productMatch.get())).
                            setQuantity(productMatch.get().getQuantity() + quantity);
                } else {
                    cart.getItems().add(new CartItem(gummie, quantity));
                    currentSession.setAttribute(CART_ATTRIBUTE, cart);
                }
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Calculate quantity of gummie with cart
     *
     * @param cart  cart with phones to recalculate
     * @param gummie gummie to recalculate
     * @return available quantity of gummie minus quantity of gummie in cart
     */
    private int countingQuantityIncludingCart(Cart cart, Gummie gummie) throws ServiceException {
        int result = 0;
        try {
            result = stockDao.availableStock(gummie.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        Integer quantityInCart = cart.getItems().stream()
                .filter(currProduct -> currProduct.getGummie().equals(gummie))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
        result -= quantityInCart;
        return result;
    }

    /**
     * Update quantity of gummie in cart
     *
     * @param cart           cart to update
     * @param productId      id of gummie to update
     * @param quantity       quantity of gummie to update
     * @param currentSession session with cart
     * @throws OutOfStockException throws when gummie quantity out of stock during updating
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void update(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        synchronized (currentSession) {
            Gummie gummie;
            try {
                gummie = gummieDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (gummie != null) {
                int availableStock = 0;
                try {
                    availableStock = stockDao.availableStock(gummie.getId());
                } catch (DaoException e) {
                    throw new ServiceException(e.getMessage());
                }
                if (quantity > availableStock) {
                    throw new OutOfStockException(gummie, quantity, availableStock);
                }
                getCartItemMatch(cart, gummie).ifPresent(cartItem -> cart.getItems().
                        get(cart.getItems().indexOf(cartItem)).
                        setQuantity(quantity));
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Delete item from cart
     *
     * @param cart           cart to delete
     * @param productId      id of gummie to delete
     * @param currentSession session with cart
     */
    @Override
    public void delete(Cart cart, Long productId, HttpSession currentSession) {
        synchronized (currentSession) {
            cart.getItems().removeIf(item -> productId.equals(item.getGummie().getId()));
            reCalculateCart(cart);
        }
    }

    /**
     * Recalculate cart
     *
     * @param cartToRecalculate cat to recalculate
     */
    @Override
    public void reCalculateCart(Cart cartToRecalculate) {
        BigDecimal totalCost = BigDecimal.ZERO;
        cartToRecalculate.setTotalItems(
                cartToRecalculate.getItems().stream().
                        map(CartItem::getQuantity).
                        mapToInt(q -> q).
                        sum()
        );
        for (CartItem item : cartToRecalculate.getItems()) {
            totalCost = totalCost.add(
                    item.getGummie().getPrice().
                            multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        cartToRecalculate.setTotalCost(totalCost);
    }

    /**
     * Find cart item in cart
     *
     * @param cart    cart in witch we find
     * @param product product to find
     * @return cartItem
     */
    private Optional<CartItem> getCartItemMatch(Cart cart, Gummie product) {
        return cart.getItems().stream().
                filter(currProduct -> currProduct.getGummie().getId().equals(product.getId())).
                findAny();
    }

    /**
     * Clear cart in request
     *
     * @param currentSession session with cart
     */
    @Override
    public void clear(HttpSession currentSession) {
        Cart cart = getCart(currentSession);
        cart.getItems().clear();
        reCalculateCart(cart);
    }

    /**
     * Remove item from cart
     *
     * @param currentSession session with cart
     * @param phoneId        id of gummie to remove
     */
    @Override
    public void remove(HttpSession currentSession, Long phoneId) {
        Cart cart = getCart(currentSession);
        cart.getItems().removeIf(item -> phoneId.equals(item.getGummie().getId()));
        reCalculateCart(cart);
    }
}
