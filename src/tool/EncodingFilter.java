package tool;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns={"/*"})
public class EncodingFilter implements Filter {
	public void init(FilterConfig filterConfig){}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException{

		// 前処理 開始
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		System.out.println("フィルタの前処理");
		// 前処理 終了

		chain.doFilter(req, res);

		// 後処理 開始
		System.out.println("フィルタの後処理");
		// 後処理 終了
	}

	public void destroy(){}
}
