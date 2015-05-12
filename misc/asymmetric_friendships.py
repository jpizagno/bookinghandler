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
    friend2 = record[1]
    friend1 = record[0]
    key1 = friend1+","+friend2
    key2 = friend2+","+friend1
    mr.emit_intermediate(key1 , 1)
    mr.emit_intermediate(key2 , -1)

def reducer(key, list_of_values):
    # key: word
    # value: list of occurrence counts
	
    sum = 0
    for value in list_of_values:
        sum += value
    #print key,list_of_values,sum
    if sum == -1:
        mr.emit( ( key.split(",")[0] , key.split(",")[1]  ))
    if sum  == 1:
        mr.emit( ( key.split(",")[0] , key.split(",")[1]  ))
        
    

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
