//package com.wiam.lms.service.reporting;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//
//
//
//@Service
//public class PdfService {
//
//    private static final String PDF_RESOURCES = "/";
//    private SpringTemplateEngine templateEngine;
//
//    @Value("${baseUrl}")
//    private String baseUrl;
//
//    @Value("${poFooterImage}")
//    private String poFooterImage;
//
//    @Value("${poHeaderImage}")
//    private String poHeaderImage;
//
//    @Autowired
//    private InventoryMapper inventoryMapper;
//
//    @Autowired
//    public PdfService(InventoryService inventoryService, SpringTemplateEngine templateEngine) {
//        this.inventoryService = inventoryService;
//        this.templateEngine = templateEngine;
//    }
//
//    public File generatePdf(PoPdfDetail details ) throws IOException, DocumentException {
//        Context context = getContext(details, "poPdfDetail");
//        String html = loadAndFillTemplate(context, "po/pdf_orders");
//        return renderPdf(html, details.getHeaderPdfDetail().getDestination());
//    }
//
//
//    private File renderPdf(String html, String outlet) throws IOException, DocumentException {
//        File file = new File("orders_"+outlet+".pdf");
//        OutputStream outputStream = new FileOutputStream(file);
//        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
//        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
//        renderer.layout();
//        renderer.createPDF(outputStream);
//        outputStream.close();
//        //file.deleteOnExit();
//        return file;
//    }
//
//    /**
//     * @param details
//     * @param variableName
//     * @return
//     */
//    public Context getContext(PoPdfDetail details, String variableName) {
//        Context context = new Context();
//        context.setVariable(variableName, details);
//        context.setVariable("baseUrl", baseUrl);
//        context.setVariable("footerImage", poFooterImage);
//        context.setVariable("headerImage", poHeaderImage);
//        return context;
//    }
//
//    /**
//     * @param objectValue
//     * @param objectName
//     * @return
//     */
//    public Context getContext(Object objectValue, String objectName) {
//        Context context = new Context();
//        context.setVariable(objectName, objectValue);
//        context.setVariable("baseUrl", baseUrl);
//        context.setVariable("footerImage", poFooterImage);
//        context.setVariable("headerImage", poHeaderImage);
//        return context;
//    }
//
//    public String loadAndFillTemplate(Context context, String templateName) {
//        return templateEngine.process(templateName, context);
//    }
//
//}
