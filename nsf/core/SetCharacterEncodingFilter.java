/**
 * DESC : 
 * Created by KCG
 * Created date : 2007. 11. 28
 * name : SetCharacterEncodingFilter.java
 */
package nsf.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by KCG
 * Created date : 2007. 11. 28
 */
public class SetCharacterEncodingFilter implements Filter
{

	/**
	 * DESC : encoding.
	 */
	protected String encoding = null;
	
	/**
	 * DESC : FilterConfig.
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * DESC : posibility of encoding.
	 */
	protected boolean ignore = true;

	/**
	 * DESC : destory. 
	 * Created by KCG
	 * Created date : 2007. 11. 28
	 */
	public void destroy()
	{

		this.encoding = null;
		this.filterConfig = null;

	}

	/**
	 * DESC : doFilter. 
	 * Created by KCG
	 * Created date : 2007. 11. 28
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{

		if (ignore || (request.getCharacterEncoding() == null))
		{
			String encoding = getEncoding( request );
			if ( encoding != null ) 
				request.setCharacterEncoding( encoding );
		}

		chain.doFilter( request, response );
	}

	/**
	 * DESC : initial. 
	 * Created by KCG
	 * Created date : 2007. 11. 28
	 */
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter( "encoding" );
		String value = filterConfig.getInitParameter( "ignore" );
		
		if ( value == null ) 
			this.ignore = true;
		else 
			if (value.equalsIgnoreCase("true")) 
				this.ignore = true;
		else 
			if (value.equalsIgnoreCase("yes")) 
				this.ignore = true;
		else
			this.ignore = false;

	}

	/**
	 * DESC : get endcoding. 
	 * Created by KCG
	 * Created date : 2007. 11. 28
	 * @param request
	 * @return
	 */
	protected String getEncoding(ServletRequest request)
	{
		return (this.encoding);

	}
}
