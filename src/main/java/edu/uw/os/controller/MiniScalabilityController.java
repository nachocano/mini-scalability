package edu.uw.os.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import edu.uw.os.service.MiniScalabilityService;

@Controller
public class MiniScalabilityController {

  private static final Logger LOG = LoggerFactory.getLogger(MiniScalabilityController.class);

	@Autowired
  private MiniScalabilityService service;

  @RequestMapping(value = "/user/{userid}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody
      void processUserRequest(@PathVariable(value = "userid") final String userId) throws IOException {
    service.processUserRequest(userId);
	}

  @ExceptionHandler({ IllegalArgumentException.class, IOException.class,
			NullPointerException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
	public void handleException(final Exception ex,
			final HttpServletResponse response) {
		LOG.error("Handling exception {}", ex.getMessage());
	}

}
