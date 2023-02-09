package br.ufsm.poli.csi.tapw.pilacoin.controller;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.repository.UsuarioRepository;
import br.ufsm.poli.csi.tapw.pilacoin.security.util.CookieUtil;
import br.ufsm.poli.csi.tapw.pilacoin.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    public ModelAndView login(@RequestParam String email, @RequestParam String senha, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        try {
            var authToken = new UsernamePasswordAuthenticationToken(email, senha);
            var authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);

                var objetoUsuario = usuarioRepository.findUsuarioByEmail(email);
                var usuario = new Usuario(objetoUsuario);

                var token = JwtUtil.generateToken(usuario);

                request.getSession().setAttribute("user", usuario);
                CookieUtil.setCookie("token", token, response);

                logger.info("Login efetuado! | Usuário: {}", usuario.getNome());
                mv.setViewName("redirect:home");
            }
        } catch (AuthenticationException e) {
            logger.error("Falha de autenticação de usuário!");
            mv.setViewName("login");
            mv.addObject("erro", true);
        }

        return mv;
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "login";
    }

}
