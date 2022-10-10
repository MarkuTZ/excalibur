# Excalibur

This is the Excalibur repo for the IS lab at UTCN 2022-2023

## Use cases for this app:
1. Login / Register
2. UCP* (User Control Panel)
3. Manage your projects
   1. Create projects
   2. Edit project details
   3. Invite members
   4. Delete project
4. Manage tasks in your projects 
   1. Create tasks. 
   2. Assign to others *if you are OWNER.*
   3. Assign to yourself *if task is UNASSIGNED.*

   
## Models:
1. User
   1. ID - PK
   2. username *
   3. password *
2. Project
   1. ID - PK
   2. name *
   3. description
   4. owner_id - FK
   5. create_date *
   6. deadline *
3. Task
   1. ID - PK
   2. name *
   3. description
   4. project_id - FK
   5. owner_id *
   6. assignee_id  // (the user that has to complete the task)
   7. priority *
   8. create_date *
   9. start_date
   10. end_date
   11. status
4. Comment
   1. ID - PK
   2. task_id - FK
   3. user_id - FK
   4. content *
   5. create_date *
