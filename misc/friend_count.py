import MapReduce
import sys

"""
produces inverted index
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line

def mapper(record):
    # key: document identifier
    # value: document contents
    key = record[0]
    friend = record[1]
    mr.emit_intermediate(key, friend)

def reducer(key, list_of_values):
    # key: word
    # value: list of occurrence counts
    total = 0
    for friend in list_of_values:
        total += 1
    mr.emit((key, total))

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
