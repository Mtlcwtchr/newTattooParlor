package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.*;
import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.*;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import by.bsuir.tattooparlor.util.exception.NoClientPresentedException;
import by.bsuir.tattooparlor.util.exception.NoDiscountPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private final IClientManager clientManager;
    private final IProductManager productManager;
    private final IOrderManager orderManager;
    private final ITattooMasterManager masterManager;
    private final ICalculationsManager calculationsManager;
    private final IDiscountManager discountManager;
    private final IAuthService userManager;

    @Autowired
    public OrderController(IClientManager clientManager, IProductManager productManager, IOrderManager orderManager, ITattooMasterManager masterManager, ICalculationsManager calculationsManager, IDiscountManager discountManager, IAuthService userManager) {
        this.clientManager = clientManager;
        this.productManager = productManager;
        this.orderManager = orderManager;
        this.masterManager = masterManager;
        this.calculationsManager = calculationsManager;
        this.discountManager = discountManager;
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
                             @RequestParam(name = "promocode") String promocode,
                             HttpSession session,
                             Model model) {
        Order order = (Order) session.getAttribute("currentOrder");
        if (order == null) {
            return "redirect:/";
        }

        try {
            User currentUser = (User) session.getAttribute("currentUser");
            Client client = null;
            if (currentUser == null) {
                client = userManager.applyGuest(clientName, contactPhone).orElseThrow(NoClientPresentedException::new);
            } else {
                client = ((Client) session.getAttribute("currentClient"));
            }
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

            try {
                if (!promocode.isEmpty()) {
                    ClientDiscount discount = discountManager.findByPromoForClient(client, promocode);
                    order.setDiscount(discount);
                    int reducedCost = (int) (totalCost * ((100.0 - discount.getDiscount().getPercentage()) / 100.0));
                    model.addAttribute("reducedCost", reducedCost);
                    model.addAttribute("discount", discount);
                }
            } catch (NoDiscountPresentedException ex) {
                ex.printStackTrace();
            }

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
                    .collect(Collectors.toList());

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
        List<TattooMaster> masters = masterManager.findAll();
        model.addAttribute("masters", masters);
        model.addAttribute("orders", mapToDateOrdered(acceptedByMaster));
        return "schedule2";
    }

    @RequestMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam(name = "id") int orderId,
                                    @RequestParam(name = "masterId") int masterId,
                                    @RequestParam(name = "priceAccepted") int priceAccepted,
                                    HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        try {
            Order order = orderManager.findById(orderId);
            TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");
            if(masterId == 0) {
                order.setOrderStatus(OrderStatus.REJECTED);
            } else if(masterId == currentMaster.getId()) {
                order.setPrice(priceAccepted);
                order.setOrderStatus(OrderStatus.ACCEPTED);
            } else {
                TattooMaster master = masterManager.findById(masterId);
                order.setMaster(master);
            }

            orderManager.saveOrder(order);
        } catch (UtilException ex) {
            ex.printStackTrace();
        }
        return "redirect:/scheduleNew";
    }

    @RequestMapping("/updateOrder")
    public String updateOrder(@RequestParam(name = "orderId") int orderId,
                              @RequestParam(name = "datetime", required = false) String dateTime,
                              @RequestParam(name = "contactPhone", required = false) String contactPhone,
                              @RequestParam(name = "totalCost", required = false) Integer totalCost,
                              @RequestParam(name = "completedImage", required = false) MultipartFile multipartFile,
                              @RequestParam(name = "orderStatus") OrderStatus orderStatus,
                              HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        try {
            Order order = orderManager.findById(orderId);

            if (dateTime != null && !dateTime.isEmpty()) {
                order.setDateTime(DateUtils.dateTimeFromHtmlString(dateTime));
            }
            if (contactPhone != null && !contactPhone.isEmpty()) {
                order.setContactPhone(contactPhone);
            }
            if (totalCost != null && totalCost != 0) {
                order.setPrice(totalCost);
            }

            if (multipartFile != null) {
                String fileName = FileUtils.getNewPictureUri(
                        Integer.toHexString(multipartFile.getName().hashCode() + new Date().hashCode()),
                        multipartFile.getContentType().replace("/", "."));

                FileUtils.trySaveNewPictureByPath(multipartFile, fileName);
                Product product = order.getProduct();
                product.setCompletedImageUri(fileName);
            }

            order.setOrderStatus(orderStatus);

            orderManager.saveOrder(order);
        } catch (UtilException | IOException ex) {
            ex.printStackTrace();
        }
        return "redirect:/schedule";
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
