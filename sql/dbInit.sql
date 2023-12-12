create database task_manager_db;
create user 'task_manager_admin'@'localhost' identified by 'Qwer!234Asdf';
grant all privileges on DATABASE_NAME.* TO 'task_manager_admin'@'localhost';
flush privileges;
