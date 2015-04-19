# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 9:39:12 PM$"

import urllib2

class WebDownloader():
    """
        This class will download the files in chunks
    """

    timeOutSeconds = 60
    chunkDownload = 16 * 1024
    fileName = ""
    
    hdr = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11',
       'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
       'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.3',
       'Accept-Encoding': 'none',
       'Accept-Language': 'en-US,en;q=0.8',
       'Connection': 'keep-alive'}

    def __init__(self, *initial_data, **kwargs):
        """
          Instaniate by providing the timout of the download and the amount of 
          data to store in chunks.  
          
          ex:
            myWebDownloader = WebDownloader({"timeOutSeconds": 60, "chunkDownload": 16 * 1024})
        """
        for dictionary in initial_data:
            for key in dictionary:
                setattr(self, key, dictionary[key])
        for key in kwargs:
            setattr(self, key, kwargs[key])
        
    def download(self):
        """
            Method has exception handling and testing for attributes
        """    
        
        if self.url is not None:
            try:
                headerUrl = urllib2.Request(self.url, headers=self.hdr)
                req = urllib2.urlopen(headerUrl, timeout=self.timeOutSeconds) 
                with open(self.fileName, 'wb') as fp:
                    while True:
                        chunk = req.read(self.chunkDownload)
                        if not chunk: break
                        fp.write(chunk)
                
                return True
            
            except urllib2.URLError, e:
                print "URL Error:  " + e
                return False
            except:
                print "Unexpected error: Do you have an open internet connection? url=" + self.url
                return False
        else:
            print "Url not set"
            return False
