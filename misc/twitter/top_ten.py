import sys
import json
import pdb
import math
import operator

def main():
    tweet_file = open(sys.argv[1])
    
    hash_tags_dict = {}
    
    for line in tweet_file:
        line_processed_dict = json.loads(line)

 
        if "entities" in line_processed_dict.keys():
            result = line_processed_dict["entities"]
            if result['hashtags'] is not None and len(result['hashtags'])>0:
                for hash_tag_listitem in result['hashtags']:
                    hash_tag_here = hash_tag_listitem['text']
                    if hash_tag_here in hash_tags_dict.keys():
                        hash_tags_dict[hash_tag_here] += 1
                    else:
                        hash_tags_dict[hash_tag_here] = 1
                        
    # they must be ordered....
    sorted_x = sorted(hash_tags_dict.iteritems(), key=operator.itemgetter(1))    
    for i in range(len(sorted_x)-1,len(sorted_x)-11,-1):
        print sorted_x[i][0],sorted_x[i][1]
    
if __name__ == '__main__':
    main()

