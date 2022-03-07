# ItKbProject
IT Knowledge Base Project

### Functionality

This app provides a simple IT Knowledge Base. Information you don't want to forget can be saved and organised by the usage of five data fields:
Title, Category, Subcategory, Description and Source.

### Objective

This project appreciates being used in practice but it was also built in a learning context. It tries to cover specific areas of Android programming. These are:
- Saving data in a local database by using the persistence library Room
- Support of different screen sizes by focussing on a Constraint Layout
- Dsiplaying data with a RecyclerView
- Organising a project with fragments
- Exchanging data by a custom csv import and export


###### Topic 1

###### Topic 2




### Decisions

###### Escaping
The databse serves the principle filtering data on input and escaping data on output
This means data that comes in is validated (filter), but data is only transformed (escape or encode) when sending it as output 
to another system that requires a specific encoding.
