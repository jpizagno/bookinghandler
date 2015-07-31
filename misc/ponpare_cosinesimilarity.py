# -*- coding: utf-8 -*-

from cosine_similarity import get_cosine_coupon
from coupon_model import Impression_Detail,Coupon,User
import pdb
from csv import DictReader
import io
import operator
import datetime
import time
import random

def example_run():
    example_run()

def printTest():
    """
    print a test
    call with:  printTest(impressions_detail_list, coupons_dict)
    """
    file = io.open("delme", 'w', encoding='utf8')
    impressionFromList = impressions_detail_list[1]
    for impressionFromListTest in impressions_detail_list:
        cosine = get_cosine_coupon(impressionFromList, impressionFromListTest, coupons_dict)
        outline = str(cosine)+"  "+impressionFromList.getPrintLine()+"|"+coupons_dict[impressionFromList.COUPON_ID_hash].getPrintline()
        file.write(outline+"\n")
        outline = "                "+impressionFromListTest.getPrintLine()+"|"+coupons_dict[impressionFromListTest.COUPON_ID_hash].getPrintline()
        file.write(outline+"\n\n")
    file.close()

if __name__ == "__main__":

    impressions_detail_list = []
    for t, line in enumerate(DictReader(open('C:/Users/pizagno_j/Ponpare/data/coupon_detail_train.csv'), delimiter=',')):
        myCouponList = Impression_Detail(**line)
        impressions_detail_list.append(myCouponList)        
    
    coupons_dict = {}
    for t, line in enumerate(DictReader(open('C:/Users/pizagno_j/Ponpare/data/coupon_list_train.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_dict[myCoupon.COUPON_ID_hash] = myCoupon    

    users_list = []
    for t, line in enumerate(DictReader(open('C:/Users/pizagno_j/Ponpare/data/user_list.csv'), delimiter=',')):
        myUser = User(**line)
        users_list.append(myUser) 
        
    coupons_test_dict = {}
    for t, line in enumerate(DictReader(open('C:/Users/pizagno_j/Ponpare/data/coupon_list_test.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_test_dict[myCoupon.COUPON_ID_hash] = myCoupon  
        
    userBest10CouponsRecommended_dict = {}  # key/value = userIdHash/list_of_coupon_hashIDs
    for user in users_list:
        user_coupons_train = []
        similarities_dict = {}  # key/value = couponTest.hash/cosine(couponTest,couponeUser)
        # get training coupons
        for impressionDetail in impressions_detail_list:
            if impressionDetail.USER_ID_hash == user.USER_ID_hash:
                user_coupons_train.append(impressionDetail)

        # compare to coupons in test dictionary:
        for couponTest in coupons_test_dict.values():
            for impressionUserTrain in user_coupons_train:
                couponUserTrain = coupons_dict[impressionUserTrain.COUPON_ID_hash]
                cosine = get_cosine_coupon(couponUserTrain, couponTest)
                similarities_dict[couponTest.COUPON_ID_hash] = cosine
        
        # sort by cosine, but in ASCENDING order, so fetch from the bottom 
        sorted_similarities_dict = sorted(similarities_dict.items(), key=operator.itemgetter(1))
        list_of_coupon_hashIDs = []
        if len(similarities_dict) == 0:
            # we have a problem. This user is in the users_list/sample_submission but not in the coupon_detail_train.csv
            print "we have a problem. This user is in the users_list/sample_submission but not in the coupon_detail_train.csv"
            print "user.USER_ID_hash = ",user.USER_ID_hash
            print " ERROR:  picking random coupons."
            print " "
            for i in range(0,10):
                couponRandomKey = random.choice(coupons_test_dict.keys())
                list_of_coupon_hashIDs.append(coupons_test_dict[couponRandomKey].COUPON_ID_hash)
        else:  
            for i in range(0,10):
                coupon = sorted_similarities_dict.pop() # coupon is tuple (coupon_ID_hash,cosine)
                list_of_coupon_hashIDs.append(coupon[0])
        # add list to user Dict
        userBest10CouponsRecommended_dict[user.USER_ID_hash] = list_of_coupon_hashIDs
        
    # open up sample file, get userid, write out list
    ts = time.time()
    st = datetime.datetime.fromtimestamp(ts).strftime('%Y%m%dT%H%M%S')
    filename  = "submission"+str(st)+".csv"
    with open(filename, 'w') as output:
        output.write('%s\n' % str("USER_ID_hash,PURCHASED_COUPONS"))
        for t, line in enumerate(DictReader(open('C:/Users/pizagno_j/Ponpare/data/sample_submission.csv'), delimiter=',')):
            userIDhash = line['USER_ID_hash']
            list_of_coupon_hashIDs = userBest10CouponsRecommended_dict[userIDhash]
            outline = userIDhash+","+" ".join(list_of_coupon_hashIDs)
            output.write('%s\n' % str(outline))