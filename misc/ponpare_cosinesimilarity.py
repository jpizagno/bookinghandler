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
from AddedValue import getFractionWomen
from tools import getNumUserInImpressions

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
    for t, line in enumerate(DictReader(open('/Users/jim/Ponpare/data/coupon_detail_train.csv'), delimiter=',')):
        myCouponList = Impression_Detail(**line)
        impressions_detail_list.append(myCouponList)        
    
    coupons_dict = {}
    for t, line in enumerate(DictReader(open('/Users/jim/Ponpare/data/coupon_list_train.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_dict[myCoupon.COUPON_ID_hash] = myCoupon    

    users_list = []
    for t, line in enumerate(DictReader(open('/Users/jim/Ponpare/data/user_list.csv'), delimiter=',')):
        myUser = User(**line)
        users_list.append(myUser) 
        
    coupons_test_dict = {}
    for t, line in enumerate(DictReader(open('/Users/jim/Ponpare/data/coupon_list_test.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_test_dict[myCoupon.COUPON_ID_hash] = myCoupon  
        
    # get fraction of women for each coupon  key/value = genreName/fraction_women
    couponFracWomen_dict = getFractionWomen(impressions_detail_list, users_list, coupons_dict)
        
    # get number of times this user has been seen in imporessions:
    numUserInImpressions_dict = getNumUserInImpressions(impressions_detail_list)
    
    # calculate the similarities between every Test and Train coupon:
    print "calculate the similarities between every Test and Train coupon"
    similarities_dict = {}  # key/value = couponTest.hash+couponTrain.hash/cosine(couponTest,couponeUser)
    for coupon_id_hash_train,coupon_train in coupons_dict.items():
        for  coupon_id_hash_test,coupon_test in coupons_test_dict.items():
            key = coupon_id_hash_test + coupon_id_hash_train
            value = get_cosine_coupon(coupon_train, coupon_test)
            similarities_dict[key] = value

    
        
    userBest10CouponsRecommended_dict = {}  # key/value = userIdHash/list_of_coupon_hashIDs
    for user in users_list:
        # get training coupons
        print "fillin user_coupons_train: user.USER_ID_hash = ",user.USER_ID_hash
        similarities_user_dict = {} # key/value = couponTest.hash/cosine(couponTest,couponeUser)
        if user.USER_ID_hash in numUserInImpressions_dict.keys():
            # can only fill user_coupons_train if user has impressions
            for impressionDetail in impressions_detail_list:
                if impressionDetail.USER_ID_hash == user.USER_ID_hash:
                    COUPON_ID_hash_train = impressionDetail.COUPON_ID_hash
                    for coupon_id_hash_test,coupon_test in coupons_test_dict.items():
                        key_fetch = coupon_id_hash_test + COUPON_ID_hash_train
                        cosine = similarities_dict[key_fetch]
                        similarities_user_dict[coupon_id_hash_test] = cosine
                if numUserInImpressions_dict[user.USER_ID_hash] == len(similarities_user_dict):
                    # found all impressions for this user, so break, optimization, I hope
                    break
        
        # sort by cosine, but in ASCENDING order, so fetch from the bottom 
        sorted_similarities_dict = sorted(similarities_user_dict.items(), key=operator.itemgetter(1))
        
        list_of_coupon_hashIDs = []
        if len(sorted_similarities_dict) == 0:
            # we have a problem. This user is in the users_list/sample_submission but not in the coupon_detail_train.csv
            print "we have a problem. This user is in the users_list/sample_submission but not in the coupon_detail_train.csv"
            print "user.USER_ID_hash = ",user.USER_ID_hash
            print " ERROR:  picking random coupons."
            print " "
            for i in range(0,10):
                couponRandomKey = random.choice(coupons_test_dict.keys())
                list_of_coupon_hashIDs.append(coupons_test_dict[couponRandomKey].COUPON_ID_hash)
        else:  
            keepLooping = True
            while keepLooping:
                coupon = sorted_similarities_dict.pop() # coupon is tuple (coupon_ID_hash,cosine)
                fractionWomen = couponFracWomen_dict[coupons_test_dict[coupon[0]].GENRE_NAME]
                if fractionWomen > 0.6 and user.SEX_ID == 'f':
                    # certainly a women coupon with women users
                    list_of_coupon_hashIDs.append(coupon[0])
                elif fractionWomen < 0.6 and user.SEX_ID == 'm':
                    # certainly a mean coupon with men users
                    list_of_coupon_hashIDs.append(coupon[0])
                if len(list_of_coupon_hashIDs) == 10:
                    keepLooping = False
                if len(sorted_similarities_dict) == 0:
                    # rare case, looked at all coupons with cosine>0.5
                    keepLooping = False
                    
        # add list to user Dict
        userBest10CouponsRecommended_dict[user.USER_ID_hash] = list_of_coupon_hashIDs
        
    # open up sample file, get userid, write out list
    ts = time.time()
    st = datetime.datetime.fromtimestamp(ts).strftime('%Y%m%dT%H%M%S')
    filename  = "submission"+str(st)+".csv"
    with open(filename, 'w') as output:
        output.write('%s\n' % str("USER_ID_hash,PURCHASED_COUPONS"))
        for t, line in enumerate(DictReader(open('/Users/jim/Ponpare/data/sample_submission.csv'), delimiter=',')):
            userIDhash = line['USER_ID_hash']
            if userIDhash in userBest10CouponsRecommended_dict.keys():
                list_of_coupon_hashIDs = userBest10CouponsRecommended_dict[userIDhash]
                outline = userIDhash+","+" ".join(list_of_coupon_hashIDs)
            else:
                outline = userIDhash+","+" "
            output.write('%s\n' % str(outline))