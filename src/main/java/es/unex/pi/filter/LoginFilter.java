package es.unex.pi.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



/**
 * Servlet Filter implementation class LoginFilter
 */
//@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }
//,          urlPatterns = { "/orders/*","/rest/*","/pages/*" })

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }
,          urlPatterns = { "/hosts/*","*.do","/pages/*","/users*","/rest/*"})

public class LoginFilter extends HttpFilter {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());  

    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.info("------- FILTER LOGIN -------");
	
		HttpServletRequest req= (HttpServletRequest) request;
		HttpServletResponse res= (HttpServletResponse) response;
		String path = ((HttpServletRequest) request).getRequestURI();
		logger.info("PATH: " + path);
		if(path.endsWith("LoginUserServlet.do") || path.endsWith("LogoutUserServlet.do")|| path.endsWith("Proyecto_Airbnb/") || path.endsWith("RegisterUserServlet.do") || path.endsWith("CheckRegisterUserServlet.do")) {
			logger.info("AAAAAAAAAAAAAAAAAA");
			chain.doFilter(req, res);
		}
		else {
			HttpSession session = req.getSession(false);
			if(session != null) {
				if(session.getAttribute("user") != null) {
					logger.info("bbbbbbbbbbbbbbbbbbb");

					chain.doFilter(req, res);
				}
				else {
					logger.info("Error: No existe el objeto usuario en la sesión");
					res.sendRedirect(req.getContextPath()+"/LogoutUserServlet.do");
				}
			}
			else {
				logger.info("Error: No existe sesión de usuario o ha caducado");
				res.sendRedirect(req.getContextPath()+"/LoginUserServlet.do");
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
