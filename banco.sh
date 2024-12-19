#!/bin/bash
set -e

echo "Limpando arquivos antigos..."
mvn clean

echo "Compilando o projeto..."
mvn compile

echo "Executando a aplicação..."
mvn exec:java
