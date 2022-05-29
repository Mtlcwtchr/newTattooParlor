package by.bsuir.tattooparlor.controller;

import antlr.StringUtils;
import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.entity.*;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.*;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static by.bsuir.tattooparlor.util.FileUtils.trySaveNewPictureByPath;

@Controller
public class UserProfileController {

    private final IAuthService authService;
    private final IClientManager clientManager;
    private final IClientRateManager clientRateManager;
    private final ITattooMasterManager tattooMasterManager;
    private final IOrderManager orderManager;
    private final IDiscountManager discountManager;

    public UserProfileController(IAuthService authService, IClientManager clientManager, IClientRateManager clientRateManager, ITattooMasterManager tattooMasterManager, IOrderManager orderManager, IDiscountManager discountManager) {
        this.authService = authService;
        this.clientManager = clientManager;
        this.clientRateManager = clientRateManager;
        this.tattooMasterManager = tattooMasterManager;
        this.orderManager = orderManager;
        this.discountManager = discountManager;
    }

    @RequestMapping("/profile")
    public String forwardToProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        if (currentUser.getRole() == UserRole.CLIENT) {
            Client client = (Client) session.getAttribute("currentClient");
            List<Product> favourites = clientRateManager.getLikedProducts(client);
            List<List<Product>> quadFavourites = ListUtils.mapToOctants(favourites);
            List<ClientDiscount> discounts = discountManager.findAllForClient(client);
            model.addAttribute("discounts", discounts);
            model.addAttribute("favourites", quadFavourites);
        }

        return "profile";
    }

    @PostMapping("/updateClientPersonalInfo")
    public String updateClientPersonalInfo(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "phone") String phone,
                                           @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                           HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser.getRole() != UserRole.CLIENT) {
            return "redirect:/profile";
        }

        try{
            if(multipartFile != null && !multipartFile.isEmpty()) {
                String profilePictureUri = FileUtils.getNewPictureUri(multipartFile.getOriginalFilename(), multipartFile.getContentType().replace("/", "."));
                trySaveNewPictureByPath(multipartFile, profilePictureUri);

                Client currentClient = (Client) session.getAttribute("currentClient");
                clientManager.updateProfilePictureUri(currentClient, profilePictureUri);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Client currentClient = (Client) session.getAttribute("currentClient");
        currentClient.setName(name);
        currentClient.setPhone(phone);
        currentClient = clientManager.update(currentClient);
        session.setAttribute("currentClient", currentClient);

        return "redirect:/profile";
    }

    @PostMapping("/updateUserPassword")
    public String updateUserPassword(@RequestParam(name = "password") String password,
                                     HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser != null) {
                long userId = currentUser.getId();
                boolean updated = authService.updatePassword(userId, password);
                if (!updated) {
                    return "redirect:/profile?error=same_password";
                }
            }
        } catch (UtilException ex) {
            ex.printStackTrace();
        }
        return "redirect:/profile";
    }

    @PostMapping("/updateMasterPersonalInfo")
    public String updateMasterPersonalInfo(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "workStarted", required = false) String workStarted,
                                           @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                           HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser.getRole() != UserRole.MODERATOR) {
            return "redirect:/profile";
        }

        try{
            if(multipartFile != null && !multipartFile.isEmpty()) {
                String profilePictureUri = FileUtils.getNewPictureUri(multipartFile.getOriginalFilename(), multipartFile.getContentType().replace("/", "."));
                trySaveNewPictureByPath(multipartFile, profilePictureUri);

                TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");
                tattooMasterManager.updateProfilePictureUri(currentMaster, profilePictureUri);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        TattooMaster currentMaster = (TattooMaster) session.getAttribute("currentMaster");
        currentMaster.setName(name);
        if(workStarted != null && !workStarted.isEmpty()) {
            currentMaster.setWorkStarted(DateUtils.dateFromHtmlString(workStarted));
        }
        currentMaster = tattooMasterManager.update(currentMaster);
        session.setAttribute("currentMaster", currentMaster);

        return "redirect:/profile";
    }

}
