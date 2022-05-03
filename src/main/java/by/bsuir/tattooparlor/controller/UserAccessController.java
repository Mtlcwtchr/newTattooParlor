package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.IAuthService;
import by.bsuir.tattooparlor.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserAccessController {

    private IAuthService authService;

    @Autowired
    public UserAccessController(IAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/profile")
    public String forwardToProfile(HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/sign-in";
        }
        return "profile";
    }

    @GetMapping("/sign-up")
    public String forwardToRegisterForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/profile";
        }
        return "sign-up";
    }

    @GetMapping("/sign-in")
    public String forwardToLoginForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/profile";
        }
        return "sign-in";
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

    @PostMapping("/authClient")
    public String authClient(
            @RequestParam(name = "login") String login,
            @RequestParam(name = "password") String password,
            HttpSession session) {
        try {
            User user = authService.tryAuthenticate(login, password);
            Client client = authService.tryAuthorizeClient(user);
            session.setAttribute("currentUser", client);
        } catch (IllegalLoginException ex) {
            return "redirect:/sign-in?error=illegal_login";
        } catch (IllegalPasswordException ex) {
            return "redirect:/sign-in?error=illegal_password";
        } catch (IllegalRolePresentedException ex) {
            return "redirect:/sign-in?error=illegal_role";
        } catch (UtilException ex) {
            return "redirect:/sign-in?error=unknown";
        }

        return "redirect:/";
    }

}
