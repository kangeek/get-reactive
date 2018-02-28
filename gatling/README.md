执行压力测试：

    cd gatling-scripts
    mvn gatling:test -Dgatling.simulationClass=test.load.sims.LoadSimulation -Dbase.url=http://localhost:8091/ -Dtest.path=hello/300 -Dsim.users=300
    mvn gatling:test -Dgatling.simulationClass=test.load.sims.LoadSimulation -Dbase.url=http://localhost:8091/ -Dtest.path=user/top/300 -Dsim.users=300
