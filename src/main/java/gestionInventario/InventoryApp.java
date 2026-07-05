package gestionInventario;

/**
 * Clase principal (punto de entrada) que muestra cómo se usa
 * {@link InventoryService} e imprime el contenido del inventario en
 * consola. Toda la lógica de presentación vive aquí, separada de la
 * lógica de negocio que está en {@link InventoryService}.
 */
public final class InventoryApp {

    private InventoryApp() {
    }

    /**
     * Crea un inventario de ejemplo y muestra su contenido por consola.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        InventoryService inventoryService = new InventoryService();
        inventoryService.addProduct(new Product("Laptop", 5, 1000.0));

        for (Product product : inventoryService.getProducts()) {
            System.out.println("Product: " + product.getName()
                    + ", Quantity: " + product.getQuantity()
                    + ", Price: $" + product.getPrice());
        }
    }
}
