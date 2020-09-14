package groovy.listeners

dispatcher.on("stateChangedEvent", {id, args -> println "stateChangedEvent for ${id} from ${args[0]} to ${args[1]}."} )