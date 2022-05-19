package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.IOrderManager;
import by.bsuir.tattooparlor.util.ITattooMasterManager;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class OrderController {

    private final IOrderManager orderManager;
    private final ITattooMasterManager masterManager;

    @Autowired
    public OrderController(IOrderManager orderManager, ITattooMasterManager masterManager) {
        this.orderManager = orderManager;
        this.masterManager = masterManager;
    }

    @GetMapping("/schedule")
    public String proceedToScheduleAccepted(HttpSession session,
                                    Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");

        List<Order> acceptedByMaster = orderManager.findAcceptedByMaster(currentMaster);
        acceptedByMaster.sort(Comparator.comparing(Order::getDateTime));

        model.addAttribute("acceptedOrders", acceptedByMaster);
        List<Order> completedByMaster = orderManager.findCompletedByMaster(currentMaster);
        completedByMaster.sort(Comparator.comparing(Order::getDateTime));

        List<Order> previouslyCompletedByMaster = completedByMaster
                .stream()
                .filter(order -> order.getDateTime().before(new Date()))
                .toList();

        completedByMaster.removeAll(previouslyCompletedByMaster);
        model.addAttribute( "completedOrders", mapToDateOrdered(completedByMaster));
        model.addAttribute("previouslyCompletedOrders", mapToDateOrdered(previouslyCompletedByMaster));
        return "schedule";
    }

    @GetMapping("/scheduleNew")
    public String proceedToScheduleUnaccepted(HttpSession session,
                                     Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/";
        }
        TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");
        List<Order> acceptedByMaster = orderManager.findUnacceptedByMaster(currentMaster);
        acceptedByMaster.sort(Comparator.comparing(Order::getDateTime));
        model.addAttribute("orders", mapToDateOrdered(acceptedByMaster));
        return "schedule2";
    }

    @PostMapping("/updateOrderStatus")
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
        return "redirect:/schedule2";
    }

    public Map<String, List<Order>> mapToDateOrdered(List<Order> orders) {
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
