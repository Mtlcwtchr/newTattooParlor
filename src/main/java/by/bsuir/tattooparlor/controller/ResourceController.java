package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class ResourceController {

    private static final String SRC = "/Users/stanislav/idea/new/TattooParlor/src/main/resources/static/imgs/";
    private static final String NO_PROFILE_PICTURE_URI = "/Users/stanislav/idea/new/TattooParlor/src/main/resources/static/imgs/profiles/NoProfilePic.jpeg";

    @GetMapping("/loadFile")
    public void loadImage(@RequestParam(name = "uri") String uri,
                          HttpServletResponse response) {
        try(ServletOutputStream stream = response.getOutputStream()) {
            byte[] bytes = Files.readAllBytes(Path.of(String.format("%s%s", uri.contains(SRC) ? "" : SRC, uri)));
            stream.write(bytes);
            stream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/loadProfilePic")
    public void loadProfilePic(HttpSession session, HttpServletResponse response) {
        Client currentClient = (Client) session.getAttribute("currentClient");
        String profilePictureUri = currentClient.getProfilePictureUri();
        if (profilePictureUri == null || profilePictureUri.isBlank()) {
           profilePictureUri = NO_PROFILE_PICTURE_URI;
        }
        try {
            response.sendRedirect("/loadFile?uri=" + profilePictureUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
