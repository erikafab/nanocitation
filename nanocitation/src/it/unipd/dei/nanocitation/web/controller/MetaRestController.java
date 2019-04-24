package it.unipd.dei.nanocitation.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.unipd.dei.nanocitation.metadata.DerefAndEnrich;
import it.unipd.dei.nanocitation.metadata.types.MetadataContainer;
import it.unipd.dei.nanocitation.text_snippet.TextSnippetCreator;
import it.unipd.dei.nanocitation.util.NoSupportedNpException;
import it.unipd.dei.nanocitation.web.model.CitationMetadata;

@RestController
@RequestMapping("rest")
public class MetaRestController
{
	@Value("res/sio.owl")
	private Resource resourceSioOwl;
	
	@GetMapping(path = "/meta/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public CitationMetadata getXMLMeta(@PathVariable String id)
	{
		
		CitationMetadata meta;
		try
		{
			meta = getMetadata(id);
			
		} catch (NoSupportedNpException | IOException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Supported");
			
		}
		return meta;
	}
	
	@GetMapping(path = "/textsnippet/{id}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String getTextSnippet(@PathVariable String id)
	{
		
		MetadataContainer metaCont = new MetadataContainer();
		try
		{
			DerefAndEnrich ed = new DerefAndEnrich(id, resourceSioOwl.getFile());
			metaCont = ed.getMeta();
			
		} catch (NoSupportedNpException | IOException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Supported");
			
		}
		TextSnippetCreator tsc = new TextSnippetCreator(metaCont);
		return tsc.generateTextSnippet();
	}
	
	private CitationMetadata getMetadata(String id) throws NoSupportedNpException, IOException
	{
		DerefAndEnrich ed = new DerefAndEnrich(id, resourceSioOwl.getFile());
		MetadataContainer metaCont;
		metaCont = ed.getMeta();
		return new CitationMetadata(metaCont);
	}
	
	
}