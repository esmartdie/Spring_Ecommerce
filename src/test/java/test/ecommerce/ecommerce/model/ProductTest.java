package test.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ProductTest {
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        product.setId(id);
        assertEquals(id, product.getId());
    }

    @Test
    public void testSetAndGetName() {
        String name = "Product ABC";
        product.setName(name);
        assertEquals(name, product.getName());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Description of Product ABC";
        product.setDescription(description);
        assertEquals(description, product.getDescription());
    }

    @Test
    public void testSetAndGetImage() {
        String image = "product_abc.jpg";
        product.setImage(image);
        assertEquals(image, product.getImage());
    }

    @Test
    public void testSetAndGetPrice() {
        double price = 100.0;
        product.setPrice(price);
        assertEquals(price, product.getPrice());
    }

    @Test
    public void testSetAndGetQuantity() {
        int quantity = 50;
        product.setQuantity(quantity);
        assertEquals(quantity, product.getQuantity());
    }

    @Test
    public void testSetAndGetUser() {
        User user = new User();
        product.setUser(user);
        assertEquals(user, product.getUser());
    }

    @Test
    public void testSetActive() {
        Boolean active = false;
        product.setActive(active);
        assertEquals(active, product.getActive());
    }

    @Test
    public void testSetAndGetInventory() {
        List<ProductInventory> inventory = new ArrayList<>();
        ProductInventory productInventory1 = new ProductInventory();
        ProductInventory productInventory2 = new ProductInventory();
        inventory.add(productInventory1);
        inventory.add(productInventory2);
        product.setInventory(inventory);
        assertEquals(inventory, product.getInventory());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String name = "Product ABC";
        String description = "Description of Product ABC";
        String image = "product_abc.jpg";
        double price = 100.0;
        int quantity = 50;
        Boolean active = true;
        User user = new User();

        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setImage(image);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setActive(active);
        product.setUser(user);

        String expectedString = "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", active=" + active +
                ", user=" + user +
                '}';

        assertEquals(expectedString, product.toString());
    }
}
