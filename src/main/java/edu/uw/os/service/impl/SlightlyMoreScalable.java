package edu.uw.os.service.impl;

import java.util.HashSet;
import java.util.Set;

import edu.uw.os.service.MiniScalabilityService;

// does lock splitting, lock for add user and add query
public class SlightlyMoreScalable implements MiniScalabilityService {

  public final Set<String> users;
  public final Set<String> queries;

  public SlightlyMoreScalable() {
    users = new HashSet<>();
    queries = new HashSet<>();
  }

  public void addUser(final String u) {
    synchronized (users) {
      users.add(u);
    }
  }

  public synchronized void addQuery(final String q) {
    synchronized (queries) {
      queries.add(q);
    }
  }

  public synchronized void removeUser(final String u) {
    synchronized (users) {
      users.remove(u);
    }
  }

  public synchronized void removeQuery(final String q) {
    synchronized (queries) {
      queries.remove(q);
    }
  }

}
