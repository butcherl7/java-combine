package combine.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

/**
 * @author Butcher
 * @since 2024-06-15
 */
public class TestFilter extends HttpFilter {

    @Serial
    private static final long serialVersionUID = -1960365675714820206L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("TestFilter...");
        super.doFilter(request, response, chain);
    }
}
