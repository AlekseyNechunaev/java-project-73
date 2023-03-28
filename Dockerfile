FROM gradle:7.4.0-jdk17

RUN gradle installDist

COPY . .

CMD ./build/install/app/bin/app