package com.zizhizhan.legacy.fp.lucene;

import com.zizhizhan.legacy.fp.Action;

import java.io.PrintWriter;

class FileAction implements Action<Integer> {

    private final PrintWriter out;


    public FileAction(PrintWriter out) {
        super();
        this.out = out;
    }

    @Override
    public void action(Integer obj) {
        out.print((char) obj.intValue());
    }

}
