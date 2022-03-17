package com.tcc.manutencao_adm_ticket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementsUserDetailsService userDetailsService;

//    @Bean
//    public static BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @Autowired
//    private ImplementsUserDetailsService implementsUserDetailsService;
//
//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception{
//        return new ImplementsUserDetailsService(usuarioRepository);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
//       http.authorizeRequests()
//               .antMatchers(HttpMethod.GET, "/").permitAll()
//               .antMatchers(HttpMethod.GET, "/cadastro_administrador").hasRole("ATENDENTE")
//               .antMatchers(HttpMethod.GET, "/home").hasRole("USER")
//               .anyRequest()
//               .authenticated()
//               .and()
//               .formLogin()
//               .permitAll()
//               .and()
//               .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//               .logoutSuccessUrl("/login").permitAll()
//               .and()
//               .httpBasic();
//
//       http.csrf().disable();
//       http.headers().frameOptions().disable();

        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/home").authenticated()

                //USER
                .antMatchers(HttpMethod.GET, "/ticket/cadastrar_ticket").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/ticket/cadastrar_ticket").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/ticket/listar_ticket").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/ticket/listar_ticket").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/ticket/view_ticket/{idTicket}")
                        .hasAnyAuthority("USER", "ATENDENTE")
                .antMatchers(HttpMethod.POST, "/ticket/view_ticket/{idTicket}")
                        .hasAnyAuthority("USER", "ATENDENTE")
                .antMatchers(HttpMethod.GET, "/ticket/remover").hasAuthority("USER")

                //MANUTENCAO
                .antMatchers(HttpMethod.GET, "/manutencao/finalizar").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.GET, "/manutencao/ticket_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.POST, "/manutencao/ticket_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.GET, "/manutencao/verificar_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.POST, "/manutencao/verificar_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.GET, "/manutencao/view_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.POST, "/manutencao/view_manutencao").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.GET, "/manutencao/view_manutencao_selecionado").hasAuthority("MANUTENCAO")
                .antMatchers(HttpMethod.POST, "/manutencao/view_manutencao_selecionado").hasAuthority("MANUTENCAO")

                //ATENDENTE
                .antMatchers(HttpMethod.GET, "/ticket/ticket_atendente").hasAuthority("ATENDENTE")
                .antMatchers(HttpMethod.POST, "/ticket/ticket_atendente").hasAuthority("ATENDENTE")
                .antMatchers(HttpMethod.GET, "/ticket/view_atendente").hasAuthority("ATENDENTE")
                .antMatchers(HttpMethod.POST, "/ticket/view_atendente").hasAuthority("ATENDENTE")
                .antMatchers(HttpMethod.GET, "/ticket/verificar_ticket").hasAuthority("ATENDENTE")
                .antMatchers(HttpMethod.POST, "/ticket/verificar_ticket").hasAuthority("ATENDENTE")

                //ADMIN
                .antMatchers(HttpMethod.GET, "/usuario/cadastrar_usuario").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/usuario/cadastrar_usuario").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/usuario/listar_usuario").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/usuario/listar_usuario").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/usuario/editar_usuario/{email}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/usuario/editar_usuario/{email}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/usuario/remover").hasAuthority("ADMIN")


                .anyRequest().authenticated()
                .and().formLogin().loginPage("/index").defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error")
                    .permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll().logoutSuccessUrl("/index")
                .and().exceptionHandling().accessDeniedPage("/403");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//                .withUser("thomass@gmail.com").password(passwordEncoder().encode("12345")).roles("ATENDENTE");
//        auth.userDetailsService(userDetailsServiceBean())
//                .passwordEncoder(passwordEncoder());
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers( "/css/**", "/img/**");
    }
}
