package com.presto.auth.config;

import com.presto.auth.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;

@Configuration
@Order(6)
public class AuthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(AuthServerConfigurer.class);

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Value("classpath:certificate/jwt.jks")
    private Resource jwtKeyResource;

    @Value("${jwt.certificate.store.password:oblongata}")
    private String keystorePassword;

    @Value("${jwt.certificate.key.alias:jwt}")
    private String keyAlias;

    @Value("${jwt.certificate.key.password:oblongata}")
    private String keyPassword;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(jwtKeyResource, keystorePassword.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @EventListener
    public void authSuccessEventListener(AuthenticationSuccessEvent authorizedEvent){
        try {
            Object principal = authorizedEvent.getAuthentication().getPrincipal();
            String userPhone = principal instanceof AuthUserDetails ? ((AuthUserDetails) principal).getUsername() : (String) principal;
            userService.updateUserStatus(userPhone, true);

        } catch (Exception ex) {
            logger.debug(ex.getMessage());
        }
    }

    @EventListener
    public void authFailedEventListener(AbstractAuthenticationFailureEvent oAuth2AuthenticationFailureEvent){
        try {
            Object principal = oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal();
            String userPhone = principal instanceof AuthUserDetails ? ((AuthUserDetails) principal).getUsername() : (String) principal;
            userService.updateUserStatus(userPhone, false);

        } catch (Exception ex) {
            logger.debug(ex.getMessage());
        }
    }

}