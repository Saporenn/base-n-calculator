package base_n_calculator.shared.project_classes;

import java.util.function.Function;

public class NodeNavigator<TNodeType extends Node<TNodeType>> {
    private TNodeType current;

    public NodeNavigator(TNodeType current) {
        this.current = current;
    }

    public NodeNavigator<TNodeType> goTo(String childName) {
        this.current = this.current.getChild(childName).orElse(this.current);
        return this;
    }

    public NodeNavigator<TNodeType> goBack() {
        this.current = this.current.getParent().orElse(this.current);
        return this;
    }

    public NodeNavigator<TNodeType> addChild(String name, Function<String, TNodeType> factory) {
        TNodeType child = factory.apply(name);
        this.current.addChild(child);
        return this;
    }

    public NodeNavigator<TNodeType> removeChild(String name) {
        this.current.removeChild(name);
        return this;
    }

    public TNodeType getCurrent() {
        return this.current;
    }
}
