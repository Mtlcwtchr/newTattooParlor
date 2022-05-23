package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.DateUtils;
import by.bsuir.tattooparlor.util.IAuthService;
import by.bsuir.tattooparlor.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class UserAccessController {

    private final IAuthService authService;

    @Autowired
    public UserAccessController(IAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/sign-up")
    public String forwardToRegisterForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/profile";
        }
        return "sign-up";
    }

    @GetMapping("/sign-up-master")
    public String forwardToRegisterMasterForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/profile";
        }
        session.setAttribute("today", new Date());
        return "sign-up-master";
    }

    @GetMapping("/signOut")
    public String signOut(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            session.removeAttribute("currentUser");
            session.removeAttribute("currentClient");
            session.removeAttribute("currentMaster");
        }
        return "redirect:/";
    }

    @GetMapping("/sign-in")
    public String forwardToLoginForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/profile";
        }
        return "sign-in";
    }

    @GetMapping("/log-out")
    public String logOut(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            session.removeAttribute("currentUser");
            session.removeAttribute("currentClient");
            session.removeAttribute("currentMaster");
        }
        return "redirect:/";
    }

    @PostMapping("/applyClient")
    public String applyClient(
            @RequestParam(name = "login") String login,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone) {
        try {
            authService.applyClient(login, password, email, name, phone);
        } catch (UserPresentedException ex) {
            return "redirect:/sign-up?error=user_exist";
        } catch (ApplyClientException | ApplyUserException ex) {
            return "redirect:/sign-up?error=internal_error";
        } catch (UtilException ex) {
            return "redirect:/sign-up?error=unknown";
        }
        return "redirect:/sign-in";
    }

    @PostMapping("/applyMaster")
    public String applyMaster(
            @RequestParam(name = "login") String login,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "workStarted") String workStarted) {
        try {
            authService.applyMaster(login, password, email, name, DateUtils.dateFromHtmlString(workStarted));
        } catch (UserPresentedException ex) {
            return "redirect:/sign-up-master?error=user_exist";
        } catch (ApplyClientException | ApplyUserException ex) {
            return "redirect:/sign-up-master?error=internal_error";
        } catch (UtilException ex) {
            return "redirect:/sign-up-master?error=unknown";
        }
        return "redirect:/sign-in";
    }

    @PostMapping("/authClient")
    public String authClient(
            @RequestParam(name = "login") String login,
            @RequestParam(name = "password") String password,
            HttpSession session) {
        try {
            User user = authService.tryAuthenticate(login, password);
            session.setAttribute("currentUser", user);
            switch (user.getRole()) {
                case CLIENT:
                    Client client = authService.tryAuthorizeClient(user);
                    session.setAttribute("currentClient", client);
                    break;
                case MODERATOR:
                    TattooMaster master = authService.tryAuthorizeMaster(user);
                    session.setAttribute("currentMaster", master);
                case ADMIN:
                    authService.tryAuthorizeAdmin(user);
            }
        } catch (IllegalLoginException ex) {
            return "redirect:/sign-in?error=illegal_login";
        } catch (IllegalPasswordException ex) {
            return "redirect:/sign-in?error=illegal_password";
        } catch (IllegalRolePresentedException ex) {
            return "redirect:/sign-in?error=illegal_role";
        } catch (UserBlockedException ex) {
            return "redirect:/sign-in?error=user_blocked";
        } catch (UtilException ex) {
            return "redirect:/sign-in?error=unknown";
        }

        return "redirect:/";
    }

}
