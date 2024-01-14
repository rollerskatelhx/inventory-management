lhx的个人的spring-boot学习项目：仓库管理系统

本系统的用户目前有写用户和读用户两种，读用户只享有查询功能，而写用户可享受一切功能包括删除、修改记录等。
系统被分为三个模块，分别是库房管理、货种管理、存量管理。存量管理对应对存量表的管理，记录由“货种id”、“库房id”、“存储量”三个字段构成。
每个模块都有查询功能，查询出记录后，可在某条记录上点击其附带的修改/删除按钮进行写操作。
仓库管理系统由四张数据表支撑，所以在运行系统前需要做数据准备，例如以下的sql（向系统用户表插入数据，用户身份0代表写用户，1代表读用户）：

CREATE TABLE commodity(
cid int not null AUTO_INCREMENT,name VARCHAR(20) not null UNIQUE,unit_price int not null,PRIMARY KEY(cid));

insert into commodity(cid,name,unit_price) values
(1,'钢笔',12),(2,'铅笔',2),(3,'圆珠笔',2.5);

create table warehouse(
wid int not null AUTO_INCREMENT,
addr varchar(60) not null,
PRIMARY KEY(wid));

insert into warehouse(wid,addr) values
(1,'上海'),(2,'广州');

create table sys_user(
id int not null AUTO_INCREMENT,
username varchar(10) not null,
status int not null,
primary key(id));

insert into sys_user(id,username,status) VALUES
(1,'pepper',0),(2,'walrus',1);

create table batch(
cid int not null,
wid int not null,
quantity int not null,
FOREIGN key(cid) REFERENCES commodity(cid),
FOREIGN key(wid) REFERENCES warehouse(wid),
primary key(cid,wid));

insert into batch(cid,wid,quantity) values
(1,2,100),(2,1,2000),(3,1,500),(4,2,1500),(2,2,900);

相关技术栈：
  后端：Spring Boot,Spring,Spring MVC,Mybatis
  前端：Thymeleaf,Bootstrap,Ajax,JQuery
  开发环境：IDEA,Maven
  数据库：MySQL
