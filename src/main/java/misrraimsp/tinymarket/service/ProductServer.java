package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.ProductRepository;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.converter.StringToCategoryConverter;
import misrraimsp.tinymarket.util.converter.StringToPriceIntervalConverter;
import misrraimsp.tinymarket.util.enums.Category;
import misrraimsp.tinymarket.util.enums.PriceInterval;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServer {

    private final ProductRepository productRepository;
    private final StringToCategoryConverter stringToCategory;
    private final StringToPriceIntervalConverter stringToPriceInterval;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findSearchResults(String text, String categoryStr, String priceIntervalStr) {

        if (text.isBlank() && categoryStr.isBlank() && priceIntervalStr.isBlank()) {
            return this.findAll();
        } else {
            Set<Long> idsByText  = (!text.isBlank()) ? this.getIdsByQueryText(text) : null;

            Set<Long> idsByCategory;
            if (!categoryStr.isBlank()) {
                Category category = stringToCategory.convert(categoryStr);
                idsByCategory =  productRepository.findIdByCategory(category);
            } else {
                idsByCategory = null;
            }

            Set<Long> idsByPrice;
            if (!priceIntervalStr.isBlank()) {
                PriceInterval priceInterval = stringToPriceInterval.convert(priceIntervalStr);
                idsByPrice = productRepository.findIdByPrice(priceInterval.getLowLimit(), priceInterval.getHighLimit());
            } else {
                idsByPrice = null;
            }

            Set<Long> resultIds = intersect(idsByText, idsByCategory, idsByPrice);
            if (resultIds.size() == 0) resultIds.add(0L);
            return productRepository.findByIds(resultIds);
        }
    }

    private Set<Long> getIdsByQueryText(String q) {
        Set<Long> idsByQ = new HashSet<>();
        String[] qs = q.split("\\s+");
        for (String query : qs){
            if (!query.isBlank()) {
                idsByQ.addAll(productRepository.findIdByTitleLike("%" + query + "%"));
                idsByQ.addAll(productRepository.findIdByDescriptionLike("%" + query + "%"));
            }
        }
        return idsByQ;
    }

    private Set<Long> intersect(Set<Long> ... sets) {
        int i = 0;
        int size = sets.length;
        while (sets[i] == null && i < size){
            i++;
        }
        if (i == size){
            return new HashSet<>();
        }
        Set<Long> result = new HashSet<>(sets[i]);
        for (int j = i + 1; j < size; j++) {
            Set<Long> s = sets[j];
            if (s != null) {
                result.retainAll(s);
            }
        }
        return result;
    }
}
