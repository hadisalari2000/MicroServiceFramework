package com.salari.framework.gatewayserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.salari.framework.common.handler.exception.ServiceException;
import com.salari.framework.common.model.audit.domain.BaseAuditItemRequest;
import com.salari.framework.common.model.audit.enums.RequestStatus;
import com.salari.framework.common.model.base.BaseDTO;
import com.salari.framework.common.utility.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Controller
@RefreshScope
public class ZuulErrorController extends AbstractErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ZuulErrorController.class);


    @Value("${zuul.error.path}")
    private String errorPath;
    private RestTemplate restTemplate;

    public ZuulErrorController(ErrorAttributes errorAttributes, RestTemplate restTemplate) {
        super(errorAttributes);
        this.restTemplate = restTemplate;
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @GetMapping(value = "/error")
    public ResponseEntity error(HttpServletRequest request) throws IOException {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NOT_FOUND) {
            throw ServiceException.builder()
                    .key("not_found_url")
                    .message(ApplicationProperties.getProperty("not_found_url"))
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        final Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");
        logger.error(exc.toString());

        BaseDTO baseDTO = BaseDTO
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .build();

        if (exc != null && exc.getCause().getMessage() != null) {
            baseDTO = new ObjectMapper().readValue(exc.getMessage().substring(4, exc.getMessage().length()), BaseDTO.class);
        }
        auditException();
        return new ResponseEntity<>(baseDTO, status);
    }

    @GetMapping(value = "/swagger", produces = "application/json;charset=UTF-8")
    public String swagger(@RequestParam String url) {
        return restTemplate.getForObject(url, String.class);
    }

    private void auditException() {
        Date datetime = new Date();
        BaseAuditItemRequest baseAuditItemRequest = new BaseAuditItemRequest(
                RequestContext.getCurrentContext().getZuulRequestHeaders().get("rrn"),
                datetime.getTime(),
                RequestStatus.FAILED
        );
        logger.debug(new Gson().toJson(baseAuditItemRequest));
    }

}
