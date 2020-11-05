package com.tt.reptool.javaClasses;

import java.util.Comparator;

public class ManagerComparator implements Comparator<Manager> {
    @Override
    public int compare(Manager o1, Manager o2) {
        if(o1.isWorking()&&!o2.isWorking())
            return -1;
        if(!o1.isWorking()&&o2.isWorking())
            return 1;
        return 0;
    }
}
