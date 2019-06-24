"""

    Autor: Jovani Brasil
    Email: jovanibrasil@gmail.com

    Script para conversao de arquivos texto para UTF-8. 
    
    O diretorio root para o caminhamento deve ser definido
    na variavel ROOT_DIR. Todos os arquivos .txt a partir
    deste diretorios serao convertidos para UTF-8.

"""
import os
import sys
import codecs

import unicodedata



#ROOT_DIR = '/home/jovani/Desktop/opened-data-sets/TeMario'
ROOT_DIR = "C:\Jovani\\new\summarizer\corpora\\temario-2004"


def remove_accents(input_str):
    nfkd_form = unicodedata.normalize('NFKD', input_str)
    return u"".join([c for c in nfkd_form if not unicodedata.combining(c)])

def convert_to_utf(file_path):
    """
        TODO documentacao
    """
    print("Decodificando ", file_path)
    try:
        lines = []
        with open(file_path, 'r', encoding='utf8') as f:
            for line in f:
                lines.append(remove_accents(line))
        #print(lines)
        with open(file_path,'w',encoding='utf8') as f:
            f.writelines(lines)
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
        ext = file_name[len(file_name)-5::]
        if ext == ".html":
            file_path = os.path.join(root, file_name)
            convert_to_utf(file_path)

