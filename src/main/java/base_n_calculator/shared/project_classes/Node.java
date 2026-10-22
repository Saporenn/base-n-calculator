package base_n_calculator.shared.project_classes;

import java.util.HashMap;
import java.util.Optional;

public abstract class Node<TNodeType extends Node<TNodeType>> {
    private final HashMap<String, TNodeType> childMap = new HashMap<>();
    private final String name;
    private TNodeType parent;

    protected Node(String name) {
        this.name = name;
    }

    protected Node(String name, TNodeType parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    @SuppressWarnings("unchecked")
    public TNodeType addChild(TNodeType childNode) {
        final String childName = childNode.getName();
        if (this.childMap.containsKey(childName)) {
            throw new IllegalArgumentException(
                    "A node named '" + childName + "' already exists at this level."
            );
        }
        this.childMap.put(childName, childNode);
        childNode.setParent((TNodeType) this);
        return (TNodeType) this;
    }

    @SuppressWarnings("unchecked")
    public TNodeType removeChild(String name) {
        TNodeType child = this.childMap.get(name);
        child.setParent(null);
        this.childMap.remove(name);
        return (TNodeType) this;
    }

    public boolean hasChild(String name) {
        return this.childMap.containsKey(name);
    }

    public Optional<TNodeType> getChild(String name) {
        return Optional.ofNullable(this.childMap.get(name));
    }

    public Optional<TNodeType> getParent() {
        return Optional.ofNullable(this.parent);
    }

    @SuppressWarnings("unchecked")
    public TNodeType setParent(TNodeType parent) {
        this.parent = parent;
        return (TNodeType) this;
    }

    @SuppressWarnings("unchecked")
    public NodeNavigator<TNodeType> navigate() {
        return new NodeNavigator<>((TNodeType)this);
    }
}