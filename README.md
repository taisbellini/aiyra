# aiyra

*Aiyra* is an extendable java simulator of Paje traces. The
extensibility is possible through a well-defined API allowing the user
to developed a plugin for the core simulator. Every time an object is
defined by the simulator, the plugin is called with the information
about that object. These kind of Paje objects are currently supported
(evolving list):

  - States
  - Links
  - Variables
  - Events
  - Containers

Each object has parameters such as value, start and end time stamps
plus additional information depending on the object type. For
instance, links also have source and destination containers identifiers.

*Aiyra* comes with some plugins:

  - CSV-like textual dump (identical to =pj_dump=)
  - Database importer

More details on the development of *aiyra* can be found in the
LabBook.org file.
