package combine.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Butcher
 * @since 2024-06-15
 */
public class GlobalFilter extends HttpFilter {

    private static final long serialVersionUID = 20249921314707892L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("GlobalFilter...");
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/test")) {
            PrintWriter writer = response.getWriter();
            writer.write("denied...");
            writer.flush();
            writer.close();
        }
        super.doFilter(request, response, chain);
    }
}
