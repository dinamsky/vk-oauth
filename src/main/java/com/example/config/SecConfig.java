package com.example.config;

import com.example.security.AuthSuccessHandlerImpl;
import com.example.security.MyUserDetailService;
import com.example.security.UserInfoTokenServicesForVk;
import com.example.security.VkCustomFilter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CompositeFilter;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@EnableWebSecurity
@EnableOAuth2Client
@Configuration
@ComponentScan("com.example.security")
public class SecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebApplicationContext applicationContext;
    private MyUserDetailService userDetailsService;
    @Autowired
    private AuthSuccessHandlerImpl successHandler;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(MyUserDetailService.class);
    }

    @Autowired
    OAuth2ClientContext oAuth2ClientContext;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
                .logoutSuccessUrl("/").permitAll().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

    }
    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    //TODO Configure UserDetails JDBC auth
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder())
                .and()
                .authenticationProvider(authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**");
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

//    @Bean
//    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//        return new SecurityEvaluationContextExtension();
//    }
    //TODO UsrDetailsDAO
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }




    @Bean
    @ConfigurationProperties("vkontakte.client")
    public AuthorizationCodeResourceDetails vk() {
        return new AuthorizationCodeResourceDetails();
    }
    @Bean
    @ConfigurationProperties("vkontakte.resource")
    public ResourceServerProperties vkResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("github.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("github.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }


    private Filter ssoFilter()

    {   CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        filters.add(googleFilter);

        VkCustomFilter vkFilter = new VkCustomFilter("/login/vkontakte");
        OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
        vkFilter.setRestTemplate(vkTemplate);
        UserInfoTokenServicesForVk tokenServicesvk = new UserInfoTokenServicesForVk(vkResource().getUserInfoUri(), vk().getClientId());
        tokenServicesvk.setRestTemplate(vkTemplate);
        vkFilter.setTokenServices(tokenServicesvk);
        filters.add(vkFilter);

        filter.setFilters(filters);

        return filter;
    }
    public String friends(){
        OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
        UserInfoTokenServicesForVk tokenServicesvk = new UserInfoTokenServicesForVk("https://api.vk.com/method/friends.get?fields=city,domain&count=10&v=5.59", vk().getClientId());
        tokenServicesvk.setRestTemplate(vkTemplate);
        JsonNode response = vkTemplate.getForObject("https://api.vk.com/method/friends.get?fields=city,domain&count=10&v=5.59", JsonNode.class);
         return response.get("response").get("items").toString();
    }
}
