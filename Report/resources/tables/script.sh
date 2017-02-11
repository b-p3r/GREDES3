
file=$1
out=$2

cat top.txt > $2
cat $file | awk '{ print $1" & "$2" & "$3"\\\\"}' >> $out
cat bottom.txt >> $2
