/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.configuration.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Admin
 */
public class TimeAccessFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     LogManager.getLogger(this.getClass().getName()).debug(">>> TimeAccessFilter .... Open 1:00 to 5:59");
        int hour = LocalDateTime.now().getHour();
        if( 1 <= hour && hour < 6){
            filterChain.doFilter(request, response);
          }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
    
}
