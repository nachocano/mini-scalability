
Mini Scalability 
=================

Minimal set of code to showcase some ideas from [The Scalable Commutativity Rule](https://pdos.csail.mit.edu/papers/commutativity:tocs.pdf) and [An Analysis of Linux Scalability to Many Cores](https://pdos.csail.mit.edu/papers/linux:osdi10.pdf) for [CSE551](https://courses.cs.washington.edu/courses/cse551/17wi/).

Requirements
------------
Java >= 7

Python >= 2.6

Maven >= 3

Matplotlib >= 1.5.3 (only if you want to generate a plot)

Description
-----------

We implemented *simple* versions of the ```creat``` syscall in user space. We used some of the techniques highlighted in the above papers. In particular, we provide 6 implementations:

* Lowest FD: the interface returns the *lowest* file descriptor available (non-commutative).
  * Coarse Grained Lock: holds a coarse-grained lock to update the data structures.
  * Fine Grained Locks: holds fine-grained locks to update the data structures.
  * Lock Free: uses lock-free collections, e.g., ```ConcurrentHashMap``` and ```ConcurrentLinkedQueue```.
* Any FD: the interface returns *any* file descriptor available (commutative). We organize the data structures using *per-core* partitions of the FD space.
  * Coarse Grained Locks: holds a coarse-grained lock to update the data structures.
  * Fine Grained Locks: holds fine-grained locks to update the data structures.
  * Lock Free: uses lock-free collections, e.g., ```ConcurrentHashMap``` and ```ConcurrentLinkedQueue```.

Instructions
------------

1. Clone repo into folder REPO_HOME.

2. Inside REPO_HOME, compile java sources using ```mvn clean package assembly:single```.

3. Execute the python runner script using the following command
    ```python -u REPO_HOME/runner.py -e REPO_HOME/target/mini-scalability-jar-with-dependencies.jar -d DESCRIPTORS -m MAX_THREADS -t TIMES > REPO_HOME/output.txt```

  where:
  * DESCRIPTORS: number of file descriptors that will be simulated in the process (e.g., 1000000)
  * MAX_THREADS: maximum number of threads. There will be a total of MAX_THREADS tests for each configuration, i.e., the tests will run using the following thread interval [1,MAX_THREADS].
  * TIMES: number of executions of each configuration, in order to average the results later.

4. Grep the output file: ```grep result REPO_HOME/output.txt > REPO_HOME/output.csv```

5. [Optional] Plot the results using ```python REPO_HOME/plotter.py -i REPO_HOME/output.csv -o REPO_HOME/fig.png```
