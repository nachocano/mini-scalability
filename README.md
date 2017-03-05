
Mini Scalability 
=================

Minimal set of code to showcase some ideas from [The Scalable Commutativity Rule](https://pdos.csail.mit.edu/papers/commutativity:tocs.pdf) for [CSE551](https://courses.cs.washington.edu/courses/cse551/17wi/).

Requirements
------------
Java >= 7

Python >= 2.6

Maven >= 3

Matplotlib >= 1.5.3 (only if you want to generate a plot)

Description
-----------

TODO

Instructions
------------

1. Clone repo into folder REPO_HOME.

2. Inside REPO_HOME, compile java sources using ```mvn clean package assembly:single```.

3. Execute the python runner script using the following command
    ```python -u REPO_HOME/runner.py -e REPO_HOME/target/mini-scalability-jar-with-dependencies.jar -d DESCRIPTORS -m MAX_THREADS -t TIMES > output.txt```

  where:
  * DESCRIPTORS: number of file descriptors that will be simulated in the process (e.g., 1000000)
  * MAX_THREADS: maximum number of threads to use for each configuration. The tests will run with [1,MAX_THREADS] interval.
  * TIMES: number of executions of each configuration, in order to average the results later.

4. Grep the output file: ```grep result output.txt > output.csv```

5. [Optional] Plot the results using ```python plotter.py -i output.csv -o fig.png```




