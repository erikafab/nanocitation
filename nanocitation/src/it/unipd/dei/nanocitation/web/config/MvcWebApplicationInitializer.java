package it.unipd.dei.nanocitation.web.config;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * This is an "Entry Point" Class i.e. here is the place from where the Servlet
 * Container (i.e. Tomcat) will star processing
 * the Web App and from where the container will understand theere will be just
 * one Servlet, the Spring's one!
 * 
 * @author erika
 *
 */
public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{

	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class[]
		{ MvcWebConfig.class };
	}

	@Override
	protected String[] getServletMappings()
	{
		return new String[]
		{ "/" };
	}

}
