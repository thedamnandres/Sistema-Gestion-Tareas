package gestionInventario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Pruebas unitarias de la clase {@link InventoryService}. */
class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

    @Test
    void addProductStoresValidProduct() {
        Product product = new Product("Laptop", 5, 1000.0);

        inventoryService.addProduct(product);

        assertEquals(1, inventoryService.getProducts().size());
        assertEquals(product, inventoryService.getProducts().get(0));
    }

    @Test
    void addProductRejectsNegativeQuantity() {
        Product product = new Product("Laptop", -1, 1000.0);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(product));
    }

    @Test
    void addProductRejectsNegativePrice() {
        Product product = new Product("Laptop", 5, -1.0);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(product));
    }

    @Test
    void addProductRejectsEmptyName() {
        Product product = new Product("", 5, 1000.0);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(product));
    }

    @Test
    void addProductRejectsNullName() {
        Product product = new Product(null, 5, 1000.0);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(product));
    }

    @Test
    void addProductRejectsNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(null));
    }

    @Test
    void addProductRejectsDuplicateName() {
        inventoryService.addProduct(new Product("Laptop", 5, 1000.0));

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.addProduct(new Product("laptop", 2, 500.0)));
    }

    @Test
    void findByNameReturnsExistingProduct() {
        inventoryService.addProduct(new Product("Laptop", 5, 1000.0));

        Optional<Product> found = inventoryService.findByName("Laptop");

        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
    }

    @Test
    void findByNameReturnsEmptyWhenNotFound() {
        Optional<Product> found = inventoryService.findByName("Nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    void getProductsReturnsUnmodifiableList() {
        assertThrows(UnsupportedOperationException.class,
                () -> inventoryService.getProducts().add(new Product("Mouse", 1, 10.0)));
    }
}
