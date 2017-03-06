import argparse
import os

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('-e', '--executable', required=True, help='executable file')
parser.add_argument('-d', '--nr_descriptors', required=True, type=int, help='number of descriptors')
parser.add_argument('-m', '--max_cores', required=True, type=int, help='max number of cores')
parser.add_argument('-c', '--client_threads', required=True, type=int, help='number of concurrent clients')
parser.add_argument('-t', '--times', required=False, type=int, default=5, help='number of times each test executes')
args = parser.parse_args()

assert args.nr_descriptors > 0
assert args.max_cores >= 1
assert args.client_threads >= 1

def main(args):
  impls = ['lowest', 'lowest -l', 'lowest -l -f', 'any', 'any -l', 'any -l -f']
  for impl in impls:
    for c in xrange(1,args.max_cores+1):
      for t in xrange(args.times):
        cmd = 'java -jar %s -i %s -s %s -t %s -c %s' % (args.executable, impl, args.nr_descriptors, args.client_threads, c)
        print 'executing %s' % cmd
        os.system(cmd)

if __name__ == '__main__':
  main(args)  