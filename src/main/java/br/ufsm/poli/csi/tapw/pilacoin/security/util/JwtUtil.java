package br.ufsm.poli.csi.tapw.pilacoin.security.util;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final long LIFETIME = Duration.ofSeconds(3600).toMillis();
    private static final String SIGNATURE = "pilacoin";

    public static String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", usuario.getEmail());
        claims.put("name", usuario.getNome());
        claims.put("roles", usuario.getAutoridade());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + LIFETIME))
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)
                .compact();
    }

    public static String getSubjectToken(String token) {
        return (token != null) ? parseToken(token).getSubject() : null;
    }

    public static boolean isExpiredToken(String token) {
        return token != null && parseToken(token).getExpiration().before(new Date());
    }

    private static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNATURE)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }
}
