package com.zizhizhan.legacies.pattern.adapter.v1;

class OurAdapter extends FrameworkXTarget {
    private FrameworkYAdaptee adaptee = new FrameworkYAdaptee();

    @Override
    public void SomeRequest(int a) {
        String b;
        b = "" + a;
        adaptee.QuiteADifferentRequest(b);
    }
}
