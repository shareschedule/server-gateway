package shareschedule.servergateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.SECRET}")
    private String SECRET;

    public long getUserId (String accessToken) throws HttpResponseException {
        String token = parseBearerAuthorization(accessToken);
        Claims payload;

        try{
            payload = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new HttpResponseException(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다.");
        }
        return Long.parseLong(payload.get("userId").toString());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private String parseBearerAuthorization(String bearer) throws HttpResponseException {
        if(!bearer.startsWith("Bearer")){
            throw new HttpResponseException(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다.");
        }

        return bearer.substring(7);
    }
}
