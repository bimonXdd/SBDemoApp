## About me

- **NAME:** Simon Prii
- **UNIVERSITY OF TARTU:** Second year CS student
- **LANGUAGES:** Estonian, English


 ## About the project
  The project is a smaller part of a solution for a delivery type application. It calculates delivery fee extras based on the weather in Tallinn, Tartu and Pärnu and based on a vehicle type(Car, Scooter, Bike).
  Weather data is taken from ![Estonias weather Station XML](https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php) and inserted to an H2 database and regional base fee is determined before. The database gets
  new information (HH:15:00) so every hour on minute 15. The project contains documentation in form of javadoc and a few tests to test the services of the partial solution.
  
  - **The update rate is configurable via resources/application.properties as cronjob.**
  - **The solution should not work if there isnt any data in the DB(change cronjob for test)** 
  - **After the fee is calculated it is returned as a json object with the corresponding calculation info.**
  - **If the weather is too harsh the fee will not be calculated and the error is given.**

 Mainly used:
   - H2 Database
   - Springboot
   - REST
    
