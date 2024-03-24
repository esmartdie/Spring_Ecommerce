package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.*;
import com.ecommerce.ecommerce.repository.IOrderStatusRepository;
import com.ecommerce.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String home(Model model, HttpSession session){

        LOG.info("User session: {}", session.getAttribute("userId"));
        model.addAttribute("products", productService.findAll());

        model.addAttribute("session", session.getAttribute("userId"));

        return "/user/home";
    }

    @GetMapping("/producthome/{id}")
    public String productHome(@PathVariable Integer id, Model model){
        LOG.info("Product Id sent as argument {}", id);

        Product product = new Product();
        Optional<Product> optionalProduct = productService.getProduct(id);
        product = optionalProduct.get();

        ProductInventory latestInventory = productInventoryService.findLastProduct(product);

        model.addAttribute("inventory", latestInventory);
        model.addAttribute("product", product);

        return "/user/product_home.html";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model, HttpSession session) {

        Object userIdObject = session.getAttribute("userId");
        if (userIdObject == null) {
            return "redirect:/user/login";
        }

        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isEmpty()) {
            return "redirect:/producthome/" + id;
        }

        Product product = optionalProduct.get();

        List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");
        if (details == null) {
            details = new ArrayList<>();
            session.setAttribute("cart", details);
        }

        Integer availableQuantity = productInventoryService.findLastProduct(product).getFinalQuantity();

        if (availableQuantity == null) {
            // Handle case where available quantity is not found
            return "redirect:/producthome/" + id;
        }

        if (quantity > availableQuantity) {
            model.addAttribute("error", "Not enough available units to complete the purchase");
            return "redirect:/producthome/" + id;
        }

        OrderDetail existingOrderDetail = details.stream()
                .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingOrderDetail != null) {
            existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + quantity);
            existingOrderDetail.setTotal(existingOrderDetail.getQuantity()*existingOrderDetail.getPrice());
        } else {
            // Add new product to the cart
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setName(product.getName());
            orderDetail.setTotal(product.getPrice() * quantity);
            orderDetail.setProduct(product);
            details.add(orderDetail);
        }

        Map<Integer, Integer> availableQuantitiesForCart = new HashMap<>();
        for (OrderDetail orderDetail : details) {
            Integer keyValueIdProduct = orderDetail.getProduct().getId();
            Integer productFinalQuantity = productInventoryService.findLastProduct(orderDetail.getProduct()).getFinalQuantity();
            availableQuantitiesForCart.put(keyValueIdProduct, productFinalQuantity);
        }

        double totalSum = details.stream().mapToDouble(OrderDetail::getTotal).sum();
        order.setTotal(totalSum);

        model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);
        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        return "user/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String removeProductCart(@PathVariable Integer id, Model model, HttpSession session) {

        List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");

        if (details != null) {

            details.removeIf(orderDetail -> orderDetail.getProduct().getId().equals(id));

            session.setAttribute("cart", details);

            double totalSum = details.stream().mapToDouble(OrderDetail::getTotal).sum();
            order.setTotal(totalSum);

            Map<Integer, Integer> availableQuantitiesForCart = new HashMap<>();
            for (OrderDetail orderDetail : details) {
                Integer keyValueIdProduct = orderDetail.getProduct().getId();
                Integer productFinalQuantity = productInventoryService.findLastProduct(orderDetail.getProduct()).getFinalQuantity();
                availableQuantitiesForCart.put(keyValueIdProduct, productFinalQuantity);
            }

            session.setAttribute("orderTotal", totalSum);

            model.addAttribute("cart", details);
            model.addAttribute("order", order);
            model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);
        }

        return "user/cart";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session){

        List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");

        Map<Integer, Integer> availableQuantitiesForCart = new HashMap<>();
        for (OrderDetail orderDetail : details) {
            Integer keyValueIdProduct = orderDetail.getProduct().getId();
            Integer productFinalQuantity = productInventoryService.findLastProduct(orderDetail.getProduct()).getFinalQuantity();
            availableQuantitiesForCart.put(keyValueIdProduct, productFinalQuantity);
        }

        model.addAttribute("cart", details);
        model.addAttribute("order", order);
        model.addAttribute("session", session.getAttribute("userId"));
        model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);

        return "/user/cart";
    }

    @PostMapping("/update/cart")
    public String updateCart(@RequestParam MultiValueMap<String, String> quantitiesMap,
                             @RequestParam(value = "productIds", required = false) List<Integer> productIds,
                             HttpSession session, Model model) {

        List<OrderDetail> cart = (List<OrderDetail>) session.getAttribute("cart");

        if (cart != null && productIds != null) {
            for (Integer productId : productIds) {
                String inputName = "quantities-" + productId;
                List<String> quantities = quantitiesMap.get(inputName);
                if (quantities != null && !quantities.isEmpty()) {
                    String quantityStr = quantities.get(0);
                    int dotIndex = quantityStr.indexOf('.');
                    String integerPart;
                    if (dotIndex != -1) {
                        integerPart = quantityStr.substring(0, dotIndex);
                    } else {
                        integerPart = quantityStr;
                    }
                    Integer quantity = Integer.parseInt(integerPart);
                    for (OrderDetail detail : cart) {
                        if (detail.getProduct().getId().equals(productId)) {
                            detail.setQuantity(quantity);
                            detail.setTotal(detail.getPrice() * quantity);
                            break;
                        }
                    }
                }
            }

            double totalSum = cart.stream().mapToDouble(detail -> detail.getPrice() * detail.getQuantity()).sum();
            order.setTotal(totalSum);
            session.setAttribute("totalSum", totalSum);
            model.addAttribute("order", order);

        }

        session.setAttribute("cart", cart);

        return "redirect:/getCart";
    }


    @PostMapping("/order")
    public String order(Model model, HttpSession session) {

        Object userIdAttribute = session.getAttribute("userId");

        if (userIdAttribute != null) {
            try {
                Integer userId = Integer.parseInt(userIdAttribute.toString());

                Optional<User> userOp = userService.findById(userId);

                if (userOp.isPresent()) {
                    User user = userOp.get();

                    // Aquí inicializa los detalles del pedido y el objeto de pedido
                    List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");
                    double totalSum = (double)session.getAttribute("totalSum");
                    Order order = new Order(); // Asegúrate de tener una instancia válida de Order

                    model.addAttribute("cart", details);
                    model.addAttribute("order", order);
                    model.addAttribute("user", user);
                    model.addAttribute("totalSum", totalSum);

                    return "user/ordersummary";
                } else {
                    // Manejar el caso donde el usuario no se encuentra
                    return "redirect:/user/login";
                }

            } catch (NumberFormatException e) {
                return "redirect:/user/login";
            }
        } else {
          return "redirect:/user/login";
       }
    }



    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, Model model){

        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.generateOrderIdentification());

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        order.setUser(user);

        OrderStatus orderStatus = orderStatusService.findById(9).get();
        order.setStatus(orderStatus);

        orderService.save(order);

        List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");

        for (OrderDetail od:details){
            od.setOrder(order);
            orderDetailService.save(od);
            createProductInventoryLog(od,order, creationDate);
        }

        String url= "redirect:/user/details/"+order.getId();


        order = new Order();
        details.clear();
        session.removeAttribute("cart");
        session.removeAttribute("sumTotal");

        return url;

    }

    private void createProductInventoryLog(OrderDetail od,Order order, Date logDate) {

        Product product = od.getProduct();

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getDate).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int finalExistence = latestInventory.getFinalQuantity();

            ProductInventory pI = new ProductInventory();
            pI.setDate(logDate);
            pI.setOperationName("Order created: "+order.getNumber());
            pI.setInitialQuantity(finalExistence);
            pI.setOperationQuantity((int) od.getQuantity());
            pI.setFinalQuantity(pI.getInitialQuantity()-pI.getOperationQuantity());
            pI.setProduct(product);
            pI.setDetails(od);

            productInventoryService.save(pI);

        }
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, Model model){
        LOG.info("Product name: {}", productName);

        List<Product> products = productService.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase())).collect(Collectors.toList());

        model.addAttribute("products", products);
        return"user/home";
    }

    @Autowired
    private IProductService productService;
    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    List<OrderDetail> details = new ArrayList<OrderDetail>();

    Order order = new Order();
    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IOrderStatusService orderStatusService;

    private ProductInventory pI = new ProductInventory();

    @Autowired
    private IProductInventoryService productInventoryService;

}
