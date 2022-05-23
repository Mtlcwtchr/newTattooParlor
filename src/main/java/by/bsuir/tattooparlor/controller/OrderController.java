package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.*;
import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.*;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import by.bsuir.tattooparlor.util.exception.NoClientPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class OrderController {

    private final IClientManager clientManager;
    private final IProductManager productManager;
    private final IOrderManager orderManager;
    private final ITattooMasterManager masterManager;
    private final ICalculationsManager calculationsManager;
    private final IAuthService userManager;

    @Autowired
    public OrderController(IClientManager clientManager, IProductManager productManager, IOrderManager orderManager, ITattooMasterManager masterManager, ICalculationsManager calculationsManager, IAuthService userManager) {
        this.clientManager = clientManager;
        this.productManager = productManager;
        this.orderManager = orderManager;
        this.masterManager = masterManager;
        this.calculationsManager = calculationsManager;
        this.userManager = userManager;
    }

    @RequestMapping("/postOrder")
    public String postOrder(HttpSession session) {
        Client currentClient = (Client) session.getAttribute("currentClient");
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if(currentOrder == null) {
            return "redirect:/";
        }
        Product orderProduct = currentOrder.getProduct();
        orderProduct.setGalleryType(GalleryType.NONE);
        Product savedProduct = productManager.save(orderProduct);
        currentOrder.setProduct(savedProduct);
        Order savedOrder = orderManager.saveOrder(currentOrder);
        if (currentClient.getOrders() == null) {
            currentClient.setOrders(new ArrayList<>());
        }
        currentClient.getOrders().add(savedOrder);
        clientManager.update(currentClient);
        return "redirect:/profile";
    }

    @RequestMapping("/placeOrder")
    public String placeOrder(@RequestParam(name = "masterId") int masterId,
                             @RequestParam(name = "imageSize") ImageSize imageSize,
                             @RequestParam(name = "colorsCount") int colorsCount,
                             @RequestParam(name = "dateTime") String dateTime,
                             @RequestParam(name = "contactPhone") String contactPhone,
                             @RequestParam(name = "clientName") String clientName,
                             HttpSession session,
                             Model model) {
        Order order = (Order) session.getAttribute("currentOrder");
        if (order == null) {
            return "redirect:/";
        }

        try {
            Client client = userManager.applyGuest(clientName, contactPhone).orElseThrow(NoClientPresentedException::new);
            TattooMaster master = null;
            if (masterId == 0) {
                master = masterManager.findPrior();
            } else {
                master = masterManager.findById(masterId);
            }
            order.setMaster(master);
            order.setDateTime(DateUtils.dateTimeFromHtmlString(dateTime));
            order.setContactPhone(contactPhone);
            order.setClient(client);
            Product product = order.getProduct();
            product.setColorsCount(colorsCount);
            int totalCost = calculationsManager.getTotalCost(product, master, imageSize);
            order.setPrice(totalCost);
            session.setAttribute("currentOrder", order);
            session.setAttribute("currentClient", client);
            model.addAttribute("currentOrder", order);
        } catch (UtilException ex) {
            ex.printStackTrace();
        }

        return "place-order";
    }

    @RequestMapping("/formOrder")
    public String proceedToFormOrder(@RequestParam(name = "itemId") int itemId,
                                     HttpSession session,
                                     Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser != null && currentUser.getRole() != UserRole.CLIENT) {
            return "redirect:/";
        }

        try {
            Product product = productManager.findById(itemId);
            Order order = new Order();
            order.setProduct(product);

            if(currentUser != null) {
                Client currentClient = (Client) session.getAttribute("currentClient");
                order.setClient(currentClient);
            }

            List<TattooMaster> masters = masterManager.findAll();

            session.setAttribute("currentOrder", order);

            model.addAttribute("currentOrder", order);
            model.addAttribute("masters", masters);
            model.addAttribute("imageSizes", ImageSize.TEN);

            return "form-order";
        } catch (UtilException ex) {
            ex.printStackTrace();
            return "redirect:/";
        }
    }

    @RequestMapping("/formOrderAfterCalculations")
    public String proceedToFormOrderFromCalculation(HttpSession session,
                                                    Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser != null && currentUser.getRole() != UserRole.CLIENT) {
            return "redirect:/";
        }
        Product product = (Product) session.getAttribute("product");
        Product savedProduct = productManager.save(product);
        System.out.println(savedProduct);

        return "redirect:/formOrder?itemId=" + savedProduct.getId();
    }

    @RequestMapping("/schedule")
    public String proceedToScheduleAccepted(HttpSession session,
                                    Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");

        List<Order> acceptedByMaster = orderManager.findAcceptedByMaster(currentMaster);
        if(!acceptedByMaster.isEmpty()) {
            acceptedByMaster.sort(Comparator.comparing(Order::getDateTime));
        }

        List<Order> completedByMaster = orderManager.findCompletedByMaster(currentMaster);
        List<Order> previouslyCompletedByMaster = Collections.emptyList();
        if (!completedByMaster.isEmpty()) {
            completedByMaster.sort(Comparator.comparing(Order::getDateTime));

            previouslyCompletedByMaster = completedByMaster
                    .stream()
                    .filter(order -> order.getDateTime().before(new Date()))
                    .toList();

            completedByMaster.removeAll(previouslyCompletedByMaster);
        }

        model.addAttribute("acceptedOrders", mapToDateOrdered(acceptedByMaster));
        model.addAttribute( "completedOrders", mapToDateOrdered(completedByMaster));
        model.addAttribute("previouslyCompletedOrders", mapToDateOrdered(previouslyCompletedByMaster));
        return "schedule";
    }

    @RequestMapping("/scheduleNew")
    public String proceedToScheduleUnaccepted(HttpSession session,
                                     Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");
        List<Order> acceptedByMaster = orderManager.findUnacceptedByMaster(currentMaster);
        if(!acceptedByMaster.isEmpty()) {
            acceptedByMaster.sort(Comparator.comparing(Order::getDateTime));
        }
        model.addAttribute("orders", mapToDateOrdered(acceptedByMaster));
        return "schedule2";
    }

    @RequestMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam(name = "id") int orderId,
                                    @RequestParam(name = "orderStatus") OrderStatus orderStatus,
                              HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() == UserRole.CLIENT) {
            return "redirect:/";
        }
        try {
            orderManager.updateOrderStatus(orderId, orderStatus);
        } catch (UtilException ex) {
            ex.printStackTrace();
        }
        return "redirect:/scheduleNew";
    }

    public Map<String, List<Order>> mapToDateOrdered(List<Order> orders) {
        if(orders == null || orders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, List<Order>> map = new HashMap<>();
        orders.sort(Comparator.comparing(Order::getDateTime));
        for (Order order : orders) {
            String date = order.getShortDateFormatted();
            if(!map.containsKey(date)) {
                map.put(date, new ArrayList<>());
            }
            map.get(date).add(order);
        }
        return map;
    }
}
