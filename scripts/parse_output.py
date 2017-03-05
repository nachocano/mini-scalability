from __future__ import division
import argparse
from collections import defaultdict

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('-i', '--input_file', required=True, help='grepped result file (grep result <output>)')
args = parser.parse_args()

def print_relative(run_s, avgs, threads_s, start=0):
  for i, run in enumerate(run_s):
    relative = avgs[(run, threads_s[-1])]/avgs[(run, threads_s[0])]
    print '%s,%s,%s' % (i+start,run,relative)


def main(args):
  threads_s = set()
  run_lowest_s = set()
  run_any_s = set()
  results = defaultdict(list)
  with open(args.input_file, 'r') as f:
    for line in f.read().splitlines():
      elements = line.split(',')
      run = elements[0].split(':')[1]
      threads = int(elements[1].split(':')[1])
      ops = float(elements[2].split(':')[1])
      results[(run, threads)].append(ops)
      threads_s.add(threads)
      if run.startswith('lowest'):
        run_lowest_s.add(run)
      else:
        run_any_s.add(run)      
  # get the averages of the runs
  avgs = {}
  for key in results.keys():
    avgs[key] = sum(results[key])/len(results[key])

  threads_s = sorted(threads_s)
  run_lowest_s = sorted(run_lowest_s)
  run_any_s = sorted(run_any_s)

  # we will just plot the throughput max cores / throughput 1 core

  # header
  print 'index,name,relative_throughput'
  print_relative(run_lowest_s, avgs, threads_s)
  print_relative(run_any_s, avgs, threads_s, start=len(run_lowest_s))


if __name__ == '__main__':
  main(args)  