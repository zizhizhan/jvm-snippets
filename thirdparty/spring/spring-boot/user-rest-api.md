# Routes

Prefix      | Verb   | URI Pattern               | Controller#Action
----------: | ------ | ------------------------- | -----------------
hello_index | GET    | /hello/index(.:format)    | hello#index
      users | GET    | /users(.:format)          | users#index
     &nbsp; | POST   | /users(.:format)          | users#create
   new_user | GET    | /users/new(.:format)      | users#new
  edit_user | GET    | /users/:id/edit(.:format) | users#edit
       user | GET    | /users/:id(.:format)      | users#show
     &nbsp; | PATCH  | /users/:id(.:format)      | users#update
     &nbsp; | PUT    | /users/:id(.:format)      | users#update
     &nbsp; | DELETE | /users/:id(.:format)      | users#destroy
       root | GET    | /                         | hello#index