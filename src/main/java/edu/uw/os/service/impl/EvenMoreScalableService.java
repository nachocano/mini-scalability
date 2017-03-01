package edu.uw.os.service.impl;

import edu.uw.os.service.MiniScalabilityService;

import java.util.concurrent.ConcurrentHashMap;

// synchronizes on this object
public class EvenMoreScalableService implements MiniScalabilityService {

  public final ConcurrentHashMap<String, String> users; // This is not a set, but a concurrent set can be derived.
  public final ConcurrentHashMap<String, String> queries;

  public EvenMoreScalableService() {
    users = new ConcurrentHashMap<>();
    queries = new ConcurrentHashMap<>();
  }

  @Override
  public void addUser(final String u) {
    users.put(u,u);
  }

  @Override
  public void addQuery(final String q) {
    queries.put(q,q);
  }

  @Override
  public void removeUser(final String u) {
    users.remove(u);
  }

  @Override
  public void removeQuery(final String q) {
    queries.remove(q);
  }

}
