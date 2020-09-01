package com.zizhizhan.legacy.pattern.builder;

class Director {

    public void Construct(AbstractBuilder abstractBuilder) {
        abstractBuilder.BuildPartA();
        abstractBuilder.BuildPartB();
        abstractBuilder.BuildPartC();
    }

}
