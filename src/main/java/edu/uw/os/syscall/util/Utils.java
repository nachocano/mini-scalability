package edu.uw.os.syscall.util;

import java.util.Random;

public class Utils {

  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";

  private Utils() {
    // avoid construction
  }

  public static String generateRandom(final Random rnd, final int size) {
    final StringBuilder res = new StringBuilder();
    for (int i = 0; i < size; i++) {
      final int randIndex = rnd.nextInt(ALPHABET.length());
      res.append(ALPHABET.charAt(randIndex));
    }
    return res.toString();
  }
}
