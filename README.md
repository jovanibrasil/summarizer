[![Build Status](https://travis-ci.org/jovanibrasil/summarizer.svg?branch=master)](https://travis-ci.org/jovanibrasil/summarizer) [![Code Coverage](https://img.shields.io/codecov/c/github/jovanibrasil/summarizer/master.svg)](https://codecov.io/gh/jovanibrasil/summarizer?branch=master)

## Projeto Summarizer

O objetivo deste projeto é desenvolver um sumarizador de textos extrativo, mono-documento, 
baseado em loǵica fuzzy e com foco na língua portuguesa.

### Executando

Primeiro é necessário criar um arquivo de configuração de sumarização, definindo caminhos para os arquivos, pipelines 
de pré-processamento e de computação de features, tipo de avaliação e de mecanismo de decisão, etc. Exemplos podem
sem observados aqui.

No caso da otimização também é necessário criar um arquivo com as configurações da otimização, como quantidade de iterações,
função de avaliação, tipo de mutação e crossover, etc.

Ambos os arquivos devem ter seu caminho indicado no arquivo summarizer.properties e na propriedade type deve ser atribuido
o tipo de execução desejado (sumarização ou otimização). 

### Montando um pipeline de sumarização

### Como utilizar a interface gráfica 

### Arquitetura da aplicação

### Pré-processamento

1. Lematization
2. Stemming
3. Segmentação de Parágrafos
4.  Segmentação de Sentenças
5.  Segmentação de Palavras (Tokenização)
6. Remoção de stop words
7. POS Tagger
8. Remoção de pontuação
9. Named Entity Recognition (NER)

### Features

1. Frequencia dos termos - Term frequency (TF)
2. Inverse Sentence Frequency (ISF)
3. Term Frequency Inverse Sentence Frequency (TF-ISF)
4. Comprimento
5. Local
6. Correlação entre comprimento e local
7. Cosine Similarity
8. TextRank
9. Similaridade com o título

### Mecanismos de tomada de decisão 

1. Lógica Fuzzy

### Otimizações 

#### Algoritmo Genético

Estão disponíveis os operadores de mutação Uniforme, Gaussiana (Normal), Creep e Limit. Os operadores de cruzamento 
(crossover) disponíveis são Média Simples, Média Geométrica, BLX-ALPHA e N-point interleave.

### Fazendo build

O primeiro passo é instalar o Maven (sudo apt install maven) e o MakeFile (sudo apt-get install make). 
Então basta ir até a raiz do projeto e executar o comando make package. No diretório target você encontrará
o diretório summarizer que contém o executável e as dependências necessárias. O executável pode ser executado
com o comando java -jar summarizer.jar.



