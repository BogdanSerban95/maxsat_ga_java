FROM pklehre/niso-lab2
ADD ArgParser.java /bin
ADD GeneticAlgorithm.java /bin
ADD Individual.java /bin
ADD MaxSat.java /bin
ADD bas782.java /bin

# RUN javac ./bin/Individual.java ./bin/MaxSat.java ./bin/ArgParser.java ./bin/GeneticAlgorithm.java ./bin/bas782.java
# RUN javac ./bin/MaxSat.java
# RUN javac ./bin/ArgParser.java
# RUN javac ./bin/GeneticAlgorithm.java
# RUN javac ./bin/bas782.java
# RUN ls /bin
RUN javac ./bin/*.java -d ./bin/.

CMD ["-username", "bas782", "-submission" , "java -cp ./bin/ bas782"]