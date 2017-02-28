package edu.uw.os.service;

public interface MiniScalabilityService {

  void addUser(final String u);

  void addQuery(final String q);

  void removeUser(final String u);

  void removeQuery(final String q);

}
