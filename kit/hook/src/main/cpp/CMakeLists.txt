cmake_minimum_required(VERSION 3.22.1)
project("sentinel-hook")

add_library(sentinel-hook SHARED sentinel-hook.cpp)

find_library(log-lib log)
target_link_libraries(sentinel-hook ${log-lib})