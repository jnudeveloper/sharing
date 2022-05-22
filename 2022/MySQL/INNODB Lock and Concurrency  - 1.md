# INNODB锁与并发 - 入门篇

## 事务的四要素

- **原子性**（Atomicity）：事务内的操作要么全部执行，要么全部不执行
- 一致性（Consistency）：事务前后，
- **隔离性**（Isolation）：事务之间没有互相影响。存在不同的隔离级别
- 持久性（Durability）：事务提交之后写到磁盘

## 并发存在的问题

- 脏读：事务1读到了事务2未提交的数据
- 不可重复读：事务1对同一行数据读两次，两次读的结果不一致（被事务2修改了）
- 幻读：事务1对同一行数据读两次，两次读到了被事务2新增的数据

## 隔离级别

- 读未提交（Read Uncommitted）：可以读到其它事务未提交的记录
- 读已提交（Read Committed）：可以读到其它事务已提交的记录
- **可重复读**（Repeatable Read）：事务内重复读同一行记录，结果是相同的
- 可序列化（Serializable）：最高级别，没有并发问题

查看隔离级别（默认是可重复读）

```sql
show variables like '%tx_isolation%';
```

## 锁的分类

### 共享锁和排它锁

- 共享锁（读锁）：被锁住的记录只能读不能写
    - 持有读锁的会话可以读表，但不能写表；
    - 允许多个会话同时持有读锁；
    - 其他会话就算没有给表加读锁，也是可以读表的，但是不能写表；
    - 其他会话申请该表写锁时会阻塞，直到锁释放
    
    ```sql
    SELECT ... LOCK IN SHARE MODE
    ```
    
- **排它锁**（写锁）：被锁住的记录可读可写，但只有一个事务可以方位该记录
    - 持有写锁的会话既可以读表，也可以写表；
    - 只有持有写锁的会话才可以访问该记录，其他会话访问该记录会被阻塞，直到锁释放；
    - 其他会话无论申请该记录的读锁或写锁，都会阻塞，直到锁释放
    
    ```sql
    INSERT
    UPDATE
    DELETE
    SELECT ... FOR UPDATE
    ```
    

### 行锁与表锁

- 表锁：开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高，并发度最低
    - SQL不走索引，则产生表锁
- 行锁：开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度也最高
    - SQL走索引，则产生行锁
    - 还有间隙锁等情况，这里不做展开

### 悲观锁和乐观锁

- 悲观锁：认为自己在使用数据的时候一定有别的线程来修改数据，所以在事务开始的时候就加锁

```sql
select number from table1 where id = 123 for update   -- 假设查到的结果为1
update table1 set number = 2 where id = 123
```

- 乐观锁：认为自己在使用数据时不会有别的线程修改数据，所以不会添加锁，只是在更新数据的时候去判断之前有没有别的线程更新了这个数据

```sql
update table1 set number = 2 where id = 123 and number = 1
```

## 实例分析：使用锁解决并发问题

### 场景

仓库里，

1. 几个工人把货物装进一个纸箱
2. 当货物装完时，贴上胶带封箱
3. 如果订单的货物已全部装箱（一个订单的货物需要装几个箱），订单状态改成完结

```sql
CREATE TABLE `order_tab` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '单号',
  `order_status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `qty` int(11) DEFAULT NULL COMMENT '下单的货物总量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_key` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `box_tab` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '单号',
  `box_id` int(11) DEFAULT NULL COMMENT '箱子编号',
  `received_qty` int(11) DEFAULT NULL COMMENT '已装数量',
  `box_status` tinyint(1) DEFAULT NULL COMMENT '是否封箱',
  PRIMARY KEY (`id`),
  KEY `idx_order_key` (`order_id`),
  KEY `idx_box_status` (`box_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 操作

- 货物放入箱子
- 货物拿出箱子

```sql
update box_tab set received_qty = ? where order_id = ?
```

- 封箱

```sql
update box_tab set box_status = 1 where order_id = ?
update order_tab set order_status = 1 where order_id = ?
```

### 并发问题

- 两个工人同时放货物
- 货物被一个工人拿出，同时另一个工人封箱
- 两个工人同时封箱

## 参考资料

1、MySQL官方文档

[MySQL :: MySQL 5.7 Reference Manual :: 14.7.1 InnoDB Locking](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking.html)

2、

[解决死锁之路 - 学习事务与隔离级别](https://www.aneasystone.com/archives/2017/10/solving-dead-locks-one.html)

3、

[解决死锁之路 - 了解常见的锁类型](https://www.aneasystone.com/archives/2017/11/solving-dead-locks-two.html)