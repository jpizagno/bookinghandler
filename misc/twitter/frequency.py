import sys
import json


def main():
    tweet_file = open(sys.argv[1])

    words_dict = {}
    
    sum_all = 0

    for line in tweet_file:
        line_processed_dict = json.loads(line)
        if "text" in line_processed_dict.keys():
            result = line_processed_dict["text"]
            words = result.split()
            
            sum_all += len(words)
            
            for word in words:
                if word not in words_dict.keys():
                    words_dict[word] = 1
                else:
                    words_dict[word] += 1
            
            
    for word in words_dict.keys():
        print word , float(words_dict[word]) / sum_all



if __name__ == '__main__':
    main()

