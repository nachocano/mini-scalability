set datafile separator ","
set title ""
set xlabel "Implementation"
set ylabel "OPS MAX / OPS 1"
set size 0.65, 0.5
#set key bottom right
set terminal postscript eps color
set output "output.eps"
#set key box lw 2 spacing 2.5 width 1.5
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set yrange [0:]
set xrange [-1:6]
set boxwidth 0.6
set xtics nomirror rotate by -45
set style fill solid
plot input using 1:3:xtic(2) t '' lc rgb "black" with boxes
