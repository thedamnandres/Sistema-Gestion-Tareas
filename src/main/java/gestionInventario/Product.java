package gestionInventario;

/**
 * Clase que representa un producto del inventario. Guarda el nombre,
 * la cantidad disponible en stock y el precio unitario del producto.
 */
public class Product {

    private final String name;
    private int quantity;
    private double price;

    /**
     * Constructor de la clase Product. Se usa para crear un producto
     * nuevo con sus datos iniciales.
     *
     * @param name     nombre del producto
     * @param quantity cantidad disponible en stock
     * @param price    precio unitario del producto
     */
    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Devuelve el nombre del producto.
     *
     * @return el nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve la cantidad disponible en stock.
     *
     * @return la cantidad disponible
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Actualiza la cantidad disponible en stock.
     *
     * @param quantity la nueva cantidad
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Devuelve el precio unitario del producto.
     *
     * @return el precio unitario
     */
    public double getPrice() {
        return price;
    }

    /**
     * Actualiza el precio unitario del producto.
     *
     * @param price el nuevo precio
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
