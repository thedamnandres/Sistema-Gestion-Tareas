package gestionInventario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio que administra la colección de productos ({@link Product})
 * del inventario. Se encarga de validar los datos antes de guardarlos y de
 * dar acceso de solo lectura a quien la use. No imprime nada por consola,
 * esa responsabilidad le corresponde a la clase de aplicación.
 */
public class InventoryService {

    private final List<Product> products = new ArrayList<>();

    /**
     * Agrega un producto al inventario, validando primero que sus datos
     * sean correctos.
     *
     * @param product el producto que se quiere agregar
     * @throws IllegalArgumentException si el producto es nulo, si el nombre
     *                                   es nulo o está vacío, si la cantidad
     *                                   es negativa, si el precio es negativo,
     *                                   o si ya existe un producto con ese
     *                                   mismo nombre
     */
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name must not be empty.");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity must not be negative.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must not be negative.");
        }
        if (findByName(product.getName()).isPresent()) {
            throw new IllegalArgumentException(
                    "A product named '" + product.getName() + "' already exists.");
        }
        products.add(product);
    }

    /**
     * Busca un producto por su nombre, sin importar mayúsculas o minúsculas.
     *
     * @param name el nombre que se quiere buscar
     * @return un {@link Optional} con el producto encontrado, o vacío si
     *         no existe ningún producto con ese nombre
     */
    public Optional<Product> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return products.stream()
                .filter(product -> name.equalsIgnoreCase(product.getName()))
                .findFirst();
    }

    /**
     * Devuelve una vista de solo lectura de los productos del inventario.
     *
     * @return la lista de productos, la cual no se puede modificar
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
