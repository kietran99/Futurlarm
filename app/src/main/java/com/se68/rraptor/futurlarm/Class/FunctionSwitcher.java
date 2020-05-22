package com.se68.rraptor.futurlarm.Class;

public class FunctionSwitcher {
    private static FunctionTab[] functionTabs = new FunctionTab[]{
            FunctionTab.LIST,
            FunctionTab.NEW,
            FunctionTab.ACHIEVEMENT,
            FunctionTab.SOCIAL};
    private static int current;

    public static FunctionTab[] getFunctionTabs() {
        return functionTabs;
    }

    public static int getCurrent() {
        return current;
    }

    public static void setCurrent(int current) {
        FunctionSwitcher.current = current;
    }

}
