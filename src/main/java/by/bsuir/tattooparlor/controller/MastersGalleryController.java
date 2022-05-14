package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.ITattooMasterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MastersGalleryController {

    private final ITattooMasterManager tattooMasterManager;

    @Autowired
    public MastersGalleryController(ITattooMasterManager tattooMasterManager) {
        this.tattooMasterManager = tattooMasterManager;
    }


    @GetMapping("/masters")
    public String proceedMastersGallery(Model model) {
        List<TattooMaster> tattooMasters = tattooMasterManager.findAll();
        model.addAttribute("masters", tattooMasters);
        return "masters";
    }

}
