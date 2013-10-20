mgadmin
=======

Admin tool for mongoDB.

Language for operations on MongoDB:

For searching:
=============

FIND {<QUERY>}
KEYS [<wanted keys>] (optional)

Example:

find {age:{$gt:30}} keys [name,age]

Insert:
=======

INSERT {<object>}

Exemplo:

insert {name:"andre", age:25}

Update:
=======

UPDATE {<SETS>} 
ON {<QUERY>} (optional)

Example:

update {$set:{age:26}} on {age:31}

remove:
=======

REMOVE {<QUERY>}

Exemplo:

remove {age:31}


Advanced:
=========

To change a collection in time of execution, you only need to do like above:

USING <connection name>.<database name>.<collection name>
<other commands>

For example:

using localhost.mydatabase.mycollection find {}
