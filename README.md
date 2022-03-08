# ItKbProject
IT Knowledge Base Project

### Functionality

This app provides a simple IT Knowledge Database. Information you don't want to forget can be saved and organised by the usage of five data fields:
Title, Category, Subcategory, Description and Source.
You can save, edit and delete entries. Searching can be done by a general keyword or within one of the above fields specifically including a date search.

<br>

### Objective

This project appreciates being used in practice but it was also built in a learning context. It covers specific areas of Android programming and tries to implement these in a good manner. These areas are:

- #### Saving data in a local database
Persistence Room library, Dao

- #### Support of different screen sizes 
Constraint Layout,Guideline

- #### Displaying data within the app
RecyclerView, AutoCompleteTextView, Live Data, Constraint Layout Manager, Landscape Mode

- #### Organising a project with fragments
Navigation

- #### Exchanging data by a custom csv import and expor
Filepicker, User Permission

- #### Seperating data by MVVM
Data Dinding, View Model, Repository

<br>

### Decisions
When implementing the app, a few decisions were taken that could have resulted elsewhere. Noteworthy decisions are listed here:

- #### Escaping
The databse serves the principle filtering data on input and escaping data on output
This means data that comes in is validated (filter), but data is only transformed (escape or encode) when sending it as output 
to another system that requires a specific encoding.
