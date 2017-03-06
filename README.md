
Mini Scalability 
=================

Minimal set of code to showcase some ideas from [The Scalable Commutativity Rule](https://pdos.csail.mit.edu/papers/commutativity:tocs.pdf) and [An Analysis of Linux Scalability to Many Cores](https://pdos.csail.mit.edu/papers/linux:osdi10.pdf) for [CSE551](https://courses.cs.washington.edu/courses/cse551/17wi/).

Requirements
------------
* Java >= 7
* Python >= 2.6
* Maven >= 3
* gnuplot >= 5.0 (only needed if you want to generate a plot)

Description
-----------

We implemented *dummy* versions of the ```creat``` syscall in user space. The implementations always return a fd (i.e., we assumed the method is invoked with *new* file names) or -1 if there are no fds available for the process. We did not focus on the *real* logic, but rather on showcasing how these *dummy* versions can scale up. We used some of the techniques highlighted in the above papers. In particular, we provide 6 implementations:

* Lowest FD: the interface returns the *lowest* file descriptor available (non-commutative).
  * Coarse Grained Lock: holds a coarse-grained lock to update the data structures.
  * Fine Grained Locks: holds fine-grained locks to update the data structures.
  * Lock Free: uses lock-free collections, e.g., ```ConcurrentHashMap``` and ```ConcurrentLinkedQueue```.
* Any FD: the interface returns *any* file descriptor available (commutative). We organize the data structures using *per-core* partitions of the fds space.
  * Coarse Grained Locks: holds a coarse-grained lock to update the data structures.
  * Fine Grained Locks: holds fine-grained locks to update the data structures.
  * Lock Free: uses lock-free collections, e.g., ```ConcurrentHashMap``` and ```ConcurrentLinkedQueue```.

Instructions
------------

1. Clone repo into folder REPO_HOME.

2. Inside REPO_HOME, compile java sources using ```mvn clean package assembly:single```.

3. Execute the python runner script using the following command
    ```python -u REPO_HOME/scripts/runner.py -e REPO_HOME/target/mini-scalability-jar-with-dependencies.jar -d DESCRIPTORS -m MAX_THREADS -t TIMES > REPO_HOME/output.txt```

  where:
  * DESCRIPTORS: number of file descriptors that will be simulated in the process (e.g., 1000000)
  * MAX_THREADS: maximum number of threads. There will be a total of MAX_THREADS tests for each configuration, i.e., the tests will run using the following thread interval [1,MAX_THREADS].
  * TIMES: number of executions of each configuration, in order to average the results later.

4. [Optional] Generate the file for plotting: ```grep result REPO_HOME/output.txt > REPO_HOME/output.csv; python scripts/parse_output.py -i REPO_HOME/output.csv > REPO_HOME/output.dat```

5. [Optional] Plot the results using ```gnuplot -e "input='REPO_HOME/output.dat'" REPO_HOME/scripts/plotter.plt```. The plot is saved in ```REPO_HOME/output.eps```.

