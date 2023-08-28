package de.presti.ree6.backend.controller;

import de.presti.ree6.backend.Server;
import de.presti.ree6.backend.utils.data.container.api.BackendStatusResponse;
import de.presti.ree6.backend.utils.data.container.api.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the Error Controller.
 */
@RestController
public class ErrorControllerImpl implements ErrorController {

    /**
     * Error Attributes in the Application
     */
    private final ErrorAttributes errorAttributes;

    /**
     * The Application version;
     */
    private final String version;

    /**
     * Controller for the Error Controller
     *
     * @param errorAttributes Attributes that give more Info about the Error.
     */
    public ErrorControllerImpl(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
        String tempVersion = Server.getInstance().getClass().getPackage().getImplementationVersion();
        version = tempVersion == null ? "4.0.6" : tempVersion;
    }

    /**
     * Handle received Error.
     * @param request Request that was sent.
     * @return Generic Response with the Error Message.
     */
    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public BackendStatusResponse handleError(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);

        return new BackendStatusResponse(false, httpStatus.getReasonPhrase(), version);
    }

    /**
     * Return the HTTP Status.
     * @param request Request that was sent.
     * @return HTTP Status.
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
