package com.bastos.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .inMemory()
                .withClient("bastos-auth-web")
                .secret(passwordEncoder.encode("bastos123456"))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("write", "read")
            .accessTokenValiditySeconds(60 * 60 * 6) // 6 horas (padrão é 12 horas)
            .refreshTokenValiditySeconds(60 * 24 * 60 * 60) // 60 dias (padrão é 30 horas)

            .and()
                .withClient("foodanalytics")
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes("authorization_code")
                .scopes("write", "read")
                .redirectUris("http://www.foodanalytics.local:8082")

            .and()
                .withClient("webadmin")
                .authorizedGrantTypes("implicit")
                .scopes("write", "read")
                .redirectUris("http://aplicacao-cliente")

            .and()
                .withClient("bastos-auth-crud")
                .secret(passwordEncoder.encode("bastosCrud123456"))
                .authorizedGrantTypes("client_credentials")
                .scopes("write", "read")

            .and()
                .withClient("check-bastos-crud")
                .secret(passwordEncoder.encode("checkbastos123456"));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
			.reuseRefreshTokens(false)
            .accessTokenConverter(jwtAccessTokenConverter())
            .tokenGranter(tokenGranter(endpoints));
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("fi156ygty345qsnmjçihbeinv30j320fw3ndmpfg34g43gn3g0ivnvn0g2f0f30");

        return jwtAccessTokenConverter;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}
