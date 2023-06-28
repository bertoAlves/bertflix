package com.bertflix.authms.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "+r48dbNC17a/PK/FU+Z5fZ+kZ8ts/Tae8amabOweC+WieT5T6fxngOJthDajx8Z5vY152yS4deAHRKNhRzheACrjUtEv66DpVvZwFCw+3OGL6ChYUEqxGd7CGWobij/z0c703lPJjLVvzzj9FN5xzXu6p1O3vJc4uJb3JSrPTWaR2RIcJZDHCCwkIu+x3+jCsIIjWj3x2OP4rAs1DW9hDBR/LQE8WpKjHwEkhKI7mgSEGaHQzpdkFQp2SO6sW//q4vRnmjTviY2Ow14R85A9PbcspSDqgPQIOrWddJgN0nA56LxM4XkhhzB5Iz3dOyxrpDm5KcXkvhyYJFbFV0J+ClPq99cL7sZnl6GcCljMtWWNF0hntUUWEe6jbD49rwMXttK/yxGzySxWOjHS1QHVoR8jcdSiPdGUqu93HXEDrdZj5tIk7S9yET6DCHmKf4vCpJuPJFq3wk53U5do86rvgHXXq1LMhaQPUC5VR3chWFL4suZ30zeTOKnN/nBLvaPWApZZZn5adjwqvqnW9tiIcthBJYe5sDHsPagjrRwBjth0MGvPDrloXOfjYPnpnFY67wGZmo27CiHQGQdLkELaGeP9Bp5BzXnf8XV2DJSvrBB8lo4jNt+K3b1SxHXQFeQEw+FsNi6wcCQzr14qm68rX64knItgXolnEkaafNuGXcpjbWx/o1f8IrMDcTjqE54OT8bGVFX8WpM4oKrwux1HlIetvlu6AGobppAOgK3X94uMtYoYzUvj6zrXi4SC08ejEgQ1GcfrlrhCtHYU7clLX8EUsu8PfI1P2qXv1wDoFUwRXmUaGtqu+2UM1w9cH8lSV63b1POJyL/vVs5SLcvfl6EZG8dTX1BIRyFqlBSJQmgO3NAsIpLS0HOUb5usIKipYqALiy2XF1J632QWEuyjclfpvIZ8mqcUwZ8nJoOanWxZvK+iNlKq0EAhv8Ofek1xKO6KtGbuiv6Bz6SkoO6NTOnMNo3MGv+G9spF4wYRAmmySyF8YIyY+KwxgrlQiX4qkA+gpqk6cCDOVQ85YXG73/MJEmeloXcDI/NksSQnn98plAEEyECwfSXjuxiv9/T+lOdej4dw+ebxVb8a0o7Ku7dPqFL+NjCfrwCGtn2RwH/HOGvSIHrhcIpPz0tNF/IIYHgOLFD9mfe9IKN3+rTuUEVhk/MqjUB3ZL9/AmyPBDUWbphgMJvQ1PiAQInh78B0EVPbKx2a1qDNbScQZpqOVkzkykGVyiqPFgQYxzQvSovTov7axJkdEVi7HefJtbzlY6d9EPIYedrFSonq1eCIceG+fkJzDUVWYMKBCrkuX1fJmXFyFeYU6A==";

    public String extractEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String email = extractEmail(jwt);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwt).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
