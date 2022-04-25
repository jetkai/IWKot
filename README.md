# IWKot
#### Under Development - Local Testing Only

---

### Project Requirements:

- IntelliJ IDEA (Version that supports Kotlin 1.6.20)
- Gradle 7.4.2
- Spring Boot 2.6.6
- MariaDB 10.7.1
- JDK 11

---

### Use Case:

- Stores (PlayerName, GUID, LastMap, LastSeen, FirstSeen, Connections & Port) 
- Information can be used for displaying "Welcome Messages" globally between different Modern Warfare 2 servers
- Data can be synced between multiple server instances, allowing to see active players on other maps
- API Replacement for IW4XAdmin

---

### TODO:

- Track additional stats such as (Play Time, Network Info, KDR, Killstreak Totals) etc
- Integrate into GSC instead of just calling local HTTP(s) requests

---

### LocalDB (SQL Script):
```sql
create table iwkot
(
    id               int auto_increment
        primary key,
    username         varchar(64)          not null,
    guid             varchar(128)         not null,
    first_seen       datetime             not null,
    last_seen        datetime             not null,
    connections      int        default 0 not null,
    last_map_name    varchar(32)          not null,
    last_server_port int                  not null,
    banned           tinyint(1) default 0 not null,
    constraint iwkot_guid_uindex
        unique (guid),
    constraint iwkot_id_uindex
        unique (id)
)
    auto_increment = 1;
```