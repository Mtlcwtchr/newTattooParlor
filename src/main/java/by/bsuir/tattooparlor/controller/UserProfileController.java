package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.IClientManager;
import by.bsuir.tattooparlor.util.IOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class UserProfileController {

    private static final String SRC = "/Users/stanislav/idea/new/TattooParlor/src/main/resources/static/imgs/profiles/";

    private final IClientManager clientManager;
    private final IOrderManager orderManager;

    @Autowired
    public UserProfileController(IClientManager clientManager, IOrderManager orderManager) {
        this.clientManager = clientManager;
        this.orderManager = orderManager;
    }

    @GetMapping("/profile")
    public String forwardToProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/sign-in";
        }

        return "profile";
    }

    @PostMapping("/updateProfilePic")
    public String updateProfilePic(HttpSession session,
                                   @RequestParam(name = "file") MultipartFile multipartFile) {
        try {
            if(multipartFile == null) {
                return "redirect:/profile";
            }

            String profilePictureUri = getProfilePictureUri(multipartFile.getOriginalFilename(), multipartFile.getContentType().replace("/", "."));
            trySaveNewPictureByPath(multipartFile, profilePictureUri);

            Client currentClient = (Client) session.getAttribute("currentClient");
            clientManager.updateProfilePictureUri(currentClient, profilePictureUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile";
    }

    private String getProfilePictureUri(String fileName, String contentType) {
        return SRC + Integer.toHexString(fileName.hashCode()) + contentType;
    }

    private void trySaveNewPictureByPath(MultipartFile multipartFile, String profilePictureUri) throws IOException {
        File file = new File(profilePictureUri);
        multipartFile.transferTo(file);
    }

}
