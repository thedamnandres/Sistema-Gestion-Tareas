package gestionInventario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void testProductConstructorAndGetters() {
        Product product = new Product("Teclado", 10, 25.50);

        assertEquals("Teclado", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(25.50, product.getPrice());
    }

    @Test
    void testSetQuantity() {
        Product product = new Product("Teclado", 10, 25.50);
        product.setQuantity(20);

        assertEquals(20, product.getQuantity());
    }

    @Test
    void testSetPrice() {
        Product product = new Product("Teclado", 10, 25.50);
        product.setPrice(29.99);

        assertEquals(29.99, product.getPrice());
    }
}
