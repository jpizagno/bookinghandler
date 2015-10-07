# -*- coding: utf-8 -*-

import re, math
from collections import Counter

WORD = re.compile(r'\w+')

def get_cosine_coupon(couponA, couponB):
    """
      going to compare:
            "GENRE_NAME","DISCOUNT_PRICE","PRICE_RATE",
            "USABLE_DATE_MON","USABLE_DATE_TUE","USABLE_DATE_WED","USABLE_DATE_THU",
            "USABLE_DATE_FRI","USABLE_DATE_SAT","USABLE_DATE_SUN","USABLE_DATE_HOLIDAY",
            "USABLE_DATE_BEFORE_HOLIDAY","ken_name","small_area_name"
    """
    # old: couponA = couponDictA[couponListA.COUPON_ID_hash]
    #old:  couponB = couponDictB[couponListB.COUPON_ID_hash]
    numerator = 0.0
    denominatorA = 0.0
    denominatorB = 0.0
    
    ##CAPSULE_TEXT
    #denominatorA += 1.0
    #denominatorB += 1.0
    #if couponA.CAPSULE_TEXT == couponB.CAPSULE_TEXT :
    #    numerator += 1
    # GENRE_NAME
    denominatorA += 1.0
    denominatorB += 1.0
    if couponA.GENRE_NAME == couponB.GENRE_NAME :
        numerator += 1
    # DISCOUNT_PRICE
    denominatorB += float(couponB.DISCOUNT_PRICE) * float(couponB.DISCOUNT_PRICE)
    denominatorA += float(couponA.DISCOUNT_PRICE) * float(couponA.DISCOUNT_PRICE)
    numerator += float(couponB.DISCOUNT_PRICE) * float(couponA.DISCOUNT_PRICE)
    # PRICE_RATE
    denominatorB += float(couponB.PRICE_RATE) * float(couponB.PRICE_RATE)
    denominatorA += float(couponA.PRICE_RATE) * float(couponA.PRICE_RATE)
    numerator += float(couponB.PRICE_RATE) * float(couponA.PRICE_RATE)
    ## change = (CATALOG_PRICE - DISCOUNT_PRICE) / CATALOG_PRICE
    #if int(couponA.CATALOG_PRICE) > 0 and int(couponB.CATALOG_PRICE) > 0:
    #    changeA = (float(couponA.CATALOG_PRICE) - float(couponA.DISCOUNT_PRICE)) / float(couponA.CATALOG_PRICE)
    #    changeB = (float(couponB.CATALOG_PRICE) - float(couponB.DISCOUNT_PRICE)) / float(couponB.CATALOG_PRICE)
    #    denominatorA += changeA * changeA
    #    denominatorB += changeB * changeB
    #    numerator += changeA * changeB
    
    ## USABLE_DATE_MON
    #denominatorB += float(couponB.USABLE_DATE_MON) * float(couponB.USABLE_DATE_MON)
    #denominatorA += float(couponA.USABLE_DATE_MON) * float(couponA.USABLE_DATE_MON)
    #numerator += float(couponA.USABLE_DATE_MON) * float(couponB.USABLE_DATE_MON)
    # # USABLE_DATE_TUE
    #denominatorB += float(couponB.USABLE_DATE_TUE) * float(couponB.USABLE_DATE_TUE)
    #denominatorA += float(couponA.USABLE_DATE_TUE) * float(couponA.USABLE_DATE_TUE)
    #numerator += float(couponA.USABLE_DATE_TUE) * float(couponB.USABLE_DATE_TUE)
    ## USABLE_DATE_WED
    #denominatorB += float(couponB.USABLE_DATE_WED) * float(couponB.USABLE_DATE_WED)
    #denominatorA += float(couponA.USABLE_DATE_WED) * float(couponA.USABLE_DATE_TUE)
    #numerator += float(couponA.USABLE_DATE_WED) * float(couponB.USABLE_DATE_WED)
    ## USABLE_DATE_THU
    #denominatorB += float(couponB.USABLE_DATE_THU) * float(couponB.USABLE_DATE_THU)
    #denominatorA += float(couponA.USABLE_DATE_THU) * float(couponA.USABLE_DATE_THU)
    #numerator += float(couponA.USABLE_DATE_THU) * float(couponB.USABLE_DATE_THU)
    ## USABLE_DATE_FRI
    #denominatorB += float(couponB.USABLE_DATE_FRI) * float(couponB.USABLE_DATE_FRI)
    #denominatorA += float(couponA.USABLE_DATE_FRI) * float(couponA.USABLE_DATE_FRI)
    #numerator += float(couponA.USABLE_DATE_FRI) * float(couponB.USABLE_DATE_FRI)
    # USABLE_DATE_SAT
    denominatorB += float(couponB.USABLE_DATE_SAT) * float(couponB.USABLE_DATE_SAT)
    denominatorA += float(couponA.USABLE_DATE_SAT) * float(couponA.USABLE_DATE_SAT)
    numerator += float(couponA.USABLE_DATE_SAT) * float(couponB.USABLE_DATE_SAT)
    ## USABLE_DATE_SUN
    #denominatorB += float(couponB.USABLE_DATE_SUN) * float(couponB.USABLE_DATE_SUN)
    #denominatorA += float(couponA.USABLE_DATE_SUN) * float(couponA.USABLE_DATE_SUN)
    #numerator += float(couponA.USABLE_DATE_SUN) * float(couponB.USABLE_DATE_SUN)
    ## USABLE_DATE_HOLIDAY
    #denominatorB += float(couponB.USABLE_DATE_HOLIDAY) * float(couponB.USABLE_DATE_HOLIDAY)
    #denominatorA += float(couponA.USABLE_DATE_HOLIDAY) * float(couponA.USABLE_DATE_HOLIDAY)
    #numerator += float(couponA.USABLE_DATE_HOLIDAY) * float(couponB.USABLE_DATE_HOLIDAY)
    ## USABLE_DATE_BEFORE_HOLIDAY
    #denominatorB += float(couponB.USABLE_DATE_BEFORE_HOLIDAY) * float(couponB.USABLE_DATE_BEFORE_HOLIDAY)
    #denominatorA += float(couponA.USABLE_DATE_BEFORE_HOLIDAY) * float(couponA.USABLE_DATE_BEFORE_HOLIDAY)
    #numerator += float(couponA.USABLE_DATE_BEFORE_HOLIDAY) * float(couponB.USABLE_DATE_BEFORE_HOLIDAY)
    ## ken_name
    #denominatorB += 1.0
    #denominatorA += 1.0
    #if couponA.ken_name == couponB.ken_name:
    #    numerator += 1
    ## small_area_name
    #denominatorB += 1.0
    #denominatorA += 1.0
    #if couponA.small_area_name == couponB.small_area_name:
    #    numerator += 1
    ## large_area_name
    #denominatorB += 1.0
    #denominatorA += 1.0
    #if couponA.large_area_name == couponB.large_area_name:
    #    numerator += 1
    
    cosine = numerator / (math.sqrt(denominatorA) * math.sqrt(denominatorB))
    return cosine
       

def get_cosine_example(vec1, vec2):
    intersection = set(vec1.keys()) & set(vec2.keys())
    numerator = sum([vec1[x] * vec2[x] for x in intersection])

    sum1 = sum([vec1[x]**2 for x in vec1.keys()])
    sum2 = sum([vec2[x]**2 for x in vec2.keys()])
    denominator = math.sqrt(sum1) * math.sqrt(sum2)

    if not denominator:
        return 0.0
    else:
        return float(numerator) / denominator

def text_to_vector(text):
    words = WORD.findall(text)
    return Counter(words)

def example_run():
    text1 = 'This is a foo bar sentence .'
    print 'text1: ',text1
    text2 = 'This sentence is similar to a foo bar sentence .'
    print 'text2: ',text2
    vector1 = text_to_vector(text1)
    vector2 = text_to_vector(text2)
    cosine = get_cosine_example(vector1, vector2)
    print 'Cosine Similarity between text1 * text2:', cosine
    print " "
    text1 = 'This is a foo bar sentence .'
    print 'text1: ',text1
    text2 = 'This is a foo bar sentence .'
    print 'text2: ',text2
    vector1 = text_to_vector(text1)
    vector2 = text_to_vector(text2)
    cosine = get_cosine_example(vector1, vector2)
    print 'Cosine Similarity between text1 * text2:', cosine
    print " "
    text1 = 'This is a foo bar sentence .'
    print 'text1: ',text1
    text2 = 'NOT similar'
    print 'text2: ',text2
    vector1 = text_to_vector(text1)
    vector2 = text_to_vector(text2)
    cosine = get_cosine_example(vector1, vector2)
    print 'Cosine Similarity between text1 * text2:', cosine
    print " "
    print " example run"
    text1 = "0 1 2 3 4 5 6 7 8 9"
    vector1 = text_to_vector(text1)
    text2 = ""
    for i in range(0,10):
        text2 += str(i) + " "
        vector2 = text_to_vector(text2) 
        print 'text1: ',text1
        print 'text2: ',text2
        cosine = get_cosine_example(vector1, vector2)
        print 'Cosine Similarity between text1 * text2:', cosine