package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import by.bsuir.tattooparlor.util.IClientManager;
import by.bsuir.tattooparlor.util.ITattooMasterManager;
import by.bsuir.tattooparlor.util.IUserManager;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsersGalleryController {

    private final IClientManager clientManager;
    private final ITattooMasterManager tattooMasterManager;
    private final IUserManager userManager;

    @Autowired
    public UsersGalleryController(IClientManager clientManager, ITattooMasterManager tattooMasterManager, IUserManager userManager) {
        this.clientManager = clientManager;
        this.tattooMasterManager = tattooMasterManager;
        this.userManager = userManager;
    }

    @GetMapping("/users")
    public String proceedUsersGallery(Model model) {
        List<Client> clients = clientManager.findAll();
        List<TattooMaster> tattooMasters = tattooMasterManager.findAll();
        model.addAttribute("masters", tattooMasters);
        model.addAttribute("clients", clients);
        return "users";
    }

    @PostMapping("/setUserStatus")
    public String proceedUserStatusChange(@RequestParam(name = "userId") int userId,
                                          @RequestParam(name = "status") UserStatus status,
                                          HttpSession session) {
        boolean isStatusSat = false;
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user != null) {
                isStatusSat = userManager.setStatus(userId, user.getRole(), status);
            }
        } catch (UtilException e) {
            e.printStackTrace();
        }
        return "redirect:/users?isStatusSat=" + isStatusSat;
    }

}
