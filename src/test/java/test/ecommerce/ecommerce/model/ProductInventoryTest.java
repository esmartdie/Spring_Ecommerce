package test.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class ProductInventoryTest {
    private ProductInventory productInventory;

    @BeforeEach
    public void setUp() {
        productInventory = new ProductInventory();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        productInventory.setId(id);
        assertEquals(id, productInventory.getId());
    }

    @Test
    public void testSetAndGetOperationName() {
        String operationName = "Restock";
        productInventory.setOperationName(operationName);
        assertEquals(operationName, productInventory.getOperationName());
    }

    @Test
    public void testSetAndGetDate() {
        Date date = new Date();
        productInventory.setDate(date);
        assertEquals(date, productInventory.getDate());
    }

    @Test
    public void testSetAndGetProduct() {
        Product product = new Product();
        productInventory.setProduct(product);
        assertEquals(product, productInventory.getProduct());
    }

    @Test
    public void testSetAndGetDetails() {
        OrderDetail details = new OrderDetail();
        productInventory.setDetails(details);
        assertEquals(details, productInventory.getDetails());
    }

    @Test
    public void testSetAndGetInitialQuantity() {
        Integer initialQuantity = 50;
        productInventory.setInitialQuantity(initialQuantity);
        assertEquals(initialQuantity, productInventory.getInitialQuantity());
    }

    @Test
    public void testSetAndGetOperationQuantity() {
        Integer operationQuantity = 20;
        productInventory.setOperationQuantity(operationQuantity);
        assertEquals(operationQuantity, productInventory.getOperationQuantity());
    }

    @Test
    public void testSetAndGetFinalQuantity() {
        Integer finalQuantity = 70;
        productInventory.setFinalQuantity(finalQuantity);
        assertEquals(finalQuantity, productInventory.getFinalQuantity());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String operationName = "Restock";
        Date date = new Date();
        Product product = new Product();
        OrderDetail details = new OrderDetail();
        Integer initialQuantity = 50;
        Integer operationQuantity = 20;
        Integer finalQuantity = 70;

        productInventory.setId(id);
        productInventory.setOperationName(operationName);
        productInventory.setDate(date);
        productInventory.setProduct(product);
        productInventory.setDetails(details);
        productInventory.setInitialQuantity(initialQuantity);
        productInventory.setOperationQuantity(operationQuantity);
        productInventory.setFinalQuantity(finalQuantity);

        String expectedString = "ProductInventory{" +
                "id=" + id +
                ", operationName='" + operationName + '\'' +
                ", date=" + date +
                ", product=" + product +
                ", details=" + details +
                ", initialQuantity=" + initialQuantity +
                ", operationQuantity=" + operationQuantity +
                ", finalQuantity=" + finalQuantity +
                '}';

        assertEquals(expectedString, productInventory.toString());
    }
}
