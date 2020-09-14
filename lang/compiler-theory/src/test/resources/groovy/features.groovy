package groovy

def x = 1000
def str = "Hello Groovy!"

def list = [1, 2, 3]

def map = [Li: 12, Yang: 32, Cao: 23]

map.each{ name, age -> println "${name} is ${age}" }

re = /.+.java/

println "hello.java" ==~ re
println re ==~ "hello.java"

list = list + [4, 5, 6]
println list.join(" -> ")
assert list.size == 6

println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"

x = 100000000000
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"

x = 1000000000000000000000
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"
x *= x
println "x = ${x}, class is ${x.class}"

