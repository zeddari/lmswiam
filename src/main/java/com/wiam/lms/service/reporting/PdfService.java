package com.wiam.lms.service.reporting;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.custom.projection.dto.PeriodicReportPdfDetail;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.service.reporting.request.PeriodicReportPdfRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PdfService {

    private static final String PDF_RESOURCES = "/";
    private SpringTemplateEngine templateEngine;

    @Autowired
    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setPrefix("templates/");
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setTemplateMode("HTML");
        pdfTemplateResolver.setCharacterEncoding("UTF-8");
        pdfTemplateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(pdfTemplateResolver);
        return templateEngine;
    }

    /**
     *
     * @param details
     * @param pdfRequest
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public File generatePdf(List<PeriodicReportPdfDetailInterface> details, PeriodicReportPdfRequest pdfRequest)
        throws IOException, DocumentException {
        Context context = getContext(details, "pdfDetails");
        String html = loadAndFillTemplate(context, "report/" + pdfRequest.getTemplateName());
        return renderPdf(html, pdfRequest.getFileName());
    }

    /**
     *
     * @param html
     * @param name
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    private File renderPdf(String html, String name) throws IOException, DocumentException {
        File file = new File(name + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer((20f * 4f) / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        //file.deleteOnExit();
        return file;
    }

    /**
     * @param details
     * @param variableName
     * @return
     */
    public Context getContext(PeriodicReportPdfDetail details, String variableName) {
        Context context = new Context();
        context.setVariable(variableName, details);

        return context;
    }

    /**
     * @param objectValue
     * @param objectName
     * @return
     */
    public Context getContext(Object objectValue, String objectName) {
        Context context = new Context();
        context.setVariable(objectName, objectValue);
        return context;
    }

    public String loadAndFillTemplate(Context context, String templateName) {
        TemplateEngine templateEngineLocal = createTemplateEngine();
        return templateEngineLocal.process(templateName, context);
    }
}
