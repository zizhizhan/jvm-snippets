package groovy

def sayHello = {
    name -> "Hello ${name}"
}

sayHello(args[0])