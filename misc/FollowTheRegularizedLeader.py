from csv import DictReader

class ftrl(object):
	def __init__(self, alpha, beta, l1, l2, bits):
		self.z = [0.] * bits
		self.n = [0.] * bits
		self.alpha = alpha
		self.beta = beta
		self.l1 = l1
		self.l2 = l2
		self.w = {}
		self.X = []
		self.y = 0.
		self.bits = bits
		self.Prediction = 0.
	
	def sgn(self, x):
		if x < 0:
			return -1  
		else:
			return 1

	def fit(self,line):
                """
                    line dict:  PURCHASE_FLG,I_DATE,PAGE_SERIAL,REFERRER_hash,VIEW_COUPON_ID_hash,USER_ID_hash,SESSION_ID_hash,PURCHASEID_hash
                """
		#try:
		#	self.USER_ID_hash = line['USER_ID_hash']
		#	del line['USER_ID_hash']
		#except:
		#	pass

		try:
			self.y = float(line['PURCHASE_FLG'])
			del line['PURCHASE_FLG']
		except:
			pass

		self.X = [0.] * len(line)
                for i, key in enumerate(line):
			val = line[key]
			self.X[i] = (abs(hash(key + '_' + val)) % self.bits)
		self.X = [0] + self.X

	def logloss(self):
		act = self.y
		pred = self.Prediction
		predicted = max(min(pred, 1. - 10e-15), 10e-15)
		return -log(predicted) if act == 1. else -log(1. - predicted)

	def predict(self):
		W_dot_x = 0.
		w = {}
		for i in self.X:
			if abs(self.z[i]) <= self.l1:
				w[i] = 0.
			else:
				w[i] = (self.sgn(self.z[i]) * self.l1 - self.z[i]) / (((self.beta + sqrt(self.n[i]))/self.alpha) + self.l2)
			W_dot_x += w[i]
		self.w = w
		self.Prediction = 1. / (1. + exp(-max(min(W_dot_x, 35.), -35.)))
		return self.Prediction

	def update(self, prediction): 
		for i in self.X:
			g = (prediction - self.y) #* i
			sigma = (1./self.alpha) * (sqrt(self.n[i] + g*g) - sqrt(self.n[i]))
			self.z[i] += g - sigma*self.w[i]
			self.n[i] += g*g


if __name__ == '__main__':

	"""
	PURCHASE_FLG,I_DATE,PAGE_SERIAL,REFERRER_hash,VIEW_COUPON_ID_hash,USER_ID_hash,SESSION_ID_hash,PURCHASEID_hash
	"""
	train = 'C:/Users/pizagno_j/Ponpare/data/coupon_visit_train.csv'
	clf = ftrl(alpha = 0.1, 
			   beta = 1., 
			   l1 = 0.1,
			   l2 = 1.0, 
			   bits = 20)

	loss = 0.
	count = 0
	for t, line in enumerate(DictReader(open(train), delimiter=',')):
		clf.fit(line)
		pred = clf.predict()
		loss += clf.logloss()
		clf.update(pred)
		count += 1
		if count%10000 == 0: 
			print ("(seen, loss) : ", (count, loss * 1./count))
		if count == 500000:  #original 100000
			break

        # use the next 100000 lines for a test
        with open('test1.csv', 'w') as output:
            for t, line in enumerate(DictReader(open(train), delimiter=',')):
                    count += 1
                    if count > 500000 and count < 600000: 
                        purchaseFlag = line['PURCHASE_FLG']
                        userID = line['USER_ID_hash']
                        clf.fit(line)
                        pred = clf.predict()
                        outline = purchaseFlag + ','
                        outline = outline + line['I_DATE'] + ','
                        outline = outline + line['PAGE_SERIAL'] + ','
                        outline = outline + line['REFERRER_hash'] + ','
                        outline = outline + line['VIEW_COUPON_ID_hash'] + ','
                        outline = outline + userID + ','
                        outline = outline + line['SESSION_ID_hash'] + ','
                        outline = outline + line['PURCHASEID_hash']              
                        outline = str(pred) + ',' + outline
                        
                        output.write('%s\n' % str(outline))
                    if count > 600000:
                        break
                    
