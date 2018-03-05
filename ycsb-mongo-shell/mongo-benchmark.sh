#!/bin/bash

#########################################################
# Parameters
#########################################################

MONGO_HOST=localhost:27017

WORKLOAD=workloada
RECORD_COUNT=30000
OPERATION_COUNT=60000
MAX_POOL_SIZE=8

#########################################################
# Functions
#########################################################
function benchmark() {

  # compose mongodb url
  if [ -n "${MAX_POOL_SIZE}" ] && [ "$1" == "mongodb" ];then
    MONGODB_URL="mongodb://${MONGO_HOST}/ycsb?w=1&maxPoolSize=${MAX_POOL_SIZE}&waitQueueMultiple=${2}"
  else
    MONGODB_URL="mongodb://${MONGO_HOST}/ycsb?w=1"
  fi

  # delete usertable first if exist.
  mongo --eval "db.usertable.drop();" ${MONGO_HOST}/ycsb >> /dev/null 2>&1

  echo -e "\n=================[$(date '+%F %T')] Begins benchmark : ${1} - ${WORKLOAD} - ${2} threads==================" | tee -a output/mongostat.log
# echo "[$(date '+%F %T')] Begins benchmark : ${1} - ${WORKLOAD} - ${2} threads" | tee -a output/mongostat.log

  [ "${1}" == "mongodb" ] && driver="s" || driver="a"

  ### load data
  bin/ycsb load ${1} -P workloads/${WORKLOAD} -s \
    -p "mongodb.url=${MONGODB_URL}" \
    -p recordcount=${RECORD_COUNT} -p operationcount=${OPERATION_COUNT} -threads ${2} \
    > output/${WORKLOAD}-${2}-load-${1}.txt \
    2> /tmp/ycsb-load.txt
  echo $(printf "[%s-%2s-l] " ${driver} ${2}) \
    $(grep "${RECORD_COUNT} operations" /tmp/ycsb-load.txt)

  ### run workload
  bin/ycsb run ${1} -P workloads/${WORKLOAD} -s \
    -p "mongodb.url=${MONGODB_URL}" \
    -p recordcount=${RECORD_COUNT} -p operationcount=${OPERATION_COUNT} -threads ${2} \
    > output/${WORKLOAD}-${2}-run-${1}.txt \
    2> /tmp/ycsb-run.txt
  echo $(printf "[%s-%2s-r] " ${driver} ${2}) \
    $(grep "${OPERATION_COUNT} operations" /tmp/ycsb-run.txt)
}

function sync_benchmark() {
  for thread_num in 1 2 4 6 8 10 20 30 40 50 60 70 80 ; do
    benchmark mongodb ${thread_num}
  done
}
function async_benchmark() {
  for thread_num in 1 2 4 6 8 10 20 30 40 50 60 70 80 ; do
    benchmark mongodb-async ${thread_num}
  done
}


#########################################################
# Main process
#########################################################
# print basic informations:
echo -e "\n>>> WORKLOAD        ${WORKLOAD}"
echo ">>> RECORD_COUNT    ${RECORD_COUNT}"
echo ">>> OPERATION_COUNT ${OPERATION_COUNT}"
[ -n "${MAX_POOL_SIZE}" ] && echo ">>> MAX_POOL_SIZE   ${MAX_POOL_SIZE} (Only for sync)"

# execute benchmark
sync_benchmark
async_benchmark
