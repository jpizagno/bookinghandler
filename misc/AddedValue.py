
from coupon_model import Coupon
from coupon_model import User
from coupon_model import Impression_Detail
from datetime import datetime as dt

def userWithdrawn(user):
    """
        check to see if user has withdrawn BEFORE 2012-06-24
    """
    if user.WITHDRAW_DATE == "NA":
        return False
    else:
        cutOff = dt.strptime("2012-06-24", "%Y-%m-%d")
        userDate = dt.strptime(user.WITHDRAW_DATE.split(" ")[0], "%Y-%m-%d") # ex: '2012-01-09 07:31:5'
        if userDate <= cutOff:
            return True
        else:
            return False


def getFractionWomen(impressions_detail_list, users_list, coupons_dict):
    """
        returns a dictionary with key/value = GENRE_NAME / fraction_women
    """
    couponFracWomen_dict = {}
    
    # get user sex:
    sex_dict = {}
    for user in users_list:
        sex_dict[user.USER_ID_hash] = user.SEX_ID
    
    for impressionDetail in impressions_detail_list:
        userID = impressionDetail.USER_ID_hash 
        genreName = coupons_dict[impressionDetail.COUPON_ID_hash].GENRE_NAME
        sex = sex_dict[userID]
        
        if genreName in couponFracWomen_dict.keys():
            # list format = (#m,#f)
            if sex == 'm':
                couponFracWomen_dict[genreName][0] = couponFracWomen_dict[genreName][0] + 1
            else:
                # f
                couponFracWomen_dict[genreName][1] = couponFracWomen_dict[genreName][1] + 1
        else:
            if sex == 'm':
                couponFracWomen_dict[genreName] = [1,0]
            else:
                couponFracWomen_dict[genreName] = [0,1]
        
    # get fraction women
    for genreName,mylist in couponFracWomen_dict.items():
        fractionWomen = float(mylist[1]) / (float(mylist[0]) + float(mylist[1]))
        couponFracWomen_dict[genreName] = fractionWomen
        
    return couponFracWomen_dict
