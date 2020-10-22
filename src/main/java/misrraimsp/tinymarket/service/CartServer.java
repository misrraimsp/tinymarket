package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.CartRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.Item;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.EntityNotFoundByIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CartRepository cartRepository;
    private final ItemServer itemServer;

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }

    public void addProduct(Product product, Cart cart) throws EntityNotFoundByIdException {
        List<Item> items = new ArrayList<>(cart.getItems());
        List<Long> matchingItemIds =
                items
                        .stream()
                        .filter(item -> item.getProduct().equals(product))
                        .map(Item::getId)
                        .collect(Collectors.toList())
                ;
        if (matchingItemIds.isEmpty()) {
            itemServer.create(product, cart);
        } else if (matchingItemIds.size() == 1) {
            itemServer.increment(matchingItemIds.get(0));
        } else {
            LOGGER.error("Trying to add a product(id={}) that is already bound to several items(ids={}) within the cart(id={})",
                    product.getId(), matchingItemIds.toString(), cart.getId());
            return;
        }
        cartRepository.save(cart);
        LOGGER.info("Product(id={}) added into cart(id={})", product.getId(), cart.getId());
    }

}
