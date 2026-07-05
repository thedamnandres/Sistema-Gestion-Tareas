package gestionInventario;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

class InventoryAppTest {

    @Test
    void testMainMethod() {
        // Ejecuta el flujo principal de la aplicación, imprimiendo por consola
        InventoryApp.main(new String[]{});
    }

    @Test
    void testPrivateConstructor() throws Exception {
        // Usamos reflexión para instanciar el constructor privado y lograr 100% de cobertura
        Constructor<InventoryApp> constructor = InventoryApp.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InventoryApp instance = constructor.newInstance();
        assertNotNull(instance);
    }
}
