#!/bin/bash

mvn clean package

INPUT="$1"
OUTPUT="$2"

java -jar target/inteligentna-sygnalizacja-swietlna-1.0-SNAPSHOT.jar $INPUT $OUTPUT
