# Neo4j_EmailHeader
In this project, I try to use Neo4j to manipulate records of Email Headers

Github Repository  [https://github.com/colin1990324/Neo4j_EmailHeader](https://github.com/colin1990324/Neo4j_EmailHeader)

The EmailHeader CSV data is part of VAST2014 Challenge dataset.

### clear old nodes and relationships
-------------------------------------------------------------------------------

    MATCH (n)
    OPTIONAL MATCH
    (n)-[r]-()
    DELETE n,r

### load Person(email address)
-------------------------------------------------------------------------------

    LOAD CSV WITH
    HEADERS FROM "[file:///home/colin/Downloads/data/addresses.csv](file:///home\colin\Downloads\data\addresses.csv)" AS csvLine

    CREATE (p:Person {id: toInt(csvLine.id), email: csvLine.address })

Return 55 nodes.

### load Emails
-------------------------------------------------------------------------------

    LOAD CSV WITH
    HEADERS FROM "[file:///home/colin/Downloads/data/emails.csv](file:///home\colin\Downloads\data\emails.csv)" AS csvLine

    CREATE (e:Email {id: toInt(csvLine.id), time: csvLine.time, content: csvLine.content })

Return 1170 nodes.

### add index to make the program faster
-------------------------------------------------------------------------------

    CREATE INDEX ON:Person(id);

    CREATE INDEX ON:Email(id);

### create person-from->email-to->person relationships
-------------------------------------------------------------------------------
create unique to make from relationships unique

    USING PERIODIC
    COMMIT 500

    LOAD CSV WITH
    HEADERS FROM "[file:///home/colin/Downloads/data/relations.csv](file:///home\colin\Downloads\data\relations.csv)" AS csvLine

    MATCH (p1:Person {id: toInt(csvLine.fromId)}),(e:Email { id: toInt(csvLine.emailId)}),(p2:Person{ id: toInt(csvLine.toId)})

    CREATE UNIQUE (p1)-[:FROM]->(e)

    CREATE(e)-[:TO]->(p2)

Created 10233 relationships, statement executed in 4567 ms.

### show all emails from p1: Person.Id = 1 send to p2:Person.Id = 2
-------------------------------------------------------------------------------

    MATCH (p1:Person)-[r1:FROM]->(e:Email)-[r2:TO]->(p2:Person)
    WHERE p1.id = 1 and p2.id=2
    RETURN p1,e,p2,r1,r2

![Model-View-Controller](https://github.com/colin1990324/Neo4j_EmailHeader/blob/master/data/image/Screen%20Shot%202015-05-19%20at%204.05.43%20PM.png)

Displaying 35 nodes, 98 relationships.

### found some cases, the email receiver list contains its sender, remove these cycles

    MATCH (p1:Person)-[r1:FROM]->(e:Email)-[r2:TO]->(p1:Person)
    delete r2

![Model-View-Controller](https://github.com/colin1990324/Neo4j_EmailHeader/blob/master/data/image/Screen%20Shot%202015-05-19%20at%204.08.43%20PM.png)

Displaying 35 nodes, 66
relationships.

### filter on date.
-------------------------------------------------------------------------------
note: there's no Date type built in Neo4j. We can use regular expression here.

MATCH (p1:Person)-[r1:FROM]->(e:Email)-[r2:TO]->(p2:Person)
WHERE p1.id = 1 and p2.id=2 and e.time=~'^1/16/2014.*'
RETURN p1,e,p2,r1,r2

![Model-View-Controller](https://github.com/colin1990324/Neo4j_EmailHeader/blob/master/data/image/Screen%20Shot%202015-05-19%20at%204.09.56%20PM.png)

Displaying 7 nodes, 10 relationships.

### find all path from p1 to p2, order by distance
-------------------------------------------------------------------------------

    MATCH p=(:Person {id: 1})-[r:FROM|:TO*1..5]-(:Person {id : 2 })
    RETURN p
    LIMIT 10

![Model-View-Controller](https://github.com/colin1990324/Neo4j_EmailHeader/blob/master/data/image/Screen%20Shot%202015-05-19%20at%205.40.57%20PM.png)

### find a email with the most receivers
-------------------------------------------------------------------------------

    MATCH (n:Email)-[r:TO]->(x)
    RETURN n as Email,
    COUNT(r) as ReceiverNumber
    ORDER BY COUNT(r) DESC
    LIMIT 10

![Model-View-Controller](https://github.com/colin1990324/Neo4j_EmailHeader/blob/master/data/image/Screen%20Shot%202015-05-19%20at%205.02.37%20PM.png)

This shows some emails are sent to the whole group

### find a person whose emails have the most average receivers
-------------------------------------------------------------------------------
trying…

### calculate words frequency, "data, Welldead, visit, report, Report, night, GIS" have high rate in the email header.
-------------------------------------------------------------------------------
trying…
 

 

 