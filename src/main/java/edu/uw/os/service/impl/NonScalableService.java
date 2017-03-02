package edu.uw.os.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.os.service.MiniScalabilityService;

// synchronizes on this object
public class NonScalableService implements MiniScalabilityService {

  private static final Logger LOG = LoggerFactory.getLogger(NonScalableService.class);

  public final Map<String, Long> usersLastLogin;

  public NonScalableService() {
    usersLastLogin = new HashMap<>(200);
  }

  @Override
  public synchronized void processUserRequest(final String u) {
    // update the last login time
    final long time = System.currentTimeMillis();
    LOG.debug("{}:{}", u, time);
    usersLastLogin.put(u, time);
        try {
          // do some processing
          Thread.sleep(10);
        } catch (final InterruptedException e) {
          LOG.error("Interrupted while updating user last login");
          throw new RuntimeException();
        }
  }
}
