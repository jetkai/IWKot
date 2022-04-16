# MW2-Server-KT
 ####Under Development - Local Testing Only

### Project Requirements:

- IntelliJ IDEA 2021.2.2+ (for Kotlin 1.5.31)
- Gradle 7.4.1
- Spring Boot 2.6.6
- MariaDB 10.7.1
- JDK 8 or JDK 11

### Use Case:

- Stores (PlayerName, GUID, LastMap, LastSeen, FirstSeen, Connections & Port) 
- Information can be used for displaying "Welcome Messages" globally between different Modern Warfare 2 servers
- Data can be synced between multiple server instances, allowing to see active players on other maps
- API Replacement for IW4XAdmin

### TODO:

- Track additional stats such as (Play Time, Network Info, KDR, Killstreak Totals) etc
- Integrate into GSC instead of just calling local HTTP(s) requests

### LocalDB (SQL Script):
```sql
create table mw2_welcome
(
id int default 0 not null,
name varchar(64) not null,
guid varchar(128) not null,
firstSeen datetime not null,
lastSeen datetime not null,
connections int default 0 not null,
lastMapName varchar(32) not null,
lastServerPort int(5) not null,
constraint mw2_welcome_id_uindex
unique (id)
);

alter table mw2_welcome
add primary key (id);
```