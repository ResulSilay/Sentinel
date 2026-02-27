cmake_minimum_required(VERSION 3.22.1)

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")
add_definitions(-DDEBUG)

project("sentinel-root")
add_library(sentinel-root SHARED sentinel-root.cpp)

find_library(log-lib log)
target_link_libraries(sentinel-root ${log-lib})