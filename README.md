# yanap
Yet Another Availabilty Planner

run "mvn package" to create an executable jar (tomcat embedded, user -httpPort option to change port on startup)

TODOS:
- variable sprint lengths!
- Plan board: date in table header
- properties file for db connection
- Datepicker

Features under consideration:
- Statistics: Charts for Availability vs Story points
- delete Sprint and usersBySprint
- delete user (what about their previous works? either mess up statistics or try to add "empty user" instead

Gold Plating:
- Consolidate availability of non-developers (Phil, Tom, ...) across teams (feasible? what about Sprint start differences?)
- what can be pulled from Jira?
