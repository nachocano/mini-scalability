import argparse
import os

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('-e', '--executable', required=True, help='executable file')
parser.add_argument('-d', '--nr_descriptors', required=True, type=int, help='number of descriptors')
parser.add_argument('-t', '--max_threads', required=True, type=int, help='max number of threads')
args = parser.parse_args()

assert args.nr_descriptors > 0
assert args.max_threads >= 1

def main(args):
  impls = ['lowest', 'lowest -l', 'any', 'any -l']
  for impl in impls:
    for t in xrange(1,args.max_threads+1):
      for k in xrange(5):
        cmd = 'java -jar %s -i %s -s %s -t %s' % (args.executable, impl, args.nr_descriptors, t)
        print 'executing %s' % cmd
        os.system(cmd)

if __name__ == '__main__':
  main(args)  