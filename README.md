[![Build Status](https://travis-ci.org/jovanibrasil/summarizer.svg?branch=master)](https://travis-ci.org/jovanibrasil/summarizer) [![Code Coverage](https://img.shields.io/codecov/c/github/jovanibrasil/summarizer/master.svg)](https://codecov.io/gh/jovanibrasil/summarizer?branch=master)

## Projeto Summarizer

O objetivo deste projeto era desenvolver um sumarizador de textos extrativo, mono-documento, 
baseado em loǵica fuzzy e com foco na língua portuguesa. Para ajudar na configuração do sistema fuzzy 
utilizado pelo sumarizador foi implementado um otimizador genético de coeficientes das funções de 
pertinência fuzzy.

### Executando

Para executar somente o sumarizador é necessário criar um arquivo de configuração de sumarização, definindo 
caminhos para os arquivos, pipelines de pré-processamento e de computação de features, tipo de avaliação e 
de mecanismo de decisão, etc. Exemplos podem sem observados [aqui](/settings/summarization).

No caso da necessidade de otimização também é necessário criar um arquivo com as configurações da otimização, 
como quantidade de iterações, função de avaliação, tipo de mutação e crossover, etc. Exemplos podem sem 
observados [aqui](/settings/optimization).

Ambos os arquivos devem ter seu caminho indicado no arquivo summarizer.properties e na propriedade type deve 
ser atribuído o tipo de execução desejado (sumarização ou otimização). Um exemplo pode ser observado 
[aqui](/settings/summarizer.properties).

Se todas as configurações estivem corretas, basta executar a aplicação com o camando:

```$ java -jar summarizer.jar```

No output da execução são indicados os diretórios onde se encontram os resultados gerados pelo processamento. 

### Arquitetura da aplicação

A arquitetura geral da aplicação é apresentada na Figura 1.0, sendo composta de um módulo sumarizador de 
textos baseado em lógica fuzzy,  um módulo otimizador de coeficientes que é baseado em algoritmos genéticos 
e um módulo de preparação de corpus. Esta arquitetura permite com que o sumarizador fuzzy tenha seus 
coeficientes otimizados.

Os parâmetros de entrada para a solução são um corpus, um conjunto de regras, as configurações de compressão
 a serem aplicadas e as configurações do processo de otimização. 

<p align="center">
<img src="/imgs/arquitetura-geral.jpg" width="40%" height="40%"/>
</p>
<p align="center">Figura 1.0 - Arquitetura geral para otimização do sumarizador</p>

O corpus é composto por pares (texto, sumário). Nas configurações de compressão temos informações de quanto 
devemos reduzir de um texto para chegar ao sumário e nas configurações de otimização temos informações como 
taxas de mutação e crossover, operadores, etc.
 
No processo de sumarização automática extrativa utilizando lógica fuzzy são necessárias uma série de etapas. 
A arquitetura do sumarizador fuzzy pode ser conferida na Figura 1.1. O sumarizador demanda 
como entrada um documento a ser sumarizado, as configurações de compressão e uma base de conhecimento. 
A saída do sumarizador é um sumário gerado a partir das configurações de entrada.

<p align="center">
<img src="/imgs/arquitetura-sumarizador.jpg" width="40%" height="40%"/>
</p>
<p align="center">Figura 1.1 Arquitetura detalhada do sumarizador</p>

A arquitetura do sumarizador é composta de quatro módulos responsáveis por etapas distintas no processo de 
sumarização. Tais módulos são: pré-processamento, computação de features das sentenças, sistema fuzzy e seleção 
e ordenação das sentenças. O pré-processamento e a computação de features são os módulos que realizam a 
preparação do documento de entrada para que o sumário possa ser gerado através dos módulos de sistema fuzzy
e seleção e ordenação de sentenças. A execução da sumarização de um documento qualquer pela solução proposta 
precisa obrigatoriamente passar pelas etapas propostas.

### Opções de Pré-processamento

1. Lematization
2. Stemming
3. Segmentação de Parágrafos
4. Segmentação de Sentenças
5. Segmentação de Palavras (Tokenização)
6. Remoção de stop words
7. POS Tagger
8. Remoção de pontuação
9. Named Entity Recognition (NER)

### Opções de Features

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
(crossover) disponíveis são Média Simples, Média Geométrica, BLX-ALPHA e N-point Interleave.

### Fazendo build

O primeiro passo é instalar o Maven (sudo apt install maven) e o MakeFile (sudo apt-get install make). 
Então basta ir até a raiz do projeto e executar o comando:
   
   ```$ make package``` 

No diretório target você encontrará o diretório summarizer que contém o executável e as dependências 
necessárias. O executável pode ser executado com o comando: 

   ```$ java -jar summarizer.jar```

A aplicação deve funcionar perfeitamente se as configurações forem feitas corretamente.



