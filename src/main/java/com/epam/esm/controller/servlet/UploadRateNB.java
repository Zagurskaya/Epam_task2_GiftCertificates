package com.epam.esm.controller.servlet;

import com.epam.esm.controller.parser.impl.RateNBParser;
import com.epam.esm.model.service.impl.RateNBServiceImpl;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.PathPage;
import com.epam.esm.entity.RateNB;
import com.epam.esm.model.service.RateNBService;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Upload rateNB to BD
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5
)
public class UploadRateNB extends HttpServlet {
    private final static String FILE_NAME = "ratenb.txt";
    private RateNBService rateNBService = new RateNBServiceImpl();

    public UploadRateNB() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<RateNB> rateNBList = new ArrayList<>();
                ServletFileUpload fileUpload = new ServletFileUpload();
                FileItemIterator items = fileUpload.getItemIterator(request);

                while (items.hasNext()) {
                    FileItemStream item = items.next();
                    if (!FILE_NAME.equals(item.getName())) {
                        request.setAttribute(AttributeName.MESSAGE, "Incorrect file name");
                    } else {
                        if (!item.isFormField()) {
                            InputStream is = item.openStream();
                            String value = Streams.asString(is);
                            RateNBParser rateNBParser = new RateNBParser();
                            List<String> rows = rateNBParser.parsTextToRowList(value);
                            rows.stream().forEach(s -> rateNBList.add(rateNBParser.parse(s)));
                        }
                        rateNBService.create(rateNBList);
                        request.setAttribute(AttributeName.MESSAGE, "File Uploaded Successfully");
                    }
                }
            } catch (Exception ex) {
                request.setAttribute(AttributeName.ERROR, "100 File Upload Failed due to " + ex);
            }
        } else {
            request.setAttribute(AttributeName.ERROR, "100 No File found");
        }
        request.getRequestDispatcher(PathPage.PATH_CONTROLLER_UPLOAD_RATE_NB).forward(request, response);
    }
}