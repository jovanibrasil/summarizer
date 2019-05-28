import magic
import os
import sys
import codecs

ROOT_DIR = "./"

for root, subdirs, files in os.walk(ROOT_DIR):
    for f in files:
        if f[:4] == "Sum-":
            new_name = f[4:len(f)-4] + "_mreference1.txt"
            print(f + " -> " + new_name)
            os.rename(os.path.join(root, f), os.path.join(root, new_name))
