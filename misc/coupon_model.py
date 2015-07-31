# -*- coding: utf-8 -*-
import math

class User(object):
    """
        REG_DATE,SEX_ID,AGE,WITHDRAW_DATE,PREF_NAME,USER_ID_hash
                2012-03-28 14:14:18,f,25,NA,,d9dca3cb44bab12ba313eaa681f663eb
                2011-05-18 00:41:48,f,34,NA,???,560574a339f1b25e57b0221e486907ed
                2011-06-13 16:36:58,m,41,NA,???,e66ae91b978b3229f8fd858c80615b73
    """
    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)
        
class Impression_Detail(object):
    """
    Example Data:  ITEM_COUNT,I_DATE,SMALL_AREA_NAME,PURCHASEID_hash,USER_ID_hash,COUPON_ID_hash
                    1,2012-03-28 15:06:06,??               ,c820a8882374a4e472f0984a8825893f,d9dca3cb44bab12ba313eaa681f663eb,34c48f84026e08355dc3bd19b427f09a
                    1,2011-07-04 23:52:54,???????????,1b4eb2435421ede98c8931c42e8220ec,560574a339f1b25e57b0221e486907ed,767673b7a777854a92b73b0934ddfae7

    
    """
    
    space_japan_delimiter =  u'\u30fb' 
    
    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)
        
    def __getattribute__(self,name):
        if name=='SMALL_AREA_NAME':
            return unicode(object.__getattribute__(self, name), "utf-8")
        else:
            return object.__getattribute__(self, name)
        
    def getSMALL_AREA_NAME_fields(self):
        return self.SMALL_AREA_NAME.split(space_japan_delimiter)
    
    def getPrintLine(self):
        # currently just the USER_ID_HASH, COUPON_ID_hash
        outline = str(self.USER_ID_hash)+","+str(self.COUPON_ID_hash)
        return outline
        
class Coupon(object):
    """
    Example Data:  
      CAPSULE_TEXT,GENRE_NAME,PRICE_RATE,CATALOG_PRICE,DISCOUNT_PRICE,DISPFROM,DISPEND,DISPPERIOD,VALIDFROM,VALIDEND,VALIDPERIOD,USABLE_DATE_MON,USABLE_DATE_TUE,USABLE_DATE_WED,USABLE_DATE_THU,USABLE_DATE_FRI,USABLE_DATE_SAT,USABLE_DATE_SUN,USABLE_DATE_HOLIDAY,USABLE_DATE_BEFORE_HOLIDAY,large_area_name,ken_name,small_area_name,COUPON_ID_hash
      ???,???,50,3000,1500,2011-07-08 12:00:00,2011-07-09 12:00:00,1,2011-07-10,2011-12-08,151,1,1,1,1,0,0,1,1,0,??,???,??,6b263844241eea98c5a97f1335ea82af

    """
    
    space_japan_delimiter =  u'\u30fb'
    
    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)
        
    def __getattribute__(self,name):
        if name=='CAPSULE_TEXT' or name=='GENRE_NAME'  or name=='large_area_name' or name=='ken_name' or name == 'small_area_name':
            return unicode(object.__getattribute__(self, name), "utf-8")
        elif name.count('USABLE_DATE')> 0:
            value = object.__getattribute__(self, name)
            if value == 'NA':
                return 1
            else:
                return value   
        elif name == 'DISCOUNT_PRICE':
            value = float(object.__getattribute__(self, name))
            if value < 1:
                return 1
            else:
                return 1./math.log10(value)
        elif name == 'PRICE_RATE':
            value = float(object.__getattribute__(self, name))
            if value < 1:
                return 0.01
            else:
                return value/100.
        else:
            return object.__getattribute__(self, name)
        
    def getPrintline(self):
        """
        print out:  
            "GENRE_NAME","DISCOUNT_PRICE","PRICE_RATE",
            "USABLE_DATE_MON","USABLE_DATE_TUE","USABLE_DATE_WED","USABLE_DATE_THU",
            "USABLE_DATE_FRI","USABLE_DATE_SAT","USABLE_DATE_SUN","USABLE_DATE_HOLIDAY",
            "USABLE_DATE_BEFORE_HOLIDAY","ken_name","small_area_name"
        """
        outline = self.GENRE_NAME
        outline += str(self.DISCOUNT_PRICE) + ","
        outline += str(self.PRICE_RATE) + ","
        outline += str(self.USABLE_DATE_MON) + ","
        outline += str(self.USABLE_DATE_TUE) + ","
        outline += str(self.USABLE_DATE_WED) + ","
        outline += str(self.USABLE_DATE_THU) + ","
        outline += str(self.USABLE_DATE_FRI) + ","
        outline += str(self.USABLE_DATE_SAT) + ","
        outline += str(self.USABLE_DATE_SUN) + ","
        outline += str(self.USABLE_DATE_HOLIDAY) + ","
        outline += str(self.USABLE_DATE_BEFORE_HOLIDAY) + ","
        outline += self.ken_name + ","
        outline += self.small_area_name
        return outline