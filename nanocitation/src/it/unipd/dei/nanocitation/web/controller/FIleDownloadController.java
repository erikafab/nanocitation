package it.unipd.dei.nanocitation.web.controller;
//package it.unipd.dei.nanop.web.controller;
//
//@Controller
//@RequestMapping("/download")
//public class FileDownloadController
//{
//    @RequestMapping("/pdf/{fileName:.+}")
//    public void downloadPDFResource( HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     @PathVariable("fileName") String fileName)
//    {
//        //If user is not authorized - he should be thrown out from here itself
//         
//        //Authorized user will download the file
//        String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/");
//        Path file = Paths.get(dataDirectory, fileName);
//        if (Files.exists(file))
//        {
//            response.setContentType("application/pdf");
//            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
//            try
//            {
//                Files.copy(file, response.getOutputStream());
//                response.getOutputStream().flush();
//            }
//            catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//}