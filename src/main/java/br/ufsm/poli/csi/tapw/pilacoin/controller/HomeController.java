package br.ufsm.poli.csi.tapw.pilacoin.controller;

import br.ufsm.poli.csi.tapw.pilacoin.repository.PilaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private PilaRepository pilaRepository;

    @GetMapping
    public String redirectToIndex() {
        return "redirect:home";
    }

    @GetMapping("home")
    public ModelAndView index(HttpServletRequest request) {
        var mv = new ModelAndView("home");

        var pilas = pilaRepository.findAll();
        mv.addObject("pilacoins", pilas);
        mv.addObject("minerandoPilacoins", false);
        mv.addObject("validandoPilacoins", false);
        mv.addObject("minerandoBlocos", false);
        mv.addObject("validandoBlocos", false);
        mv.addObject("transferindoPilacoin", false);

        return mv;
    }

}
