cmake_minimum_required(VERSION 3.22.1)

add_definitions(-DDEBUG)

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")

project("sentinel-hook")

add_library(sentinel-hook SHARED sentinel-hook.cpp)

find_library(log-lib log)
target_link_libraries(sentinel-hook ${log-lib})