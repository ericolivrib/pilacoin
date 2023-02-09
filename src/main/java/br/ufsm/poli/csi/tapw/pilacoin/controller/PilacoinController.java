package br.ufsm.poli.csi.tapw.pilacoin.controller;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.repository.UsuarioRepository;
import br.ufsm.poli.csi.tapw.pilacoin.service.PilacoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("pilacoin")
public class PilacoinController {

    @Autowired
    private PilacoinService pilacoinService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("minerar-pilacoins")
    public ModelAndView minerarPilacoins() {
        var mv = new ModelAndView("home");

        Usuario usuario = getUsuarioLogado();

        pilacoinService.iniciar(0, usuario);
        mv.addObject("minerandoPilacoins", true);

        return mv;
    }

    @GetMapping("validar-pilacoins")
    public ModelAndView validarPilacoins() {
        var mv = new ModelAndView("home");

        Usuario usuario = getUsuarioLogado();

        pilacoinService.iniciar(1, usuario);
        mv.addObject("validandoPilacoins", true);

        return mv;
    }

    @GetMapping("minerar-blocos")
    public ModelAndView minerarBlocos() {
        var mv = new ModelAndView("home");

        Usuario usuario = getUsuarioLogado();

        pilacoinService.iniciar(2, usuario);
        mv.addObject("minerandoBlocos", true);

        return mv;
    }

    @GetMapping("validar-blocos")
    public ModelAndView validarBlocos() {
        var mv = new ModelAndView("home");

        Usuario usuario = getUsuarioLogado();

        pilacoinService.iniciar(3, usuario);
        mv.addObject("validandoBlocos", true);

        return mv;
    }

    @GetMapping("transferir-pilacoin")
    public ModelAndView transferirPilacoin() {
        var mv = new ModelAndView("home");

        Usuario usuario = getUsuarioLogado();

        pilacoinService.iniciar(4, usuario);
        mv.addObject("transferindoPilacoin", true);

        return mv;
    }

    @GetMapping("parar-mineracao-pilacoins")
    public ModelAndView pararMineracaoPilacoins() {
        var mv = new ModelAndView("home");

        List<String> nonces = pilacoinService.parar(0);

        mv.addObject("nonces", nonces);
        mv.addObject("mpf", true);

        return mv;
    }

    @GetMapping("parar-validacao-pila")
    public ModelAndView pararValidacaoPilacoins() {
        var mv = new ModelAndView("home");

        List<String> nonces = pilacoinService.parar(1);

        mv.addObject("nonces", nonces);
        mv.addObject("vpf", true);

        return mv;
    }

    @GetMapping("parar-mineracao-blocos")
    public ModelAndView pararMineracaoBlocos() {
        var mv = new ModelAndView("home");

        List<String> nonces = pilacoinService.parar(2);

        mv.addObject("nonces", nonces);
        mv.addObject("mbf", true);

        return mv;
    }

    @GetMapping("parar-validacao-blocos")
    public ModelAndView pararValidacaoBlocos() {
        var mv = new ModelAndView("home");

        List<String> nonces = pilacoinService.parar(3);

        mv.addObject("nonces", nonces);
        mv.addObject("vbf", true);

        return mv;
    }

    @GetMapping("parar-transferencia-pila")
    public ModelAndView pararTransferenciaPila() {
        var mv = new ModelAndView("home");

        List<String> nonces = pilacoinService.parar(4);

        mv.addObject("nonces", nonces);
        mv.addObject("tpf", true);

        return mv;
    }



    private Usuario getUsuarioLogado() {
        User USER = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[][] obj = usuarioRepository.findUsuarioByEmail(USER.getUsername());
        return new Usuario(obj);
    }

}
