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
        scores[term.decode('utf-8')] = int(score)  # Convert the score to an integer.
    afinnfile.close()

    import pdb
    #pdb.set_trace()
    
    line_sentiment_dict = {}  # key=line value=sentiment
    
    word_dict = {} # key=word  value=list of sentiments
    
    # read second file , and assign score to each line
    for line in tweet_file:
        line_processed_dict = json.loads(line)
        #print line_processed_dict
        list_sentiments_here = []
        if "text" in line_processed_dict.keys():
            result = line_processed_dict["text"]
            words = result.split()
            for term in scores.keys():
                for word in words:
                    if word == term:
                        list_sentiments_here.append(scores[term])
                        
            if len(list_sentiments_here) > 0:
                for word in words:
                    if word not in scores.keys():
                        # found word that is not already in sentiments
                        if word not in word_dict.keys():
                            # new so add first term
                            word_dict[word] = [list_sentiments_here[0]]
                        else:
                            for i in range(1,len(list_sentiments_here)):
                                word_dict[word].append(list_sentiments_here[i])
                                    
    tweet_file.close()
    
    #word_dict now has term(=key) and a list of sentiments (=values)
    for word in word_dict.keys():
        #num_pos = 0
        #num_neg = 0
        #sentiment_list = word_dict[word]
        #for sentiment in sentiment_list:
        #    if sentiment < 0:
        #        num_neg += 1
        #    if sentiment > 0:
        #        num_pos += 1       
        #print word, float(num_pos) / float(num_neg)
        #
        sum = 0
        sentiment_list = word_dict[word]
        for sentiment in sentiment_list:
            sum += float(sentiment)
        print word,sum / len(sentiment_list)
    
    

if __name__ == '__main__':
    main()
