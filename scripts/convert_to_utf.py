"""

    Autor: Jovani Brasil
    Email: jovanibrasil@gmail.com

    Script para conversao de arquivos texto para UTF-8. 
    
    O diretorio root para o caminhamento deve ser definido
    na variavel ROOT_DIR. Todos os arquivos .txt a partir
    deste diretorios serao convertidos para UTF-8.

"""

import magic
import os
import sys
import codecs

#ROOT_DIR = '/home/jovani/Desktop/opened-data-sets/TeMario'
ROOT_DIR = "C:\Projects\Summarizer\corpora"

def convert_to_utf(file_path):
    """
        TODO documentacao
    """
    print("Decodificando ", file_path)
    try:
        with open(file_path, 'rb') as f:
            a = f.read().decode('iso8859-1')
            f.close()

        with codecs.open(file_path,'w',encoding='utf8') as f:
            f.write(a)
    except Exception:
        print("Aconteceu um erro na conversao do arquivo. \n" + file_path)


# walk realizada um caminhamento para descobrir arquivos
# e subdiretorios em uma arvore de diretorios. 
for root, subdirs, files in os.walk(ROOT_DIR):
    for file_name in files:      
        ext = file_name[len(file_name)-4::]
        if ext == ".txt":
            file_path = os.path.join(root, file_name)
            convert_to_utf(file_path)