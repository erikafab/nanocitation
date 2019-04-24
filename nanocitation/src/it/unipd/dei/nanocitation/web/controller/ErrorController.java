package it.unipd.dei.nanocitation.web.controller;
//package it.unipd.dei.nanop.web.controller;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.ErrorHandler;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class ErrorController
//{
//	
//	@RequestMapping("/error")
//	public String handleError(HttpServletRequest request)
//	{
//		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//		
//		if (status != null)
//		{
//			Integer statusCode = Integer.valueOf(status.toString());
//			
//			if (statusCode == HttpStatus.NOT_FOUND.value())
//			{
//				return "error";
//			}
//			else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
//			{
//				return "error";
//			}
//		}
//		return "error";
//	}
//	
//}