from __future__ import division
import argparse
from collections import defaultdict
import matplotlib.pyplot as plt

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('-i', '--input_file', required=True, help='grepped result file (grep result <output>)')
parser.add_argument('-o', '--output_file', required=True, help='output figure file')
args = parser.parse_args()

def main(args):
  threads_s = set()
  run_s = set()
  results = defaultdict(list)
  with open(args.input_file, 'r') as f:
    for line in f.read().splitlines():
      elements = line.split(',')
      run = elements[0].split(':')[1]
      threads = int(elements[1].split(':')[1])
      ops = float(elements[2].split(':')[1])
      results[(run, threads)].append(ops)
      run_s.add(run)
      threads_s.add(threads)
  # get the averages of the runs
  avgs = {}
  for key in results.keys():
    avgs[key] = sum(results[key])/len(results[key])
    #print key, avgs[key]

  proc_results = []
  for run in run_s:
    tmp = []
    for threads in threads_s:
      tmp.append(avgs[(run,threads)])
    proc_results.append(tmp)

  x = list(threads_s)
  fig = plt.figure()
  fig.suptitle("Mini-Scalability Results", fontsize=13, fontweight='bold')
  for i in xrange(len(run_s)):
    plt.plot(x, proc_results[i])
  plt.xlabel("cores")
  plt.ylabel("Avg. OPS")
  plt.legend(run_s)
  plt.xticks(x)
  #plt.show()  
  fig.savefig(args.output_file)

if __name__ == '__main__':
  main(args)  