将`mongo-benchmark.sh`置于`YCSB`根目录下，即与`bin`同级，然后执行即可。

1. Parameters下为一些基础配置，其中`MAX_POOL_SIZE`可以不配置，如果不配置则不限制连接池大小；
2. Functions下为测试函数，其中，`benchmark`函数定义了测试的方式：
  * 如果设置了`MAX_POOL_SIZE`，则会对同步驱动（即`mongodb`）起作用；
  * 每次测试前会先删除数据库中的`usertable`；
  * 先后执行load和run，分别进行数据集的加载和数据操作；
  * 测试结果会重定向到`output/{WORKLOAD}-{THREAD}-{load/run}-{DRIVER}.txt`；
  * 测试过程中会输出每次测试的数据摘要。
3. Main process下是脚本执行的内容，首先输出基本的配置数据，然后循环执行测试。