package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.ItemRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.Item;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.EntityNotFoundByIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ItemServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ItemRepository itemRepository;


    public Item persist(Item item) {
        return itemRepository.save(item);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundByIdException(itemId, Item.class.getSimpleName()));
    }

    public Item create(Product product, Cart cart) throws EntityNotFoundByIdException {
        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(1);
        cart.addItem(item);
        Item savedItem = itemRepository.save(item);
        LOGGER.info("Item(id={}) created (productId={})", savedItem.getId(), product.getId());
        return savedItem;
    }

    public void increment(Long id) throws EntityNotFoundByIdException {
        Item item = this.findById(id);
        item.setQuantity(1 + item.getQuantity());
        itemRepository.save(item);
        LOGGER.info("Item(id={}) incremented (new quantity = {})", item.getId(), item.getQuantity());
    }


}
