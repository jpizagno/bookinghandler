import MapReduce
import sys

"""
makes a join.  
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line

def mapper(record):
    # key: order_id or JOIN column
    # value: everything 
    key = record[1]
    value_list = record
    mr.emit_intermediate(key, value_list)

def reducer(key, list_of_values):
    # key: order_id
    # value: list of occurrence counts

    order_here = list_of_values[0]

    for line_item in list_of_values[1:] :
        out_list = []
        for order_col_value in order_here:
            out_list.append(order_col_value)
        for lineitem_col_value in line_item :
            out_list.append(lineitem_col_value)
        mr.emit(out_list )
        


# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
