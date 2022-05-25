package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Discount;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.DiscountStatus;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import by.bsuir.tattooparlor.util.*;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class DiscountController {

    private final IDiscountManager discountManager;

    public DiscountController(IDiscountManager discountManager) {
        this.discountManager = discountManager;
    }

    @RequestMapping("/discounts")
    public String proceedUsersGallery(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null || currentUser.getRole() == UserRole.CLIENT) {
            return "redirect:/";
        }
        List<Discount> discounts = discountManager.findAll();
        model.addAttribute("discounts", discounts);
        return "sales-admin";
    }

    @PostMapping("/addNewDiscount")
    public String addNewDiscount(@RequestParam(name = "percentage") int percerntage,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "expireDate") String expireDate,
                                 HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null || currentUser.getRole() == UserRole.CLIENT) {
            return "redirect:/";
        }

        discountManager.create(percerntage, description, DateUtils.dateFromHtmlString(expireDate));

        return "redirect:/discounts";
    }

    @PostMapping("/deleteDiscount")
    public String deleteDiscount(@RequestParam(name = "discountId") int discountId,
                                 HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null || currentUser.getRole() == UserRole.CLIENT) {
            return "redirect:/";
        }

        discountManager.delete(discountId);

        return "redirect:/discounts";
    }

}
