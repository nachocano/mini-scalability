package edu.uw.os.syscall;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.Validate;
import edu.uw.os.syscall.fd.FD;
import edu.uw.os.syscall.fd.impl.AnyFDImpl;
import edu.uw.os.syscall.fd.impl.LowestFDImpl;
import edu.uw.os.syscall.util.Utils;


public class FDDriver {

  private static enum FDImplEnum {
    LOWEST("lowest")
    {
      @Override
      public FD createImpl(final int size, final int threads) {
        // don't care the number of threads here
        return new LowestFDImpl(size);
      }
    },
    ANY("any")
    {
      @Override
      public FD createImpl(final int size, final int threads) {
        return new AnyFDImpl(size, threads);
      }
    };

    private final String impl;

    FDImplEnum(final String impl) {
      this.impl = impl;
    }

    abstract FD createImpl(int size, int threads);

    public static FDImplEnum fromString(final String impl) {
      for (final FDImplEnum type : FDImplEnum.values()) {
        if (type.impl.equalsIgnoreCase(impl)) {
            return type;
        }
      }
      throw new IllegalArgumentException(String.format("invalid impl %s", impl));
    }

    @Override
    public String toString() {
      return impl;
    }
 }

  private final int size;
  private final int threads;
  private final ExecutorService executor;
  private final FDImplEnum fdImplEnum;
  private final FD fd;

  public FDDriver(final String[] args) throws Exception {
    final Options options = new Options();
    options.addOption("s", true, "size");
    options.addOption("i", true, "impl");
    options.addOption("t", true, "threads");
    final CommandLineParser parser = new DefaultParser();
    try {
      final CommandLine line = parser.parse(options, args);
      size = Integer.valueOf(line.getOptionValue("s"));
      Validate.isTrue(size > 0);
      final String implStr = line.getOptionValue("i");
      fdImplEnum = FDImplEnum.fromString(implStr);
      threads = Integer.valueOf(line.getOptionValue("t"));
      Validate.isTrue(threads > 0);
      executor = Executors.newFixedThreadPool(threads);
      fd = fdImplEnum.createImpl(size, threads);
    } catch (final Exception e) {
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("FDDriver", options);
      throw e;
    }
  }

  public void run() {
    final int processors = Runtime.getRuntime().availableProcessors();
    if (threads > processors) {
      System.out.println(String.format("WARNING: more threads than processors"));
    }
    System.out.println(String.format("threads %s, processors %s", threads, processors));
    final long start = System.nanoTime();
    final List<Future<Integer>> futures = new LinkedList<>();
    final Random rnd = new Random(17);
    for (int i = 0; i < size; i++) {
      // 10-char file names, assumming that we don't have repeated files
      final String file = Utils.generateRandom(rnd, 10);
      final Future<Integer> future = executor.submit(new FDTask(fd, file));
      futures.add(future);
    }
    final List<Integer> descriptors = new LinkedList<Integer>();
    try {

      for (final Future<Integer> future : futures) {
        final Integer descriptor = future.get();
        descriptors.add(descriptor);
      }
    } catch (final InterruptedException e) {
      System.err.println(String.format("interrupted exception: %s", e.getMessage()));
    } catch (final ExecutionException e) {
      System.err.println(String.format("execution exception: %s", e.getMessage()));
    } finally {
      executor.shutdown();
    }
    //System.out.println(Arrays.toString(descriptors.toArray()));
    final long end = System.nanoTime();
    final double secs = (double)(end - start) / 1000000;
    System.out.println(String.format("total time %s", secs));
    final double opsPerSec = size/secs;
    System.out.println(String.format("%s|threads:%s,OPS:%.2f", fdImplEnum.toString(), threads, opsPerSec));
  }

  private static final class FDTask implements Callable<Integer> {

    private final FD fd;
    private final String file;

    FDTask(final FD fd, final String file) {
      this.fd = fd;
      this.file = file;
    }

    @Override
    public Integer call() throws Exception {
      return fd.creat(file);
    }
  }

  public static void main(final String[] args) throws Exception {
    final FDDriver driver = new FDDriver(args);
    driver.run();
  }


}
