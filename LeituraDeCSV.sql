set global local_infile=true;
USE AirportDjakstra_mysql;
CREATE TABLE Airports;
LOAD DATA LOCAL INFILE 'C:\Users\Joseph\Documents\ESTUDOS\IME_GRADUACAO\IME_2022\COMP\6Sem\LabProgII\Study_Java-Python\Java\AirportDjakstraHomeWork\airpot_data'
INTO TABLE Airports FIELDS TERMINATED BY ','
ENCLOSED BY ',' LINES TERMINATED BY '\n';
show tables;
SELECT * FROM airport_DATA;
