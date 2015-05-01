package ca.core.logic;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2014/5/28
 * Time: 11:18
 */
public class EncodingFilter implements Filter {
    String encoding = null;
    public void destroy() {
        this.encoding = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if(encoding != null) {
            request.setCharacterEncoding(this.encoding);
            response.setCharacterEncoding(this.encoding);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.encoding = config.getInitParameter("encoding");
    }
}
