import MapReduce
import sys

"""
@ jim pizagno 06.06.2014
This code will take data from a sparse matrix "a" and multiply it times 
sparse matrix "b".
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line

def mapper(record):
    # key: column of final matrix
    # value: row/column of final matrix
    if record[0] == "a":
        i = record[1]
        j = record[2]
        value = record[3]
        for k in range(0,5):
            emit = str(i) + "," + str(k)
            mr.emit_intermediate( emit , ["a", j, value])
    else:
        j = record[1]
        k = record[2]
        value = record[3]
        for i in range(0,5):
            emit = str(i) + "," + str(k)
            mr.emit_intermediate( emit  ,  ["b", j , value])

def reducer(key, list_of_values):
    # key: column of final matrix
    # value: sum of values
    list_A  = [0,0,0,0,0]
    list_B = [0,0,0,0,0]
    for value in list_of_values:
        if value[0] == "a":
            list_A[value[1]] = value[2]
        if value[0] == "b":
            list_B[value[1]] = value[2]

    sum = 0
    for j in range(0,5):
        sum += list_A[j] * list_B[j]
    i = key.split(",")[0]
    j = key.split(",")[1]
    mr.emit(( int(i) , int(j) , sum))

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
