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
from AddedValue import userWithdrawn
from tools import getNumUserInImpressions
import numpy
import pdb

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

    path = '/Users/jim/Ponpare/data/'

    users_list = []
    for t, line in enumerate(DictReader(open(path+'user_list.csv'), delimiter=',')):
        myUser = User(**line)
        users_list.append(myUser) 

    train_coupons_user_dict = {}
    impressions_detail_list = []
    for t, line in enumerate(DictReader(open(path+'coupon_detail_train.csv'), delimiter=',')):
        myCouponList = Impression_Detail(**line)
        impressions_detail_list.append(myCouponList)  
        # fill train_coupons_user_dict
        USER_ID_hash = line['USER_ID_hash']
        if USER_ID_hash in train_coupons_user_dict.keys():
            train_coupons_user_dict[USER_ID_hash].append(line['COUPON_ID_hash'])
        else:
            mylist = []
            mylist.append(line['COUPON_ID_hash'])
            train_coupons_user_dict[USER_ID_hash] = mylist
    
    coupons_dict = {}
    for t, line in enumerate(DictReader(open(path+'coupon_list_train.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_dict[myCoupon.COUPON_ID_hash] = myCoupon    
        
    coupons_test_dict = {}
    for t, line in enumerate(DictReader(open(path+'coupon_list_test.csv'), delimiter=',')):
        myCoupon = Coupon(**line)
        coupons_test_dict[myCoupon.COUPON_ID_hash] = myCoupon  
        
    # get fraction of women for each coupon  key/value = genreName/fraction_women
    couponFracWomen_dict = getFractionWomen(impressions_detail_list, users_list, coupons_dict)
    del(impressions_detail_list)
        
    ## get number of times this user has been seen in imporessions:
    #numUserInImpressions_dict = getNumUserInImpressions(impressions_detail_list)
    
    # calculate the similarities between every Test and Train coupon:
    print "calculate the similarities between every Test and Train coupon"
    similarities_dict_dict = {}  # dict of coupon_train.COUPON_ID_hash, key of dictionary coupont_test sorted
    for coupon_id_hash_train,coupon_train in coupons_dict.items():
        coupon_test_innder_dict = {}
        for coupon_id_hash_test,coupon_test in coupons_test_dict.items():
            key = coupon_id_hash_test
            value = get_cosine_coupon(coupon_train, coupon_test)
            coupon_test_innder_dict[key] = value
        coupon_test_inner_dict_sorted = sorted(coupon_test_innder_dict.items(), key=operator.itemgetter(1))
        similarities_dict_dict[coupon_id_hash_train] = coupon_test_inner_dict_sorted
        
    userBest10CouponsRecommended_dict = {}  # key/value = userIdHash/list_of_coupon_hashIDs
    userNoImpressions_list = []  # list of users
    for user in users_list:
        print "user ",user.USER_ID_hash
        
        if userWithdrawn(user):
            print "user withdrawn, given empty list.",user.USER_ID_hash
            userBest10CouponsRecommended_dict[user.USER_ID_hash] = []
        else:
            list_of_coupons = [] 
            list_of_coupon_hashIDs = []
            if user.USER_ID_hash not in train_coupons_user_dict.keys():
                userNoImpressions_list.append(user)
            else:
                train_coupons_id_hash_user_list = train_coupons_user_dict[user.USER_ID_hash] # list of coupons user clicked on
                for train_coupons_id_hash in train_coupons_id_hash_user_list:
                    coupons_put_back_in = []
                    coupons_dict = similarities_dict_dict[train_coupons_id_hash]
                    for i in range(0,10):
                        coupon = coupons_dict.pop()
                        list_of_coupons.append(coupon) # record (hash,cosine)
                        coupons_put_back_in.append(coupon)
                    # put those coupons back in dictionary
                    for i in range(len(coupons_put_back_in)-1,-1,-1):
                        coupons_dict.insert(len(coupons_dict), coupons_put_back_in[i])
                    similarities_dict_dict[train_coupons_id_hash] = coupons_dict

                # list_of_coupons has test coupons for this user with cosines. sort and pick top ten.
                list_of_coupons_sorted = sorted(list_of_coupons,  key=operator.itemgetter(1))
                for i in range(0,10):
                    #exclude based on cosine similarity < 0.95
                    if float(list_of_coupons_sorted[i][1]) > 0.90:
                        list_of_coupon_hashIDs.append(list_of_coupons_sorted[i][0])

                # add to final 
                userBest10CouponsRecommended_dict[user.USER_ID_hash] = list_of_coupon_hashIDs

 
    # go through users without impressions, get list from another user with same age/sex
    for userNoImpression in userNoImpressions_list:
        for user in users_list:
            if user.USER_ID_hash != userNoImpression.USER_ID_hash:
                if user.SEX_ID == userNoImpression.SEX_ID :
                    if user.AGE == userNoImpression.AGE :
                        # found
                        userBest10CouponsRecommended_dict[USER_ID_hash] = userBest10CouponsRecommended_dict[user.USER_ID_hash]
                        break 
    
    
    # open up sample file, get userid, write out list
    ts = time.time()
    st = datetime.datetime.fromtimestamp(ts).strftime('%Y%m%dT%H%M%S')
    filename  = "submission"+str(st)+".csv"
    with open(filename, 'w') as output:
        output.write('%s\n' % str("USER_ID_hash,PURCHASED_COUPONS"))
        for t, line in enumerate(DictReader(open(path+'sample_submission.csv'), delimiter=',')):
            userIDhash = line['USER_ID_hash']
            if userIDhash in userBest10CouponsRecommended_dict.keys():
                list_of_coupon_hashIDs = userBest10CouponsRecommended_dict[userIDhash]
                outline = userIDhash+","+" ".join(list_of_coupon_hashIDs)
            else:
                outline = userIDhash+","+" "
            output.write('%s\n' % str(outline))