# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:58:23 AM$"


"""
   This only works for Python 2.7+
   
   To test the entire system just run:
        shell% python RunTests.py
"""

import sys

if sys.version_info[0] >= 2 and sys.version_info[1] < 7:
    raise Exception("to run this test must have Python version 2.7")

import os
os.system("python -m unittest discover -s Tests -p '*.py'")

