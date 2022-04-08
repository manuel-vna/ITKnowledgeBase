# ITKnowledgeBase
IT Knowledge Base Project

This mobile appplication provides a simple IT Knowledge database. Information you don't want to forget can be saved and organised by the usage of five data fields:
Title, Category, Subcategory, Description and Source.
You can save, edit and delete knowledge entries. Searching can be done by a general keyword or within one of the above fields specifically, including a search by the saving date of the entry. Entries can be imported and exported.

<br>

### Objective

The app was built in a learning context. This means besides its use case it covers specific learning aspects within the world of Android programming which it tries to implement in a good manner by using the latest methods and frameworks. These are:

- #### Saving data in a local database
Persistence Room library, Dao

- #### Adaptive Layout
Constraint Layout,Guideline, Landscape Mode

- #### Displaying data
RecyclerView, AutoCompleteTextView, Live Data, ActionBar

- #### Organising a project with activities and fragments
Intent, Navigation

- #### Exchanging data by a custom csv import and export
Filepicker, User Permission, Scoped Storage

- #### Seperating data by MVVM
Data Binding, View Model, Repository

<br>

### Decisions
When implementing the app a few decisions were taken that could have resulted elsewhere. Noteworthy decisions are listed here:

- #### Storing data: Escaping 
The databse serves the principle filtering data on input and escaping data on output
This means data that comes in is validated (filter), but data is only transformed (escape or encode) when sending it as output 
to another system that requires a specific encoding.

- #### Update and delete Entries: Second activity
An own,second activity is used for deleting and updating databse entries. This activity is called 'UpdateDeleteEntryActivity'.
An activity-to-activity Intent seems a solid solution for the involved task of transfering data between a RecyclerViewAdapter
and an Activity. It probably exists a comparable (or even better) solution within one single activity, but wasn't found here.

<br>

