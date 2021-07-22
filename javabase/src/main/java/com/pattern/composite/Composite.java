package com.pattern.composite;


import java.util.ArrayList;
import java.util.List;

public class Composite extends Component {

    private List<Component> componentList =new ArrayList<Component>();


    public Composite(String name) {
        super(name);
    }

    @Override
    public void add(Component component) {
        componentList.add(component);
    }

    @Override
    public void remove(Component component) {
        componentList.remove(component);
    }

    @Override
    public void display(int deepth) {
        deepth++;
        for (Component component : componentList) {
            super.display(deepth);
            component.display(deepth);
        }
    }

    @Override
    public void duty() {
        super.duty();
    }
}
