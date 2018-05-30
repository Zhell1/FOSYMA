import numpy as np
import random

def tire(proba):
    r = random.random()
    s = 0
    for i in range(len(proba)):
        p = proba[i]
        s += p
        if r < s:
            return i
    raise ValueError("Not a proba")

def generateTrans(n):
    """ n : taille couloir """
    M = []
    L = [0 for i in range(n) ]
    first = L[::]
    first[0] = 1
    M.append(first)
    for pos in range(1, n-1):
        p = L[::]
        p[pos-1] = 1/2
        p[pos+1] = 1/2
        M.append(p)
    last = L[::]
    last[n-1] = 1
    M.append(last)
    return np.array(M)
        

# trans = np.array([[1/2,1/2,0,0,0], [1/2, 0, 1/2, 0, 0], [0, 1/2, 0, 1/2, 0], [0, 0, 1/2, 0 , 1/2], [0,0,0,0,1]])
# print('trans :', trans)

TC = 10

trans = generateTrans(TC)
print('t :', trans)

s1 = TC // 2 -1
s2 = TC // 2 + 1
n = 20
nbIt = 10000

nbState = trans.shape[1]
res1 = np.zeros(nbState)
res2 = np.zeros(nbState)

for k in range(nbIt):
    s1 = TC // 2 -1
    s2 = TC // 2 + 1
    for i in range(n):
        p1 = trans[s1]
        p2 = trans[s2]
        r1 = tire(p1)
        r2 = tire(p2)
        if r1 != r2:
            s1 = r1
            s2 = r2
    res1[s1] += 1
    res2[s2] += 1

res1 /= nbIt
res2 /= nbIt
print('res1 :', res1)
print('res2 : ', res2)
