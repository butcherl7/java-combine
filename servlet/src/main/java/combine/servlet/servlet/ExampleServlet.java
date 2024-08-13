package combine.servlet.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Butcher
 * @since 2024-06-15
 */
@WebServlet(name = "ExampleServlet", urlPatterns = "/example")
public class ExampleServlet extends HttpServlet {

    private static final long serialVersionUID = 8775391570409166030L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter writer = resp.getWriter()) {
            writer.write("This is a Example...");
            writer.flush();
        } catch (Exception ignored) {
        }
    }
}
