package it.unipd.dei.nanocitation.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.http.MediaType;

import it.unipd.dei.nanocitation.landing_page.LandingPageCreator;
import it.unipd.dei.nanocitation.metadata.DerefAndEnrich;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.text_snippet.TextSnippetCreator;
import it.unipd.dei.nanocitation.util.NoSupportedNpException;
import it.unipd.dei.nanocitation.web.model.Form;
import it.unipd.dei.nanocitation.web.model.LandingPage;
import it.unipd.dei.nanocitation.web.model.CitationMetadata;

/**
 * @author erika
 *
 */
@Controller
@SessionAttributes("metaContainer")
public class UrlController
{
	private static final String LANDINGPAGEURL = "http://nanocitation.dei.unipd.it/landingpage/";
	
	
	/**
	 * This is a staticResoces which several Classes use for dictionary purpose
	 */
	@Value("res/sio.owl")
	private Resource resourceSioOwl;
	
	/**
	 * Questo è il metodo che quando viene invocato dal corrispettivo url
	 * effettua la creazione del form e quindi ritorna il relativo template
	 * 
	 * @param model
	 *            which will automatically created by Spring
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/")
	public String emptUrlManipulation(Model model) throws Exception
	{
		return "index";
	}
	
	@GetMapping("/index")
	public String indexUrlManipulation(Model model) throws Exception
	{
		return "index";
	}
	
	// @GetMapping("/dueindexx")
	// public String indexxUrlManipulation(Model model) throws Exception
	// {
	// return "dueindexx";
	// }
	
	/**
	 * Questo è il metodo che quando viene invocato dal corrispettivo url
	 * effettua la creazione del form e quindi ritorna il relativo template
	 * 
	 * @param model
	 *            which will automaticly creted by spring
	 * @return
	 */
	@GetMapping("/form")
	public String creaForm(Model model)
	{
		model.addAttribute("form", new Form());
		return "form";
	}
	
	@GetMapping("/examples/{id}")
	public String mapEx(@PathVariable("id") String id, Model model)
	{
		return "lpexample" + id;
	}
	
	@GetMapping("/form2")
	public String creaForm2(Model model)
	{
		return "form2np";
	}
	
	/**
	 * This is the methodwhich retrieves from model attribute Form the Id of
	 * Nano Publication and use it for generate the landing page
	 * 
	 * 
	 * @param form
	 * @param model
	 *            which will automaticly creted by spring
	 * @return
	 * @throws IOException
	 */
	
	@PostMapping("/landingpage")
	public String automaticTextSnippetCreatorFromForm(@ModelAttribute Form form, Model model) throws IOException
	{
		try
		{
			addLandingPageToTheModel(form.getNpId(), model);
			
		} catch (IOException e)
		{
			return "404"; // return different page TODO
		} catch (NoSupportedNpException e)
		{
			return "errorNoSupported";
		}
		return "landingpagetemplate";
		
	}
	
	/**
	 * This is the method which retrieves from URL the NanoPublication Id and
	 * uses it for generate the landing page
	 * 
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/landingpage/{id}")
	public String automaticTextSnippetCreatorFromUrl(@PathVariable("id") String id, Model model) throws IOException
	{
		try
		{
			addLandingPageToTheModel(id, model);
		} catch (IOException e)
		{
			return "404";
		} catch (NoSupportedNpException e)
		{
			return "errorNoSupported";
		}
		return "landingpagetemplate";
	}
	
	
	@GetMapping("/staticfile")
	public String visualizzaUnaRisorsaStaticaHtml()
	{
		return "staticfile";
	}
	
	private void addLandingPageToTheModel(String npId, Model model) throws IOException, NoSupportedNpException
	{
		DerefAndEnrich ed = new DerefAndEnrich(npId, resourceSioOwl.getFile());
		
		MetadataContainer meta = ed.getMeta();
		setAttributeSession(meta);
		
		TextSnippetCreator tscreat = new TextSnippetCreator(meta);
		String textsnippet = tscreat.generateTextSnippet();
		
		LandingPageCreator lpcreat = new LandingPageCreator(meta, textsnippet);
		
		LandingPage lp = lpcreat.createPage();
		
		model.addAttribute("metaContainer", meta);
		model.addAttribute("landingPage", lp);
		
	}
	
	@ModelAttribute("metaContainer")
	public MetadataContainer setAttributeSession(MetadataContainer meta)
	{
		return meta;
	}
	
	@GetMapping(path = "/download/meta", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE  })
	private @ResponseBody CitationMetadata downloadMeta(@ModelAttribute("metaContainer") MetadataContainer metaContainer)
	{
		CitationMetadata m = new CitationMetadata(metaContainer);
		return m;
	}
	
	 @GetMapping("/restdoc")
	 public String restDoc()
	 {
	 return "restdoc";
	 }
	
	
}
