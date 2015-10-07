import sys
import json

def hw():
    print 'Hello, world!'

def lines(fp):
    print str(len(fp.readlines()))

def main():
    sent_file = open(sys.argv[1])
    tweet_file = open(sys.argv[2])
    #hw()
    #lines(sent_file)
    #lines(tweet_file)
    
    # first create dictionary with scores
    afinnfile = open(sys.argv[1]) #open("AFINN-111.txt")
    scores = {} # initialize an empty dictionary
    for line in afinnfile:
        term, score  = line.split("\t")  # The file is tab-delimited. "\t" means "tab character"
        scores[term] = int(score)  # Convert the score to an integer.
    afinnfile.close()

    #import pdb
    #pdb.set_trace()
    
    line_sentiment_dict = {}  # key=line value=sentiment
    
    # read second file , and assign score to each line
    for line in tweet_file:
        line_processed_dict = json.loads(line)
        #print line_processed_dict
        sum_here = 0
        for term in scores.keys():
            #print term
            #print term.decode('utf-8')
            term_decoded = term.decode('utf-8')
            for key in line_processed_dict.keys():
                #print key
                count = key.count(term_decoded)  #term) 
                thing = line_processed_dict[key]
                #print thing,type(thing)
                if type(thing) == type("") and thing is not None:
                    count = count + thing.count(term)
                
                sum_here += count * scores[term]
        print sum_here#,line_processed_dict

    tweet_file.close()
    
   

if __name__ == '__main__':
    main()
