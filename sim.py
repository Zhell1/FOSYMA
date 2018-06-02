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
    first[0] = 1/2
    first[1] = 1/2
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

TC = 5

trans = generateTrans(TC)
print('t :', trans)

state = 0
n = 10
nbIt = 1000

nbState = trans.shape[1]
resTable = np.zeros(nbState)

for k in range(nbIt):
    state = 0
    for i in range(n):
        proba = trans[state]
        state = tire(proba)
    # print('final state :', state)
    resTable[state] += 1

resTable /= nbIt
print('resTable :', resTable)
