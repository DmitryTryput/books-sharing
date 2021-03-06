package by.tryputs.bookssharing.filter;

import by.tryputs.bookssharing.dto.user.UserSignInDto;
import by.tryputs.bookssharing.service.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_URL = "/login";

    private final TokenAuthenticationService tokenAuthenticationService;
    private final Jackson2ObjectMapperBuilder objectMapperBuilder;

    private ObjectMapper objectMapper;

    public LoginFilter(final TokenAuthenticationService tokenAuthenticationService,
                       final AuthenticationManager authenticationManager,
                       final Jackson2ObjectMapperBuilder objectMapperBuilder) {
        super(new AntPathRequestMatcher(LOGIN_URL));
        this.objectMapperBuilder = objectMapperBuilder;
        this.tokenAuthenticationService = tokenAuthenticationService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response)
        throws AuthenticationException, IOException {
        final UserSignInDto credentials = getObjectMapper().readValue(request.getInputStream(), UserSignInDto.class);

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getEmail(), credentials.getPassword(), Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult) {
        // Write Authorization to Headers of Response.
        final String alias = authResult.getName();
        final String roles =
                authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(
                        ","));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(alias, null,
                authResult.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())));
        tokenAuthenticationService.addAuthentication(response, alias, roles);
    }

    private ObjectMapper getObjectMapper() {
        return objectMapper == null ? objectMapper = objectMapperBuilder.build() : objectMapper;
    }
}
