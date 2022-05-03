package by.bsuir.tattooparlor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class ResourceController {

    private static final String SRC = "/Users/stanislav/idea/new/TattooParlor/src/main/resources/static/imgs/";

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

}
