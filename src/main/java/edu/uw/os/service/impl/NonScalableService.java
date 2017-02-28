package edu.uw.os.service.impl;

import java.util.HashSet;
import java.util.Set;

import edu.uw.os.service.MiniScalabilityService;

// synchronizes on this object
public class NonScalableService implements MiniScalabilityService {

  public final Set<String> users;
  public final Set<String> queries;

  public NonScalableService() {
    users = new HashSet<>();
    queries = new HashSet<>();
  }

  public synchronized void addUser(final String u) {
    users.add(u);
  }

  public synchronized void addQuery(final String q) {
    queries.add(q);
  }

  public synchronized void removeUser(final String u) {
    users.remove(u);
  }

  public synchronized void removeQuery(final String q) {
    queries.remove(q);
  }

}
