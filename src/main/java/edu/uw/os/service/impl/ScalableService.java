package edu.uw.os.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.os.service.MiniScalabilityService;

// uses concurrent collection
public class ScalableService implements MiniScalabilityService {

  private static final Logger LOG = LoggerFactory.getLogger(NonScalableService.class);

  public final Map<String, Long> usersLastLogin;

  public ScalableService() {
    usersLastLogin = new ConcurrentHashMap<>(200);
  }

  @Override
  public void processUserRequest(final String u) {
    // update user last login
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
